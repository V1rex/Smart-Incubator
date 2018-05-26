package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.R;

public class StartupProfileActivity extends AppCompatActivity {
    private TextView mStartupNameTextView, mStartupDomainTextView, mStartupFondTextView, mStartupChiffreTextView, mStartupNeedTextView, mStartupJuridiqueTextView , mStartupCreateDateTextView, mStartupNumberEmployees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_profile);

        mStartupNameTextView = (TextView) findViewById(R.id.startup_name_profile);
        mStartupDomainTextView = (TextView) findViewById(R.id.startup_domain_profile);
        mStartupFondTextView = (TextView) findViewById(R.id.startup_fond_profile);
        mStartupChiffreTextView= (TextView) findViewById(R.id.startup_chiffre_profile);
        mStartupNeedTextView = (TextView) findViewById(R.id.startup_need_profile);
        mStartupJuridiqueTextView = (TextView) findViewById(R.id.startup_jurdique_profile);
        mStartupCreateDateTextView = (TextView) findViewById(R.id.startup_date_creation_profile);
        mStartupNumberEmployees = (TextView) findViewById(R.id.startup_number_employees_profile);

        Intent intent = getIntent();
        Startup startup = (Startup) intent.getExtras().getSerializable("Object Startup");
        mStartupNameTextView.setText(startup.getmStartupName());
        mStartupDomainTextView.setText(startup.getmDomain());
        mStartupFondTextView.setText(startup.getmFond());
        mStartupChiffreTextView.setText(startup.getmChiffre());
        mStartupNeedTextView.setText(startup.getmNeed());
        mStartupJuridiqueTextView.setText(startup.getmJuridiqueSatatus());
        mStartupCreateDateTextView.setText(startup.getmCreationDate());
        mStartupNumberEmployees.setText(startup.getmNumberEmployees());

    }
}
