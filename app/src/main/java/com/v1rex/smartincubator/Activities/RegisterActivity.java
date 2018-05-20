package com.v1rex.smartincubator.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.v1rex.smartincubator.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String KEY_USERNAME_ENTRY = "email_entry";
    private static final String KEY_PASSWORD_ENTRY = "password_entry";

    private FirebaseAuth mAuth;

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mRegisterBtn;
    private LinearLayout mProgressRegisterLayout;
    private TextView mErrorRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        mUserNameEditText = (EditText) findViewById(R.id.user_register_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_register_edit_text);
        mProgressRegisterLayout = (LinearLayout) findViewById(R.id.progess_register);
        mErrorRegisterTextView = (TextView) findViewById(R.id.error_register_message);

        mRegisterBtn = (Button) findViewById(R.id.register_action_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        if (savedInstanceState != null){
            String savedEmail = savedInstanceState.getString(KEY_USERNAME_ENTRY);
            mUserNameEditText.setText(savedEmail);

            String savedPassword = savedInstanceState.getString(KEY_PASSWORD_ENTRY);
            mPasswordEditText.setText(savedPassword);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(KEY_USERNAME_ENTRY, mUserNameEditText.getText().toString());
        outState.putString(KEY_PASSWORD_ENTRY, mPasswordEditText.getText().toString());

        super.onSaveInstanceState(outState);

    }



    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void register(){
        mErrorRegisterTextView.setVisibility(View.GONE);
        if(mAuth == null){

        }

        boolean cancel = false;

        String email = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(password) ) {
            mErrorRegisterTextView.setVisibility(View.VISIBLE);
            mErrorRegisterTextView.setText(getString(R.string.error_field_password_required));
            cancel = true;

        } else if(!isPasswordValid(password)){
            mErrorRegisterTextView.setVisibility(View.VISIBLE);
            mErrorRegisterTextView.setText(getString(R.string.error_invalid_password));
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mErrorRegisterTextView.setVisibility(View.VISIBLE);
            mErrorRegisterTextView.setText(getString(R.string.error_field_username_required));
            cancel = true;


        } else if (!isEmailValid(email)) {
            mErrorRegisterTextView.setVisibility(View.VISIBLE);
            mErrorRegisterTextView.setText(getString(R.string.error_invalid_email));
            cancel = true;
        }


        if( cancel == false){
            mErrorRegisterTextView.setVisibility(View.GONE);
            mUserNameEditText.setVisibility(View.INVISIBLE);
            mPasswordEditText.setVisibility(View.INVISIBLE);
            mRegisterBtn.setVisibility(View.INVISIBLE);
            mProgressRegisterLayout.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressRegisterLayout.setVisibility(View.GONE);
                    if(!task.isSuccessful()){
                        mUserNameEditText.setVisibility(View.VISIBLE);
                        mPasswordEditText.setVisibility(View.VISIBLE);
                        mRegisterBtn.setVisibility(View.VISIBLE);
                        mErrorRegisterTextView.setVisibility(View.VISIBLE);
                        mErrorRegisterTextView.setText(getString(R.string.error_action_registration_failed));

                    } else{
                        mUserNameEditText.setVisibility(View.VISIBLE);
                        mPasswordEditText.setVisibility(View.VISIBLE);
                        mRegisterBtn.setVisibility(View.VISIBLE);
                        mErrorRegisterTextView.setVisibility(View.VISIBLE);
                        mErrorRegisterTextView.setText(getString(R.string.error_action_registration_sucessed));
                    }

                }
            });

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
