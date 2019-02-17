package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.v1rex.smartincubator.Model.Meeting
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_mentor_profile.*

import java.text.SimpleDateFormat
import java.util.Calendar

class MentorProfileActivity : AppCompatActivity() {

    private var mentor: Mentor? = null
    private val database = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var user: User? = null

    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = storage.reference

    private val IMAGE_PHOTO_FIREBASE : String = "firebase"

    private val REFERENCE_PROFILE_PHOTO : String = "profileImages/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)

        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance()


        // getting the userId for the purpose of the choosing what Startup to show
        val intent = intent
        val userId = intent.getStringExtra("Mentor userId")


        // showing a popup for sending a meeting
        fab_message_mentor.setOnClickListener {
            val intent = Intent(this, SendMessagesActivity::class.java)
            intent.putExtra("name", mentor!!.mFirstName + " " + mentor!!.mLastName)
            intent.putExtra("needSpecialty", mentor!!.mSpeciality)
            intent.putExtra("userId", mentor!!.mUserId)
            intent.putExtra("type","Mentor" )
            startActivity(intent)
        }




        val valueEventListenerMentor = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific mentor
                mentor = dataSnapshot.child(userId).getValue<Mentor>(Mentor::class.java)
                show()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        // loading the specific startups informations
        refUser = database.getReference("Data").child("mentors")

        // listening for the change in the Startup database
        refUser!!.addValueEventListener(valueEventListenerMentor)




        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific mentor
                user = dataSnapshot.child(mAuth!!.uid.toString()).getValue<User>(User::class.java)
                user!!.registrationToken = FirebaseInstanceId.getInstance().token!!
                var ref = refUser!!.child(user!!.mUserId.toString())
                ref.setValue(user)
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        refUser = database.getReference("Data").child("users")

        // listening for the change in the Startup database
        refUser!!.addValueEventListener(valueEventListener)
    }



    /**
     * showing the mentor informations from the Startup object
     */
    private fun show() {
        mentor_name_profile.text = mentor!!.mFirstName + " " + mentor!!.mLastName
        mentor_speciality_profile.text = mentor!!.mSpeciality
        mentor_city_profile.text = mentor!!.mCity
        mentor_email_profile.text = mentor!!.mEmail
        mentor_number_phone_profile.text = mentor!!.mPhoneNumber

        var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + mentor!!.mUserId)
        if(mentor!!.mProfilePhoto == IMAGE_PHOTO_FIREBASE){
            Glide.with(baseContext).load(referrencePhoto)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .placeholder(R.drawable.profile)
                    .fitCenter()
                    .into(profile_photo_mentor)
        }

    }
}
