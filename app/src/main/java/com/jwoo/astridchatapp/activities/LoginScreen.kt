package com.jwoo.astridchatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.jwoo.astridchatapp.R
import kotlinx.android.synthetic.main.activity_login_screen.*

class LoginScreen : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        mAuth = FirebaseAuth.getInstance()

        btnLoginEnter.setOnClickListener {
            var emailAddress = txtLoginEmailAddress.text.toString()
            var password = txtLoginPassword.text.toString()

            if ((!TextUtils.isEmpty(emailAddress)) && (!TextUtils.isEmpty(password))){
                loginUser(emailAddress, password)
            }
            else {
                log("AstridChatApp-LoginUser", "Please fill out all fields.")
            }
        }
    }

    fun loginUser(emailAddress:String, password:String){
        mAuth!!.signInWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener{
                task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        log("AstridChatApp-LoginUser", "Login successful. - $emailAddress", false)

                        var dashboardIntent = Intent(this, Dashboard::class.java)
                        dashboardIntent.putExtra("display_name", emailAddress)
                        startActivity(dashboardIntent)
                    }
                    else {
                        log("AstridChatApp-LoginUser", "Failed to login.")
                    }
            }
    }

    fun log (tag:String, msg:String, showToast: Boolean = true){
        if (showToast){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        Log.d(tag, msg)
    }
}