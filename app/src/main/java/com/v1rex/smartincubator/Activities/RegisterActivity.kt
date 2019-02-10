package com.v1rex.smartincubator.Activities

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
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

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
                    registerView.visibility = View.GONE
                    startActivity(Intent(this@RegisterActivity, UserInformationsActivity::class.java))
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
