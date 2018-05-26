package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.R;

public class MentorProfileActivity extends AppCompatActivity {

    TextView mMentorNameTextView, mMentorSpecialityTextView, mMentorCityTextView, mMentorEmailTextView, mMentorPhoneNumber;
    private Mentor mentor;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);

        mMentorNameTextView = (TextView) findViewById(R.id.mentor_name_profile);
        mMentorSpecialityTextView = (TextView)findViewById(R.id.mentor_speciality_profile);
        mMentorCityTextView = (TextView) findViewById(R.id.mentor_city_profile);
        mMentorEmailTextView = (TextView) findViewById(R.id.mentor_email_profile);
        mMentorPhoneNumber = (TextView) findViewById(R.id.mentor_number_phone_profile);

        Intent intent = getIntent();
        final String userId = intent.getStringExtra("Mentor userId");

        ValueEventListener valueEventListenerMentor = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mentor = dataSnapshot.child(userId).getValue(Mentor.class);
                show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refUser = database.getReference("Data").child("mentors");
        refUser.addValueEventListener(valueEventListenerMentor);

    }

    private void show(){
        mMentorNameTextView.setText(mentor.getmFirstName() + " " + mentor.getmLastName());
        mMentorSpecialityTextView.setText(mentor.getmSpeciality());
        mMentorCityTextView.setText(mentor.getmCity());
        mMentorEmailTextView.setText(mentor.getmEmail());
        mMentorPhoneNumber.setText(mentor.getmPhoneNumber());

    }
}
