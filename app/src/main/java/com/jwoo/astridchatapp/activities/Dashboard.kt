package com.jwoo.astridchatapp.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.jwoo.astridchatapp.R
import com.jwoo.astridchatapp.adapters.SectionpagerAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    var sectionAdapter: SectionpagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Dashboard"

        sectionAdapter = SectionpagerAdapter(supportFragmentManager)
        vpDashboard.adapter = sectionAdapter
        tabLayoutDashboard.setupWithViewPager(vpDashboard)
        tabLayoutDashboard.setTabTextColors(Color.BLACK, Color.WHITE)

//        var extras = intent.extras
//        txtViewWelcome.text = "Welcome, " + extras!!.getString("display_name") + "!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item != null){
            if (item.itemId == R.id.menuLogout) {
                FirebaseAuth.getInstance().signOut()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else if (item.itemId == R.id.menuSetting) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return true
    }
}
