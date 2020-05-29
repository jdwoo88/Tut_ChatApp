package com.jwoo.astridchatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.utilities.SharedFunctions

class SendMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        supportActionBar!!.title = "Chat"

        var extras = intent.extras
        if (extras != null)
        {
            var userId = extras!!.get("userId").toString()

            SharedFunctions().Log(
                this,
                "AstridChatApp-SendMessageActivity",
                "Send Message to $userId"
            )
        }
    }
}
