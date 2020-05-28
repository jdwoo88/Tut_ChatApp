package com.jwoo.astridchatapp.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast

class SharedFunctions {
    fun Log (context: Context, tag:String, msg:String, showToast: Boolean = true){
        if (showToast){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        Log.d(tag, msg)
    }
}