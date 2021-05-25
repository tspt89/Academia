package com.tspt.academia

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tspt.academia.Models.Users

class Utils {
    companion object {
        val currentUser = Users()

        fun signOut(context: Context){
            Firebase.auth.signOut()

            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)

        }
    }
}