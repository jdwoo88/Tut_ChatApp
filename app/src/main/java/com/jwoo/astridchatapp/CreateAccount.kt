package com.jwoo.astridchatapp

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
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                log("AstridChatApp-CreateUser", "Please fill out all fields.")
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
                        userObject.put("status", "-nothing-")
                        userObject.put("image", "-nothing-")
                        userObject.put("thum_image", "-nothing-")
                        
                        mDatabase!!.setValue(userObject)
                            .addOnCompleteListener{
                                task: Task<Void> ->
                                if (task.isSuccessful) {
                                    log("AstridChatApp-CreateUser", "Account has been created. - $userId")
                                }
                                else {
                                    log("AstridChatApp-CreateUser", "FirebaseDatabase - Failed to create user account.")
                                }
                            }
                    }
                    else {
                        log("AstridChatApp-CreateUser", "FirebaseAuth - Failed to create user account.")
                    }
            }
    }

    fun log (tag:String, msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        Log.d(tag, msg)
    }
}
