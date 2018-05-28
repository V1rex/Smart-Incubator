package com.v1rex.smartincubator.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.v1rex.smartincubator.R;

public class MentorViewHolder extends RecyclerView.ViewHolder {
    private TextView mNameTextView, mSpecialityTextView,mCityTextView;
    View mView ;
    public MentorViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mNameTextView = (TextView) mView.findViewById(R.id.mentor_name_text_view);
        mCityTextView = (TextView) mView.findViewById(R.id.mentor_city_text_view);
        mSpecialityTextView = (TextView) mView.findViewById(R.id.mentor_speciality_text_view);

    }

    public void setmNameTextView(String name){
        mNameTextView.setText(name);
    }

    public void setmCityTextView(String city){
        mCityTextView.setText("City: " +  city);
    }

    public void setmSpecialityTextView(String speciality){
        mSpecialityTextView.setText("Speciality: " + speciality);
    }
}
