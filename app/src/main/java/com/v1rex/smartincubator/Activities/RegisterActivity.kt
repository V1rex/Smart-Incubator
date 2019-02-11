package com.v1rex.smartincubator.Activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.R.drawable.calendar
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_send_messages.*
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Getting instance of the firebase Auth for registering the User
        mAuth = FirebaseAuth.getInstance()

        link_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        register_action_btn.setOnClickListener { register() }

        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString(KEY_USERNAME_ENTRY)
            user_register_edit_text.setText(savedEmail)

            val savedPassword = savedInstanceState.getString(KEY_PASSWORD_ENTRY)
            password_register_edit_text.setText(savedPassword)
        }

        sexe_requierd_text_view.visibility = View.GONE
        account_type_requierd_text_view.visibility = View.GONE


        var cal = Calendar.getInstance()

        val dateSetLisener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf : String = SimpleDateFormat(myFormat, Locale.US).format(cal.time)

            age_edit_text.setText(sdf)
        }

        age_edit_text.setOnClickListener {
            DatePickerDialog(this, dateSetLisener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }


    }

    /**
     * Method for registering the user
     */
    private fun register() {

        if (mAuth == null) {
            return
        }

        // a boolean for canceling the registering if something is wrong
        var cancel = false

        val email = user_register_edit_text.text.toString()
        val password = password_register_edit_text.text.toString()
        val lastName = last_name_edit_text.text.toString()
        val firstName = first_name_edit_text.text.toString()
        val age = age_edit_text.text.toString()
        var sexe = ""
        var accountType = ""

        val maleSelected = male_selected.isChecked
        val femaleSelected = female_selected.isChecked
        val startupSelected = startup_button.isChecked
        val mentorSlected = mentor_button.isChecked


        if(!isNetworkAvailable()){
            input_layout_password_register.error = getString(R.string.error_internet_required)
            cancel = true
        } else if (TextUtils.isEmpty(password)) {
            input_layout_password_register.error = getString(R.string.error_field_password_required)
            cancel = true
        } else if (!isPasswordValid(password)) {
            input_layout_password_register.error = getString(R.string.error_invalid_password)
            cancel = true
        }


        if (TextUtils.isEmpty(email)) {
            input_layout_email_register.error = getString(R.string.error_field_username_required)
            cancel = true
        } else if (!isEmailValid(email)) {
            input_layout_email_register.error = getString(R.string.error_invalid_email)
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


        // if everything is good , register the user
        if (cancel == false) {
            registerView.visibility = View.INVISIBLE
            progess_register.visibility = View.VISIBLE

            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                progess_register.visibility = View.GONE
                if (!task.isSuccessful) {
                    user_register_edit_text.visibility = View.VISIBLE
                    password_register_edit_text.visibility = View.VISIBLE
                    register_action_btn.visibility = View.VISIBLE
                    input_layout_email_register.error = getString(R.string.error_action_registration_failed)
                } else {

                    val user = User(lastName, firstName, email, sexe, age, accountType, mAuth!!.uid.toString())
                    var firebaseMessaging = FirebaseInstanceId.getInstance().token
                    user.registrationToken = firebaseMessaging.toString()
                    // Setting where to submit the user in the firebase realtime database
                    val usersRef = ref.child("users")
                    val userRef = usersRef.child(user.mUserId)
                    userRef.setValue(user)
                    registerView.visibility = View.GONE

                    if (mentorSlected == true) {
                    startActivity(Intent(this, MentorRegisterActivity::class.java))
                    } else if (startupSelected == true) {
                    startActivity(Intent(this, StartupRegisterActivity::class.java))
                    }

                }
            }

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Check if the email is good
     * @param email
     * @return boolean
     */
    private fun isEmailValid(email: String): Boolean {
        return email.toString().contains("@")
    }

    /**
     * Check if the password is good
     * @param password
     * @return boolean
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    companion object {
        private val KEY_USERNAME_ENTRY = "email_entry"
        private val KEY_PASSWORD_ENTRY = "password_entry"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_USERNAME_ENTRY, user_register_edit_text.text.toString())
        outState.putString(KEY_PASSWORD_ENTRY, password_register_edit_text.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }
}


