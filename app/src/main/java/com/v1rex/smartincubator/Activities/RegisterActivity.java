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

public class RegisterActivity extends AppCompatActivity {

    private static final String KEY_USERNAME_ENTRY = "email_entry";
    private static final String KEY_PASSWORD_ENTRY = "password_entry";

    private FirebaseAuth mAuth;

    private EditText mUserNameEditText, mPasswordEditText;
    private TextInputLayout mEmailTextInputLayout, mPasswordInputLayout;
    private Button mRegisterBtn;
    private LinearLayout mProgressRegisterLayout , mRegisterView;
    private TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Getting instance of the firebase Auth for registering the User
        mAuth = FirebaseAuth.getInstance();

        mUserNameEditText = (EditText) findViewById(R.id.user_register_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_register_edit_text);
        mProgressRegisterLayout = (LinearLayout) findViewById(R.id.progess_register);
        mRegisterView = (LinearLayout) findViewById(R.id.registerView);
        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_email_register);
        mPasswordInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password_register);
        mLoginTextView = (TextView) findViewById(R.id.link_login);

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

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
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Method for registering the user
     */

    private void register(){

        if(mAuth == null){
            return ;
        }

        // a boolean for canceling the registering if something is wrong
        boolean cancel = false;

        String email = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(password) ) {
            mPasswordInputLayout.setError(getString(R.string.error_field_password_required));
            cancel = true;
        } else if(!isPasswordValid(password)){
            mPasswordInputLayout.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout.setError(getString(R.string.error_field_username_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailTextInputLayout.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }

        // if everything is good , register the user
        if( cancel == false){
            mRegisterView.setVisibility(View.INVISIBLE);
            mProgressRegisterLayout.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressRegisterLayout.setVisibility(View.GONE);
                    if(!task.isSuccessful()){
                        mUserNameEditText.setVisibility(View.VISIBLE);
                        mPasswordEditText.setVisibility(View.VISIBLE);
                        mRegisterBtn.setVisibility(View.VISIBLE);
                        mEmailTextInputLayout.setError(getString(R.string.error_action_registration_failed));
                    } else{
                        mRegisterView.setVisibility(View.GONE);
                        startActivity(new Intent(RegisterActivity.this, UserInformationsActivity.class));
                    }

                }
            });

        }
    }

    /**
     * Check if the email is good
     * @param email
     * @return boolean
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Check if the password is good
     * @param password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
