package com.jwoo.astridchatapp.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.models.FriendlyMessage
import com.jwoo.astridchatapp.utilities.SharedFunctions
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_send_message.*
import kotlinx.android.synthetic.main.message_row.view.*

class SendMessageActivity : AppCompatActivity() {

    var toUserId: String? = null
    var fromUserId: String? = null
    var mFirebaseDatabaseRef: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null

    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Chat"

        var extras = intent.extras
        if (extras != null) {
            toUserId = extras.get("userId").toString()
            var toDisplayName = extras.get("displayName").toString()
            mLinearLayoutManager = LinearLayoutManager(this)
            mLinearLayoutManager!!.stackFromEnd = true

            mFirebaseUser = FirebaseAuth.getInstance().currentUser
            fromUserId = mFirebaseUser!!.uid
            mFirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

            val userQuery: Query = FirebaseDatabase.getInstance().reference.child("Messages")
            var firebaseRecyclerOptions = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(userQuery, FriendlyMessage::class.java)
                .setLifecycleOwner(this)
                .build()

            mFirebaseAdapter = object :
                FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(firebaseRecyclerOptions) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MessageViewHolder {
                    val view = LayoutInflater.from(applicationContext)
                        .inflate(R.layout.message_row, parent, false)
                    return MessageViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: MessageViewHolder,
                    position: Int,
                    model: FriendlyMessage
                ) {
                    holder.bindView(model, position, fromUserId.toString(), toUserId.toString())
                }

            }

            SharedFunctions().Log(
                this,
                "AstridChatApp-SendMessageActivity",
                "Send Message to $toUserId",
                false
            )

            chatRecyclerView.layoutManager = mLinearLayoutManager
            chatRecyclerView.adapter = mFirebaseAdapter

            btnChatSendMessage.setOnClickListener {
                // toDisplayName
                // fromUserId
                var frdlyMsg: FriendlyMessage = FriendlyMessage(
                    fromUserId.toString(),
                    toUserId.toString(),
                    txtChatSendMessage.text.toString(),
                    toDisplayName
                )

                mFirebaseDatabaseRef!!.child("Messages").push().setValue(frdlyMsg)
                txtChatSendMessage.setText("")
            }
        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            friendlyMessage: FriendlyMessage,
            position: Int,
            meUserId: String,
            toUserId: String
        ) {
            var profilePicTo = itemView.findViewById<CircleImageView>(R.id.imgChatUserTo)
            var chatMessageTo = itemView.txtChatMessageTo
            var chatMessengerTo = itemView.txtChatMessengerTo

            var profilePicFrom = itemView.findViewById<CircleImageView>(R.id.imgChatUserFrom)
            var chatMessageFrom = itemView.txtChatMessageFrom
            var chatMessengerFrom = itemView.txtChatMessengerFrom

            var isMe: Boolean = friendlyMessage.fromId == meUserId
            if (isMe) {
                profilePicFrom!!.visibility = View.VISIBLE
                chatMessageFrom!!.visibility = View.VISIBLE
                chatMessengerFrom!!.visibility = View.VISIBLE

                profilePicTo!!.visibility = View.GONE
                chatMessageTo!!.visibility = View.GONE
                chatMessengerTo!!.visibility = View.GONE

                mFirebaseDatabaseRef!!.child("Users").child(meUserId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var imgUrl = dataSnapshot.child("thumbnail_image").value
                            var dislayName = dataSnapshot.child("displayName").value
                            chatMessageFrom.text = friendlyMessage.text
                            chatMessengerFrom.text = "I wrote..."//dislayName.toString()

                            if (!TextUtils.isEmpty(imgUrl.toString())) {
                                Picasso.with(itemView.context)
                                    .load(imgUrl.toString())
                                    .placeholder(R.drawable.default_avata)
                                    .error(R.drawable.default_avata)
                                    .into(profilePicFrom)
                            } else {
                                Picasso.with(itemView.context)
                                    .load(R.drawable.default_avata)
                                    .into(profilePicFrom)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
            } else {
                profilePicFrom!!.visibility = View.GONE
                chatMessageFrom!!.visibility = View.GONE
                chatMessengerFrom!!.visibility = View.GONE

                profilePicTo!!.visibility = View.VISIBLE
                chatMessageTo!!.visibility = View.VISIBLE
                chatMessengerTo!!.visibility = View.VISIBLE

                mFirebaseDatabaseRef!!.child("Users").child(toUserId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var imgUrl = dataSnapshot.child("thumbnail_image").value
                            var dislayName = dataSnapshot.child("displayName").value

                            chatMessageTo.text = friendlyMessage.text
                            chatMessengerTo.text = dislayName.toString() + " wrote..."

                            if (!TextUtils.isEmpty(imgUrl.toString())) {
                                Picasso.with(itemView.context)
                                    .load(imgUrl.toString())
                                    .placeholder(R.drawable.default_avata)
                                    .error(R.drawable.default_avata)
                                    .into(profilePicTo)
                            } else {
                                Picasso.with(itemView.context)
                                    .load(R.drawable.default_avata)
                                    .into(profilePicTo)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            SharedFunctions().Log(
                                itemView.context,
                                "AstridChatApp-SendMessageActivity",
                                "Error: ${databaseError.message}"
                            )
                        }
                    })
            }
        }
    }
}
