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
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SplashActivity.this, BottonNavigationActivity.class));

        } else if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }
}
