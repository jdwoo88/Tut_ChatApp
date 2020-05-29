package com.jwoo.astridchatapp.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.models.Users
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

            var id = getRef(position).key
            profilePic.setOnClickListener {
                Toast.makeText(context, id, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int, model: Users) {
        holder.bindView(model, position)
    }
}