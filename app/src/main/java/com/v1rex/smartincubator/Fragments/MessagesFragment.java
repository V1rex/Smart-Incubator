package com.v1rex.smartincubator.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.v1rex.smartincubator.Activities.MentorProfileActivity;
import com.v1rex.smartincubator.Activities.StartupProfileActivity;
import com.v1rex.smartincubator.Model.Meeting;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;
import com.v1rex.smartincubator.ViewHolder.MeetingsViewHolder;
import com.v1rex.smartincubator.ViewHolder.MentorViewHolder;

public class MessagesFragment extends Fragment {

    private RecyclerView mList;
    DatabaseReference mReference;
    FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Meeting> options;
    private FirebaseAuth mAuth;

    private FirebaseDatabase databaseMeetings = FirebaseDatabase.getInstance();
    private DatabaseReference ref = databaseMeetings.getReference("Data");
    private ValueEventListener valueEventListener;
    private User user;

    private LinearLayout mLinearLayoutReceived, mLinearLayoutSented;
    private ImageButton mExitReceived , mUpdateReceived;
    private TextView mReceivedUserName , mReceivedEmail, mReceivedUserType, mSeeProfileReceived;
    private RadioButton mAcceptButton, mRefuseButton;



    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String userId;
        view = inflater.inflate(R.layout.fragment_message, container, false);
        mLinearLayoutReceived = (LinearLayout) view.findViewById(R.id.linearlayout_received);

        mExitReceived = (ImageButton) view.findViewById(R.id.exit_received);
        mUpdateReceived = (ImageButton) view.findViewById(R.id.update_received);

        mAcceptButton = (RadioButton) view.findViewById(R.id.accept_received_button);
        mRefuseButton = (RadioButton) view.findViewById(R.id.refuse_received_button);

        mReceivedUserName =(TextView) view.findViewById(R.id.received_user_name);
        mReceivedEmail = (TextView) view.findViewById(R.id.received_user_email);
        mReceivedUserType =(TextView) view.findViewById(R.id.received_user_type);
        mSeeProfileReceived = (TextView) view.findViewById(R.id.see_profile_received);


        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("Data").child("users").child(mAuth.getUid()).child("mettings");
        mReference.keepSynced(true);

        mList =(RecyclerView) view.findViewById(R.id.meetings_recyclerview);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        options = new FirebaseRecyclerOptions.Builder<Meeting>().setQuery(mReference, Meeting.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder>(options) {

            @NonNull
            @Override
            public MeetingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_meetings,parent, false);
                return new MeetingsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MeetingsViewHolder holder, int position, @NonNull final Meeting model) {
                holder.setmPlaceTextView(model.getmPlace());
                holder.setmTypeEditText(model.getmType());
                holder.setmDateEditText(model.getmDate());
                holder.setmStatusEditText(model.getAccepte());
                final String userId = model.getmUserIdSent();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getmType().equals("You received")) {
                            ValueEventListener valueEventListenerMeeting = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    user = dataSnapshot.child(userId).getValue(User.class);
                                    if(model.getAccepte().equals("accepted")){
                                        mAcceptButton.setChecked(true);

                                    } else if(model.getAccepte().equals("refused")){
                                        mRefuseButton.setChecked(true);
                                    }
                                    mLinearLayoutReceived.setVisibility(View.VISIBLE);
                                    mReceivedUserName.setText("UserName: "+user.getmFirstName() + " " + user.getmLastName());
                                    mReceivedEmail.setText("User Email: " + user.getmEmail());
                                    mReceivedUserType.setText("User Type: " + user.getmAccountType());
                                    mSeeProfileReceived.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(user.getmAccountType().equals("Startup")){
                                                Intent intent = new Intent(getActivity(), StartupProfileActivity.class);
                                                intent.putExtra("UserId Startup", user.getmUserId());
                                                startActivity(intent);
                                            } else if(user.getmAccountType().equals("Mentor")){
                                                Intent intent = new Intent(getActivity(), MentorProfileActivity.class);
                                                intent.putExtra("Mentor userId", user.getmUserId());
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                    mExitReceived.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mLinearLayoutReceived.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                    mUpdateReceived.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mAcceptButton.isChecked() == true)
                                            {
                                                Meeting meeting = new Meeting(model.getmUserIdSent(), model.getmUserIdReceived(), model.getmPlace(), model.getmDate(), "accepted", model.getmType()) ;
                                                DatabaseReference userRef2 = ref.child(meeting.getmUserIdSent()).child("mettings").child(meeting.getmUserIdReceived());
                                                userRef2.setValue(meeting);

                                                DatabaseReference userRef = ref.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                                                userRef.setValue(meeting);
                                                mLinearLayoutReceived.setVisibility(View.GONE);
                                            } else if(mRefuseButton.isChecked() == true){
                                                Meeting meeting = new Meeting(model.getmUserIdSent(), model.getmUserIdReceived(), model.getmPlace(), model.getmDate(), "refused", model.getmType()) ;
                                                DatabaseReference userRef2 = ref.child(meeting.getmUserIdSent()).child("mettings").child(meeting.getmUserIdReceived());
                                                userRef2.setValue(meeting);
                                                DatabaseReference userRef = ref.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                                                userRef.setValue(meeting);
                                                mLinearLayoutReceived.setVisibility(View.GONE);


                                            } else{
                                                mLinearLayoutReceived.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            ref = databaseMeetings.getReference("Data").child("users");
                            ref.addValueEventListener(valueEventListenerMeeting);



                        } else if(model.equals("You sented")){


                        }

                    }
                });

            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
