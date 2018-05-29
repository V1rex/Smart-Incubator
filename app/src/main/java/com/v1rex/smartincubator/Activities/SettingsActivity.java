package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;

public class SettingsActivity extends AppCompatActivity {

    private String accountType;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsers, refMentors, refStartups ;
    private FirebaseAuth mAuth;
    private Mentor mentor;
    private Startup startup;

    private TextInputLayout mStartupNameInputLayout, mAssociateInputLayout, mDescriptionInputLayout, mWebsiteInputLayout, mFacebookInputLayout, mDateIncubationInputLayout, mJurdiqueInputLayout, mCreationDateInputLayout, mNumberEmployeesInputLayout, mObjectiveInputLayout, mFondInputLayout, mChiffreInputLayout, mDomainInputLayout, mNeedInputLayout;

    private EditText mStartupNameEditText, mAssociateEditText, mDescriptionEditText, mWebsiteEditText, mFacebookEditText, mDateIncubationEditText, mJurdiqueEditText, mCreationDateEditText, mNumberEmployeesEditText, mObjectiveEditText, mFondEditText, mChiffreEditText, mDomainEditText, mNeedEditText;

    private Button mSubmitStartup;
    private ValueEventListener valueEventListenerUser, valueEventListenerMentor, valueEventListenerStartup;

    private EditText mLastNameEditText , mFirstNameEditText, mCityEditText, mSpecialityEditText, mEmailEditText , mPhoneNumberEditText;
    private TextInputLayout mLastNameTextInput , mFirstNameTextInput, mCityTextInput, mSpecialityTextInput, mEmailTextInput, mPhoneNumberTextInput;
    private Button mSubmitMentorBtn;


    private View mMentorInformationsRelativeLayout, mStartupInformationsRelativeLayout, mLoadingData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance();

        mMentorInformationsRelativeLayout = (RelativeLayout) findViewById(R.id.mentor_layout_settings);
        mStartupInformationsRelativeLayout = (RelativeLayout) findViewById(R.id.startup_layout_settings);
        mLoadingData = (LinearLayout) findViewById(R.id.progress_loading);

        // getting user infromations for the purpose of getting the user account type
        refUsers = database.getReference("Data").child("users");

        valueEventListenerUser = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.child(mAuth.getUid()).getValue(User.class);
                    accountType = user.getmAccountType();
                    choose();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        refUsers.addValueEventListener(valueEventListenerUser);


    }

    /**
     * choosing wich view to display and load mentor or startup informations
     */
    private void choose(){
        //if account type is a startup show the view for it
        if(accountType.equals("Startup")){
            mStartupInformationsRelativeLayout.setVisibility(View.VISIBLE);
            valueEventListenerStartup = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    startup = dataSnapshot.child(mAuth.getUid()).getValue(Startup.class);
                    openStartup();
                    mLoadingData.setVisibility(View.GONE);
                    refStartups.removeEventListener(this);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            refStartups = database.getReference("Data").child("startups");

            refStartups.addValueEventListener(valueEventListenerStartup);

        }
        //if account type is a mentor show the view for it
        else if(accountType.equals("Mentor")){
            mMentorInformationsRelativeLayout.setVisibility(View.VISIBLE);

            valueEventListenerMentor = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        mentor = dataSnapshot.child(mAuth.getUid()).getValue(Mentor.class);
                        openMentor();
                        mLoadingData.setVisibility(View.GONE);
                        refMentors.removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            refMentors = database.getReference("Data").child("mentors");
            refMentors.addValueEventListener(valueEventListenerMentor);



        }
    }

    /**
     * show mentor informations in the text field etc.
     */
    private void openMentor(){
        if(mentor != null){
            mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text_mentor_settings);
            mLastNameTextInput = (TextInputLayout)findViewById(R.id.last_name_input_text_mentor_settings);
            mLastNameEditText.setText(mentor.getmLastName());

            mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text_mentor_settings);
            mFirstNameTextInput = (TextInputLayout)findViewById(R.id.first_name_input_text_mentor_settings);
            mFirstNameEditText.setText(mentor.getmFirstName());

            mCityEditText = (EditText) findViewById(R.id.city_edit_text_mentor_settings);
            mCityTextInput = (TextInputLayout)findViewById(R.id.city_input_text_mentor_settings);
            mCityEditText.setText(mentor.getmCity());

            mSpecialityEditText = (EditText) findViewById(R.id.speciality_edit_text_mentor_settings);
            mSpecialityTextInput = (TextInputLayout) findViewById(R.id.speciality_input_text_mentor_settings);
            mSpecialityEditText.setText(mentor.getmSpeciality());

            mEmailEditText = (EditText) findViewById(R.id.email_edit_text_mentor_settings);
            mEmailTextInput = (TextInputLayout) findViewById(R.id.email_input_text_mentor_settings);
            mEmailEditText.setText(mentor.getmEmail());

            mPhoneNumberEditText = (EditText) findViewById(R.id.number_phone_edit_text_mentor_settings);
            mPhoneNumberTextInput = (TextInputLayout) findViewById(R.id.number_phone_input_text_mentor_settings);
            mPhoneNumberEditText.setText(mentor.getmPhoneNumber());
        } else {


            mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text_mentor_settings);
            mLastNameTextInput = (TextInputLayout) findViewById(R.id.last_name_input_text_mentor_settings);

            mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text_mentor_settings);
            mFirstNameTextInput = (TextInputLayout) findViewById(R.id.first_name_input_text_mentor_settings);


            mCityEditText = (EditText) findViewById(R.id.city_edit_text_mentor_settings);
            mCityTextInput = (TextInputLayout) findViewById(R.id.city_input_text_mentor_settings);


            mSpecialityEditText = (EditText) findViewById(R.id.speciality_edit_text_mentor_settings);
            mSpecialityTextInput = (TextInputLayout) findViewById(R.id.speciality_input_text_mentor_settings);


            mEmailEditText = (EditText) findViewById(R.id.email_edit_text_mentor_settings);
            mEmailTextInput = (TextInputLayout) findViewById(R.id.email_input_text_mentor_settings);


            mPhoneNumberEditText = (EditText) findViewById(R.id.number_phone_edit_text_mentor_settings);
            mPhoneNumberTextInput = (TextInputLayout) findViewById(R.id.number_phone_input_text_mentor_settings);

        }


        mSubmitMentorBtn = (Button) findViewById(R.id.submit_action_btn_mentor_settings);
        mSubmitMentorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMentor();
            }
        });


    }

    /**
     * show startup informations in the text field etc.
     */
    private void openStartup(){
        if(startup != null){
            mStartupNameEditText = (EditText) findViewById(R.id.startup_name_edit_text_startp_settings);
            mStartupNameInputLayout = (TextInputLayout) findViewById(R.id.startup_name_input_text_startup_settings);
            mStartupNameEditText.setText(startup.getmStartupName());


            mAssociateEditText = (EditText) findViewById(R.id.associate_edit_text_startp_settings);
            mAssociateInputLayout = (TextInputLayout) findViewById(R.id.associate_input_text_startup_settings);
            mAssociateEditText.setText(startup.getmAssociate());

            mDescriptionEditText = (EditText) findViewById(R.id.description_edit_text_startp_settings);
            mDescriptionInputLayout = (TextInputLayout) findViewById(R.id.description_input_text_startup_settings);
            mDescriptionEditText.setText(startup.getmDescription());


            mWebsiteEditText = (EditText) findViewById(R.id.website_edit_text_startp_settings);
            mWebsiteInputLayout = (TextInputLayout) findViewById(R.id.website_input_text_startup_settings);
            mWebsiteEditText.setText(startup.getmWebsite());

            mFacebookEditText = (EditText) findViewById(R.id.facebook_edit_text_startp_settings);
            mFacebookInputLayout = (TextInputLayout) findViewById(R.id.facebook_input_text_startup_settings);
            mFacebookEditText.setText(startup.getmPageFacebook());

            mDateIncubationEditText = (EditText) findViewById(R.id.date_incubation_edit_text_startp_settings);
            mDateIncubationInputLayout = (TextInputLayout) findViewById(R.id.date_incubation_input_text_startup_settings);
            mDateIncubationEditText.setText(startup.getmDateOfIncubation());

            mJurdiqueEditText = (EditText) findViewById(R.id.juridique_status_edit_text_startp_settings);
            mJurdiqueInputLayout = (TextInputLayout) findViewById(R.id.juridique_status_input_text_startup_settings);
            mJurdiqueEditText.setText(startup.getmJuridiqueSatatus());

            mCreationDateEditText = (EditText) findViewById(R.id.creation_date_edit_text_startp_settings);
            mCreationDateInputLayout = (TextInputLayout) findViewById(R.id.creation_date_input_text_startup_settings);
            mCreationDateEditText.setText(startup.getmCreationDate());

            mNumberEmployeesEditText = (EditText) findViewById(R.id.number_employees_edit_text_startp_settings);
            mNumberEmployeesInputLayout = (TextInputLayout) findViewById(R.id.number_employees_input_text_startup_settings);
            mNumberEmployeesEditText.setText(startup.getmNumberEmployees());

            mObjectiveEditText = (EditText) findViewById(R.id.objective_edit_text_startp_settings);
            mObjectiveInputLayout = (TextInputLayout) findViewById(R.id.objective_input_text_startup_settings);
            mObjectiveEditText.setText(startup.getmObjective());

            mFondEditText = (EditText) findViewById(R.id.fond_edit_text_startp_settings);
            mFondInputLayout = (TextInputLayout) findViewById(R.id.fond_input_text_startup_settings);
            mFondEditText.setText(startup.getmFond());

            mChiffreEditText = (EditText) findViewById(R.id.chiffre_edit_text_startp_settings);
            mChiffreInputLayout = (TextInputLayout) findViewById(R.id.chiffre_input_text_startup_settings);
            mChiffreEditText.setText(startup.getmChiffre());

            mDomainEditText = (EditText) findViewById(R.id.domain_edit_text_startp_settings);
            mDomainInputLayout = (TextInputLayout) findViewById(R.id.domain_input_text_startup_settings);
            mDomainEditText.setText(startup.getmDomain());

            mNeedEditText = (EditText) findViewById(R.id.need_edit_text_startp_settings);
            mNeedInputLayout = (TextInputLayout) findViewById(R.id.need_input_text_startup_settings);
            mNeedEditText.setText(startup.getmNeed());

        }
        mStartupNameEditText = (EditText) findViewById(R.id.startup_name_edit_text_startp_settings);
        mStartupNameInputLayout = (TextInputLayout) findViewById(R.id.startup_name_input_text_startup_settings);


        mAssociateEditText = (EditText) findViewById(R.id.associate_edit_text_startp_settings);
        mAssociateInputLayout = (TextInputLayout) findViewById(R.id.associate_input_text_startup_settings);


        mDescriptionEditText = (EditText) findViewById(R.id.description_edit_text_startp_settings);
        mDescriptionInputLayout = (TextInputLayout) findViewById(R.id.description_input_text_startup_settings);



        mWebsiteEditText = (EditText) findViewById(R.id.website_edit_text_startp_settings);
        mWebsiteInputLayout = (TextInputLayout) findViewById(R.id.website_input_text_startup_settings);


        mFacebookEditText = (EditText) findViewById(R.id.facebook_edit_text_startp_settings);
        mFacebookInputLayout = (TextInputLayout) findViewById(R.id.facebook_input_text_startup_settings);


        mDateIncubationEditText = (EditText) findViewById(R.id.date_incubation_edit_text_startp_settings);
        mDateIncubationInputLayout = (TextInputLayout) findViewById(R.id.date_incubation_input_text_startup_settings);


        mJurdiqueEditText = (EditText) findViewById(R.id.juridique_status_edit_text_startp_settings);
        mJurdiqueInputLayout = (TextInputLayout) findViewById(R.id.juridique_status_input_text_startup_settings);


        mCreationDateEditText = (EditText) findViewById(R.id.creation_date_edit_text_startp_settings);
        mCreationDateInputLayout = (TextInputLayout) findViewById(R.id.creation_date_input_text_startup_settings);


        mNumberEmployeesEditText = (EditText) findViewById(R.id.number_employees_edit_text_startp_settings);
        mNumberEmployeesInputLayout = (TextInputLayout) findViewById(R.id.number_employees_input_text_startup_settings);


        mObjectiveEditText = (EditText) findViewById(R.id.objective_edit_text_startp_settings);
        mObjectiveInputLayout = (TextInputLayout) findViewById(R.id.objective_input_text_startup_settings);


        mFondEditText = (EditText) findViewById(R.id.fond_edit_text_startp_settings);
        mFondInputLayout = (TextInputLayout) findViewById(R.id.fond_input_text_startup_settings);


        mChiffreEditText = (EditText) findViewById(R.id.chiffre_edit_text_startp_settings);
        mChiffreInputLayout = (TextInputLayout) findViewById(R.id.chiffre_input_text_startup_settings);


        mDomainEditText = (EditText) findViewById(R.id.domain_edit_text_startp_settings);
        mDomainInputLayout = (TextInputLayout) findViewById(R.id.domain_input_text_startup_settings);


        mNeedEditText = (EditText) findViewById(R.id.need_edit_text_startp_settings);
        mNeedInputLayout = (TextInputLayout) findViewById(R.id.need_input_text_startup_settings);


        mSubmitStartup = (Button) findViewById(R.id.submit_action_btn_startup_settings);
        mSubmitStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              submitStartup();
            }
        });

    }

    /**
     * update mentor informations
     */
    private void submitMentor(){

        String lastName = mLastNameEditText.getText().toString();
        String firstName  = mFirstNameEditText.getText().toString();
        String city = mCityEditText.getText().toString();
        String specialty = mSpecialityEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String phoneNumber = mPhoneNumberEditText.getText().toString();
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
            refMentors = database.getReference("Data");
            Mentor mentor = new Mentor(city, specialty , lastName, firstName, email, phoneNumber, mAuth.getUid());
            DatabaseReference mentorsRef = refMentors.child("mentors");
            DatabaseReference mentorRef = mentorsRef.child(mentor.getmUserId());


            mentorRef.setValue(mentor);
            startActivity(new Intent(SettingsActivity.this, BottonNavigationActivity.class));
            finish();
        }

    }


    /**
     * update startup informations
     */
    private void submitStartup(){
        String mStartupName = mStartupNameEditText.getText().toString();
        String mAssociate = mAssociateEditText.getText().toString();
        String mDescription = mDescriptionEditText.getText().toString();
        String mWebsite = mWebsiteEditText.getText().toString();
        String mPageFacebook = mFacebookEditText.getText().toString();
        String mDateOfIncubation = mDateIncubationEditText.getText().toString();
        String mJuridiqueSatatus = mJurdiqueEditText.getText().toString();
        String mCreationDate = mCreationDateEditText.getText().toString();
        String mNumberEmployees = mNumberEmployeesEditText.getText().toString();
        String mObjective = mObjectiveEditText.getText().toString();
        String mFond = mFondEditText.getText().toString();
        String mChiffre = mChiffreEditText.getText().toString();
        String mNeed = mNeedEditText.getText().toString();
        String mDomain = mDomainEditText.getText().toString();

        boolean cancel = false;

        if(TextUtils.isEmpty(mStartupName)){
            mNeedInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(mDescription)){
            mDescriptionInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(mDateOfIncubation)){
            mDateIncubationInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(mJuridiqueSatatus)){
            mJurdiqueInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(mCreationDate)){
            mCreationDateInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }
        if(TextUtils.isEmpty(mNumberEmployees)){
            mNumberEmployeesInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }if(TextUtils.isEmpty(mFond)){
            mFondInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }if(TextUtils.isEmpty(mChiffre)){
            mChiffreInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }if(TextUtils.isEmpty(mDomain)){
            mDomainInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        } if(TextUtils.isEmpty(mNeed)){
            mNeedInputLayout.setError(getString(R.string.field_requierd));
            cancel = true;
        }


        if(cancel == false){
            Startup startup = new Startup(mStartupName, mAssociate, mDescription, mWebsite, mPageFacebook,  mDateOfIncubation, mJuridiqueSatatus, mCreationDate, mNumberEmployees, mObjective, mFond,  mChiffre, mAuth.getUid(), mNeed, mDomain);


            refStartups = database.getReference("Data");

            DatabaseReference startupsRef = refStartups.child("startups");
            DatabaseReference startupRef = startupsRef.child(startup.getmUserId());
            startupRef.setValue(startup);

            startActivity(new Intent(SettingsActivity.this, BottonNavigationActivity.class));
        }

    }
}
