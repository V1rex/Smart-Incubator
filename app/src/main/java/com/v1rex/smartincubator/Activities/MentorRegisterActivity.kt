package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_mentor_register.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class MentorRegisterActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Data")
    private var mAuth: FirebaseAuth? = null

    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = storage.reference

    private val PICK_IMAGE_REQUEST : Int = 71
    private var filePath : Uri? = null

    private val IMAGE_PHOTO_FIREBASE : String = "firebase"

    private val IMAGE_PHOTO_MAN : String = "man"

    private val REFERENCE_PROFILE_PHOTO : String = "profileImages/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_register)

        // get instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        fun chooseImage(){
            var intent : Intent  = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }


        submit_action_btn_mentor.setOnClickListener { submit() }

        upload_photo_mentor.setOnClickListener {
            chooseImage()
        }

        profile_image_mentor.setOnClickListener {
            chooseImage()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_IMAGE_REQUEST && resultCode === Activity.RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                upload_photo_mentor.visibility = View.GONE
                uploaded_mentor_photo.visibility = View.VISIBLE
                profile_image_mentor.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    private fun submit() {
        val lastName = last_name_edit_text_mentor.text.toString()
        val firstName = first_name_edit_text_mentor.text.toString()
        val city = city_edit_text_mentor.text.toString()
        val specialty = speciality_edit_text_mentor.text.toString()
        val email = email_edit_text_mentor.text.toString()
        val phoneNumber = number_phone_edit_text_mentor.text.toString()

        // boolean for cancelling the submit if something is wrong
        var cancel = false

        if (TextUtils.isEmpty(lastName)) {
            last_name_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(firstName)) {
            first_name_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(city)) {
            city_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(specialty)) {
            speciality_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(email)) {
            email_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true

        }
        if (TextUtils.isEmpty(phoneNumber)) {
            number_phone_input_text_mentor.error = getString(R.string.field_requierd)
            cancel = true
        }


        if (cancel == false) {

            var profilePhoto : String = IMAGE_PHOTO_MAN
            if(filePath == null){
                profilePhoto = IMAGE_PHOTO_MAN
            }else if (filePath != null){
                profilePhoto = IMAGE_PHOTO_FIREBASE

                var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mAuth!!.uid.toString())
                referrencePhoto.putFile(filePath!!)
            }

            // Creating a Mentor object
            val mentor = Mentor(city, specialty, lastName, firstName, email, phoneNumber, mAuth!!.uid.toString(), profilePhoto)

            // Setting where to store informations about the mentor in the firebase Realtime Database
            val mentorsRef = ref.child("mentors")
            val mentorRef = mentorsRef.child(mentor.mUserId)
            mentorRef.setValue(mentor)

            startActivity(Intent(this@MentorRegisterActivity, BottonNavigationActivity::class.java))
        }

    }

    override fun onBackPressed() {

    }
}
