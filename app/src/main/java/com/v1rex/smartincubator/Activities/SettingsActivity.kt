package com.v1rex.smartincubator.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.graphics.Bitmap
import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File


class SettingsActivity : AppCompatActivity() {

    private var accountType: String? = null
    private val database = FirebaseDatabase.getInstance()
    private var refUsers: DatabaseReference? = null
    private var refMentors: DatabaseReference? = null
    private var refStartups: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mentor: Mentor? = null
    private var startup: Startup? = null

    private var valueEventListenerUser: ValueEventListener? = null
    private var valueEventListenerMentor: ValueEventListener? = null
    private var valueEventListenerStartup: ValueEventListener? = null

    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = storage.reference

    private val IMAGE_PHOTO_FIREBASE : String = "firebase"

    private val REFERENCE_PROFILE_PHOTO : String = "profileImages/"

    private val PICK_IMAGE_REQUEST : Int = 71
    private var filePath : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance()

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

        refUsers!!.addValueEventListener(valueEventListenerUser as ValueEventListener)


        fun chooseImage(){
            var intent : Intent  = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        profile_image_startup_settings.setOnClickListener {
            chooseImage()
        }

        profile_image_mentor_settings.setOnClickListener {
            chooseImage()
        }

    }

    /**
     * choosing wich view to display and load mentor or startup informations
     */
    private fun choose() {
        //if account type is a startup show the view for it
        if (accountType == "Startup") {
            startup_layout_settings.visibility = View.VISIBLE
            valueEventListenerStartup = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    startup = dataSnapshot.child(mAuth!!.uid!!).getValue<Startup>(Startup::class.java)
                    var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mAuth!!.uid.toString())
                    if(startup!!.mPhotoProfile == IMAGE_PHOTO_FIREBASE){
                        Glide.with(baseContext).load(referrencePhoto)
                                .apply(RequestOptions.skipMemoryCacheOf(true))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .placeholder(R.drawable.profile)
                                .fitCenter()
                                .into(profile_image_startup_settings)
                    }
                    openStartup()
                    progress_loading.visibility = View.GONE
                    refStartups!!.removeEventListener(this)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            refStartups = database.getReference("Data").child("startups")

            refStartups!!.addValueEventListener(valueEventListenerStartup as ValueEventListener)
        } else if (accountType == "Mentor") {
            mentor_layout_settings.visibility = View.VISIBLE

            valueEventListenerMentor = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mentor = dataSnapshot.child(mAuth!!.uid!!).getValue<Mentor>(Mentor::class.java)
                    var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mAuth!!.uid.toString())
                    if(mentor!!.mProfilePhoto == IMAGE_PHOTO_FIREBASE){
                        Glide.with(baseContext).load(referrencePhoto)
                                .apply(RequestOptions.skipMemoryCacheOf(true))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .placeholder(R.drawable.profile)
                                .fitCenter()
                                .into(profile_image_mentor_settings)
                    }
                    openMentor()
                    progress_loading.visibility = View.GONE
                    refMentors!!.removeEventListener(this)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            refMentors = database.getReference("Data").child("mentors")
            refMentors!!.addValueEventListener(valueEventListenerMentor as ValueEventListener)


        }//if account type is a mentor show the view for it
    }

    /**
     * show mentor informations in the text field etc.
     */
    private fun openMentor() {
        if (mentor != null) {
            last_name_edit_text_mentor_settings.setText(mentor!!.mLastName)

            first_name_edit_text_mentor_settings.setText(mentor!!.mFirstName)

            city_edit_text_mentor_settings.setText(mentor!!.mCity)

            speciality_edit_text_mentor_settings.setText(mentor!!.mSpeciality)

            email_edit_text_mentor_settings.setText(mentor!!.mEmail)

            number_phone_edit_text_mentor_settings.setText(mentor!!.mPhoneNumber)
        }

        submit_action_btn_mentor_settings.setOnClickListener { submitMentor() }


    }

    /**
     * show startup informations in the text field etc.
     */
    private fun openStartup() {
        if (startup != null) {
            startup_name_edit_text_startp_settings.setText(startup!!.mStartupName)

            description_edit_text_startp_settings.setText(startup!!.mDescription)

            domain_edit_text_startp_settings.setText(startup!!.mDomain)

            need_edit_text_startp_settings.setText(startup!!.mNeed)

        }
        submit_action_btn_startup_settings.setOnClickListener { submitStartup() }

    }

    /**
     * update mentor informations
     */
    private fun submitMentor() {
        val lastName = last_name_edit_text_mentor_settings.text.toString()
        val firstName = first_name_edit_text_mentor_settings.text.toString()
        val city = city_edit_text_mentor_settings.text.toString()
        val specialty = speciality_edit_text_mentor_settings.text.toString()
        val email = email_edit_text_mentor_settings.text.toString()
        val phoneNumber = number_phone_edit_text_mentor_settings.text.toString()
        var cancel = false

        if (TextUtils.isEmpty(lastName)) {
            last_name_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(firstName)) {
            first_name_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(city)) {
            city_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(specialty)) {
            speciality_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(email)) {
            email_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true

        }
        if (TextUtils.isEmpty(phoneNumber)) {
            number_phone_input_text_mentor_settings.error = getString(R.string.field_requierd)
            cancel = true
        }

        if (cancel == false) {

            var profilePhoto : String = mentor!!.mProfilePhoto
            if (filePath != null){
                profilePhoto = IMAGE_PHOTO_FIREBASE

                var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mAuth!!.uid.toString())
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val data = byteArrayOutputStream.toByteArray()
                referrencePhoto.putBytes(data)
            }

            val mentor = Mentor(city, specialty, lastName, firstName, email, phoneNumber, mAuth!!.uid.toString(), profilePhoto)

            refMentors = database.getReference("Data")
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

        val mStartupName = startup_name_edit_text_startp_settings.text.toString()
        val mDescription = description_edit_text_startp_settings.text.toString()
        val mNeed = need_edit_text_startp_settings.text.toString()
        val mDomain = domain_edit_text_startp_settings.text.toString()

        var cancel = false

        if (TextUtils.isEmpty(mStartupName)) {
            need_input_text_startup_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDescription)) {
            description_input_text_startup_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mDomain)) {
            domain_input_text_startup_settings.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNeed)) {
            need_input_text_startup_settings.error = getString(R.string.field_requierd)
            cancel = true
        }


        if (cancel == false) {
            var profilePhoto : String = startup!!.mPhotoProfile
            if (filePath != null){
                profilePhoto = IMAGE_PHOTO_FIREBASE

                var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mAuth!!.uid.toString())
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val data = byteArrayOutputStream.toByteArray()
                referrencePhoto.putBytes(data)
            }
            val startup = Startup(mStartupName, mDescription, mAuth!!.uid.toString(), mNeed, mDomain , profilePhoto)

            refStartups = database.getReference("Data")

            val startupsRef = refStartups!!.child("startups")
            val startupRef = startupsRef.child(startup.mUserId)
            startupRef.setValue(startup)


            startActivity(Intent(this@SettingsActivity, BottonNavigationActivity::class.java))
            finish()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            try {
                var file = File(filePath!!.path)
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
//                val byteArrayOutputStream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
//                val data = byteArrayOutputStream.toByteArray()
//                var bitmapFinal = BitmapFactory.decodeByteArray(data,0, data.size)


                Glide.with(this).load(filePath).fitCenter().into(profile_image_startup_settings)
                Glide.with(this).load(filePath).fitCenter().into(profile_image_mentor_settings)
//                profile_image_startup_settings.setImageBitmap(bitmapFinal)
//                profile_image_mentor_settings.setImageBitmap(bitmapFinal)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    override fun onBackPressed() {
        finish()
    }




}
