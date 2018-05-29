package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.v1rex.smartincubator.R;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the user is logged for purpose of choosing the mainActivity for the app
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SplashActivity.this, BottonNavigationActivity.class));
        } else if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }



        finish();
    }
}
