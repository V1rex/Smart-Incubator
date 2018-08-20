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

class StartupRegisterActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    private var mStartupNameInputLayout: TextInputLayout? = null
    private var mAssociateInputLayout: TextInputLayout? = null
    private var mDescriptionInputLayout: TextInputLayout? = null
    private var mWebsiteInputLayout: TextInputLayout? = null
    private var mFacebookInputLayout: TextInputLayout? = null
    private var mDateIncubationInputLayout: TextInputLayout? = null
    private var mJurdiqueInputLayout: TextInputLayout? = null
    private var mCreationDateInputLayout: TextInputLayout? = null
    private var mNumberEmployeesInputLayout: TextInputLayout? = null
    private var mObjectiveInputLayout: TextInputLayout? = null
    private var mFondInputLayout: TextInputLayout? = null
    private var mChiffreInputLayout: TextInputLayout? = null
    private var mDomainInputLayout: TextInputLayout? = null
    private var mNeedInputLayout: TextInputLayout? = null

    private var mStartupNameEditText: EditText? = null
    private var mAssociateEditText: EditText? = null
    private var mDescriptionEditText: EditText? = null
    private var mWebsiteEditText: EditText? = null
    private var mFacebookEditText: EditText? = null
    private var mDateIncubationEditText: EditText? = null
    private var mJurdiqueEditText: EditText? = null
    private var mCreationDateEditText: EditText? = null
    private var mNumberEmployeesEditText: EditText? = null
    private var mObjectiveEditText: EditText? = null
    private var mFondEditText: EditText? = null
    private var mChiffreEditText: EditText? = null
    private var mDomainEditText: EditText? = null
    private var mNeedEditText: EditText? = null

    private var mSubmitStartup: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup_register)
        // gettting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        mStartupNameEditText = findViewById<View>(R.id.startup_name_edit_text_startp) as EditText
        mStartupNameInputLayout = findViewById<View>(R.id.startup_name_input_text_startup) as TextInputLayout

        mAssociateEditText = findViewById<View>(R.id.associate_edit_text_startp) as EditText
        mAssociateInputLayout = findViewById<View>(R.id.associate_input_text_startup) as TextInputLayout

        mDescriptionEditText = findViewById<View>(R.id.description_edit_text_startp) as EditText
        mDescriptionInputLayout = findViewById<View>(R.id.description_input_text_startup) as TextInputLayout

        mStartupNameEditText = findViewById<View>(R.id.startup_name_edit_text_startp) as EditText
        mStartupNameInputLayout = findViewById<View>(R.id.startup_name_input_text_startup) as TextInputLayout

        mWebsiteEditText = findViewById<View>(R.id.website_edit_text_startp) as EditText
        mWebsiteInputLayout = findViewById<View>(R.id.website_input_text_startup) as TextInputLayout

        mFacebookEditText = findViewById<View>(R.id.facebook_edit_text_startp) as EditText
        mFacebookInputLayout = findViewById<View>(R.id.facebook_input_text_startup) as TextInputLayout


        mDateIncubationEditText = findViewById<View>(R.id.date_incubation_edit_text_startp) as EditText
        mDateIncubationInputLayout = findViewById<View>(R.id.date_incubation_input_text_startup) as TextInputLayout

        mJurdiqueEditText = findViewById<View>(R.id.juridique_status_edit_text_startp) as EditText
        mJurdiqueInputLayout = findViewById<View>(R.id.juridique_status_input_text_startup) as TextInputLayout

        mCreationDateEditText = findViewById<View>(R.id.creation_date_edit_text_startp) as EditText
        mCreationDateInputLayout = findViewById<View>(R.id.creation_date_input_text_startup) as TextInputLayout

        mNumberEmployeesEditText = findViewById<View>(R.id.number_employees_edit_text_startp) as EditText
        mNumberEmployeesInputLayout = findViewById<View>(R.id.number_employees_input_text_startup) as TextInputLayout

        mObjectiveEditText = findViewById<View>(R.id.objective_edit_text_startp) as EditText
        mObjectiveInputLayout = findViewById<View>(R.id.objective_input_text_startup) as TextInputLayout

        mFondEditText = findViewById<View>(R.id.fond_edit_text_startp) as EditText
        mFondInputLayout = findViewById<View>(R.id.fond_input_text_startup) as TextInputLayout

        mChiffreEditText = findViewById<View>(R.id.chiffre_edit_text_startp) as EditText
        mChiffreInputLayout = findViewById<View>(R.id.chiffre_input_text_startup) as TextInputLayout

        mDomainEditText = findViewById<View>(R.id.domain_edit_text_startp) as EditText
        mDomainInputLayout = findViewById<View>(R.id.domain_input_text_startup) as TextInputLayout

        mNeedEditText = findViewById<View>(R.id.need_edit_text_startp) as EditText
        mNeedInputLayout = findViewById<View>(R.id.need_input_text_startup) as TextInputLayout

        mSubmitStartup = findViewById<View>(R.id.submit_action_btn_startup) as Button
        mSubmitStartup!!.setOnClickListener { submit() }
    }

    /**
     * A method for submitting the startup informations
     */
    private fun submit() {

        val mStartupName = mStartupNameEditText!!.text.toString()
        val mAssociate = mAssociateEditText!!.text.toString()
        val mDescription = mDescriptionEditText!!.text.toString()
        val mWebsite = mWebsiteEditText!!.text.toString()
        val mPageFacebook = mFacebookEditText!!.text.toString()
        val mDateOfIncubation = mDateIncubationEditText!!.text.toString()
        val mJuridiqueSatatus = mJurdiqueEditText!!.text.toString()
        val mCreationDate = mCreationDateEditText!!.text.toString()
        val mNumberEmployees = mNumberEmployeesEditText!!.text.toString()
        val mObjective = mObjectiveEditText!!.text.toString()
        val mFond = mFondEditText!!.text.toString()
        val mChiffre = mChiffreEditText!!.text.toString()
        val mNeed = mNeedEditText!!.text.toString()
        val mDomain = mDomainEditText!!.text.toString()

        // a boolean for cancelling action if something is wrong
        var cancel = false

        if (TextUtils.isEmpty(mStartupName)) {
            mNeedInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDescription)) {
            mDescriptionInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDateOfIncubation)) {
            mDateIncubationInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mJuridiqueSatatus)) {
            mJurdiqueInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mCreationDate)) {
            mCreationDateInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNumberEmployees)) {
            mNumberEmployeesInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mFond)) {
            mFondInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mChiffre)) {
            mChiffreInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDomain)) {
            mDomainInputLayout!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNeed)) {
            mNeedInputLayout!!.error = getString(R.string.field_requierd)
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
