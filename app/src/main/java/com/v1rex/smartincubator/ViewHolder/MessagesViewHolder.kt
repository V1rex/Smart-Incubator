package com.v1rex.smartincubator.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.v1rex.smartincubator.R
import java.text.SimpleDateFormat
import java.util.*


class MessagesViewHolder (private val mView: View) : RecyclerView.ViewHolder(mView) {

    private val nameTextView: TextView
    private val messageTextView: TextView
    private val userImageView  : ImageView
    private val timeTextView : TextView


    init {
        nameTextView = mView.findViewById<View>(R.id.name_receiver_text) as TextView
        messageTextView = mView.findViewById<View>(R.id.message_receiver_text) as TextView
        userImageView = mView.findViewById<View>(R.id.message_receiver_photo) as ImageView
        timeTextView = mView.findViewById<View>(R.id.message_receiver_time) as TextView
    }

    fun setNameTextView(name: String) {
        nameTextView.setText(name)
    }

    fun setMessageEditText(message: String){
        var messageFinal = message
        if(message.length > 50){
            messageFinal = "${messageFinal.substring(0,50)}..."
        }
        messageTextView.setText(messageFinal)
    }

    fun setUserImageView(resource : Int){
        userImageView.setImageResource(resource)
    }

    fun setTimeTextView(time : String){
        var long  = time.toLong()

        var date = Date(long)
        var time : String = SimpleDateFormat("HH:mm").format(date)

        timeTextView.setText(time)
    }




}