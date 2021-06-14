package com.tspt.academia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.tspt.academia.Administradores.HomeAdminActivity
import com.tspt.academia.other_users.HomeUsersActivity
import com.tspt.academia.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        title = "Authentication"


        isLoggedIn()

        binding.loginButton.setOnClickListener {
            println("Login")
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if( email.isNotEmpty() && password.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Login exitoso", Toast.LENGTH_LONG).show()
                            val uid = Firebase.auth.currentUser?.uid.toString()
                            notificationSetup()
                            getRole(uid)


                        } else {
                            Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

    private fun notificationSetup(){
        val db = Firebase.database.reference
        val id = Firebase.auth.currentUser?.uid.toString()
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            println("este es el token del dispositivo: ${it.result}")
        }

        //Registro por id del usuario
        println("Email de este usuario: ${Firebase.auth.currentUser?.email} ${Firebase.auth.currentUser?.uid}")
        FirebaseMessaging.getInstance().subscribeToTopic(id)

        //En caso de tener actividades
        db.child("users").child(id).child("actividades").get().addOnCompleteListener {
            if(it.isSuccessful){
                println("Resultado de la querie ${it.result!!.children.toList()}")
                it.result!!.children.forEach {
                    println("Actividades de este usuario: ${it.key} - ${it.value}")
                    FirebaseMessaging.getInstance().subscribeToTopic(it.key.toString())
                }
            } else {
                it.exception?.printStackTrace()
            }
        }

        //Por rol del usuario
        Firebase.database.reference.child("users").child(id).child("role").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println("Rol del usuario: ${it.result!!.value}")

                    when(it.result!!.value.toString().toInt()){
                        1 -> FirebaseMessaging.getInstance().subscribeToTopic("Administrador")
                        2 -> FirebaseMessaging.getInstance().subscribeToTopic("Instructor")
                        3 -> FirebaseMessaging.getInstance().subscribeToTopic("Responsable")
                        4 -> FirebaseMessaging.getInstance().subscribeToTopic("Tutor")
                        5 -> FirebaseMessaging.getInstance().subscribeToTopic("Alumno")
                    }
                }
            }

    }

    private fun isLoggedIn(){
        val auth = Firebase.auth

        if(auth.currentUser != null){
            notificationSetup()
            val db = Firebase.database.reference
            val user = db.child("users").child(auth.currentUser!!.uid)

            getRole(Firebase.auth.currentUser!!.uid)

            user.get().addOnSuccessListener {



                var role = (it.child("role").getValue() as Long).toInt()
                println("Rol del usuario es $role")



            }.addOnFailureListener {
                println("Error getting data $it")
            }


        }

    }

    private fun getRole(uid: String) {
        val db = Firebase.database.reference
        val user = db.child("users").child(uid)
        var r = -1


        user.get().addOnSuccessListener {

            /*Utils.currentUser.email = it.child("email").getValue() as String
            Utils.currentUser.date = it.child("date").getValue() as String
            Utils.currentUser.name = it.child("nombre").getValue() as String
            Utils.currentUser.telephone = it.child("telefono").getValue() as String
            Utils.currentUser.role = (it.child("role").getValue() as Long).toInt()*/

            var role = (it.child("role").getValue() as Long).toInt()
            println("El rol del usuario es: $role")
            when(role) {
                1 -> {
                    println("Entra a Home Admin Activity")
                    val ia = Intent(this, HomeAdminActivity::class.java)
                    ia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(ia)
                }

                else -> {
                    println("Entra a Home Users Activity")
                    val i = Intent(this, HomeUsersActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)
                }
            }


        }.addOnFailureListener {
            println("Error getting data $it")
        }


    }
}