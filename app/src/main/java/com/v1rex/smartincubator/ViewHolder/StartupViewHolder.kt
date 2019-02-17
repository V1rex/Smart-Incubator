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


class StartupViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
    private val mNameTextView: TextView
    private val mNeedTextView: TextView
    private val mDomainTextView: TextView
    private val mImageProfile : CircleImageView

    init {
        mNameTextView = mView.findViewById<View>(R.id.startup_name_text_view) as TextView
        mNeedTextView = mView.findViewById<View>(R.id.startup_need_text_view) as TextView
        mDomainTextView = mView.findViewById<View>(R.id.startup_domain_text_view) as TextView
        mImageProfile = mView.findViewById<View>(R.id.startup_profile_image_view) as CircleImageView
    }


    fun setmNameTextView(name: String) {
        mNameTextView.text = name

    }

    fun setmNeedTextView(need: String) {
        mNeedTextView.text = need

    }

    fun setmDomainTextView(domain: String) {
        mDomainTextView.text = domain

    }

    fun setImageProfileImageView(storageRef : StorageReference , context : Context?){
        Glide.with(context!!).load(storageRef)
                .placeholder(R.drawable.startup)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .fitCenter()
                .into(mImageProfile)
    }
}
