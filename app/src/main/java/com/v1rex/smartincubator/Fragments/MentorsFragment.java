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
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.v1rex.smartincubator.Activities.MentorProfileActivity;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.R;
import com.v1rex.smartincubator.ViewHolder.MentorViewHolder;


public class MentorsFragment extends Fragment {

    private RecyclerView mList;
    private LinearLayout mLoaderMentor;
    private DatabaseReference mReference;
    private FirebaseRecyclerAdapter<Mentor, MentorViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Mentor> options;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mentors, container, false);

        //Setting where to find data in the realtime database
        mReference = FirebaseDatabase.getInstance().getReference().child("Data").child("mentors");
        mReference.keepSynced(true);

        mLoaderMentor = (LinearLayout) view.findViewById(R.id.mentor_load_progress);
        mLoaderMentor.setVisibility(View.VISIBLE);

        mList = (RecyclerView) view.findViewById(R.id.mentors_recyclerview);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        options = new FirebaseRecyclerOptions.Builder<Mentor>().setQuery(mReference, Mentor.class).build();

        // setting the firebaseRecyclerAdapter for the showing Mentors
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Mentor, MentorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MentorViewHolder holder, int position, @NonNull final Mentor model) {
                mLoaderMentor.setVisibility(View.GONE);
                holder.setmNameTextView(model.getmLastName() + " "+ model.getmFirstName());
                holder.setmCityTextView(model.getmCity());
                holder.setmSpecialityTextView(model.getmSpeciality());

                // open the mentor profile if the item is clicked
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MentorProfileActivity.class);
                        intent.putExtra("Mentor userId", model.getmUserId());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mentors_layout,parent, false);
                return new MentorViewHolder(view) ;
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
