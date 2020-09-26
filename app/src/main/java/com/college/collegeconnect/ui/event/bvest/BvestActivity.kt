package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.EventsAdapter
import com.college.collegeconnect.datamodels.Events
import kotlinx.android.synthetic.main.activity_bvest.*

class BvestActivity : AppCompatActivity() {
    lateinit var bvestViewModel:BvestViewModel
    lateinit var adapter:EventsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest)

        bvestViewModel = ViewModelProvider(this).get(BvestViewModel::class.java)
        eventSwipeBvest.setOnRefreshListener {
            loadData()
        }
        loadData()
    }

    private fun loadData(){
        eventSwipeBvest.isRefreshing = true
        bvestViewModel.loadData()
        bvestViewModel.listEvents.observe(this, Observer{
            eventsRecyclerBvest.layoutManager = LinearLayoutManager(this)
            adapter = EventsAdapter(this,it)
            eventsRecyclerBvest.adapter=adapter
            eventSwipeBvest.isRefreshing=false
    })
    }
}