package com.tspt.academia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.Administradores.HomeAdminActivity
import com.tspt.academia.OtherUsers.HomeUsersActivity
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
                            getRole(uid)



                        } else {
                            Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

    private fun isLoggedIn(){
        val auth = Firebase.auth

        if(auth.currentUser != null){
            val db = Firebase.database.reference
            val user = db.child("users").child(auth.currentUser!!.uid)

            user.get().addOnSuccessListener {

                Utils.currentUser.email = it.child("email").getValue() as String
                Utils.currentUser.date = it.child("date").getValue() as String
                Utils.currentUser.name = it.child("nombre").getValue() as String
                Utils.currentUser.telephone = it.child("telefono").getValue() as String
                Utils.currentUser.role = (it.child("role").getValue() as Long).toInt()

                var role = Utils.currentUser.role
                when(role) {
                    1 -> {
                        val intent = Intent(this, HomeAdminActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    else -> {
                        val intent = Intent(this, HomeUsersActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }


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

            Utils.currentUser.email = it.child("email").getValue() as String
            Utils.currentUser.date = it.child("date").getValue() as String
            Utils.currentUser.name = it.child("nombre").getValue() as String
            Utils.currentUser.telephone = it.child("telefono").getValue() as String
            Utils.currentUser.role = (it.child("role").getValue() as Long).toInt()

            var role = Utils.currentUser.role
            when(role) {
                1 -> {
                    val intent = Intent(this, HomeAdminActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                else -> {
                    val intent = Intent(this, HomeUsersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }


        }.addOnFailureListener {
            println("Error getting data $it")
        }


    }
}