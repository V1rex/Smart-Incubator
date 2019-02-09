package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_mentor_register.*


class MentorRegisterActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_register)

        // get instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        submit_action_btn_mentor.setOnClickListener { submit() }
    }

    private fun submit() {
        val lastName = last_name_edit_text_mentor.text.toString()
        val firstName = first_name_edit_text_mentor.text.toString()
        val city = city_edit_text_mentor.text.toString()
        val specialty = speciality_edit_text_mentor.text.toString()
        val email = email_edit_text_mentor.text.toString()
        val phoneNumber = number_phone_edit_text_mentor.text.toString()

        // boolean for cancelling the submit if something is wrong
        var cancel = false

        if (TextUtils.isEmpty(lastName)) {
            last_name_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(firstName)) {
            first_name_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(city)) {
            city_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(specialty)) {
            speciality_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(email)) {
            email_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true

        }
        if (TextUtils.isEmpty(phoneNumber)) {
            number_phone_input_text_mentor.error = getString(R.string.field_requierd)
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

    override fun onBackPressed() {

    }
}
