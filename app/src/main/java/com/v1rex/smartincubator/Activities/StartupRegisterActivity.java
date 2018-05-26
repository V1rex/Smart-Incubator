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
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.R;

public class StartupRegisterActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("Data");
    private FirebaseAuth mAuth;

    private TextInputLayout mStartupNameInputLayout, mAssociateInputLayout, mDescriptionInputLayout, mWebsiteInputLayout, mFacebookInputLayout, mDateIncubationInputLayout, mJurdiqueInputLayout, mCreationDateInputLayout, mNumberEmployeesInputLayout, mObjectiveInputLayout, mFondInputLayout, mChiffreInputLayout, mDomainInputLayout, mNeedInputLayout;

    private EditText mStartupNameEditText, mAssociateEditText, mDescriptionEditText, mWebsiteEditText, mFacebookEditText, mDateIncubationEditText, mJurdiqueEditText, mCreationDateEditText, mNumberEmployeesEditText, mObjectiveEditText, mFondEditText, mChiffreEditText, mDomainEditText, mNeedEditText;

    private Button mSubmitStartup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_register);
        mAuth = FirebaseAuth.getInstance();

        mStartupNameEditText = (EditText) findViewById(R.id.startup_name_edit_text_startp);
        mStartupNameInputLayout = (TextInputLayout) findViewById(R.id.startup_name_input_text_startup);

        mAssociateEditText = (EditText) findViewById(R.id.associate_edit_text_startp);
        mAssociateInputLayout = (TextInputLayout) findViewById(R.id.associate_input_text_startup);

        mDescriptionEditText = (EditText) findViewById(R.id.description_edit_text_startp);
        mDescriptionInputLayout = (TextInputLayout) findViewById(R.id.description_input_text_startup);

        mStartupNameEditText = (EditText) findViewById(R.id.startup_name_edit_text_startp);
        mStartupNameInputLayout = (TextInputLayout) findViewById(R.id.startup_name_input_text_startup);

        mWebsiteEditText = (EditText) findViewById(R.id.website_edit_text_startp);
        mWebsiteInputLayout = (TextInputLayout) findViewById(R.id.website_input_text_startup);

        mFacebookEditText = (EditText) findViewById(R.id.facebook_edit_text_startp);
        mFacebookInputLayout = (TextInputLayout) findViewById(R.id.facebook_input_text_startup);


        mDateIncubationEditText = (EditText) findViewById(R.id.date_incubation_edit_text_startp);
        mDateIncubationInputLayout = (TextInputLayout) findViewById(R.id.date_incubation_input_text_startup);

        mJurdiqueEditText = (EditText) findViewById(R.id.juridique_status_edit_text_startp);
        mJurdiqueInputLayout = (TextInputLayout) findViewById(R.id.juridique_status_input_text_startup);

        mCreationDateEditText = (EditText) findViewById(R.id.creation_date_edit_text_startp);
        mCreationDateInputLayout = (TextInputLayout) findViewById(R.id.creation_date_input_text_startup);

        mNumberEmployeesEditText = (EditText) findViewById(R.id.number_employees_edit_text_startp);
        mNumberEmployeesInputLayout = (TextInputLayout) findViewById(R.id.number_employees_input_text_startup);

        mObjectiveEditText = (EditText) findViewById(R.id.objective_edit_text_startp);
        mObjectiveInputLayout = (TextInputLayout) findViewById(R.id.objective_input_text_startup);

        mFondEditText = (EditText) findViewById(R.id.fond_edit_text_startp);
        mFondInputLayout = (TextInputLayout) findViewById(R.id.fond_input_text_startup);

        mChiffreEditText = (EditText) findViewById(R.id.chiffre_edit_text_startp);
        mChiffreInputLayout = (TextInputLayout) findViewById(R.id.chiffre_input_text_startup);

        mDomainEditText = (EditText) findViewById(R.id.domain_edit_text_startp);
        mDomainInputLayout = (TextInputLayout) findViewById(R.id.domain_input_text_startup);

        mNeedEditText = (EditText) findViewById(R.id.need_edit_text_startp);
        mNeedInputLayout = (TextInputLayout) findViewById(R.id.need_input_text_startup);

        mSubmitStartup = (Button) findViewById(R.id.submit_action_btn_startup);
        mSubmitStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit(){

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

                DatabaseReference startupsRef = ref.child("startups");
                DatabaseReference startupRef = startupsRef.child(startup.getmUserId());
                startupRef.setValue(startup);

                startActivity(new Intent(StartupRegisterActivity.this, BottonNavigationActivity.class));
            }

    }



    @Override
    public void onBackPressed() {


    }
}
