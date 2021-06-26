package com.tspt.academia.other_users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tspt.academia.R

class HomeUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_otherusers)


        getRol()
    }

    private fun getRol(): Int {
        val id = Firebase.auth.currentUser!!.uid
        var rol = -1

        Firebase.database.reference.child("users").child(id).child("role").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    println("Rol del usuario: ${it.result!!.value}")
                    rol = it.result!!.value.toString().toInt()

                    when(rol){
                        1 -> title = "Administrador"
                        2 -> title = "Instructor"
                        3 -> title = "Responsable"
                        4 -> title = "Padre/Tutor"
                        5 -> title = "Alumno"
                    }
                } else {
                    rol = -2
                }
            }



        return rol
    }
}