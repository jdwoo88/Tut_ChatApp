package com.jwoo.astridchatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportActionBar!!.title = "Status"

        var extras = intent.extras
        var status = extras!!.getString("status")
        if (status != null){
            txtStatusUpdate.setText(status)
        }

        btnStatusSave.setOnClickListener {
            saveStatus()
        }

    }

    fun saveStatus(){
        mCurrentUser = FirebaseAuth.getInstance().currentUser
        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        var status =  txtStatusUpdate.text.toString()

        mDatabase!!.child("status").setValue(status).addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                SharedFunctions().Log(this, "AstridChatApp-StatusActivity", "Update Successful.", false)
                finish()
            }
            else {
                SharedFunctions().Log(this, "AstridChatApp-StatusActivity", "Update Failed.")
            }
        }
    }
}
