package com.v1rex.smartincubator.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.v1rex.smartincubator.Activities.MentorProfileActivity
import com.v1rex.smartincubator.Activities.StartupProfileActivity
import com.v1rex.smartincubator.Model.Meeting
import com.v1rex.smartincubator.Model.User
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.MeetingsViewHolder
import kotlinx.android.synthetic.main.fragment_meetings.*


class MeetingsFragment : Fragment() {

    private var mList: RecyclerView? = null
    private var mLoaderMessage: LinearLayout? = null
    private var mReference: DatabaseReference? = null
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder>? = null
    private var options: FirebaseRecyclerOptions<Meeting>? = null
    private var mAuth: FirebaseAuth? = null

    private val databaseMeetings = FirebaseDatabase.getInstance()
    private var ref = databaseMeetings.getReference("Data")
    private var user: User? = null

    private var mLinearLayoutReceived: LinearLayout? = null
    private val mLinearLayoutSented: LinearLayout? = null
    private var mExitReceived: ImageButton? = null
    private var mUpdateReceived: ImageButton? = null
    private var mReceivedUserName: TextView? = null
    private var mReceivedEmail: TextView? = null
    private var mReceivedUserType: TextView? = null
    private var mSeeProfileReceived: TextView? = null
    private var mAcceptButton: RadioButton? = null
    private var mRefuseButton: RadioButton? = null

    private var mEmptyMeetingsText: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val userId: String

        val view  : View? = inflater.inflate(R.layout.fragment_meetings, container, false)
        mLinearLayoutReceived = view!!.findViewById<View>(R.id.linearlayout_received) as LinearLayout

        mExitReceived = view!!.findViewById<View>(R.id.exit_received) as ImageButton
        mUpdateReceived = view!!.findViewById<View>(R.id.update_received) as ImageButton

        mAcceptButton = view!!.findViewById<View>(R.id.accept_received_button) as RadioButton
        mRefuseButton = view!!.findViewById<View>(R.id.refuse_received_button) as RadioButton

        mReceivedUserName = view!!.findViewById<View>(R.id.received_user_name) as TextView
        mReceivedEmail = view!!.findViewById<View>(R.id.received_user_email) as TextView
        mReceivedUserType = view!!.findViewById<View>(R.id.received_user_type) as TextView
        mSeeProfileReceived = view!!.findViewById<View>(R.id.see_profile_received) as TextView

        mEmptyMeetingsText = view!!.findViewById<View>(R.id.empty_meetings_message) as TextView
        mEmptyMeetingsText!!.visibility = View.GONE

        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance()

        //setting where to find meetings informations
        mReference = FirebaseDatabase.getInstance().reference.child("Data").child("users").child(mAuth!!.uid!!).child("meetings")
        mReference!!.keepSynced(true)


        mLoaderMessage = view!!.findViewById<View>(R.id.message_load_progress) as LinearLayout
        mLoaderMessage!!.visibility = View.VISIBLE

        mList = view!!.findViewById<View>(R.id.meetings_recyclerview) as RecyclerView
        mList!!.setHasFixedSize(true)
        mList!!.layoutManager = LinearLayoutManager(this.activity)


        options = FirebaseRecyclerOptions.Builder<Meeting>().setQuery(mReference!!, Meeting::class.java!!).build()

        // setting the firebaseRecyclerAdapter for the showing Mentors
        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder>(options!!) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingsViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_meetings, parent, false)
                return MeetingsViewHolder(view)
            }

            override fun onDataChanged() {
                super.onDataChanged()
                if(itemCount == 0){
                    mLoaderMessage!!.visibility = View.GONE
                    mEmptyMeetingsText!!.visibility = View.VISIBLE

                }
            }


            override fun onBindViewHolder(holder: MeetingsViewHolder, position: Int, model: Meeting) {
                    mLoaderMessage!!.visibility = View.GONE
                    holder.setmPlaceTextView(model.mPlace)
                    holder.setmTypeEditText(model.mType)
                    holder.setmDateEditText(model.mDate)
                    holder.setmStatusEditText(model.accepte)
                    val userId = model.mUserIdSent

                    // open meetings informations
                    holder.itemView.setOnClickListener {
                        // if the user received the meeting then show this
                        if (model.mType == "You received") {
                            // Loading user informations who sent the meetings
                            val valueEventListenerMeeting = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Store user informations on a object
                                    user = dataSnapshot.child(userId!!).getValue<User>(User::class.java)


                                    if (model.accepte == "accepted") {
                                        mAcceptButton!!.isChecked = true
                                    } else if (model.accepte == "refused") {
                                        mRefuseButton!!.isChecked = true
                                    }
                                    mLinearLayoutReceived!!.visibility = View.VISIBLE
                                    mReceivedUserName!!.setText("UserName: " + user!!.mFirstName + " " + user!!.mLastName)
                                    mReceivedEmail!!.setText( "User Email: " + user!!.mEmail)
                                    mReceivedUserType!!.setText("User Type: " + user!!.mAccountType)

                                    mSeeProfileReceived!!.setOnClickListener {
                                        if (user!!.mAccountType == "Startup") {
                                            // Open the mentors profile who sent the meetings
                                            val intent = Intent(activity, StartupProfileActivity::class.java)
                                            intent.putExtra("UserId Startup", user!!.mUserId)
                                            startActivity(intent)
                                        } else if (user!!.mAccountType == "Mentor") {
                                            // Open the mentors profile who sent the meetings
                                            val intent = Intent(activity, MentorProfileActivity::class.java)
                                            intent.putExtra("Mentor userId", user!!.mUserId)
                                            startActivity(intent)
                                        }
                                    }

                                    mExitReceived!!.setOnClickListener { mLinearLayoutReceived!!.visibility = View.INVISIBLE }

                                    // Update data if the user clicked on the green check button
                                    mUpdateReceived!!.setOnClickListener {
                                        //if accepted
                                        if (mAcceptButton!!.isChecked == true) {
                                            // Store meeting informations in object Meeting
                                            val meeting = Meeting(model.mUserIdSent.toString(), model.mUserIdReceived.toString(), model.mPlace.toString(), model.mDate.toString(), "accepted", model.mType.toString())

                                            //Settings where to update meetings informations in the part of the part who sent
                                            val userRef2 = ref.child(meeting.mUserIdSent).child("meetings").child(meeting.mUserIdReceived)
                                            // Update meeting
                                            userRef2.setValue(meeting)

                                            //Settings where to update meetings informations in the part of the part who sent
                                            val userRef = ref.child(meeting.mUserIdReceived).child("meetings").child(meeting.mUserIdSent)
                                            // Update meeting
                                            userRef.setValue(meeting)
                                            mLinearLayoutReceived!!.visibility = View.GONE
                                        } else if (mRefuseButton!!.isChecked == true) {
                                            // Store meeting informations in object Meeting
                                            val meeting = Meeting(model.mUserIdSent.toString(), model.mUserIdReceived.toString(), model.mPlace.toString(), model.mDate.toString(), "refused", model.mType.toString())
                                            //Settings where to update meetings informations in the part of the part who sent
                                            val userRef2 = ref.child(meeting.mUserIdSent).child("meetings").child(meeting.mUserIdReceived)
                                            // Update meeting
                                            userRef2.setValue(meeting)

                                            //Settings where to update meetings informations in the part of the part who sent
                                            val userRef = ref.child(meeting.mUserIdReceived).child("meetings").child(meeting.mUserIdSent)
                                            // Update meeting
                                            userRef.setValue(meeting)
                                            mLinearLayoutReceived!!.visibility = View.GONE

                                        } else {
                                            // if the user who received the meetings didn't update anything so don't send any data to the server
                                            mLinearLayoutReceived!!.visibility = View.GONE
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            }

                            ref = databaseMeetings.getReference("Data").child("users")
                            ref.addValueEventListener(valueEventListenerMeeting)


                        } else if (model.mType == "You sented") {
                            // TODO will update the app for showing a popup where the user who sented the meeeitng can change the informations about it
                        }// if the user sented the meeting then show this
                    }

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
