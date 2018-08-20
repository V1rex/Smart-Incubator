package com.v1rex.smartincubator.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.v1rex.smartincubator.Model.Mentor
import com.v1rex.smartincubator.Model.Startup
import com.v1rex.smartincubator.R
import com.v1rex.smartincubator.ViewHolder.MentorViewHolder
import com.v1rex.smartincubator.ViewHolder.StartupViewHolder
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private var mReferenceStartups: DatabaseReference? = null
    private var StartupfirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Startup, StartupViewHolder>? = null
    private var Startupoptions: FirebaseRecyclerOptions<Startup>? = null

    private var mReferenceMentors: DatabaseReference? = null
    private var MentorfirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Mentor, MentorViewHolder>? = null
    private var Mentoroptions: FirebaseRecyclerOptions<Mentor>? = null

    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // setting the return button manually
        search_back_button.setOnClickListener {
            startActivity(Intent(this@SearchActivity, BottonNavigationActivity::class.java))
            finish()
        }

        search(searchText)

        searchText = search_view_activity.query.toString()

        search_view_activity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchText = search_view_activity.query.toString()
                search(searchText)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }


    /**
     * search between startups or mentors
     * @param search
     */
    private fun search(search: String?) {
        if (startup_radio_button_search.isChecked == true) {
            linearlayout_startup_search.visibility = View.VISIBLE
            linearlayout_mentor_search.visibility = View.GONE
            search_startups_recyclerview.setHasFixedSize(true)
            search_startups_recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)
            mReferenceStartups = FirebaseDatabase.getInstance().reference.child("Data").child("startups")
            mReferenceStartups!!.keepSynced(true)
            val query = mReferenceStartups!!.orderByChild("mNeed").startAt(search)

            Startupoptions = FirebaseRecyclerOptions.Builder<Startup>().setQuery(query, Startup::class.java!!).build()

            StartupfirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Startup, StartupViewHolder>(Startupoptions!!) {
                override fun onBindViewHolder(holder: StartupViewHolder, position: Int, model: Startup) {
                    holder.setmNeedTextView("Need :" + " " + model.mNeed)
                    holder.setmNameTextView(model.mStartupName)
                    holder.setmDomainTextView("Domain :" + " " + model.mDomain)


                    holder.itemView.setOnClickListener {
                        val intent = Intent(this@SearchActivity, StartupProfileActivity::class.java)
                        intent.putExtra("UserId Startup", model.mUserId)
                        startActivity(intent)
                    }

                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartupViewHolder {

                    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_startups_layout, parent, false)

                    return StartupViewHolder(view)
                }
            }

            search_startups_recyclerview.adapter = StartupfirebaseRecyclerAdapter
            StartupfirebaseRecyclerAdapter!!.startListening()

        } else if (mentor_radio_button_search.isChecked == true) {
            linearlayout_startup_search.visibility = View.GONE
            linearlayout_mentor_search.visibility = View.VISIBLE
            search_mentors_recyclerview.setHasFixedSize(true)
            search_mentors_recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)


            mReferenceMentors = FirebaseDatabase.getInstance().reference.child("Data").child("mentors")
            mReferenceMentors!!.keepSynced(true)
            // setting the query for the search
            val query = mReferenceMentors!!.orderByChild("mSpeciality").startAt(search)

            Mentoroptions = FirebaseRecyclerOptions.Builder<Mentor>().setQuery(query, Mentor::class.java).build()

            MentorfirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Mentor, MentorViewHolder>(Mentoroptions!!) {
                override fun onBindViewHolder(holder: MentorViewHolder, position: Int, model: Mentor) {
                    holder.setmNameTextView(model.mLastName + " " + model.mFirstName)
                    holder.setmCityTextView(model.mCity)
                    holder.setmSpecialityTextView(model.mSpeciality)


                    holder.itemView.setOnClickListener {
                        val intent = Intent(this@SearchActivity, MentorProfileActivity::class.java)
                        intent.putExtra("Mentor userId", model.mUserId)
                        startActivity(intent)
                    }

                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_mentors_layout, parent, false)

                    return MentorViewHolder(view)
                }
            }

            search_mentors_recyclerview.adapter = MentorfirebaseRecyclerAdapter
            MentorfirebaseRecyclerAdapter!!.startListening()
        }
    }


}
