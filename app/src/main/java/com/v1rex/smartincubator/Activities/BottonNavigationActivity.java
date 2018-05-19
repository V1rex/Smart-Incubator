package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.v1rex.smartincubator.Fragments.MentorsFragment;
import com.v1rex.smartincubator.Fragments.MessagesFragment;
import com.v1rex.smartincubator.Fragments.StartupsFragment;
import com.v1rex.smartincubator.R;

public class BottonNavigationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_navigation);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.startups_name_fragment));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartupsFragment()).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
//                startActivity(new Intent(BottonNavigationActivity.this, SettingsActivity.class));
                return true;
            case  R.id.action_search:
                startActivity(new Intent(BottonNavigationActivity.this, SearchActivity.class));
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BottonNavigationActivity.this, MainActivity.class));

                default:
                    return super.onOptionsItemSelected(item);
        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment selectedFragment = null;
                    switch(item.getItemId()){
                        case R.id.nav_startups:
                            selectedFragment = new StartupsFragment();
                            toolbar.setSubtitle(getString(R.string.startups_name_fragment));
                            break;
                        case R.id.nav_users:
                            selectedFragment = new MentorsFragment();
                            toolbar.setSubtitle(getString(R.string.mentors_name_fragments));
                            break;

                        case R.id.nav_messages:
                            selectedFragment = new MessagesFragment();
                            toolbar.setSubtitle(getString(R.string.messages_name_fragments));
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;

                }
            };
}
