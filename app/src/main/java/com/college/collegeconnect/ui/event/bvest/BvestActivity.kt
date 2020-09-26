package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.BvestEventsAdapter
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestViewModel
import kotlinx.android.synthetic.main.activity_bvest.*

class BvestActivity : AppCompatActivity() {

    lateinit var bvestViewModel: BvestViewModel
    lateinit var adapter:BvestEventsAdapter

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
        bvestViewModel.getList().observe(this, {
            eventsRecyclerBvest.layoutManager = GridLayoutManager(this,2)
            adapter = BvestEventsAdapter(this,it)
            eventsRecyclerBvest.adapter=adapter
            eventSwipeBvest.isRefreshing=false
    })
    }
}