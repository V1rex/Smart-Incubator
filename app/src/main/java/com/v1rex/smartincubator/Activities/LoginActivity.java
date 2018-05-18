package com.v1rex.smartincubator.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.v1rex.smartincubator.R;

public class LoginActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    Button mLoginEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
