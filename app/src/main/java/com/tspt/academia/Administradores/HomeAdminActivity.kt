package com.tspt.academia.Administradores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tspt.academia.R

class HomeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        title = "Administrador"
    }
}