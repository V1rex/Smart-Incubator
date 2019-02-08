package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.SendMessagesViewHolder
import kotlinx.android.synthetic.main.activity_send_messages.*
import java.util.*
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.text.TextUtils
import com.google.firebase.database.*
import com.v1rex.smartincubator.Model.*
import java.text.SimpleDateFormat


class SendMessagesActivity : AppCompatActivity() {

    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Message, SendMessagesViewHolder>? = null

    private val database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val ref = database.getReference("Data")
    private val refNotif = database.getReference("Data")

    private val databaseUser = FirebaseDatabase.getInstance()
    private var refUser: DatabaseReference? = null

    private var user: User? = null
    private var startup: Startup? = null
    private var mentor: Mentor? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_messages)

        var nameUser : String? = null
        var need_specialityUser : String? = null
        var typeUser : String? = null
        var registrationTokenUser : String? = null

        return_button.setOnClickListener{
            finish()
        }

        val intent = intent
        var name = intent.getStringExtra("name")
        var need_speciality = intent.getStringExtra("needSpecialty")
        var type = intent.getStringExtra("type")
        var userId = intent.getStringExtra("userId")
        var registerToken = intent.getStringExtra("registerToken")

        message_name.setText(name)
        message_need_specialty.setText(need_speciality)

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

        more_info_button.setOnClickListener{
            var intent : Intent? = null
            when(type){
                "Mentor" -> {intent = Intent(this, MentorProfileActivity::class.java)
                    intent.putExtra("Mentor userId", userId)
                startActivity(intent)}
                "Startup" -> {intent = Intent(this, StartupProfileActivity::class.java)
                    intent.putExtra("UserId Startup", userId)
                startActivity(intent)}




            }
        }

        fab_message_mentor.setOnClickListener {


            val now = Date()

            val timeSent = System.currentTimeMillis().toString()
            var time : String= SimpleDateFormat("yyyy-MM-dd HH:mm").format(now)
            var message1 = Message(message_edit_text.text.toString() , mAuth!!.uid.toString(), userId , time)
            var messageInformations = MessageInformations(name, need_speciality, userId, type, timeSent , message1.message)

            var notification = Notification(mAuth!!.uid.toString(), userId, registerToken , message_edit_text.text.toString(), nameUser.toString(), need_specialityUser.toString() , typeUser.toString() )
            message_edit_text.setText("")



            var reference1 = refSented.child(mAuth!!.uid.toString()).child(userId).child(timeSent)
            message1.parent = userId
            reference1.setValue(message1)
            var reference2 = refSented.child(userId).child(mAuth!!.uid.toString()).child(timeSent)
            message1.parent = mAuth!!.uid.toString()
            reference2.setValue(message1)

            var refLatest = ref.child("LatestMessage")

            var reference3 = refLatest.child(mAuth!!.uid.toString()).child(userId)
            reference3.setValue(messageInformations)

            var reference4 = refLatest.child(userId).child(mAuth!!.uid.toString())
            messageInformations.userId = mAuth!!.uid.toString()

            messageInformations.name = nameUser.toString()
            messageInformations.need_speciality = need_specialityUser.toString()
            messageInformations.typeUser = typeUser.toString()
            reference4.setValue(messageInformations)

            var ref = refNotif.child("Notifications").child(notification.receiverUid).child(notification.senderUid).child(timeSent)
            ref.setValue(notification)

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
                    holder.setMessageTimeTextView(model.sentTime)
                } else {
                    holder.setMessageTextViewReceived(model.message)
                    holder.setMessageTimeTextView(model.sentTime)

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
                need_specialityUser = user!!.mAccountType
                typeUser = user!!.mAccountType
                registrationTokenUser = user!!.registrationToken

                refUser = databaseUser.getReference("Data").child("users")
                val valueEventListenerReceiver = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // using the stored userId for getting the specific startup
                        var user2 = dataSnapshot.child(userId).getValue<User>(User::class.java)
                        registerToken = user2!!.registrationToken
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
                // listening for the change in the Startup database
                refUser!!.addValueEventListener(valueEventListenerReceiver)



                if(user!!.mAccountType == "Startup"){

                    refUser = database.getReference("Data").child("startups")
                    val valueEventListenerMentor = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // using the stored userId for getting the specific startup
                            startup = dataSnapshot.child(mAuth!!.uid.toString()).getValue<Startup>(Startup::class.java)
                            nameUser = startup!!.mStartupName
                            need_specialityUser = startup!!.mNeed
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
                            need_specialityUser = mentor!!.mSpeciality

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



        send_meetings_button.setOnClickListener {
            meeting_linearlayout.visibility = View.VISIBLE
        }

        exit_mettings.setOnClickListener {
            meeting_linearlayout.visibility = View.GONE
        }

        send_meetings.setOnClickListener {
            var meetingPlace : String = meeting_place_edit_text.text.toString()

            if (TextUtils.isEmpty(meetingPlace)) {
                input_layout_place_meeting.error = getString(R.string.field_requierd)
            }else{
                val day = date_picker.dayOfMonth
                val month = date_picker.month
                val year = date_picker.year
                val hour = time_picker.currentHour
                val minute = time_picker.currentMinute

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute)

                val date = SimpleDateFormat("yyyy.MM.dd 'at' hh:mm")
                val dateAndTime = date.format(calendar.time)

                // creating a meeting object for the user who will receive the meeting
                val meeting = Meeting(mAuth!!.uid.toString(), userId, meetingPlace, dateAndTime, "", "You received")

                // setting where to store meetings informations for the user who will receive it
                val usersRef = ref.child("Meetings")
                val userRef = usersRef.child(meeting.mUserIdReceived).child(meeting.mUserIdSent)

                // store the meetings for user who received
                userRef.setValue(meeting)
                meeting_linearlayout.visibility = View.GONE

                // creating a meeting object for the user who send the meeting
                val meeting2 = Meeting(mAuth!!.uid.toString(), userId, meetingPlace, dateAndTime, "", "You sented")
                // setting where to store meetings informations for the user who send it
                val usersRef1 = ref.child("Meetings")
                val userRef2 = usersRef1.child(meeting.mUserIdSent).child(meeting2.mUserIdReceived)

                // store the meetings for user who sent
                userRef2.setValue(meeting2)


            }
        }



        set_date_btn.setOnClickListener {
            date_picker_layout.visibility = View.VISIBLE
        }

        finished_date.setOnClickListener {
            date_picker_layout.visibility = View.GONE
        }



        set_time_btn.setOnClickListener {
            time_picker_layout.visibility = View.VISIBLE
        }

        finished_time.setOnClickListener {
            time_picker_layout.visibility = View.GONE
        }

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
