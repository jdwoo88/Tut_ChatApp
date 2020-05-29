package com.jwoo.astridchatapp.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File


class SettingsActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mCurrentUser: FirebaseUser? = null
    private var mStorageRef: StorageReference? = null
    private val GALLERY_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageRef = FirebaseStorage.getInstance().reference

        retrieveDetails()

        btnSettingsStatus.setOnClickListener {
            var intent: Intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", txtSettingsStatus.text.toString())
            startActivity(intent)
        }

        btnSettingsImage.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri? = data!!.data
            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                var thumBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(100)
                    .compressToBitmap(thumbFile)

                var byteArray = ByteArrayOutputStream()
                thumBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArray)
                var thumbByteArray: ByteArray = byteArray.toByteArray()

                var filePath = mStorageRef!!.child("Chat_profile_Images").child(userId + ".jpg")

                var thumbFilePath = mStorageRef!!.child("Chat_profile_Images").child("thumbnails")
                    .child(userId + ".jpg")

                filePath.putFile(resultUri)
                    .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                        if (task.isSuccessful) {
                            var downloadUrlTask =
                                task.result!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri: Uri? ->

                                    var downloadUrl = uri.toString()
                                    SharedFunctions().Log(
                                        applicationContext,
                                        "AstridChatApp-Settings",
                                        "Profile image saved. - $downloadUrl",
                                        false
                                    )

                                    var uploadTask: UploadTask =
                                        thumbFilePath.putBytes(thumbByteArray)
                                    uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                                        if (task.isSuccessful) {
                                            var thumbUrlTask =
                                                task.result!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri: Uri? ->

                                                    var thumbUrl = uri.toString()
                                                    SharedFunctions().Log(
                                                        applicationContext,
                                                        "AstridChatApp-Settings",
                                                        "Thumbnail image saved. - $thumbUrl",
                                                        false
                                                    )

                                                    var updateObject = HashMap<String, Any>()
                                                    updateObject.put("image", downloadUrl)
                                                    updateObject.put("thumbnail_image", thumbUrl)

                                                    mDatabase!!.updateChildren(updateObject)
                                                        .addOnCompleteListener { task: Task<Void> ->
                                                            if (task.isSuccessful) {
                                                                SharedFunctions().Log(
                                                                    applicationContext,
                                                                    "AstridChatApp-Settings",
                                                                    "Profile update success."
                                                                )
                                                            } else {
                                                                SharedFunctions().Log(
                                                                    applicationContext,
                                                                    "AstridChatApp-Settings",
                                                                    "Profile update failed."
                                                                )
                                                            }
                                                        }
                                                }
                                        } else {
                                            SharedFunctions().Log(
                                                applicationContext,
                                                "AstridChatApp-Settings",
                                                "Error saving thumb image."
                                            )
                                        }
                                    }
                                }
                        } else {
                            SharedFunctions().Log(
                                applicationContext,
                                "AstridChatApp-Settings",
                                "Error saving profile image."
                            )
                        }
                    }
            }
        }
    }

    fun retrieveDetails() {
        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot.child("displayName").value
                var image = dataSnapshot.child("image").value
                var status = dataSnapshot.child("status").value
                var thumbnail_image = dataSnapshot.child("thumbnail_image").value

                txtSettingsDisplayName.text = displayName.toString()
                txtSettingsStatus.text = status.toString()

                if (!image!!.equals("")) {
                    Picasso.with(applicationContext)
                        .load(image.toString())
                        .placeholder(R.drawable.default_avata)
                        .into(imgSettingsProfile)
                }

                SharedFunctions().Log(
                    applicationContext,
                    "AstridChatApp-Settings",
                    "Account successfully retrieved. - $userId; $displayName",
                    false
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                SharedFunctions().Log(
                    applicationContext,
                    "AstridChatApp-Settings",
                    "Account fetch failed. - ${databaseError.message}"
                )
            }
        })
    }
}
