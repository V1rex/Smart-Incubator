package com.v1rex.smartincubator.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.v1rex.smartincubator.R
import kotlinx.android.synthetic.main.item_message_received.view.*

class SendMessagesViewHolder (private val mView: View, private var number : Int) : RecyclerView.ViewHolder(mView) {

    private var messageTextViewReceived : TextView? = null
    private var messageTextViewSented : TextView? = null

    init {
        if(number == 1){
            messageTextViewSented = mView.findViewById(R.id.message_body_sent)
        } else if(number == 2){
            messageTextViewReceived = mView.findViewById(R.id.message_body_received)
        }
    }

    fun setMessageTextViewReceived(message : String){
        messageTextViewReceived!!.setText(message)
    }

    fun setMessageTextViewSented(message : String){
        messageTextViewSented!!.setText(message)
    }
}