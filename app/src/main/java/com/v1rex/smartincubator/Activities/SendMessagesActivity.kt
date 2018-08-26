package com.v1rex.smartincubator.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.MeetingsViewHolder
import com.v1rex.smartincubator.ViewHolder.SendMessagesViewHolder
import kotlinx.android.synthetic.main.activity_send_messages.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import android.support.v7.widget.RecyclerView.NO_POSITION
import com.google.firebase.database.*
import com.v1rex.smartincubator.Model.*


class SendMessagesActivity : AppCompatActivity() {

    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Message, SendMessagesViewHolder>? = null

    private val database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val ref = database.getReference("Data")

    private val databaseUser = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null

    private var user: User? = null
    private var startup: Startup? = null
    private var mentor: Mentor? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_messages)

        var nameUser : String? = null
        return_button.setOnClickListener{
            finish()
        }

        val intent = intent
        var name = intent.getStringExtra("name")
        var type = intent.getStringExtra("type")
        var userId = intent.getStringExtra("userId")

        message_name.setText(name)
        message_type.setText(type)

        var refSented = ref.child("Messages")


        message_edit_text.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().trim().isEmpty()){
                    fab_message_mentor.isEnabled = false
                } else {
                    fab_message_mentor.isEnabled = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        fab_message_mentor.setOnClickListener {
            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
            val day = now.get(Calendar.DAY_OF_MONTH)
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)


            val timeSent = System.currentTimeMillis().toString()
            var time : String= "$day/$month/$year $hour:$minute"
            var message1 = Message(message_edit_text.text.toString() , mAuth!!.uid.toString(), userId , time)
            var messageInformations = MessageInformations(name, userId, message1.message)
            message_edit_text.setText("")

            var reference1 = refSented.child(mAuth!!.uid.toString()).child(userId).child(timeSent)
            reference1.setValue(message1)
            var reference2 = refSented.child(userId).child(mAuth!!.uid.toString()).child(timeSent)
            reference2.setValue(message1)

            var reference3 = refSented.child(mAuth!!.uid.toString()).child("Latest messages").child(userId)
            reference3.setValue(messageInformations)

            var reference4 = refSented.child(userId).child("Latest messages").child(mAuth!!.uid.toString())
            messageInformations.userId = mAuth!!.uid.toString()
            messageInformations.name = nameUser.toString()
            reference4.setValue(messageInformations)

            message_list.smoothScrollToPosition(firebaseRecyclerAdapter!!.itemCount)
        }

        var mReference = refSented.child(mAuth!!.uid.toString()).child(userId)
        mReference!!.keepSynced(true)

        var list = message_list as RecyclerView
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true



        var options : FirebaseRecyclerOptions<Message>? = FirebaseRecyclerOptions.Builder<Message>().setQuery(mReference!! , Message::class.java!!).build()


        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Message , SendMessagesViewHolder>(options!!){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMessagesViewHolder {
                var view : View? = null
                var viewHolder : SendMessagesViewHolder? = null
                if(viewType == 1){
                    view = LayoutInflater.from(this@SendMessagesActivity).inflate(R.layout.item_message_sent, parent, false)
                    viewHolder = SendMessagesViewHolder(view!!, viewType)

                } else if(viewType == 2){
                   view = LayoutInflater.from(this@SendMessagesActivity).inflate(R.layout.item_message_received, parent, false)
                    viewHolder = SendMessagesViewHolder(view!! , viewType)
                }



                return viewHolder!!
            }

            override fun onBindViewHolder(holder: SendMessagesViewHolder, position: Int, model: Message) {
                var uid = mAuth!!.uid.toString()
                if(model.userSendId == uid){
                    holder.setMessageTextViewSented(model.message)
                } else {
                    holder.setMessageTextViewReceived(model.message)
                }

            }

            override fun getItemViewType(position: Int): Int {
                var uid = mAuth!!.uid.toString()
                val message = getItem(position)
                if(uid.equals(message.userSendId)){
                    return 1
                } else{
                    return 2
                }
            }

        }

        firebaseRecyclerAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)

                val messageCount = firebaseRecyclerAdapter!!.getItemCount()
                val lastCompletelyVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if (lastCompletelyVisiblePosition == NO_POSITION || positionStart >= messageCount - 1 && lastCompletelyVisiblePosition == positionStart - 1) {
                    list.scrollToPosition(positionStart)
                }

            }
        })

        list.layoutManager = linearLayoutManager
        list.adapter = firebaseRecyclerAdapter

        refUser = databaseUser.getReference("Data").child("users")
        val valueEventListenerMentor = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // using the stored userId for getting the specific startup
                user = dataSnapshot.child(mAuth!!.uid.toString()).getValue<User>(User::class.java)
                nameUser = user!!.mFirstName +" " + user!!.mLastName
                if(user!!.mAccountType == "Startup"){

                    refUser = database.getReference("Data").child("startups")
                    val valueEventListenerMentor = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // using the stored userId for getting the specific startup
                            startup = dataSnapshot.child(mAuth!!.uid.toString()).getValue<Startup>(Startup::class.java)
                            nameUser = startup!!.mStartupName
                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    }
                    // listening for the change in the Startup database
                    refUser!!.addValueEventListener(valueEventListenerMentor)

                } else{
                    refUser = database.getReference("Data").child("mentors")
                    val valueEventListenerMentor = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // using the stored userId for getting the specific startup
                            mentor = dataSnapshot.child(mAuth!!.uid.toString()).getValue<Mentor>(Mentor::class.java)
                            nameUser = mentor!!.mLastName + " " + mentor!!.mFirstName

                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    }
                    // listening for the change in the Startup database
                    refUser!!.addValueEventListener(valueEventListenerMentor)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        // listening for the change in the Startup database
        refUser!!.addValueEventListener(valueEventListenerMentor)


    }

    override fun onStart() {
        super.onStart()
        firebaseRecyclerAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()

        firebaseRecyclerAdapter?.stopListening()
    }
}
