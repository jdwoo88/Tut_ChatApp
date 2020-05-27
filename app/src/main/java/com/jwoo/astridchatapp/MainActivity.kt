package com.jwoo.astridchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }
}
