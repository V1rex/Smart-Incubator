package com.v1rex.smartincubator.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.v1rex.smartincubator.Model.Mentor;
import com.v1rex.smartincubator.Model.Startup;
import com.v1rex.smartincubator.R;
import com.v1rex.smartincubator.ViewHolder.MentorViewHolder;
import com.v1rex.smartincubator.ViewHolder.StartupViewHolder;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ImageButton mBackButton;
    private RadioButton mStartupRadioButton, mMentorRadioButton;

    private DatabaseReference mReferenceStartups;
    private FirebaseRecyclerAdapter<Startup, StartupViewHolder> StartupfirebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Startup> Startupoptions;

    private DatabaseReference mReferenceMentors;
    private FirebaseRecyclerAdapter<Mentor, MentorViewHolder> MentorfirebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Mentor> Mentoroptions;

    private RecyclerView mStartupsRecyclerView , mMentorsRecyclerView;
    private LinearLayout mStartupsLinearLayout, mMentorsLinearLayout;

    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // setting the return button manually
        mBackButton = (ImageButton) findViewById(R.id.search_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, BottonNavigationActivity.class));
                finish();
            }
        });

        mStartupsRecyclerView = (RecyclerView) findViewById(R.id.search_startups_recyclerview);

        mMentorsRecyclerView = (RecyclerView) findViewById(R.id.search_mentors_recyclerview);

        mMentorsLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_mentor_search);


        mStartupsLinearLayout = (LinearLayout) findViewById(R.id.linearlayout_startup_search);

        mStartupRadioButton = (RadioButton) findViewById(R.id.startup_radio_button_search);
        mMentorRadioButton = (RadioButton) findViewById(R.id.mentor_radio_button_search);
        search(searchText);

        mSearchView = (SearchView) findViewById(R.id.search_view_activity);
        searchText = mSearchView.getQuery().toString();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = mSearchView.getQuery().toString();
                search(searchText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });



    }


    /**
     * search between startups or mentors
     * @param search
     */
    private void search(String search){
        if(mStartupRadioButton.isChecked() == true){
            mStartupsLinearLayout.setVisibility(View.VISIBLE);
            mMentorsLinearLayout.setVisibility(View.GONE);
            mStartupsRecyclerView.setHasFixedSize(true);
            mStartupsRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
            mReferenceStartups = FirebaseDatabase.getInstance().getReference().child("Data").child("startups");
            mReferenceStartups.keepSynced(true);
            Query query = mReferenceStartups.orderByChild("mNeed").startAt(search);

            Startupoptions = new FirebaseRecyclerOptions.Builder<Startup>().setQuery(query, Startup.class).build();

            StartupfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Startup, StartupViewHolder>(Startupoptions) {
                @Override
                protected void onBindViewHolder(@NonNull StartupViewHolder holder, int position, @NonNull final Startup model) {
                    holder.setmNeedTextView("Need :" + " "+  model.getmNeed());
                    holder.setmNameTextView(model.getmStartupName());
                    holder.setmDomainTextView("Domain :" + " "+ model.getmDomain());


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, StartupProfileActivity.class);
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

            mStartupsRecyclerView.setAdapter(StartupfirebaseRecyclerAdapter);
            StartupfirebaseRecyclerAdapter.startListening();

        } else if( mMentorRadioButton.isChecked() == true){
            mStartupsLinearLayout.setVisibility(View.GONE);
            mMentorsLinearLayout.setVisibility(View.VISIBLE);
            mMentorsRecyclerView.setHasFixedSize(true);
            mMentorsRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


            mReferenceMentors = FirebaseDatabase.getInstance().getReference().child("Data").child("mentors");
            mReferenceMentors.keepSynced(true);
            // setting the query for the search
            Query query = mReferenceMentors.orderByChild("mSpeciality").startAt(search);

            Mentoroptions = new FirebaseRecyclerOptions.Builder<Mentor>().setQuery(query, Mentor.class).build();

            MentorfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Mentor, MentorViewHolder>(Mentoroptions) {
                @Override
                protected void onBindViewHolder(@NonNull MentorViewHolder holder, int position, @NonNull final Mentor model) {
                    holder.setmNameTextView(model.getmLastName() + " "+ model.getmFirstName());
                    holder.setmCityTextView(model.getmCity());
                    holder.setmSpecialityTextView(model.getmSpeciality());


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SearchActivity.this, MentorProfileActivity.class);
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

            mMentorsRecyclerView.setAdapter(MentorfirebaseRecyclerAdapter);
            MentorfirebaseRecyclerAdapter.startListening();
        }
    }


}
