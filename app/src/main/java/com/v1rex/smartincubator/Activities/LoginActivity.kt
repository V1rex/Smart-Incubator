package com.v1rex.smartincubator.Activities


import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import com.google.firebase.auth.FirebaseAuth

import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_login.*
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager




class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // getting instance of the firebase auth for authenticating the user
        mAuth = FirebaseAuth.getInstance()

        login_action_btn.setOnClickListener { login() }

        link_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        progress_login.visibility = View.GONE

        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString(KEY_USERNAME_ENTRY)
            user_login_edit_text.setText(savedEmail)

            val savedPassword = savedInstanceState.getString(KEY_PASSWORD_ENTRY)
            password_login_edit_text.setText(savedPassword)
        }

    }

    /**
     * a method for login to the firebase by email method
     */
    private fun login() {

        if (mAuth == null) {
            return
        }

        // a boolean for canceling the process of login if something is wrong
        var cancel = false

        val emailOrUserName = user_login_edit_text.text.toString()
        val password = password_login_edit_text.text.toString()

        if(!isNetworkAvailable()){
            input_layout_password.error = getString(R.string.error_internet_required)
            cancel = true
        } else if (!TextUtils.isEmpty(password)) {
            input_layout_password.error = getString(R.string.error_field_password_required)
            cancel = true

        } else if (!isPasswordValid(password)) {
            input_layout_password.error = getString(R.string.error_invalid_password)
            cancel = true

        } else {
            cancel = false
        }


        if (TextUtils.isEmpty(emailOrUserName)) {
            input_layout_email.error = getString(R.string.error_field_username_required)
            cancel = true
        } else if (!isEmailValid(emailOrUserName)) {
            input_layout_email.error = getString(R.string.error_invalid_email)
            cancel = true
        } else {
            cancel = false
        }

        // if nothing is wrong then login
        if (cancel == false) {
            user_login_edit_text.visibility = View.INVISIBLE
            password_login_edit_text.visibility = View.INVISIBLE
            login_action_btn.visibility = View.INVISIBLE
            input_layout_email.visibility = View.INVISIBLE
            input_layout_password.visibility = View.INVISIBLE
            progress_login.visibility = View.VISIBLE

            mAuth!!.signInWithEmailAndPassword(emailOrUserName, password).addOnCompleteListener(this) { task ->
                progress_login.visibility = View.GONE

                if (!task.isSuccessful) {
                    user_login_edit_text.visibility = View.VISIBLE
                    password_login_edit_text.visibility = View.VISIBLE
                    login_action_btn.visibility = View.VISIBLE
                    input_layout_email.visibility = View.VISIBLE
                    input_layout_password.visibility = View.VISIBLE
                    input_layout_email.error = getString(R.string.error_action_loging_failed)

                } else {
                    loginView.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, BottonNavigationActivity::class.java)
                    startActivity(intent)
                    finish()
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
     * checking the email
     * @param email
     * @return boolean
     */
    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    /**
     * checking the password
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

    override fun onBackPressed() {
        finish()
    }

    // save the email and password when the user have make the app in the background
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_USERNAME_ENTRY, user_login_edit_text.text.toString())
        outState.putString(KEY_PASSWORD_ENTRY, password_login_edit_text.text.toString())
        super.onSaveInstanceState(outState)
    }
}
