package com.v1rex.smartincubator.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.v1rex.smartincubator.R;

public class RegisterActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mPasswordEditText;
    EditText mRePasswordEditText;
    Button mLoginEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}
