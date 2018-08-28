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
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_mentor_profile.*

import java.text.SimpleDateFormat
import java.util.Calendar

class MentorProfileActivity : AppCompatActivity() {

    private var mentor: Mentor? = null
    private val database = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private val ref = database.getReference("Data")
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()


        // getting the userId for the purpose of the choosing what Startup to show
        val intent = intent
        val userId = intent.getStringExtra("Mentor userId")

        /* finished_date_mentor.setOnClickListener { date_picker_mentor_layout.visibility = View.GONE }

        finished_time_mentor.setOnClickListener { time_picker_mentor_layout.visibility = View.GONE } */

        /*  set_date_mentor_btn.setOnClickListener { date_picker_mentor_layout.visibility = View.VISIBLE }

        set_time_mentor_btn.setOnClickListener { time_picker_mentor_layout.visibility = View.VISIBLE }

        exit_mettings_mentor.setOnClickListener { meeting_mentor_linearlayout.visibility = View.GONE }

        send_mettings_mentor.setOnClickListener {
            if (TextUtils.isEmpty(meeting_place_edit_text.text.toString())) {
                input_layout_place_meeting.error = getString(R.string.field_requierd)
            } else {
                val day = date_picker_mentor.dayOfMonth
                val month = date_picker_mentor.month
                val year = date_picker_mentor.year
                val hour = time_picker_mentor.currentHour
                val minute = time_picker_mentor.currentMinute

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute)

                val date = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")
                val dateAndTime = date.format(calendar.time)

                // creating a meeting object for the user who will receive the meeting
                val meeting = Meeting(mAuth!!.uid.toString(), userId, meeting_place_edit_text.text.toString(), dateAndTime, "", "You received")

                // setting where to store meetings informations for the user who will receive it
                val usersRef = ref.child("users")
                val userRef = usersRef.child(meeting.mUserIdReceived).child("mettings").child(meeting.mUserIdSent)


                // store the meetings for user who received
                userRef.setValue(meeting)
                meeting_mentor_linearlayout.visibility = View.GONE

                // creating a meeting object for the user who send the meeting
                val meeting2 = Meeting(mAuth!!.uid.toString(), userId, meeting_place_edit_text.text.toString(), dateAndTime, "", "You sented")

                // setting where to store meetings informations for the user who send it
                val usersRef1 = ref.child("users")
                val userRef2 = usersRef1.child(meeting.mUserIdSent).child("mettings").child(meeting2.mUserIdReceived)

                // store the meetings for user who sent
                userRef2.setValue(meeting2)
            }
        } */

        // showing a popup for sending a meeting
        fab_message_mentor.setOnClickListener {
            val intent = Intent(this, SendMessagesActivity::class.java)
            intent.putExtra("name", mentor!!.mFirstName + " " + mentor!!.mLastName)
            intent.putExtra("needSpecialty", mentor!!.mSpeciality)
            intent.putExtra("userId", mentor!!.mUserId)
            intent.putExtra("type","Mentor" )
            startActivity(intent)
        }


        val valueEventListenerMentor = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific mentor
                mentor = dataSnapshot.child(userId).getValue<Mentor>(Mentor::class.java)
                show()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        // loading the specific startups informations
        refUser = database.getReference("Data").child("mentors")

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

        // listening for the change in the Startup database
        refUser!!.addValueEventListener(valueEventListener)
    }



    /**
     * showing the mentor informations from the Startup object
     */
    private fun show() {
        mentor_name_profile.text = mentor!!.mFirstName + " " + mentor!!.mLastName
        mentor_speciality_profile.text = mentor!!.mSpeciality
        mentor_city_profile.text = mentor!!.mCity
        mentor_email_profile.text = mentor!!.mEmail
        mentor_number_phone_profile.text = mentor!!.mPhoneNumber

    }
}
