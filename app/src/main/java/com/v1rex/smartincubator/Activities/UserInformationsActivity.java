package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;

public class UserInformationsActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("Data");
    private FirebaseAuth mAuth;

    private TextInputLayout mFirstNameTextInputLayout, mLastNameTextInputLayout , mEmailTextInputLayout , mAgeTextInputLayout;
    private EditText mFirstNameEditText, mLastNameEditText, mEmailEditText , mAgeEditText;
    private RadioButton mMaleButton, mFemaleButton , mStartupButton, mMentorButton;
    private Button mSubmit;
    private TextView mSexeRequierdTextView, mAccountTypeRequierdTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_informations);
        mAuth = FirebaseAuth.getInstance();

        mFirstNameTextInputLayout = (TextInputLayout) findViewById(R.id.first_name_input_text);
        mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text);

        mLastNameTextInputLayout = (TextInputLayout) findViewById(R.id.last_name_input_text);
        mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text);

        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.email_input_text);
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);

        mAgeTextInputLayout = (TextInputLayout) findViewById(R.id.age_input_text);
        mAgeEditText = (EditText) findViewById(R.id.age_edit_text);

        mMaleButton = (RadioButton) findViewById(R.id.male_selected);

        mFemaleButton = (RadioButton) findViewById(R.id.female_selected);
        mStartupButton = (RadioButton) findViewById(R.id.startup_button);

        mMentorButton = (RadioButton) findViewById(R.id.mentor_button);

        mSexeRequierdTextView = (TextView) findViewById(R.id.sexe_requierd_text_view);
        mAccountTypeRequierdTextView = (TextView) findViewById(R.id.account_type_requierd_text_view);

        mSubmit = (Button) findViewById(R.id.submit_action_btn);

        // get the email of the user
        mEmailEditText.setText(mAuth.getCurrentUser().getEmail());

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Method for submitting the user informations
     */
    private void submit(){
        mSexeRequierdTextView.setVisibility(View.GONE);
        mAccountTypeRequierdTextView.setVisibility(View.GONE);
        // a boolean to cancel submit if something is not good
        boolean cancel = false;

        String lastName= mLastNameEditText.getText().toString();
        String firstName = mFirstNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String age = mAgeEditText.getText().toString();
        String sexe = "";
        String accountType = "";


        Boolean maleSelected = mMaleButton.isChecked();
        Boolean femaleSelected = mFemaleButton.isChecked();
        Boolean startupSelected = mStartupButton.isChecked();
        Boolean mentorSlected = mMentorButton.isChecked();

        if(!isEmailValid(email)){
            mEmailTextInputLayout.setError(getString(R.string.error_invalid_email));
            cancel = true;
        } else if(TextUtils.isEmpty(email)){
            mEmailTextInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(TextUtils.isEmpty(lastName)){
            mLastNameTextInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(TextUtils.isEmpty(firstName)){
            mFirstNameTextInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(TextUtils.isEmpty(age)){
            mAgeTextInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }

        if(maleSelected == false && femaleSelected == false){
            cancel = true;
            mSexeRequierdTextView.setVisibility(View.VISIBLE);

        } else{
            if(maleSelected == true){
                sexe = "Male";

            } else
            {
                sexe = "Female";
            }

        }

        if(startupSelected== false && mentorSlected == false){
            cancel = true;
            mAccountTypeRequierdTextView.setVisibility(View.VISIBLE);
        } else{
            if(startupSelected == true){
                accountType = "Startup";

            } else if(mentorSlected == true){
                accountType = "Mentor";
            }

        }

        if(cancel == false){
            //Creating a new User object for submitting it
            User user = new User(lastName, firstName, email, sexe, age,accountType, mAuth.getUid());

            // Setting where to submit the user in the firebase realtime database
            DatabaseReference usersRef = ref.child("users");
            DatabaseReference userRef = usersRef.child(user.getmUserId());
            userRef.setValue(user);


            if(mentorSlected == true){
                startActivity(new Intent(UserInformationsActivity.this, MentorRegisterActivity.class));
            } else if (startupSelected == true){
                startActivity(new Intent(UserInformationsActivity.this, StartupRegisterActivity.class));
            }
        }




    }

    // check if the email is good
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

}
