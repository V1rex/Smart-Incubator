package com.v1rex.smartincubator.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.v1rex.smartincubator.Model.Message
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_send_messages.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class SendMessagesActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val ref = database.getReference("Data")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_messages)

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

        fab_message_mentor.setOnClickListener {
            val now = Calendar.getInstance()
            val year = now.get(Calendar.YEAR)
            val month = now.get(Calendar.MONTH) + 1 // Note: zero based!
            val day = now.get(Calendar.DAY_OF_MONTH)
            val hour = now.get(Calendar.HOUR_OF_DAY)
            val minute = now.get(Calendar.MINUTE)
            val second = now.get(Calendar.SECOND)
            val millis = now.get(Calendar.MILLISECOND)


            var time : String= "$year $month $day $hour $minute ${second} $millis"
            var message1 = Message(message_edit_text.text.toString() , mAuth!!.uid.toString(), userId , time)
            var reference1 = refSented.child(mAuth!!.uid.toString()).child(userId).child(message1.sentTime)
            reference1.setValue(message1)
            var reference2 = refSented.child(userId).child(mAuth!!.uid.toString()).child(message1.sentTime)
            reference2.setValue(message1)

        }




    }
}
