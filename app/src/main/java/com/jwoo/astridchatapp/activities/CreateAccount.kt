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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

        btnCreateAccountnSave.setOnClickListener {
            var displayName:String = txtCreateAccountDisplayName.text.toString().trim()
            var emailAddress:String = txtCreateAccountEmailAddress.text.toString().trim()
            var password:String = txtCreateAccountPassword.text.toString().trim()

            if ((!TextUtils.isEmpty(displayName)) && (!TextUtils.isEmpty(emailAddress)) && (!TextUtils.isEmpty(password))){
                createAccount(emailAddress, password, displayName)
            }
            else {
                SharedFunctions().Log(this,"AstridChatApp-CreateUser", "Please fill out all fields.")
            }
        }
    }

    fun createAccount(email: String, password: String, displayName: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        var currentUser = mAuth!!.currentUser
                        var userId = currentUser!!.uid

                        mDatabase = FirebaseDatabase.getInstance().reference
                            .child("Users")
                            .child(userId)

                        var userObject: HashMap<String, String> = HashMap<String, String>()
                        userObject.put("displayName", displayName)
                        userObject.put("status", "")
                        userObject.put("image", "")
                        userObject.put("thumbnail_image", "")
                        
                        mDatabase!!.setValue(userObject)
                            .addOnCompleteListener{
                                task: Task<Void> ->
                                if (task.isSuccessful) {
                                    SharedFunctions().Log(this, "AstridChatApp-CreateUser", "Account has been created. - $userId", false)

                                    var dashboardIntent = Intent(this, Dashboard::class.java)
                                    dashboardIntent.putExtra("display_name", displayName)
                                    startActivity(dashboardIntent)
                                }
                                else {
                                    SharedFunctions().Log(this,"AstridChatApp-CreateUser", "FirebaseDatabase - Failed to create user account.")
                                }
                            }
                    }
                    else {
                        SharedFunctions().Log(this,"AstridChatApp-CreateUser", "FirebaseAuth - Failed to create user account.")
                    }
            }
    }
}
