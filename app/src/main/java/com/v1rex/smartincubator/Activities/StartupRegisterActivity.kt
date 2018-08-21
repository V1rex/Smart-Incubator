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
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_startup_register.*

class StartupRegisterActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_register)

        // gettting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        submit_action_btn_startup.setOnClickListener { submit() }
    }

    /**
     * A method for submitting the startup informations
     */
    private fun submit() {

        val mStartupName = startup_name_edit_text_startp.text.toString()
        val mAssociate = associate_edit_text_startp.text.toString()
        val mDescription = description_edit_text_startp.text.toString()
        val mWebsite = website_edit_text_startp.text.toString()
        val mPageFacebook = facebook_edit_text_startp.text.toString()
        val mDateOfIncubation = date_incubation_edit_text_startp.text.toString()
        val mJuridiqueSatatus = juridique_status_edit_text_startp.text.toString()
        val mCreationDate = creation_date_edit_text_startp.text.toString()
        val mNumberEmployees = number_employees_edit_text_startp.text.toString()
        val mObjective = objective_edit_text_startp.text.toString()
        val mFond = fond_edit_text_startp.text.toString()
        val mChiffre = chiffre_edit_text_startp.text.toString()
        val mNeed = need_edit_text_startp.text.toString()
        val mDomain = domain_edit_text_startp.text.toString()

        // a boolean for cancelling action if something is wrong
        var cancel = false

        if (TextUtils.isEmpty(mStartupName)) {
            startup_name_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDescription)) {
            description_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDateOfIncubation)) {
            date_incubation_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mJuridiqueSatatus)) {
            juridique_status_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mCreationDate)) {
            creation_date_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNumberEmployees)) {
            number_employees_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mFond)) {
            fond_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mChiffre)) {
            chiffre_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDomain)) {
            domain_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNeed)) {
            need_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }


        if (cancel == false) {
            //Creating a new Startup object
            val startup = Startup(mStartupName, mAssociate, mDescription, mWebsite, mPageFacebook, mDateOfIncubation, mJuridiqueSatatus, mCreationDate, mNumberEmployees, mObjective, mFond, mChiffre, mAuth!!.uid.toString(), mNeed, mDomain)

            // setting where to store startup informations in the firebase Realtime Database
            val startupsRef = ref.child("startups")
            val startupRef = startupsRef.child(startup.mUserId)
            startupRef.setValue(startup)

            startActivity(Intent(this@StartupRegisterActivity, BottonNavigationActivity::class.java))
        }

    }

    override fun onBackPressed() {

    }
}
