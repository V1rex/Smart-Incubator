package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.firebase.auth.FirebaseAuth
import com.v1rex.smartincubator.Fragments.MentorsFragment
import com.v1rex.smartincubator.Fragments.MeetingsFragment
import com.v1rex.smartincubator.Fragments.MessagesFragment
import com.v1rex.smartincubator.Fragments.StartupsFragment
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_botton_navigation.*

class BottonNavigationActivity : AppCompatActivity() {

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: android.support.v4.app.Fragment? = null
        when (item.itemId) {
            R.id.nav_startups -> {
                selectedFragment = StartupsFragment()
                my_toolbar.subtitle = getString(R.string.startups_name_fragment)
            }
            R.id.nav_users -> {
                selectedFragment = MentorsFragment()
                my_toolbar.subtitle = getString(R.string.mentors_name_fragments)
            }

            R.id.nav_messages -> {
                selectedFragment = MessagesFragment()
                my_toolbar.subtitle = getString(R.string.messages_name_fragments)
            }

            R.id.nav_meetings -> {
                selectedFragment = MeetingsFragment()
                my_toolbar.subtitle = getString(R.string.meetings_name_fragments)
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botton_navigation)

        // Setting the toolbar
        setSupportActionBar(my_toolbar as Toolbar)
        my_toolbar.title = getString(R.string.app_name)
        my_toolbar.subtitle = getString(R.string.startups_name_fragment)

        bottom_navigation.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, StartupsFragment()).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@BottonNavigationActivity, SettingsActivity::class.java))
                return true
            }
            R.id.action_search -> {
                startActivity(Intent(this@BottonNavigationActivity, SearchActivity::class.java))
                return true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@BottonNavigationActivity, LoginActivity::class.java))
                finish()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onBackPressed() {
        finish()
    }
}
