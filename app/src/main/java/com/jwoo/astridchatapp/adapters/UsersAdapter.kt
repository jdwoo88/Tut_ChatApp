package com.jwoo.astridchatapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.activities.ProfileActivity
import com.jwoo.astridchatapp.activities.SendMessageActivity
import com.jwoo.astridchatapp.models.Users
import com.jwoo.astridchatapp.utilities.SharedFunctions
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.users_row.view.*

class UsersAdapter(
    var context: Context,
    var firebaseRecyclerOptions: FirebaseRecyclerOptions<Users>
) : FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(firebaseRecyclerOptions) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: Users, position: Int) {
            var profilePic = itemView.findViewById<CircleImageView>(R.id.imgUserProfile)
            var profileName = itemView.txtUserName
            var profileStatus = itemView.txtUserStatus

            profileName.text = user.displayName.toString()
            profileStatus.text = user.status.toString()

            if (!TextUtils.isEmpty(user.thumbnail_image)) {
                Picasso.with(context)
                    .load(user.thumbnail_image)
                    .placeholder(R.drawable.default_avata)
                    .error(R.drawable.default_avata)
                    .into(profilePic)
            } else {
                Picasso.with(context)
                    .load(R.drawable.default_avata)
                    .into(profilePic)
            }

            var userId = getRef(position).key
            profilePic.setOnClickListener {

                var options = arrayOf("Open Profile", "Send Message")
                var builder = AlertDialog.Builder(context)
                builder.setTitle("Select Options")
                builder.setItems(
                    options,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        var username: String = user.displayName.toString()
                        var userStat: String = user.status.toString()
                        var profilePic: String = user.image.toString()

                        when (which) {
                            0 -> {
                                // Open Profile
                                var profileIntent = Intent(context, ProfileActivity::class.java)
                                profileIntent.putExtra("userId", userId)
                                context.startActivity(profileIntent)

                                SharedFunctions().Log(
                                    context,
                                    "AstridChatApp-UsersAdapter",
                                    "Open profile of $userId",
                                    false
                                )
                            }
                            1 -> {
                                // Send Message
                                var sendMessage = Intent(context, SendMessageActivity::class.java)
                                sendMessage.putExtra("userId", userId)
                                sendMessage.putExtra("displayName", username)
                                context.startActivity(sendMessage)

                                SharedFunctions().Log(
                                    context,
                                    "AstridChatApp-UsersAdapter",
                                    "Send message to $userId",
                                    false
                                )
                            }
                        }
                    })

                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.users_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int, model: Users) {
        holder.bindView(model, position)
    }
}