package com.v1rex.smartincubator.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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


public class LoginActivity extends AppCompatActivity {
    private static final String KEY_USERNAME_ENTRY = "email_entry";
    private static final String KEY_PASSWORD_ENTRY = "password_entry";

    private FirebaseAuth mAuth;

    private EditText mUserNameEditText, mPasswordEditText;
    private Button mLoginBtn;
    private TextInputLayout mEmailTextInputLaout, mPasswordTextInputLayout;
    private LinearLayout mProgressLayout, mLoginView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        mLoginView = (LinearLayout) findViewById(R.id.loginView);
        mEmailTextInputLaout = (TextInputLayout) findViewById(R.id.input_layout_email);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
        mUserNameEditText = (EditText) findViewById(R.id.user_login_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_login_edit_text);
        mLoginBtn = (Button) findViewById(R.id.login_action_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        mProgressLayout = (LinearLayout) findViewById(R.id.progress_login);
        mProgressLayout.setVisibility(View.GONE);

        if(savedInstanceState != null){
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

    private void login()
    {

        if (mAuth == null) {
            return;
        }

        boolean cancel = false;

        String emailOrUserName = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(password) ) {
            mPasswordTextInputLayout.setError(getString(R.string.error_field_password_required));
            cancel = true;

        } else if(!isPasswordValid(password)){
            mPasswordTextInputLayout.setError(getString(R.string.error_invalid_password));
            cancel = true;
        } else{
            cancel = false;
        }



        if (TextUtils.isEmpty(emailOrUserName)) {
            mEmailTextInputLaout.setError(getString(R.string.error_field_username_required));

            cancel = true;


        } else if (!isEmailValid(emailOrUserName)) {
            mEmailTextInputLaout.setError(getString(R.string.error_invalid_email));
            cancel = true;
        } else{
            cancel = false;
        }

        if(cancel == false){
            mUserNameEditText.setVisibility(View.INVISIBLE);
            mPasswordEditText.setVisibility(View.INVISIBLE);
            mLoginBtn.setVisibility(View.INVISIBLE);
            mProgressLayout.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(emailOrUserName,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressLayout.setVisibility(View.GONE);

                    if(!task.isSuccessful()){
                        mUserNameEditText.setVisibility(View.VISIBLE);
                        mPasswordEditText.setVisibility(View.VISIBLE);
                        mLoginBtn.setVisibility(View.VISIBLE);
                        mEmailTextInputLaout.setError(getString(R.string.error_action_loging_failed));

                    } else{

                        mLoginView.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, BottonNavigationActivity.class);
                        startActivity(intent);
                        finish();

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
