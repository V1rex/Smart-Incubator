package com.v1rex.smartincubator.Activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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


import com.v1rex.smartincubator.R;



public class LoginActivity extends AppCompatActivity {

    private static final String KEY_USERNAME_ENTRY = "email_entry";
    private static final String KEY_PASSWORD_ENTRY = "password_entry";

    private FirebaseAuth mAuth;

    private EditText mUserNameEditText, mPasswordEditText;
    private Button mLoginBtn;
    private TextInputLayout mEmailTextInputLaout, mPasswordTextInputLayout;
    private LinearLayout mProgressLayout, mLoginView;
    private TextView mRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // getting instance of the firebase auth for authenticating the user
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

        mRegisterLink = (TextView) findViewById(R.id.link_register);
        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
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


    // save the email and password when the user have make the app in the background
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_USERNAME_ENTRY, mUserNameEditText.getText().toString());
        outState.putString(KEY_PASSWORD_ENTRY, mPasswordEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * a method for login to the firebase by email method
     */
    private void login()
    {

        if (mAuth == null) {
            return;
        }

        // a boolean for canceling the process of login if something is wrong
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
        } else {
            cancel = false;
        }

        // if nothing is wrong then login
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * checking the email
     * @param email
     * @return boolean
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    /**
     * checking the password
     * @param password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
