package com.jwoo.astridchatapp.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUserDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "User Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var extras = intent.extras
        if (extras != null) {
            var userId = extras.get("userId").toString()
            mCurrentUser = FirebaseAuth.getInstance().currentUser

            setupProfile(userId)
        }
    }

    private fun setupProfile(userId: String) {
        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
        mUserDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot.child("displayName").value
                var status = dataSnapshot.child("status").value
                var profilePic = dataSnapshot.child("image").value
                var thumbnail = dataSnapshot.child("thumbnail_image").value

                txtProfileDisplayName.text = displayName.toString()
                txtProfileStatus.text = status.toString()

                if (!TextUtils.isEmpty(profilePic.toString())) {
                    Picasso.with(this@ProfileActivity)
                        .load(profilePic.toString())
                        .placeholder(R.drawable.default_avata)
                        .error(R.drawable.default_avata)
                        .into(imgProfileImage)
                } else {
                    Picasso.with(this@ProfileActivity)
                        .load(R.drawable.default_avata)
                        .into(imgProfileImage)
                }

                SharedFunctions().Log(
                    applicationContext,
                    "AstridChatApp-ProfileActivity",
                    "Loading Profile - $userId",
                    false
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                SharedFunctions().Log(
                    this@ProfileActivity,
                    "AstridChatApp-ProfileActivity",
                    "Profile Error - ${databaseError.message}"
                )
            }
        })
    }
}
