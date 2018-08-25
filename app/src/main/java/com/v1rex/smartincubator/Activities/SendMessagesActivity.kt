package com.v1rex.smartincubator.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.activity_send_messages.*

class SendMessagesActivity : AppCompatActivity() {

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


    }
}
