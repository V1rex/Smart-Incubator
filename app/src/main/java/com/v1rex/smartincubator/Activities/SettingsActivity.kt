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
import android.widget.RelativeLayout

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R

class SettingsActivity : AppCompatActivity() {

    private var accountType: String? = null
    private val database = FirebaseDatabase.getInstance()
    private var refUsers: DatabaseReference? = null
    private var refMentors: DatabaseReference? = null
    private var refStartups: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mentor: Mentor? = null
    private var startup: Startup? = null

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
    private var valueEventListenerUser: ValueEventListener? = null
    private var valueEventListenerMentor: ValueEventListener? = null
    private var valueEventListenerStartup: ValueEventListener? = null

    private var mLastNameEditText: EditText? = null
    private var mFirstNameEditText: EditText? = null
    private var mCityEditText: EditText? = null
    private var mSpecialityEditText: EditText? = null
    private var mEmailEditText: EditText? = null
    private var mPhoneNumberEditText: EditText? = null
    private var mLastNameTextInput: TextInputLayout? = null
    private var mFirstNameTextInput: TextInputLayout? = null
    private var mCityTextInput: TextInputLayout? = null
    private var mSpecialityTextInput: TextInputLayout? = null
    private var mEmailTextInput: TextInputLayout? = null
    private var mPhoneNumberTextInput: TextInputLayout? = null
    private var mSubmitMentorBtn: Button? = null


    private var mMentorInformationsRelativeLayout: View? = null
    private var mStartupInformationsRelativeLayout: View? = null
    private var mLoadingData: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance()

        mMentorInformationsRelativeLayout = findViewById<View>(R.id.mentor_layout_settings) as RelativeLayout
        mStartupInformationsRelativeLayout = findViewById<View>(R.id.startup_layout_settings) as RelativeLayout
        mLoadingData = findViewById<View>(R.id.progress_loading) as LinearLayout

        // getting user infromations for the purpose of getting the user account type
        refUsers = database.getReference("Data").child("users")

        valueEventListenerUser = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.child(mAuth!!.uid!!).getValue<User>(User::class.java)
                accountType = user!!.mAccountType
                choose()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        }

        refUsers!!.addValueEventListener(valueEventListenerUser)


    }

    /**
     * choosing wich view to display and load mentor or startup informations
     */
    private fun choose() {
        //if account type is a startup show the view for it
        if (accountType == "Startup") {
            mStartupInformationsRelativeLayout!!.visibility = View.VISIBLE
            valueEventListenerStartup = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    startup = dataSnapshot.child(mAuth!!.uid!!).getValue<Startup>(Startup::class.java)
                    openStartup()
                    mLoadingData!!.visibility = View.GONE
                    refStartups!!.removeEventListener(this)


                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            refStartups = database.getReference("Data").child("startups")

            refStartups!!.addValueEventListener(valueEventListenerStartup)

        } else if (accountType == "Mentor") {
            mMentorInformationsRelativeLayout!!.visibility = View.VISIBLE

            valueEventListenerMentor = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    mentor = dataSnapshot.child(mAuth!!.uid!!).getValue<Mentor>(Mentor::class.java)
                    openMentor()
                    mLoadingData!!.visibility = View.GONE
                    refMentors!!.removeEventListener(this)

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            }
            refMentors = database.getReference("Data").child("mentors")
            refMentors!!.addValueEventListener(valueEventListenerMentor)


        }//if account type is a mentor show the view for it
    }

    /**
     * show mentor informations in the text field etc.
     */
    private fun openMentor() {
        if (mentor != null) {
            mLastNameEditText = findViewById<View>(R.id.last_name_edit_text_mentor_settings) as EditText
            mLastNameTextInput = findViewById<View>(R.id.last_name_input_text_mentor_settings) as TextInputLayout
            mLastNameEditText!!.setText(mentor!!.mLastName)

            mFirstNameEditText = findViewById<View>(R.id.first_name_edit_text_mentor_settings) as EditText
            mFirstNameTextInput = findViewById<View>(R.id.first_name_input_text_mentor_settings) as TextInputLayout
            mFirstNameEditText!!.setText(mentor!!.mFirstName)

            mCityEditText = findViewById<View>(R.id.city_edit_text_mentor_settings) as EditText
            mCityTextInput = findViewById<View>(R.id.city_input_text_mentor_settings) as TextInputLayout
            mCityEditText!!.setText(mentor!!.mCity)

            mSpecialityEditText = findViewById<View>(R.id.speciality_edit_text_mentor_settings) as EditText
            mSpecialityTextInput = findViewById<View>(R.id.speciality_input_text_mentor_settings) as TextInputLayout
            mSpecialityEditText!!.setText(mentor!!.mSpeciality)

            mEmailEditText = findViewById<View>(R.id.email_edit_text_mentor_settings) as EditText
            mEmailTextInput = findViewById<View>(R.id.email_input_text_mentor_settings) as TextInputLayout
            mEmailEditText!!.setText(mentor!!.mEmail)

            mPhoneNumberEditText = findViewById<View>(R.id.number_phone_edit_text_mentor_settings) as EditText
            mPhoneNumberTextInput = findViewById<View>(R.id.number_phone_input_text_mentor_settings) as TextInputLayout
            mPhoneNumberEditText!!.setText(mentor!!.mPhoneNumber)
        } else {


            mLastNameEditText = findViewById<View>(R.id.last_name_edit_text_mentor_settings) as EditText
            mLastNameTextInput = findViewById<View>(R.id.last_name_input_text_mentor_settings) as TextInputLayout

            mFirstNameEditText = findViewById<View>(R.id.first_name_edit_text_mentor_settings) as EditText
            mFirstNameTextInput = findViewById<View>(R.id.first_name_input_text_mentor_settings) as TextInputLayout


            mCityEditText = findViewById<View>(R.id.city_edit_text_mentor_settings) as EditText
            mCityTextInput = findViewById<View>(R.id.city_input_text_mentor_settings) as TextInputLayout


            mSpecialityEditText = findViewById<View>(R.id.speciality_edit_text_mentor_settings) as EditText
            mSpecialityTextInput = findViewById<View>(R.id.speciality_input_text_mentor_settings) as TextInputLayout


            mEmailEditText = findViewById<View>(R.id.email_edit_text_mentor_settings) as EditText
            mEmailTextInput = findViewById<View>(R.id.email_input_text_mentor_settings) as TextInputLayout


            mPhoneNumberEditText = findViewById<View>(R.id.number_phone_edit_text_mentor_settings) as EditText
            mPhoneNumberTextInput = findViewById<View>(R.id.number_phone_input_text_mentor_settings) as TextInputLayout

        }


        mSubmitMentorBtn = findViewById<View>(R.id.submit_action_btn_mentor_settings) as Button
        mSubmitMentorBtn!!.setOnClickListener { submitMentor() }


    }

    /**
     * show startup informations in the text field etc.
     */
    private fun openStartup() {
        if (startup != null) {
            mStartupNameEditText = findViewById<View>(R.id.startup_name_edit_text_startp_settings) as EditText
            mStartupNameInputLayout = findViewById<View>(R.id.startup_name_input_text_startup_settings) as TextInputLayout
            mStartupNameEditText!!.setText(startup!!.mStartupName)


            mAssociateEditText = findViewById<View>(R.id.associate_edit_text_startp_settings) as EditText
            mAssociateInputLayout = findViewById<View>(R.id.associate_input_text_startup_settings) as TextInputLayout
            mAssociateEditText!!.setText(startup!!.mAssociate)

            mDescriptionEditText = findViewById<View>(R.id.description_edit_text_startp_settings) as EditText
            mDescriptionInputLayout = findViewById<View>(R.id.description_input_text_startup_settings) as TextInputLayout
            mDescriptionEditText!!.setText(startup!!.mDescription)


            mWebsiteEditText = findViewById<View>(R.id.website_edit_text_startp_settings) as EditText
            mWebsiteInputLayout = findViewById<View>(R.id.website_input_text_startup_settings) as TextInputLayout
            mWebsiteEditText!!.setText(startup!!.mWebsite)

            mFacebookEditText = findViewById<View>(R.id.facebook_edit_text_startp_settings) as EditText
            mFacebookInputLayout = findViewById<View>(R.id.facebook_input_text_startup_settings) as TextInputLayout
            mFacebookEditText!!.setText(startup!!.mPageFacebook)

            mDateIncubationEditText = findViewById<View>(R.id.date_incubation_edit_text_startp_settings) as EditText
            mDateIncubationInputLayout = findViewById<View>(R.id.date_incubation_input_text_startup_settings) as TextInputLayout
            mDateIncubationEditText!!.setText(startup!!.mDateOfIncubation)

            mJurdiqueEditText = findViewById<View>(R.id.juridique_status_edit_text_startp_settings) as EditText
            mJurdiqueInputLayout = findViewById<View>(R.id.juridique_status_input_text_startup_settings) as TextInputLayout
            mJurdiqueEditText!!.setText(startup!!.mJuridiqueSatatus)

            mCreationDateEditText = findViewById<View>(R.id.creation_date_edit_text_startp_settings) as EditText
            mCreationDateInputLayout = findViewById<View>(R.id.creation_date_input_text_startup_settings) as TextInputLayout
            mCreationDateEditText!!.setText(startup!!.mCreationDate)

            mNumberEmployeesEditText = findViewById<View>(R.id.number_employees_edit_text_startp_settings) as EditText
            mNumberEmployeesInputLayout = findViewById<View>(R.id.number_employees_input_text_startup_settings) as TextInputLayout
            mNumberEmployeesEditText!!.setText(startup!!.mNumberEmployees)

            mObjectiveEditText = findViewById<View>(R.id.objective_edit_text_startp_settings) as EditText
            mObjectiveInputLayout = findViewById<View>(R.id.objective_input_text_startup_settings) as TextInputLayout
            mObjectiveEditText!!.setText(startup!!.mObjective)

            mFondEditText = findViewById<View>(R.id.fond_edit_text_startp_settings) as EditText
            mFondInputLayout = findViewById<View>(R.id.fond_input_text_startup_settings) as TextInputLayout
            mFondEditText!!.setText(startup!!.mFond)

            mChiffreEditText = findViewById<View>(R.id.chiffre_edit_text_startp_settings) as EditText
            mChiffreInputLayout = findViewById<View>(R.id.chiffre_input_text_startup_settings) as TextInputLayout
            mChiffreEditText!!.setText(startup!!.mChiffre)

            mDomainEditText = findViewById<View>(R.id.domain_edit_text_startp_settings) as EditText
            mDomainInputLayout = findViewById<View>(R.id.domain_input_text_startup_settings) as TextInputLayout
            mDomainEditText!!.setText(startup!!.mDomain)

            mNeedEditText = findViewById<View>(R.id.need_edit_text_startp_settings) as EditText
            mNeedInputLayout = findViewById<View>(R.id.need_input_text_startup_settings) as TextInputLayout
            mNeedEditText!!.setText(startup!!.mNeed)

        }
        mStartupNameEditText = findViewById<View>(R.id.startup_name_edit_text_startp_settings) as EditText
        mStartupNameInputLayout = findViewById<View>(R.id.startup_name_input_text_startup_settings) as TextInputLayout


        mAssociateEditText = findViewById<View>(R.id.associate_edit_text_startp_settings) as EditText
        mAssociateInputLayout = findViewById<View>(R.id.associate_input_text_startup_settings) as TextInputLayout


        mDescriptionEditText = findViewById<View>(R.id.description_edit_text_startp_settings) as EditText
        mDescriptionInputLayout = findViewById<View>(R.id.description_input_text_startup_settings) as TextInputLayout



        mWebsiteEditText = findViewById<View>(R.id.website_edit_text_startp_settings) as EditText
        mWebsiteInputLayout = findViewById<View>(R.id.website_input_text_startup_settings) as TextInputLayout


        mFacebookEditText = findViewById<View>(R.id.facebook_edit_text_startp_settings) as EditText
        mFacebookInputLayout = findViewById<View>(R.id.facebook_input_text_startup_settings) as TextInputLayout


        mDateIncubationEditText = findViewById<View>(R.id.date_incubation_edit_text_startp_settings) as EditText
        mDateIncubationInputLayout = findViewById<View>(R.id.date_incubation_input_text_startup_settings) as TextInputLayout


        mJurdiqueEditText = findViewById<View>(R.id.juridique_status_edit_text_startp_settings) as EditText
        mJurdiqueInputLayout = findViewById<View>(R.id.juridique_status_input_text_startup_settings) as TextInputLayout


        mCreationDateEditText = findViewById<View>(R.id.creation_date_edit_text_startp_settings) as EditText
        mCreationDateInputLayout = findViewById<View>(R.id.creation_date_input_text_startup_settings) as TextInputLayout


        mNumberEmployeesEditText = findViewById<View>(R.id.number_employees_edit_text_startp_settings) as EditText
        mNumberEmployeesInputLayout = findViewById<View>(R.id.number_employees_input_text_startup_settings) as TextInputLayout


        mObjectiveEditText = findViewById<View>(R.id.objective_edit_text_startp_settings) as EditText
        mObjectiveInputLayout = findViewById<View>(R.id.objective_input_text_startup_settings) as TextInputLayout


        mFondEditText = findViewById<View>(R.id.fond_edit_text_startp_settings) as EditText
        mFondInputLayout = findViewById<View>(R.id.fond_input_text_startup_settings) as TextInputLayout


        mChiffreEditText = findViewById<View>(R.id.chiffre_edit_text_startp_settings) as EditText
        mChiffreInputLayout = findViewById<View>(R.id.chiffre_input_text_startup_settings) as TextInputLayout


        mDomainEditText = findViewById<View>(R.id.domain_edit_text_startp_settings) as EditText
        mDomainInputLayout = findViewById<View>(R.id.domain_input_text_startup_settings) as TextInputLayout


        mNeedEditText = findViewById<View>(R.id.need_edit_text_startp_settings) as EditText
        mNeedInputLayout = findViewById<View>(R.id.need_input_text_startup_settings) as TextInputLayout


        mSubmitStartup = findViewById<View>(R.id.submit_action_btn_startup_settings) as Button
        mSubmitStartup!!.setOnClickListener { submitStartup() }

    }

    /**
     * update mentor informations
     */
    private fun submitMentor() {

        val lastName = mLastNameEditText!!.text.toString()
        val firstName = mFirstNameEditText!!.text.toString()
        val city = mCityEditText!!.text.toString()
        val specialty = mSpecialityEditText!!.text.toString()
        val email = mEmailEditText!!.text.toString()
        val phoneNumber = mPhoneNumberEditText!!.text.toString()
        var cancel = false

        if (TextUtils.isEmpty(lastName)) {
            mLastNameTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(city)) {
            mCityTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(specialty)) {
            mSpecialityTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(email)) {
            mEmailTextInput!!.error = getString(R.string.field_requierd)
            cancel = true

        }
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberTextInput!!.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (cancel == false) {
            refMentors = database.getReference("Data")
            val mentor = Mentor(city, specialty, lastName, firstName, email, phoneNumber, mAuth!!.uid.toString())
            val mentorsRef = refMentors!!.child("mentors")
            val mentorRef = mentorsRef.child(mentor.mUserId)


            mentorRef.setValue(mentor)
            startActivity(Intent(this@SettingsActivity, BottonNavigationActivity::class.java))
            finish()
        }

    }


    /**
     * update startup informations
     */
    private fun submitStartup() {
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
            val startup = Startup(mStartupName, mAssociate, mDescription, mWebsite, mPageFacebook, mDateOfIncubation, mJuridiqueSatatus, mCreationDate, mNumberEmployees, mObjective, mFond, mChiffre, mAuth!!.uid.toString(), mNeed, mDomain)


            refStartups = database.getReference("Data")

            val startupsRef = refStartups!!.child("startups")
            val startupRef = startupsRef.child(startup.mUserId)
            startupRef.setValue(startup)

            startActivity(Intent(this@SettingsActivity, BottonNavigationActivity::class.java))
        }

    }
}
