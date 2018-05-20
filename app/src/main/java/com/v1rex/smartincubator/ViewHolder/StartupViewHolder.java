package com.v1rex.smartincubator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.v1rex.smartincubator.R;



public class StartupViewHolder extends RecyclerView.ViewHolder {
    private TextView mNameTextView, mNeedTextView, mDomainTextView;
    private View mView;

    public StartupViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mNameTextView = (TextView)  mView.findViewById(R.id.startup_name_text_view);
        mNeedTextView = (TextView) mView.findViewById(R.id.startup_need_text_view);
        mDomainTextView = (TextView) mView.findViewById(R.id.startup_domain_text_view);
    }


    public void setmNameTextView(String name) {
        mNameTextView.setText(name);

    }

    public void setmNeedTextView(String need) {
        mNeedTextView.setText(need);

    }

    public void setmDomainTextView(String domain) {
        mDomainTextView.setText(domain);

    }
}
