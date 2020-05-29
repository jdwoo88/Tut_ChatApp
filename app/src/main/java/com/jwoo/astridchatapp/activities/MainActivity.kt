package com.jwoo.astridchatapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->
            user = firebaseAuth.currentUser
            if (user != null) {
                var dashboardIntent = Intent(this, Dashboard::class.java)
                var emailAddress = user!!.email
                dashboardIntent.putExtra("display_name", emailAddress)
                startActivity(dashboardIntent)

                SharedFunctions().Log(
                    this,
                    "AstridChatApp-MainActivity",
                    "Auto login complete. - $emailAddress",
                    false
                )
            }
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

        btnMainCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccount::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}
