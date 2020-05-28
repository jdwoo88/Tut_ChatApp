package com.jwoo.astridchatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jwoo.astridchatapp.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"
    }
}
