package com.v1rex.smartincubator.Activities;

import android.content.Intent;
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

    private EditText mLastNameEditText , mFirstNameEditText, mCityEditText, mSpecialityEditText, mEmailEditText , mPhoneNumberEditText;
    private TextInputLayout mLastNameTextInput , mFirstNameTextInput, mCityTextInput, mSpecialityTextInput, mEmailTextInput, mPhoneNumberTextInput;
    private Button mSubmitMentorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_register);
        // get instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance();

        mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text_mentor);
        mLastNameTextInput = (TextInputLayout)findViewById(R.id.last_name_input_text_mentor);


        mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text_mentor);
        mFirstNameTextInput = (TextInputLayout)findViewById(R.id.first_name_input_text_mentor);

        mCityEditText = (EditText) findViewById(R.id.city_edit_text_mentor);
        mCityTextInput = (TextInputLayout)findViewById(R.id.city_input_text_mentor);

        mSpecialityEditText = (EditText) findViewById(R.id.speciality_edit_text_mentor);
        mSpecialityTextInput = (TextInputLayout) findViewById(R.id.speciality_input_text_mentor);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_text_mentor);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.email_input_text_mentor);

        mPhoneNumberEditText = (EditText) findViewById(R.id.number_phone_edit_text_mentor);
        mPhoneNumberTextInput = (TextInputLayout) findViewById(R.id.number_phone_input_text_mentor);


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
        String email = mEmailEditText.getText().toString();
        String phoneNumber = mPhoneNumberEditText.getText().toString();

        // boolean for cancelling the submit if something is wrong
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
        } if(TextUtils.isEmpty(email)){
            mEmailTextInput.setError(getString(R.string.field_requierd));
            cancel = true;

        } if(TextUtils.isEmpty(phoneNumber)){
            mPhoneNumberTextInput.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(cancel == false){
            // Creating a Mentor object
            Mentor mentor = new Mentor(city, specialty , lastName, firstName, email, phoneNumber, mAuth.getUid());

            // Setting where to store informations about the mentor in the firebase Realtime Database
            DatabaseReference mentorsRef = ref.child("mentors");
            DatabaseReference mentorRef = mentorsRef.child(mentor.getmUserId());
            mentorRef.setValue(mentor);


            startActivity(new Intent(MentorRegisterActivity.this, BottonNavigationActivity.class));
        }


    }
}
