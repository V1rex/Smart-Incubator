package com.v1rex.smartincubator.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mLoginBtn;
    LinearLayout mProgressLayout;
    TextView mErrorLoginTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();


        mUserNameEditText = (EditText) findViewById(R.id.user_login_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_login_edit_text);
        mLoginBtn = (Button) findViewById(R.id.login_action_btn);
        mErrorLoginTextView = (TextView)findViewById(R.id.error_login_message);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        mProgressLayout = (LinearLayout) findViewById(R.id.progess_login);
        mProgressLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void login()
    {
        mErrorLoginTextView.setVisibility(View.GONE);
        if (mAuth == null) {
            return;
        }

        boolean cancel = false;

        String emailOrUserName = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(password) ) {
            mErrorLoginTextView.setVisibility(View.VISIBLE);
            mErrorLoginTextView.setText(getString(R.string.error_field_password_required));
            cancel = true;

        } else if(!isPasswordValid(password)){
            mErrorLoginTextView.setVisibility(View.VISIBLE);
            mErrorLoginTextView.setText(getString(R.string.error_invalid_password));
            cancel = true;
        } else{
            cancel = false;
        }



        if (TextUtils.isEmpty(emailOrUserName)) {
            mErrorLoginTextView.setVisibility(View.VISIBLE);
            mErrorLoginTextView.setText(getString(R.string.error_field_username_required));
            cancel = true;


        } else if (!isEmailValid(emailOrUserName)) {
            mErrorLoginTextView.setVisibility(View.VISIBLE);
            mErrorLoginTextView.setText(getString(R.string.error_invalid_email));
            cancel = true;
        } else{
            cancel = false;
        }

        if(cancel == false){
            mErrorLoginTextView.setVisibility(View.GONE);
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
                        mErrorLoginTextView.setVisibility(View.VISIBLE);
                        mErrorLoginTextView.setText("Login Failed");

                    } else{
                        mUserNameEditText.setVisibility(View.VISIBLE);
                        mPasswordEditText.setVisibility(View.VISIBLE);
                        mLoginBtn.setVisibility(View.VISIBLE);
                        mErrorLoginTextView.setVisibility(View.VISIBLE);
                        mErrorLoginTextView.setText("Logged Successfully");

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
