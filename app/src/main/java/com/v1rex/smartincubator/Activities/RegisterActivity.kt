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

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mUserNameEditText: EditText? = null
    private var mPasswordEditText: EditText? = null
    private var mEmailTextInputLayout: TextInputLayout? = null
    private var mPasswordInputLayout: TextInputLayout? = null
    private var mRegisterBtn: Button? = null
    private var mProgressRegisterLayout: LinearLayout? = null
    private var mRegisterView: LinearLayout? = null
    private var mLoginTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Getting instance of the firebase Auth for registering the User
        mAuth = FirebaseAuth.getInstance()

        mUserNameEditText = findViewById<View>(R.id.user_register_edit_text) as EditText
        mPasswordEditText = findViewById<View>(R.id.password_register_edit_text) as EditText
        mProgressRegisterLayout = findViewById<View>(R.id.progess_register) as LinearLayout
        mRegisterView = findViewById<View>(R.id.registerView) as LinearLayout
        mEmailTextInputLayout = findViewById<View>(R.id.input_layout_email_register) as TextInputLayout
        mPasswordInputLayout = findViewById<View>(R.id.input_layout_password_register) as TextInputLayout
        mLoginTextView = findViewById<View>(R.id.link_login) as TextView

        mLoginTextView!!.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        mRegisterBtn = findViewById<View>(R.id.register_action_btn) as Button
        mRegisterBtn!!.setOnClickListener { register() }

        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString(KEY_USERNAME_ENTRY)
            mUserNameEditText!!.setText(savedEmail)

            val savedPassword = savedInstanceState.getString(KEY_PASSWORD_ENTRY)
            mPasswordEditText!!.setText(savedPassword)
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putString(KEY_USERNAME_ENTRY, mUserNameEditText!!.text.toString())
        outState.putString(KEY_PASSWORD_ENTRY, mPasswordEditText!!.text.toString())

        super.onSaveInstanceState(outState)

    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
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

        val email = mUserNameEditText!!.text.toString()
        val password = mPasswordEditText!!.text.toString()

        if (TextUtils.isEmpty(password)) {
            mPasswordInputLayout!!.error = getString(R.string.error_field_password_required)
            cancel = true
        } else if (!isPasswordValid(password)) {
            mPasswordInputLayout!!.error = getString(R.string.error_invalid_password)
            cancel = true
        }


        if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout!!.error = getString(R.string.error_field_username_required)
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailTextInputLayout!!.error = getString(R.string.error_invalid_email)
            cancel = true
        }

        // if everything is good , register the user
        if (cancel == false) {
            mRegisterView!!.visibility = View.INVISIBLE
            mProgressRegisterLayout!!.visibility = View.VISIBLE

            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                mProgressRegisterLayout!!.visibility = View.GONE
                if (!task.isSuccessful) {
                    mUserNameEditText!!.visibility = View.VISIBLE
                    mPasswordEditText!!.visibility = View.VISIBLE
                    mRegisterBtn!!.visibility = View.VISIBLE
                    mEmailTextInputLayout!!.error = getString(R.string.error_action_registration_failed)
                } else {
                    mRegisterView!!.visibility = View.GONE
                    startActivity(Intent(this@RegisterActivity, UserInformationsActivity::class.java))
                }
            }

        }
    }

    /**
     * Check if the email is good
     * @param email
     * @return boolean
     */
    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.toString().contains("@")
    }

    /**
     * Check if the password is good
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
