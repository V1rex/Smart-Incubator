package com.v1rex.smartincubator.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;

import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Data");
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAuth = FirebaseAuth.getInstance();

//        User user1 = new User("Bahij","Amine","go4computergeek.com",mAuth.getUid());
//        Mentor mentor1 = new Mentor("Casablanca","Programmation","Normal",mAuth.getUid(), "Mohamed","Bahij");
//        Startup startup1 = new Startup("V1re", "nne","startu qui hack le monde","wwww.v1rex.com","V1rex","14/0/2002","SRL","14/7/2017","programmation","14","hack hack","140000","14777",mAuth.getUid(),"programmation","sante");
//
//        DatabaseReference usersRef = ref.child("users");
//        DatabaseReference userRef = usersRef.child(user1.getmUserId());
//        userRef.setValue(user1);
//
//        DatabaseReference mentorsRef = ref.child("mentors");
//        DatabaseReference mentorRef = mentorsRef.child(mentor1.getmUserId());
//        mentorRef.setValue(mentor1);
//
//        DatabaseReference startupsRef = ref.child("startups");
//        DatabaseReference startupRef = startupsRef.child(startup1.getmUserId());
//        startupRef.setValue(startup1);

    }
}
