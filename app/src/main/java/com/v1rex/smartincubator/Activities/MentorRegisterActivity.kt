package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.R

class MentorRegisterActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    private var mLastNameEditText: EditText? = null
    private var mFirstNameEditText: EditText? = null
    private var mCityEditText: EditText? = null
    private var mSpecialityEditText: EditText? = null
    private var mEmailEditText: EditText? = null
    private var mPhoneNumberEditText: EditText? = null
    private var mLastNameTextInput: TextInputLayout? = null
    private var mFirstNameTextInput: TextInputLayout? = null
    private var mCityTextInput: TextInputLayout? = null
    private var mSpecialityTextInput: TextInputLayout? = null
    private var mEmailTextInput: TextInputLayout? = null
    private var mPhoneNumberTextInput: TextInputLayout? = null
    private var mSubmitMentorBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_register)
        // get instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        mLastNameEditText = findViewById<View>(R.id.last_name_edit_text_mentor) as EditText
        mLastNameTextInput = findViewById<View>(R.id.last_name_input_text_mentor) as TextInputLayout


        mFirstNameEditText = findViewById<View>(R.id.first_name_edit_text_mentor) as EditText
        mFirstNameTextInput = findViewById<View>(R.id.first_name_input_text_mentor) as TextInputLayout

        mCityEditText = findViewById<View>(R.id.city_edit_text_mentor) as EditText
        mCityTextInput = findViewById<View>(R.id.city_input_text_mentor) as TextInputLayout

        mSpecialityEditText = findViewById<View>(R.id.speciality_edit_text_mentor) as EditText
        mSpecialityTextInput = findViewById<View>(R.id.speciality_input_text_mentor) as TextInputLayout

        mEmailEditText = findViewById<View>(R.id.email_edit_text_mentor) as EditText
        mEmailTextInput = findViewById<View>(R.id.email_input_text_mentor) as TextInputLayout

        mPhoneNumberEditText = findViewById<View>(R.id.number_phone_edit_text_mentor) as EditText
        mPhoneNumberTextInput = findViewById<View>(R.id.number_phone_input_text_mentor) as TextInputLayout


        mSubmitMentorBtn = findViewById<View>(R.id.submit_action_btn_mentor) as Button
        mSubmitMentorBtn!!.setOnClickListener { submit() }


    }

    override fun onBackPressed() {

    }

    private fun submit() {
        val lastName = mLastNameEditText!!.text.toString()
        val firstName = mFirstNameEditText!!.text.toString()
        val city = mCityEditText!!.text.toString()
        val specialty = mSpecialityEditText!!.text.toString()
        val email = mEmailEditText!!.text.toString()
        val phoneNumber = mPhoneNumberEditText!!.text.toString()

        // boolean for cancelling the submit if something is wrong
        var cancel = false

        if (TextUtils.isEmpty(lastName)) {
            mLastNameTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(city)) {
            mCityTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(specialty)) {
            mSpecialityTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(email)) {
            mEmailTextInput!!.error = getString(R.string.field_requierd)
            cancel = true

        }
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (cancel == false) {
            // Creating a Mentor object
            val mentor = Mentor(city, specialty, lastName, firstName, email, phoneNumber, mAuth!!.uid.toString())

            // Setting where to store informations about the mentor in the firebase Realtime Database
            val mentorsRef = ref.child("mentors")
            val mentorRef = mentorsRef.child(mentor.mUserId)
            mentorRef.setValue(mentor)


            startActivity(Intent(this@MentorRegisterActivity, BottonNavigationActivity::class.java))
        }


    }
}
