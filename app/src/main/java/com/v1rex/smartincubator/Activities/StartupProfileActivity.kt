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
import com.v1rex.smartincubator.Model.Meeting
import com.v1rex.smartincubator.Model.Startup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_profile)
        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        // getting the userId for the purpose of the choosing what Startup to show
        val intent = intent
        val userId = intent.getStringExtra("UserId Startup")

       /* finished_date_startup.setOnClickListener { date_picker_startup_layout.visibility = View.GONE }

        finished_time_startup.setOnClickListener { time_picker_startup_layout.visibility = View.GONE } */

        // showing a popup for sending a meeting
        fab.setOnClickListener {
//            meeting_startup_linearlayout.visibility = View.VISIBLE
            val intent = Intent(this, SendMessagesActivity::class.java)
            intent.putExtra("name", startup!!.mStartupName)
            intent.putExtra("type", startup!!.mNeed)
            intent.putExtra("userId", startup!!.mUserId)
            startActivity(intent)
        }


        /*set_date_startup_btn.setOnClickListener { date_picker_startup_layout.visibility = View.VISIBLE }

        set_time_startup_btn.setOnClickListener { time_picker_startup_layout.visibility = View.VISIBLE }

        exit_mettings_startup.setOnClickListener { meeting_startup_linearlayout.visibility = View.GONE } */


       /* send_mettings_startup.setOnClickListener {
            if (TextUtils.isEmpty(meeting_place_edit_text_startup.text.toString())) {
                input_layout_place_meeting_startup.error = getString(R.string.field_requierd)
            } else {
                val day = date_picker_startup.dayOfMonth
                val month = date_picker_startup.month
                val year = date_picker_startup.year
                val hour = time_picker_startup.currentHour
                val minute = time_picker_startup.currentMinute

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute)

                val date = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")
                val dateAndTime = date.format(calendar.time)

                // creating a meeting object for the user who will receive the meeting
                val meeting = Meeting(mAuth!!.uid.toString(), userId, meeting_place_edit_text_startup.text.toString(), dateAndTime, "", "Received")

                // setting where to store meetings informations for the user who will receive it
                val usersRef = ref.child("users")
                val userRef = usersRef.child(meeting.mUserIdReceived).child("mettings").child(meeting.mUserIdSent)
                // store the meetings for user who received
                userRef.setValue(meeting)
                meeting_startup_linearlayout.visibility = View.GONE

                // creating a meeting object for the user who send the meeting
                val meeting2 = Meeting(mAuth!!.uid.toString(), userId, meeting_place_edit_text_startup.text.toString(), dateAndTime, "", "You sented")
                // setting where to store meetings informations for the user who send it
                val usersRef1 = ref.child("users")
                val userRef2 = usersRef1.child(meeting.mUserIdSent).child("mettings").child(meeting2.mUserIdReceived)

                // store the meetings for user who sent
                userRef2.setValue(meeting2)
            }
        } */

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
    }

    /**
     * showing the startup informations from the Startup object
     */
    private fun show() {
        startup_name_profile.text = startup!!.mStartupName
        startup_domain_profile.text = startup!!.mDomain
        startup_fond_profile.text = startup!!.mFond
        startup_chiffre_profile.text = startup!!.mChiffre
        startup_need_profile.text = startup!!.mNeed
        startup_jurdique_profile.text = startup!!.mJuridiqueSatatus
        startup_date_creation_profile.text = startup!!.mCreationDate
        startup_number_employees_profile.text = startup!!.mNumberEmployees
    }
}
