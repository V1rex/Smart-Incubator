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
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Activities.SendMessagesActivity
import com.v1rex.smartincubator.Model.Meeting
import com.v1rex.smartincubator.Model.MessageInformations

import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.MeetingsViewHolder
import com.v1rex.smartincubator.ViewHolder.MessagesViewHolder

class MessagesFragment : Fragment() {

    private var mList: RecyclerView? = null
    private var mLoaderMessage: LinearLayout? = null
    private var mReference: DatabaseReference? = null
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<MessageInformations, MessagesViewHolder>? = null
    private var options: FirebaseRecyclerOptions<MessageInformations>? = null
    private var mAuth: FirebaseAuth? = null

    private val databaseMeetings = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)

        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance()

        //setting where to find meetings informations
        mReference = FirebaseDatabase.getInstance().reference.child("Data").child("LatestMessage").child(mAuth!!.uid!!)
        mReference!!.keepSynced(true)
        val query = mReference!!.orderByChild("time")

        mList = view.findViewById<RecyclerView>(R.id.messages_recyclerview) as RecyclerView
        mList!!.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(this.activity)

        mList!!.layoutManager = linearLayoutManager
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        options = FirebaseRecyclerOptions.Builder<MessageInformations>().setQuery(query, MessageInformations::class.java!!).build()

        firebaseRecyclerAdapter = object :FirebaseRecyclerAdapter<MessageInformations,MessagesViewHolder>(options!!){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_message, parent, false)
                return MessagesViewHolder(view)
            }

            override fun onBindViewHolder(holder: MessagesViewHolder, position: Int, model: MessageInformations) {
                holder.setNameTextView(model.name)
                holder.setMessageEditText(model.latestMessage)

                holder.itemView.setOnClickListener {
                    val intent = Intent(activity!!.baseContext, SendMessagesActivity::class.java)
                    intent.putExtra("name", model.name)
                    intent.putExtra("needSpecialty", model.need_speciality)
                    intent.putExtra("userId", model.userId)
                    intent.putExtra("type", model.typeUser)

                    startActivity(intent)
                }


            }
        }

        mList!!.adapter = firebaseRecyclerAdapter

        return view
    }

    override fun onStart() {
        super.onStart()
        firebaseRecyclerAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        firebaseRecyclerAdapter!!.stopListening()
    }


}
