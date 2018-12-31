package com.v1rex.smartincubator.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.v1rex.smartincubator.R

class MeetingsViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

    private val mPlaceTextView: TextView
    private val mTypeEditText: TextView
    private val mDateEditText: TextView
    private val mStatusEditText: TextView

    init {
        mPlaceTextView = mView.findViewById<View>(R.id.meetings_place) as TextView
        mTypeEditText = mView.findViewById<View>(R.id.meetings_type) as TextView
        mDateEditText = mView.findViewById<View>(R.id.meetings_date) as TextView
        mStatusEditText = mView.findViewById<View>(R.id.meetings_status) as TextView
    }

    fun setmPlaceTextView(place: String) {
        mPlaceTextView.text = "Place: $place"
    }

    fun setmTypeEditText(type: String) {
        mTypeEditText.text = "Type: $type"
    }

    fun setmDateEditText(date: String) {
        mDateEditText.text = "Date: $date"
    }

    fun setmStatusEditText(status: String) {
        mStatusEditText.text = "Status: $status"

    }
}
