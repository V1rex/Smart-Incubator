package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.v1rex.smartincubator.Model.Meeting;
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StartupProfileActivity extends AppCompatActivity {
    private TextView mStartupNameTextView, mStartupDomainTextView, mStartupFondTextView, mStartupChiffreTextView, mStartupNeedTextView, mStartupJuridiqueTextView , mStartupCreateDateTextView, mStartupNumberEmployees;
    private Startup startup;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUser;


    private Button mSetDateBtn, mSetTimeBtn;
    private ImageButton mExitButton, mSendButton, mExitDate, mExitTime;
    private TextInputLayout mPLaceTextInput;
    private EditText mPlaceEdit;

    private FloatingActionButton mSendMessageButton;
    private DatePicker mDate;
    private TimePicker mTime;

    private LinearLayout mMeetingsInformations, mDateLayout, mTimeLayout;
    private FirebaseAuth mAuth;

    private FirebaseDatabase databaseMeetings = FirebaseDatabase.getInstance();
    private DatabaseReference ref = databaseMeetings.getReference("Data");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_profile);
        // getting instance of the Auth firebase
        mAuth = FirebaseAuth.getInstance();

        // getting the userId for the purpose of the choosing what Startup to show
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("UserId Startup");

        mDate = (DatePicker) findViewById(R.id.date_picker_startup);
        mTime = (TimePicker) findViewById(R.id.time_picker_startup);

        mExitDate = (ImageButton) findViewById(R.id.finished_date_startup);
        mExitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
            }
        });

        mExitTime = (ImageButton) findViewById(R.id.finished_time_startup);
        mExitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLayout.setVisibility(View.GONE);
            }
        });

        mMeetingsInformations = (LinearLayout) findViewById(R.id.meeting_startup_linearlayout);
        mDateLayout = (LinearLayout) findViewById(R.id.date_picker_startup_layout);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_picker_startup_layout);

        // showing a popup for sending a meeting
        mSendMessageButton = (FloatingActionButton) findViewById(R.id.fab);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeetingsInformations.setVisibility(View.VISIBLE);

            }
        });



        mStartupNameTextView = (TextView) findViewById(R.id.startup_name_profile);
        mStartupDomainTextView = (TextView) findViewById(R.id.startup_domain_profile);
        mStartupFondTextView = (TextView) findViewById(R.id.startup_fond_profile);
        mStartupChiffreTextView= (TextView) findViewById(R.id.startup_chiffre_profile);
        mStartupNeedTextView = (TextView) findViewById(R.id.startup_need_profile);
        mStartupJuridiqueTextView = (TextView) findViewById(R.id.startup_jurdique_profile);
        mStartupCreateDateTextView = (TextView) findViewById(R.id.startup_date_creation_profile);
        mStartupNumberEmployees = (TextView) findViewById(R.id.startup_number_employees_profile);

        mSetDateBtn = (Button) findViewById(R.id.set_date_startup_btn);
        mSetDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.VISIBLE);
            }
        });
        mSetTimeBtn = (Button) findViewById(R.id.set_time_startup_btn);
        mSetTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLayout.setVisibility(View.VISIBLE);
            }
        });

        mExitButton = (ImageButton) findViewById(R.id.exit_mettings_startup);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeetingsInformations.setVisibility(View.GONE);

            }
        });

        mSendButton = (ImageButton) findViewById(R.id.send_mettings_startup);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mPlaceEdit.getText().toString())){
                    mPLaceTextInput.setError(getString(R.string.field_requierd));
                } else{
                    int day = mDate.getDayOfMonth();
                    int month = mDate.getMonth();
                    int year = mDate.getYear();
                    int hour = mTime.getCurrentHour();
                    int minute = mTime.getCurrentMinute();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day,hour,minute);

                    SimpleDateFormat date = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm");
                    String dateAndTime = date.format(calendar.getTime());

                    // creating a meeting object for the user who will receive the meeting
                    Meeting meeting = new Meeting(mAuth.getUid(), userId, mPlaceEdit.getText().toString(), dateAndTime, "","Received" );

                    // setting where to store meetings informations for the user who will receive it
                    DatabaseReference usersRef = ref.child("users");
                    DatabaseReference userRef = usersRef.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                    // store the meetings for user who received
                    userRef.setValue(meeting);
                    mMeetingsInformations.setVisibility(View.GONE);

                    // creating a meeting object for the user who send the meeting
                    Meeting meeting2 = new Meeting(mAuth.getUid(), userId, mPlaceEdit.getText().toString(), dateAndTime, "","You sented" );
                    // setting where to store meetings informations for the user who send it
                    DatabaseReference usersRef1 = ref.child("users");
                    DatabaseReference userRef2 = usersRef1.child(meeting.getmUserIdSent()).child("mettings").child(meeting2.getmUserIdReceived());

                    // store the meetings for user who sent
                    userRef2.setValue(meeting2);
                }

            }
        });
        mPLaceTextInput = (TextInputLayout) findViewById(R.id.input_layout_place_meeting_startup);
        mPlaceEdit = (EditText) findViewById(R.id.meeting_place_edit_text_startup);

        // loading the specific startups informations
        refUser = database.getReference("Data").child("startups");
        ValueEventListener valueEventListenerMentor = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // using the stored userId for getting the specific startup
                startup = dataSnapshot.child(userId).getValue(Startup.class);
                show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // listening for the change in the Startup database
        refUser.addValueEventListener(valueEventListenerMentor);
    }

    /**
     * showing the startup informations from the Startup object
     */
    private void show(){
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
