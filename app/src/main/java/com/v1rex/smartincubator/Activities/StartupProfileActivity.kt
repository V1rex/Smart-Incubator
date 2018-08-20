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

import java.text.SimpleDateFormat
import java.util.Calendar

class StartupProfileActivity : AppCompatActivity() {
    private var mStartupNameTextView: TextView? = null
    private var mStartupDomainTextView: TextView? = null
    private var mStartupFondTextView: TextView? = null
    private var mStartupChiffreTextView: TextView? = null
    private var mStartupNeedTextView: TextView? = null
    private var mStartupJuridiqueTextView: TextView? = null
    private var mStartupCreateDateTextView: TextView? = null
    private var mStartupNumberEmployees: TextView? = null
    private var startup: Startup? = null

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

    private var mSendMessageButton: FloatingActionButton? = null
    private var mDate: DatePicker? = null
    private var mTime: TimePicker? = null

    private var mMeetingsInformations: LinearLayout? = null
    private var mDateLayout: LinearLayout? = null
    private var mTimeLayout: LinearLayout? = null
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

        mDate = findViewById<View>(R.id.date_picker_startup) as DatePicker
        mTime = findViewById<View>(R.id.time_picker_startup) as TimePicker

        mExitDate = findViewById<View>(R.id.finished_date_startup) as ImageButton
        mExitDate!!.setOnClickListener { mDateLayout!!.visibility = View.GONE }

        mExitTime = findViewById<View>(R.id.finished_time_startup) as ImageButton
        mExitTime!!.setOnClickListener { mTimeLayout!!.visibility = View.GONE }

        mMeetingsInformations = findViewById<View>(R.id.meeting_startup_linearlayout) as LinearLayout
        mDateLayout = findViewById<View>(R.id.date_picker_startup_layout) as LinearLayout
        mTimeLayout = findViewById<View>(R.id.time_picker_startup_layout) as LinearLayout

        // showing a popup for sending a meeting
        mSendMessageButton = findViewById<View>(R.id.fab) as FloatingActionButton
        mSendMessageButton!!.setOnClickListener { mMeetingsInformations!!.visibility = View.VISIBLE }



        mStartupNameTextView = findViewById<View>(R.id.startup_name_profile) as TextView
        mStartupDomainTextView = findViewById<View>(R.id.startup_domain_profile) as TextView
        mStartupFondTextView = findViewById<View>(R.id.startup_fond_profile) as TextView
        mStartupChiffreTextView = findViewById<View>(R.id.startup_chiffre_profile) as TextView
        mStartupNeedTextView = findViewById<View>(R.id.startup_need_profile) as TextView
        mStartupJuridiqueTextView = findViewById<View>(R.id.startup_jurdique_profile) as TextView
        mStartupCreateDateTextView = findViewById<View>(R.id.startup_date_creation_profile) as TextView
        mStartupNumberEmployees = findViewById<View>(R.id.startup_number_employees_profile) as TextView

        mSetDateBtn = findViewById<View>(R.id.set_date_startup_btn) as Button
        mSetDateBtn!!.setOnClickListener { mDateLayout!!.visibility = View.VISIBLE }
        mSetTimeBtn = findViewById<View>(R.id.set_time_startup_btn) as Button
        mSetTimeBtn!!.setOnClickListener { mTimeLayout!!.visibility = View.VISIBLE }

        mExitButton = findViewById<View>(R.id.exit_mettings_startup) as ImageButton
        mExitButton!!.setOnClickListener { mMeetingsInformations!!.visibility = View.GONE }

        mSendButton = findViewById<View>(R.id.send_mettings_startup) as ImageButton
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
                val meeting = Meeting(mAuth!!.uid.toString(), userId, mPlaceEdit!!.text.toString(), dateAndTime, "", "Received")

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
        mPLaceTextInput = findViewById<View>(R.id.input_layout_place_meeting_startup) as TextInputLayout
        mPlaceEdit = findViewById<View>(R.id.meeting_place_edit_text_startup) as EditText

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
        mStartupNameTextView!!.text = startup!!.mStartupName
        mStartupDomainTextView!!.text = startup!!.mDomain
        mStartupFondTextView!!.text = startup!!.mFond
        mStartupChiffreTextView!!.text = startup!!.mChiffre
        mStartupNeedTextView!!.text = startup!!.mNeed
        mStartupJuridiqueTextView!!.text = startup!!.mJuridiqueSatatus
        mStartupCreateDateTextView!!.text = startup!!.mCreationDate
        mStartupNumberEmployees!!.text = startup!!.mNumberEmployees
    }
}
