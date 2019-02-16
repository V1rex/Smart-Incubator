package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.v1rex.smartincubator.Model.Meeting
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_startup_profile.*

import java.text.SimpleDateFormat
import java.util.Calendar

class StartupProfileActivity : AppCompatActivity() {
    private var startup: Startup? = null

    private val database = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null

    private var mAuth: FirebaseAuth? = null

    private val databaseMeetings = FirebaseDatabase.getInstance()
    private val ref = databaseMeetings.getReference("Data")

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_profile)

        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        // getting the userId for the purpose of the choosing what Startup to show
        val intent = intent
        val userId = intent.getStringExtra("UserId Startup")


        // showing a popup for sending a messages
        fab.setOnClickListener {
            val intent = Intent(this, SendMessagesActivity::class.java)
            intent.putExtra("name", startup!!.mStartupName )
            intent.putExtra("needSpecialty", startup!!.mNeed )
            intent.putExtra("userId", startup!!.mUserId )
            intent.putExtra("type","Startup" )
            startActivity(intent)
        }

        // loading the specific startups informations
        refUser = database.getReference("Data").child("startups")
        val valueEventListenerMentor = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific startup
                startup = dataSnapshot.child(userId).getValue<Startup>(Startup::class.java)
                show()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        // listening for the change in the Startup database
        refUser!!.addValueEventListener(valueEventListenerMentor)



        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific mentor
                user = dataSnapshot.child(mAuth!!.uid.toString()).getValue<User>(User::class.java)
                user!!.registrationToken = FirebaseInstanceId.getInstance().token!!
                var ref = refUser!!.child(user!!.mUserId.toString())
                ref.setValue(user)
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        refUser = database.getReference("Data").child("users")

        // geeting informations about the user
        refUser!!.addValueEventListener(valueEventListener)
    }

    /**
     * showing the startup informations from the Startup object
     */
    private fun show() {
        startup_name_profile.text = startup!!.mStartupName
        startup_domain_profile.text = startup!!.mDomain
        startup_need_profile.text = startup!!.mNeed
    }
}
