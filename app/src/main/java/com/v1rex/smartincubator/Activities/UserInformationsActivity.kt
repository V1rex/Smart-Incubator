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
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_user_informations.*

class UserInformationsActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_informations)

        mAuth = FirebaseAuth.getInstance()

        // get the email of the user
        email_edit_text.setText(mAuth!!.currentUser!!.email)
        submit_action_btn.setOnClickListener { submit() }
    }

    /**
     * Method for submitting the user informations
     */
    private fun submit() {
        sexe_requierd_text_view.visibility = View.GONE
        account_type_requierd_text_view.visibility = View.GONE
        // a boolean to cancel submit if something is not good
        var cancel = false

        val lastName = last_name_edit_text.text.toString()
        val firstName = first_name_edit_text.text.toString()
        val email = email_edit_text.text.toString()
        val age = age_edit_text.text.toString()
        var sexe = ""
        var accountType = ""


        val maleSelected = male_selected.isChecked
        val femaleSelected = female_selected.isChecked
        val startupSelected = startup_button.isChecked
        val mentorSlected = mentor_button.isChecked

        if (!isEmailValid(email)) {
            email_input_text.error = getString(R.string.error_invalid_email)
            cancel = true
        } else if (TextUtils.isEmpty(email)) {
            email_input_text.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(lastName)) {
            last_name_input_text.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(firstName)) {
            first_name_input_text.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (TextUtils.isEmpty(age)) {
            age_input_text.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (maleSelected == false && femaleSelected == false) {
            cancel = true
            sexe_requierd_text_view.visibility = View.VISIBLE

        } else {
            if (maleSelected == true) {
                sexe = "Male"

            } else {
                sexe = "Female"
            }

        }

        if (startupSelected == false && mentorSlected == false) {
            cancel = true
            account_type_requierd_text_view.visibility = View.VISIBLE
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
            var firebaseMessaging = FirebaseInstanceId.getInstance().token
            user.registrationToken = firebaseMessaging.toString()
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

        return email.contains("@")
    }

    override fun onBackPressed() {

    }

}
