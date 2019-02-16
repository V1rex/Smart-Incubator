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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.v1rex.smartincubator.Activities.StartupProfileActivity
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.StartupViewHolder

class StartupsFragment : Fragment() {

    private var mList: RecyclerView? = null
    private var mLoaderStartup: LinearLayout? = null
    private var mReference: DatabaseReference? = null
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Startup, StartupViewHolder>? = null
    private var options: FirebaseRecyclerOptions<Startup>? = null

    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference : StorageReference = storage.reference

    private val IMAGE_PHOTO_MAN : String = "man"

    private val IMAGE_PHOTO_FIREBASE : String = "firebase"

    private val REFERENCE_PROFILE_PHOTO : String = "profileImages/"

    private var mAuth: FirebaseAuth? = null

    private var mEmptyStartupsText: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View? = inflater.inflate(R.layout.fragment_startups, container, false)

        mAuth = FirebaseAuth.getInstance()

        //Setting where to find data in the realtime database
        mReference = FirebaseDatabase.getInstance().reference.child("Data").child("startups")
        mReference!!.keepSynced(true)

        // showing a waiting loader
        mLoaderStartup = view!!.findViewById<View>(R.id.startup_load_progress) as LinearLayout
        mLoaderStartup!!.visibility = View.VISIBLE

        mList = view!!.findViewById<View>(R.id.startups_recyclerview) as RecyclerView
        mList!!.setHasFixedSize(true)
        mList!!.layoutManager = LinearLayoutManager(this.activity)

        mEmptyStartupsText = view!!.findViewById<View>(R.id.empty_startups_message) as TextView
        mEmptyStartupsText!!.visibility = View.GONE

        options = FirebaseRecyclerOptions.Builder<Startup>().setQuery(mReference!!, Startup::class.java!!).build()



        // setting the firebaseRecyclerAdapter for the showing Startups
        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Startup, StartupViewHolder>(options!!) {
            override fun onBindViewHolder(holder: StartupViewHolder, position: Int, model: Startup) {
                mLoaderStartup!!.visibility = View.GONE
                mEmptyStartupsText!!.visibility = View.GONE
                holder.setmNeedTextView("Need :" + " " + model.mNeed)
                holder.setmNameTextView(model.mStartupName)
                holder.setmDomainTextView("Domain :" + " " + model.mDomain)

                var referrencePhoto : StorageReference = storageReference.child(REFERENCE_PROFILE_PHOTO + model.mUserId)
                var string = model.mPhotoProfileUrl
                if(string == IMAGE_PHOTO_FIREBASE){
                   holder.setImageProfileImageView(referrencePhoto , context)
                }


                // open the startup profile if the item is clicked
                holder.itemView.setOnClickListener {
                    val intent = Intent(activity, StartupProfileActivity::class.java)
                    intent.putExtra("UserId Startup", model.mUserId)
                    startActivity(intent)
                }
            }


            override fun onDataChanged() {
                super.onDataChanged()
                if(itemCount == 0){
                    mLoaderStartup!!.visibility = View.GONE
                    mEmptyStartupsText!!.visibility = View.VISIBLE

                }
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartupViewHolder {

                val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_startups_layout, parent, false)
                return StartupViewHolder(view)
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

