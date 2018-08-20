package com.v1rex.smartincubator.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.v1rex.smartincubator.R


class StartupViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
    private val mNameTextView: TextView
    private val mNeedTextView: TextView
    private val mDomainTextView: TextView

    init {
        mNameTextView = mView.findViewById<View>(R.id.startup_name_text_view) as TextView
        mNeedTextView = mView.findViewById<View>(R.id.startup_need_text_view) as TextView
        mDomainTextView = mView.findViewById<View>(R.id.startup_domain_text_view) as TextView
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
}
