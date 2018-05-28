package com.v1rex.smartincubator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.v1rex.smartincubator.R;

public class MeetingsViewHolder extends RecyclerView.ViewHolder {

    private TextView mPlaceTextView, mTypeEditText, mDateEditText , mStatusEditText;
    private View mView;

    public MeetingsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mPlaceTextView = (TextView) mView.findViewById(R.id.meetings_place);
        mTypeEditText = (TextView) mView.findViewById(R.id.meetings_type);
        mDateEditText = (TextView) mView.findViewById(R.id.meetings_date);
        mStatusEditText = (TextView) mView.findViewById(R.id.meetings_status);
    }

    public void setmPlaceTextView(String place) {

        mPlaceTextView.setText("Place: " + place);
    }

    public void setmTypeEditText(String type) {
        mTypeEditText.setText("Type: "+ type);
    }

    public void setmDateEditText(String date) {
        mDateEditText.setText("Date: "+date);
    }

    public void setmStatusEditText(String status) {
        mStatusEditText.setText("Status: " + status);

    }
}
