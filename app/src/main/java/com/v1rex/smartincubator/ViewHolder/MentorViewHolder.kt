package com.v1rex.smartincubator.ViewHolder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.StorageReference

import com.v1rex.smartincubator.R
import de.hdodenhof.circleimageview.CircleImageView

class MentorViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
    private val mNameTextView: TextView
    private val mSpecialityTextView: TextView
    private val mCityTextView: TextView
    private val mImageProfile : CircleImageView

    init {
        mNameTextView = mView.findViewById<View>(R.id.mentor_name_text_view) as TextView
        mCityTextView = mView.findViewById<View>(R.id.mentor_city_text_view) as TextView
        mSpecialityTextView = mView.findViewById<View>(R.id.mentor_speciality_text_view) as TextView
        mImageProfile = mView.findViewById<View>(R.id.mentor_profile_image_view) as CircleImageView

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

    fun setImageProfileImageView(storageRef : StorageReference, context : Context?){
        Glide.with(context!!).load(storageRef)
                .placeholder(R.drawable.profile)
                .into(mImageProfile)
    }
}
