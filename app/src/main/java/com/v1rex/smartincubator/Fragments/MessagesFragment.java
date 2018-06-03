package com.v1rex.smartincubator.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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
import com.v1rex.smartincubator.Model.User;
import com.v1rex.smartincubator.R;
import com.v1rex.smartincubator.ViewHolder.MeetingsViewHolder;

public class MessagesFragment extends Fragment {

    private RecyclerView mList;
    private LinearLayout mLoaderMessage;
    private DatabaseReference mReference;
    private FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Meeting> options;
    private FirebaseAuth mAuth;

    private FirebaseDatabase databaseMeetings = FirebaseDatabase.getInstance();
    private DatabaseReference ref = databaseMeetings.getReference("Data");
    private User user;

    private LinearLayout mLinearLayoutReceived, mLinearLayoutSented;
    private ImageButton mExitReceived , mUpdateReceived;
    private TextView mReceivedUserName , mReceivedEmail, mReceivedUserType, mSeeProfileReceived;
    private RadioButton mAcceptButton, mRefuseButton;

    private View view;
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


        // getting Auth firebase instance
        mAuth = FirebaseAuth.getInstance();

        //setting where to find meetings informations
        mReference = FirebaseDatabase.getInstance().getReference().child("Data").child("users").child(mAuth.getUid()).child("mettings");
        mReference.keepSynced(true);

        mLoaderMessage = (LinearLayout) view.findViewById(R.id.message_load_progress);
        mLoaderMessage.setVisibility(View.VISIBLE);

        mList =(RecyclerView) view.findViewById(R.id.meetings_recyclerview);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        options = new FirebaseRecyclerOptions.Builder<Meeting>().setQuery(mReference, Meeting.class).build();

        // setting the firebaseRecyclerAdapter for the showing Mentors
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Meeting, MeetingsViewHolder>(options) {
            @NonNull
            @Override
            public MeetingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_meetings,parent, false);
                return new MeetingsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MeetingsViewHolder holder, int position, @NonNull final Meeting model) {
                mLoaderMessage.setVisibility(View.GONE);
                holder.setmPlaceTextView(model.getmPlace());
                holder.setmTypeEditText(model.getmType());
                holder.setmDateEditText(model.getmDate());
                holder.setmStatusEditText(model.getAccepte());
                final String userId = model.getmUserIdSent();

                // open meetings informations
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // if the user received the meeting then show this
                        if(model.getmType().equals("You received")) {
                            // Loading user informations who sent the meetings
                            ValueEventListener valueEventListenerMeeting = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Store user informations on a object
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
                                                // Open the mentors profile who sent the meetings
                                                Intent intent = new Intent(getActivity(), StartupProfileActivity.class);
                                                intent.putExtra("UserId Startup", user.getmUserId());
                                                startActivity(intent);
                                            } else if(user.getmAccountType().equals("Mentor")){
                                                // Open the mentors profile who sent the meetings
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

                                    // Update data if the user clicked on the green check button
                                    mUpdateReceived.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //if accepted
                                            if (mAcceptButton.isChecked() == true)
                                            {
                                                // Store meeting informations in object Meeting
                                                Meeting meeting = new Meeting(model.getmUserIdSent(), model.getmUserIdReceived(), model.getmPlace(), model.getmDate(), "accepted", model.getmType()) ;

                                                //Settings where to update meetings informations in the part of the part who sent
                                                DatabaseReference userRef2 = ref.child(meeting.getmUserIdSent()).child("mettings").child(meeting.getmUserIdReceived());
                                                // Update meeting
                                                userRef2.setValue(meeting);

                                                //Settings where to update meetings informations in the part of the part who sent
                                                DatabaseReference userRef = ref.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                                                // Update meeting
                                                userRef.setValue(meeting);
                                                mLinearLayoutReceived.setVisibility(View.GONE);
                                            } else if(mRefuseButton.isChecked() == true){
                                                // Store meeting informations in object Meeting
                                                Meeting meeting = new Meeting(model.getmUserIdSent(), model.getmUserIdReceived(), model.getmPlace(), model.getmDate(), "refused", model.getmType()) ;
                                                //Settings where to update meetings informations in the part of the part who sent
                                                DatabaseReference userRef2 = ref.child(meeting.getmUserIdSent()).child("mettings").child(meeting.getmUserIdReceived());
                                                // Update meeting
                                                userRef2.setValue(meeting);

                                                //Settings where to update meetings informations in the part of the part who sent
                                                DatabaseReference userRef = ref.child(meeting.getmUserIdReceived()).child("mettings").child(meeting.getmUserIdSent());
                                                // Update meeting
                                                userRef.setValue(meeting);
                                                mLinearLayoutReceived.setVisibility(View.GONE);

                                            } else{
                                                // if the user who received the meetings didn't update anything so don't send any data to the server
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


                        }
                        // if the user sented the meeting then show this
                        else if(model.equals("You sented")){
                            // TODO will update the app for showing a popup where the user who sented the meeeitng can change the informations about it
                        }

                    }
                });

            }
        };
        mList.setAdapter(firebaseRecyclerAdapter);
        return view;
    }

    // Listening for changes in the Realtime Database
    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    // Stop listening for changes in the Realtime Database
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
