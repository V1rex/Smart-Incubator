package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.R;

public class MentorRegisterActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("Data");
    private FirebaseAuth mAuth;

    private EditText mLastNameEditText , mFirstNameEditText, mCityEditText, mSpecialityEditText;
    private TextInputLayout mLastNameTextInput , mFirstNameTextInput, mCityTextInput, mSpecialityTextInput;
    private Button mSubmitMentorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_register);
        mAuth = FirebaseAuth.getInstance();

        mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text_mentor);
        mLastNameTextInput = (TextInputLayout)findViewById(R.id.last_name_input_text_mentor);


        mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text_mentor);
        mFirstNameTextInput = (TextInputLayout)findViewById(R.id.first_name_input_text_mentor);

        mCityEditText = (EditText) findViewById(R.id.city_edit_text_mentor);
        mCityTextInput = (TextInputLayout)findViewById(R.id.city_input_text_mentor);

        mSpecialityEditText = (EditText) findViewById(R.id.speciality_edit_text_mentor);
        mSpecialityTextInput = (TextInputLayout) findViewById(R.id.speciality_input_text_mentor);

        mSubmitMentorBtn = (Button) findViewById(R.id.submit_action_btn_mentor);
        mSubmitMentorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });



    }

    @Override
    public void onBackPressed() {

    }

    private void submit(){
        String lastName = mLastNameEditText.getText().toString();
        String firstName  = mFirstNameEditText.getText().toString();
        String city = mCityEditText.getText().toString();
        String specialty = mSpecialityEditText.getText().toString();
        boolean cancel = false;

        if(TextUtils.isEmpty(lastName)){
            mLastNameTextInput.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(firstName)){
            mFirstNameTextInput.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(city)){
            mCityTextInput.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(specialty)){
            mSpecialityTextInput.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(cancel == false){
            Mentor mentor = new Mentor(city, specialty , lastName, firstName, mAuth.getUid());
            DatabaseReference mentorsRef = ref.child("mentors");
            DatabaseReference mentorRef = mentorsRef.child(mentor.getmUserId());
            mentorRef.setValue(mentor);
            startActivity(new Intent(MentorRegisterActivity.this, BottonNavigationActivity.class));
        }


    }
}
