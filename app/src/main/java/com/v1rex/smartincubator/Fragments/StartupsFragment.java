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
import com.v1rex.smartincubator.Activities.StartupProfileActivity;
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.R;
import com.v1rex.smartincubator.ViewHolder.StartupViewHolder;

public class StartupsFragment extends Fragment {

    private RecyclerView mList;
    private LinearLayout mLoaderStartup;
    private DatabaseReference mReference;
    private FirebaseRecyclerAdapter<Startup, StartupViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Startup> options;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_startups, container, false);

        //Setting where to find data in the realtime database
        mReference = FirebaseDatabase.getInstance().getReference().child("Data").child("startups");
        mReference.keepSynced(true);

        mLoaderStartup = (LinearLayout) view.findViewById(R.id.startup_load_progress);
        mLoaderStartup.setVisibility(View.VISIBLE);

        mList = (RecyclerView) view.findViewById(R.id.startups_recyclerview);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        options = new FirebaseRecyclerOptions.Builder<Startup>().setQuery(mReference, Startup.class).build();

        // setting the firebaseRecyclerAdapter for the showing Startups
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Startup, StartupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StartupViewHolder holder, int position, @NonNull final Startup model) {
                mLoaderStartup.setVisibility(View.GONE);
                holder.setmNeedTextView("Need :" + " "+  model.getmNeed());
                holder.setmNameTextView(model.getmStartupName());
                holder.setmDomainTextView("Domain :" + " "+ model.getmDomain());


                // open the startup profile if the item is clicked
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), StartupProfileActivity.class);
                        intent.putExtra("UserId Startup", model.getmUserId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public StartupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_startups_layout, parent, false);
                return new StartupViewHolder(view);
            }


        };

        mList.setAdapter(firebaseRecyclerAdapter);
        return view ;
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

