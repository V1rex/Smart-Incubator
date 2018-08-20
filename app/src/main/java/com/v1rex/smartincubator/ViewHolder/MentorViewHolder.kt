package com.v1rex.smartincubator.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.v1rex.smartincubator.R

class MentorViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
    private val mNameTextView: TextView
    private val mSpecialityTextView: TextView
    private val mCityTextView: TextView

    init {
        mNameTextView = mView.findViewById<View>(R.id.mentor_name_text_view) as TextView
        mCityTextView = mView.findViewById<View>(R.id.mentor_city_text_view) as TextView
        mSpecialityTextView = mView.findViewById<View>(R.id.mentor_speciality_text_view) as TextView

    }

    fun setmNameTextView(name: String) {
        mNameTextView.text = name
    }

    fun setmCityTextView(city: String) {
        mCityTextView.text = "City: $city"
    }

    fun setmSpecialityTextView(speciality: String) {
        mSpecialityTextView.text = "Speciality: $speciality"
    }
}
