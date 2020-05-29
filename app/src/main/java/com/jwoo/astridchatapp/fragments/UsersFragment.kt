package com.jwoo.astridchatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.adapters.UsersAdapter
import com.jwoo.astridchatapp.models.Users
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val userQuery: Query = FirebaseDatabase.getInstance().reference.child("Users")

        val options = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(userQuery, Users::class.java)
            .setLifecycleOwner(this)
            .build()

        recyclerViewFragmentUser.setHasFixedSize(true)
        recyclerViewFragmentUser.adapter = UsersAdapter(context!!, options)
        recyclerViewFragmentUser.layoutManager = linearLayoutManager
    }
}
