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
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.R

import java.text.SimpleDateFormat
import java.util.Calendar

class MentorProfileActivity : AppCompatActivity() {

    private var mMentorNameTextView: TextView? = null
    private var mMentorSpecialityTextView: TextView? = null
    private var mMentorCityTextView: TextView? = null
    private var mMentorEmailTextView: TextView? = null
    private var mMentorPhoneNumber: TextView? = null

    private var mSendMessageButton: FloatingActionButton? = null
    private var mentor: Mentor? = null
    private val database = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null

    private var mSetDateBtn: Button? = null
    private var mSetTimeBtn: Button? = null
    private var mExitButton: ImageButton? = null
    private var mSendButton: ImageButton? = null
    private var mExitDate: ImageButton? = null
    private var mExitTime: ImageButton? = null
    private var mPLaceTextInput: TextInputLayout? = null
    private var mPlaceEdit: EditText? = null

    private var mDate: DatePicker? = null
    private var mTime: TimePicker? = null

    private var mMeetingsInformations: LinearLayout? = null
    private var mDateLayout: LinearLayout? = null
    private var mTimeLayout: LinearLayout? = null
    private var mAuth: FirebaseAuth? = null

    private val databaseMeetings = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()
        // getting the userId for the purpose of the choosing what Startup to show
        val intent = intent
        val userId = intent.getStringExtra("Mentor userId")

        mDate = findViewById<View>(R.id.date_picker_mentor) as DatePicker
        mTime = findViewById<View>(R.id.time_picker_mentor) as TimePicker

        mExitDate = findViewById<View>(R.id.finished_date_mentor) as ImageButton
        mExitDate!!.setOnClickListener { mDateLayout!!.visibility = View.GONE }

        mExitTime = findViewById<View>(R.id.finished_time_mentor) as ImageButton
        mExitTime!!.setOnClickListener { mTimeLayout!!.visibility = View.GONE }

        mMeetingsInformations = findViewById<View>(R.id.meeting_mentor_linearlayout) as LinearLayout
        mDateLayout = findViewById<View>(R.id.date_picker_mentor_layout) as LinearLayout
        mTimeLayout = findViewById<View>(R.id.time_picker_mentor_layout) as LinearLayout

        // showing a popup for sending a meeting
        mSendMessageButton = findViewById<View>(R.id.fab_message_mentor) as FloatingActionButton
        mSendMessageButton!!.setOnClickListener { mMeetingsInformations!!.visibility = View.VISIBLE }

        mMentorNameTextView = findViewById<View>(R.id.mentor_name_profile) as TextView
        mMentorSpecialityTextView = findViewById<View>(R.id.mentor_speciality_profile) as TextView
        mMentorCityTextView = findViewById<View>(R.id.mentor_city_profile) as TextView
        mMentorEmailTextView = findViewById<View>(R.id.mentor_email_profile) as TextView
        mMentorPhoneNumber = findViewById<View>(R.id.mentor_number_phone_profile) as TextView

        mSetDateBtn = findViewById<View>(R.id.set_date_mentor_btn) as Button
        mSetDateBtn!!.setOnClickListener { mDateLayout!!.visibility = View.VISIBLE }
        mSetTimeBtn = findViewById<View>(R.id.set_time_mentor_btn) as Button
        mSetTimeBtn!!.setOnClickListener { mTimeLayout!!.visibility = View.VISIBLE }

        mExitButton = findViewById<View>(R.id.exit_mettings_mentor) as ImageButton
        mExitButton!!.setOnClickListener { mMeetingsInformations!!.visibility = View.GONE }
        mSendButton = findViewById<View>(R.id.send_mettings_mentor) as ImageButton
        mSendButton!!.setOnClickListener {
            if (TextUtils.isEmpty(mPlaceEdit!!.text.toString())) {
                mPLaceTextInput!!.error = getString(R.string.field_requierd)
            } else {
                val day = mDate!!.dayOfMonth
                val month = mDate!!.month
                val year = mDate!!.year
                val hour = mTime!!.currentHour
                val minute = mTime!!.currentMinute

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute)

                val date = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")
                val dateAndTime = date.format(calendar.time)

                // creating a meeting object for the user who will receive the meeting
                val meeting = Meeting(mAuth!!.uid.toString(), userId, mPlaceEdit!!.text.toString(), dateAndTime, "", "You received")

                // setting where to store meetings informations for the user who will receive it
                val usersRef = ref.child("users")
                val userRef = usersRef.child(meeting.mUserIdReceived).child("mettings").child(meeting.mUserIdSent)
                // store the meetings for user who received
                userRef.setValue(meeting)
                mMeetingsInformations!!.visibility = View.GONE

                // creating a meeting object for the user who send the meeting
                val meeting2 = Meeting(mAuth!!.uid.toString(), userId, mPlaceEdit!!.text.toString(), dateAndTime, "", "You sented")
                // setting where to store meetings informations for the user who send it
                val usersRef1 = ref.child("users")
                val userRef2 = usersRef1.child(meeting.mUserIdSent).child("mettings").child(meeting2.mUserIdReceived)
                // store the meetings for user who sent
                userRef2.setValue(meeting2)
            }
        }
        mPLaceTextInput = findViewById<View>(R.id.input_layout_place_meeting) as TextInputLayout
        mPlaceEdit = findViewById<View>(R.id.meeting_place_edit_text) as EditText


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

    }

    /**
     * showing the mentor informations from the Startup object
     */
    private fun show() {
        mMentorNameTextView!!.text = mentor!!.mFirstName + " " + mentor!!.mLastName
        mMentorSpecialityTextView!!.text = mentor!!.mSpeciality
        mMentorCityTextView!!.text = mentor!!.mCity
        mMentorEmailTextView!!.text = mentor!!.mEmail
        mMentorPhoneNumber!!.text = mentor!!.mPhoneNumber

    }
}
