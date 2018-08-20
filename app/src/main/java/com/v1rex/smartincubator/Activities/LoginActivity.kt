package com.v1rex.smartincubator.Activities


import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


import com.v1rex.smartincubator.R


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mUserNameEditText: EditText? = null
    private var mPasswordEditText: EditText? = null
    private var mLoginBtn: Button? = null
    private var mEmailTextInputLaout: TextInputLayout? = null
    private var mPasswordTextInputLayout: TextInputLayout? = null
    private var mProgressLayout: LinearLayout? = null
    private var mLoginView: LinearLayout? = null
    private var mRegisterLink: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // getting instance of the firebase auth for authenticating the user
        mAuth = FirebaseAuth.getInstance()

        mLoginView = findViewById<View>(R.id.loginView) as LinearLayout
        mEmailTextInputLaout = findViewById<View>(R.id.input_layout_email) as TextInputLayout
        mPasswordTextInputLayout = findViewById<View>(R.id.input_layout_password) as TextInputLayout
        mUserNameEditText = findViewById<View>(R.id.user_login_edit_text) as EditText
        mPasswordEditText = findViewById<View>(R.id.password_login_edit_text) as EditText
        mLoginBtn = findViewById<View>(R.id.login_action_btn) as Button

        mLoginBtn!!.setOnClickListener { login() }

        mRegisterLink = findViewById<View>(R.id.link_register) as TextView
        mRegisterLink!!.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
        mProgressLayout = findViewById<View>(R.id.progress_login) as LinearLayout
        mProgressLayout!!.visibility = View.GONE


        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString(KEY_USERNAME_ENTRY)
            mUserNameEditText!!.setText(savedEmail)

            val savedPassword = savedInstanceState.getString(KEY_PASSWORD_ENTRY)
            mPasswordEditText!!.setText(savedPassword)
        }

    }


    // save the email and password when the user have make the app in the background
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_USERNAME_ENTRY, mUserNameEditText!!.text.toString())
        outState.putString(KEY_PASSWORD_ENTRY, mPasswordEditText!!.text.toString())
        super.onSaveInstanceState(outState)
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

        val emailOrUserName = mUserNameEditText!!.text.toString()
        val password = mPasswordEditText!!.text.toString()

        if (!TextUtils.isEmpty(password)) {
            mPasswordTextInputLayout!!.error = getString(R.string.error_field_password_required)
            cancel = true

        } else if (!isPasswordValid(password)) {
            mPasswordTextInputLayout!!.error = getString(R.string.error_invalid_password)
            cancel = true
        } else {
            cancel = false
        }



        if (TextUtils.isEmpty(emailOrUserName)) {
            mEmailTextInputLaout!!.error = getString(R.string.error_field_username_required)
            cancel = true
        } else if (!isEmailValid(emailOrUserName)) {
            mEmailTextInputLaout!!.error = getString(R.string.error_invalid_email)
            cancel = true
        } else {
            cancel = false
        }

        // if nothing is wrong then login
        if (cancel == false) {
            mUserNameEditText!!.visibility = View.INVISIBLE
            mPasswordEditText!!.visibility = View.INVISIBLE
            mLoginBtn!!.visibility = View.INVISIBLE
            mProgressLayout!!.visibility = View.VISIBLE

            mAuth!!.signInWithEmailAndPassword(emailOrUserName, password).addOnCompleteListener(this) { task ->
                mProgressLayout!!.visibility = View.GONE

                if (!task.isSuccessful) {
                    mUserNameEditText!!.visibility = View.VISIBLE
                    mPasswordEditText!!.visibility = View.VISIBLE
                    mLoginBtn!!.visibility = View.VISIBLE
                    mEmailTextInputLaout!!.error = getString(R.string.error_action_loging_failed)

                } else {

                    mLoginView!!.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, BottonNavigationActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }


        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     * checking the email
     * @param email
     * @return boolean
     */
    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }


    /**
     * checking the password
     * @param password
     * @return boolean
     */
    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    companion object {

        private val KEY_USERNAME_ENTRY = "email_entry"
        private val KEY_PASSWORD_ENTRY = "password_entry"
    }
}
