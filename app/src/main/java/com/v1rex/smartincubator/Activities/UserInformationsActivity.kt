package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R

class UserInformationsActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    private var mFirstNameTextInputLayout: TextInputLayout? = null
    private var mLastNameTextInputLayout: TextInputLayout? = null
    private var mEmailTextInputLayout: TextInputLayout? = null
    private var mAgeTextInputLayout: TextInputLayout? = null
    private var mFirstNameEditText: EditText? = null
    private var mLastNameEditText: EditText? = null
    private var mEmailEditText: EditText? = null
    private var mAgeEditText: EditText? = null
    private var mMaleButton: RadioButton? = null
    private var mFemaleButton: RadioButton? = null
    private var mStartupButton: RadioButton? = null
    private var mMentorButton: RadioButton? = null
    private var mSubmit: Button? = null
    private var mSexeRequierdTextView: TextView? = null
    private var mAccountTypeRequierdTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_informations)
        mAuth = FirebaseAuth.getInstance()

        mFirstNameTextInputLayout = findViewById<View>(R.id.first_name_input_text) as TextInputLayout
        mFirstNameEditText = findViewById<View>(R.id.first_name_edit_text) as EditText

        mLastNameTextInputLayout = findViewById<View>(R.id.last_name_input_text) as TextInputLayout
        mLastNameEditText = findViewById<View>(R.id.last_name_edit_text) as EditText

        mEmailTextInputLayout = findViewById<View>(R.id.email_input_text) as TextInputLayout
        mEmailEditText = findViewById<View>(R.id.email_edit_text) as EditText

        mAgeTextInputLayout = findViewById<View>(R.id.age_input_text) as TextInputLayout
        mAgeEditText = findViewById<View>(R.id.age_edit_text) as EditText

        mMaleButton = findViewById<View>(R.id.male_selected) as RadioButton

        mFemaleButton = findViewById<View>(R.id.female_selected) as RadioButton
        mStartupButton = findViewById<View>(R.id.startup_button) as RadioButton

        mMentorButton = findViewById<View>(R.id.mentor_button) as RadioButton

        mSexeRequierdTextView = findViewById<View>(R.id.sexe_requierd_text_view) as TextView
        mAccountTypeRequierdTextView = findViewById<View>(R.id.account_type_requierd_text_view) as TextView

        mSubmit = findViewById<View>(R.id.submit_action_btn) as Button

        // get the email of the user
        mEmailEditText!!.setText(mAuth!!.currentUser!!.email)

        mSubmit!!.setOnClickListener { submit() }

    }

    override fun onBackPressed() {

    }

    /**
     * Method for submitting the user informations
     */
    private fun submit() {
        mSexeRequierdTextView!!.visibility = View.GONE
        mAccountTypeRequierdTextView!!.visibility = View.GONE
        // a boolean to cancel submit if something is not good
        var cancel = false

        val lastName = mLastNameEditText!!.text.toString()
        val firstName = mFirstNameEditText!!.text.toString()
        val email = mEmailEditText!!.text.toString()
        val age = mAgeEditText!!.text.toString()
        var sexe = ""
        var accountType = ""


        val maleSelected = mMaleButton!!.isChecked
        val femaleSelected = mFemaleButton!!.isChecked
        val startupSelected = mStartupButton!!.isChecked
        val mentorSlected = mMentorButton!!.isChecked

        if (!isEmailValid(email)) {
            mEmailTextInputLayout!!.error = getString(R.string.error_invalid_email)
            cancel = true
        } else if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(lastName)) {
            mLastNameTextInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameTextInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(age)) {
            mAgeTextInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (maleSelected == false && femaleSelected == false) {
            cancel = true
            mSexeRequierdTextView!!.visibility = View.VISIBLE

        } else {
            if (maleSelected == true) {
                sexe = "Male"

            } else {
                sexe = "Female"
            }

        }

        if (startupSelected == false && mentorSlected == false) {
            cancel = true
            mAccountTypeRequierdTextView!!.visibility = View.VISIBLE
        } else {
            if (startupSelected == true) {
                accountType = "Startup"

            } else if (mentorSlected == true) {
                accountType = "Mentor"
            }

        }

        if (cancel == false) {
            //Creating a new User object for submitting it
            val user = User(lastName, firstName, email, sexe, age, accountType, mAuth!!.uid.toString())

            // Setting where to submit the user in the firebase realtime database
            val usersRef = ref.child("users")
            val userRef = usersRef.child(user.mUserId)
            userRef.setValue(user)


            if (mentorSlected == true) {
                startActivity(Intent(this@UserInformationsActivity, MentorRegisterActivity::class.java))
            } else if (startupSelected == true) {
                startActivity(Intent(this@UserInformationsActivity, StartupRegisterActivity::class.java))
            }
        }


    }

    // check if the email is good
    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

}
