package com.v1rex.smartincubator.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.v1rex.smartincubator.Activities.MentorProfileActivity
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.MentorViewHolder


class MentorsFragment : Fragment() {

    private var mList: RecyclerView? = null
    private var mLoaderMentor: LinearLayout? = null
    private var mReference: DatabaseReference? = null
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Mentor, MentorViewHolder>? = null
    private var options: FirebaseRecyclerOptions<Mentor>? = null

    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = storage.reference

    private val IMAGE_PHOTO_FIREBASE : String = "firebase"

    private val REFERENCE_PROFILE_PHOTO : String = "profileImages/"

    private var mEmptyMentorsText: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View? = inflater.inflate(R.layout.fragment_mentors, container, false)

        //Setting where to find data in the realtime database
        mReference = FirebaseDatabase.getInstance().reference.child("Data").child("mentors")
        mReference!!.keepSynced(true)

        mLoaderMentor = view!!.findViewById<View>(R.id.mentor_load_progress) as LinearLayout
        mLoaderMentor!!.visibility = View.VISIBLE

        mList = view!!.findViewById<View>(R.id.mentors_recyclerview) as RecyclerView
        mList!!.setHasFixedSize(true)
        mList!!.layoutManager = LinearLayoutManager(this.activity)

        mEmptyMentorsText = view!!.findViewById<View>(R.id.empty_mentors_message) as TextView
        mEmptyMentorsText!!.visibility = View.GONE

        options = FirebaseRecyclerOptions.Builder<Mentor>().setQuery(mReference!!, Mentor::class.java!!).build()

        // setting the firebaseRecyclerAdapter for the showing Mentors
        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Mentor, MentorViewHolder>(options!!) {
            override fun onBindViewHolder(holder: MentorViewHolder, position: Int, model: Mentor) {
                mLoaderMentor!!.visibility = View.GONE
                mEmptyMentorsText!!.visibility = View.GONE
                holder.setmNameTextView(model.mLastName + " " + model.mFirstName)
                holder.setmCityTextView(model.mCity)
                holder.setmSpecialityTextView(model.mSpeciality)

                var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + model.mUserId)
                var string = model.mProfilePhotoUrl
                if(string == IMAGE_PHOTO_FIREBASE){
                    holder.setImageProfileImageView(referrencePhoto , context)
                }

                // open the mentor profile if the item is clicked
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity, MentorProfileActivity::class.java)
                    intent.putExtra("Mentor userId", model.mUserId)
                    startActivity(intent)
                }

            }

            override fun onDataChanged() {
                super.onDataChanged()
                if(itemCount == 0){
                    mLoaderMentor!!.visibility = View.GONE
                    mEmptyMentorsText!!.visibility = View.VISIBLE

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_mentors_layout, parent, false)
                return MentorViewHolder(view)
            }
        }
        mList!!.adapter = firebaseRecyclerAdapter
        return view
    }

    // Listening for changes in the Realtime Database
    override fun onStart() {
        super.onStart()
        firebaseRecyclerAdapter!!.startListening()
    }

    // Stop listening for changes in the Realtime Database
    override fun onStop() {
        super.onStop()
        firebaseRecyclerAdapter!!.stopListening()
    }
}
