package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.media.Image;
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
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MentorProfileActivity extends AppCompatActivity {

    private TextView mMentorNameTextView, mMentorSpecialityTextView, mMentorCityTextView, mMentorEmailTextView, mMentorPhoneNumber;

    private FloatingActionButton mSendMessageButton;
    private Mentor mentor;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUser;

    private Button mSetDateBtn, mSetTimeBtn;
    private ImageButton mExitButton, mSendButton, mExitDate, mExitTime;
    private TextInputLayout mPLaceTextInput;
    private EditText mPlaceEdit;

    private DatePicker mDate;
    private TimePicker mTime;

    private LinearLayout mMeetingsInformations, mDateLayout, mTimeLayout;
    private FirebaseAuth mAuth;

    private FirebaseDatabase databaseMeetings = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("Mentor userId");

        mDate = (DatePicker) findViewById(R.id.date_picker_mentor);
        mTime = (TimePicker) findViewById(R.id.time_picker_mentor);

        mExitDate = (ImageButton) findViewById(R.id.finished_date_mentor);
        mExitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
            }
        });

        mExitTime = (ImageButton) findViewById(R.id.finished_time_mentor);
        mExitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLayout.setVisibility(View.GONE);
            }
        });

        mMeetingsInformations = (LinearLayout) findViewById(R.id.meeting_mentor_linearlayout);
        mDateLayout = (LinearLayout) findViewById(R.id.date_picker_mentor_layout);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_picker_mentor_layout);

        mSendMessageButton = (FloatingActionButton) findViewById(R.id.fab_message_mentor);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeetingsInformations.setVisibility(View.VISIBLE);

            }
        });

        mMentorNameTextView = (TextView) findViewById(R.id.mentor_name_profile);
        mMentorSpecialityTextView = (TextView)findViewById(R.id.mentor_speciality_profile);
        mMentorCityTextView = (TextView) findViewById(R.id.mentor_city_profile);
        mMentorEmailTextView = (TextView) findViewById(R.id.mentor_email_profile);
        mMentorPhoneNumber = (TextView) findViewById(R.id.mentor_number_phone_profile);

        mSetDateBtn = (Button) findViewById(R.id.set_date_mentor_btn);
        mSetDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.VISIBLE);
            }
        });
        mSetTimeBtn = (Button) findViewById(R.id.set_time_mentor_btn);
        mSetTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLayout.setVisibility(View.VISIBLE);
            }
        });

        mExitButton = (ImageButton) findViewById(R.id.exit_mettings_mentor);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeetingsInformations.setVisibility(View.GONE);

            }
        });
        mSendButton = (ImageButton) findViewById(R.id.send_mettings_mentor);
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

                    Meeting meeting = new Meeting(mAuth.getUid(), userId, mPlaceEdit.getText().toString(), dateAndTime, "","You received" );

                    DatabaseReference usersRef = ref.child("users");
                    DatabaseReference userRef = usersRef.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                    userRef.setValue(meeting);
                    mMeetingsInformations.setVisibility(View.GONE);


                    Meeting meeting2 = new Meeting(mAuth.getUid(), userId, mPlaceEdit.getText().toString(), dateAndTime, "","You sented" );
                    DatabaseReference usersRef1 = ref.child("users");
                    DatabaseReference userRef2 = usersRef1.child(meeting.getmUserIdSent()).child("mettings").child(meeting2.getmUserIdReceived());
                    userRef2.setValue(meeting2);
                }

            }
        });
        mPLaceTextInput = (TextInputLayout) findViewById(R.id.input_layout_place_meeting);
        mPlaceEdit = (EditText) findViewById(R.id.meeting_place_edit_text);







        ValueEventListener valueEventListenerMentor = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mentor = dataSnapshot.child(userId).getValue(Mentor.class);
                show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        refUser = database.getReference("Data").child("mentors");
        refUser.addValueEventListener(valueEventListenerMentor);

    }

    private void show(){
        mMentorNameTextView.setText(mentor.getmFirstName() + " " + mentor.getmLastName());
        mMentorSpecialityTextView.setText(mentor.getmSpeciality());
        mMentorCityTextView.setText(mentor.getmCity());
        mMentorEmailTextView.setText(mentor.getmEmail());
        mMentorPhoneNumber.setText(mentor.getmPhoneNumber());

    }
}
