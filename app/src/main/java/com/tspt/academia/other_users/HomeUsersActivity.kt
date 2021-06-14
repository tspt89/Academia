package com.tspt.academia.other_users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tspt.academia.R

class HomeUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_otherusers)

        title = "Otros usuarios"
    }
}