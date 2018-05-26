package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.R;

public class MentorProfileActivity extends AppCompatActivity {

    TextView mMentorNameTextView, mMentorSpecialityTextView, mMentorCityTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);

        mMentorNameTextView = (TextView) findViewById(R.id.mentor_name_profile);
        mMentorSpecialityTextView = (TextView)findViewById(R.id.mentor_speciality_profile);
        mMentorCityTextView = (TextView) findViewById(R.id.mentor_city_profile);

        Intent intent = getIntent();
        Mentor mentor = (Mentor) intent.getExtras().getSerializable("Object Mentor");

        mMentorNameTextView.setText(mentor.getmFirstName() + " " + mentor.getmLastName());
        mMentorSpecialityTextView.setText(mentor.getmSpeciality());
        mMentorCityTextView.setText(mentor.getmCity());
    }
}
