package com.v1rex.smartincubator.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_mentor_register.*
import kotlinx.android.synthetic.main.activity_startup_register.*
import java.io.IOException

class StartupRegisterActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_startup_register)

        // gettting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()

        fun chooseImage(){
            var intent : Intent  = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        submit_action_btn_startup.setOnClickListener { submit() }

        upload_photo_startup.setOnClickListener {
            chooseImage()
        }

        profile_image_startup.setOnClickListener {
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
                upload_photo_startup.visibility = View.GONE
                uploaded_startup_photo.visibility = View.VISIBLE
                profile_image_startup.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * A method for submitting the startup informations
     */
    private fun submit() {

        val mStartupName = startup_name_edit_text_startp.text.toString()
        val mDescription = description_edit_text_startp.text.toString()
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
        if (TextUtils.isEmpty(mDomain)) {
            domain_input_text_startup.error = getString(R.string.field_requierd)
            cancel = true
        }
        if (TextUtils.isEmpty(mNeed)) {
            need_input_text_startup.error = getString(R.string.field_requierd)
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

            //Creating a new Startup object
            val startup = Startup(mStartupName, mDescription, mNeed, mDomain,  mAuth!!.uid.toString(), profilePhoto)

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
