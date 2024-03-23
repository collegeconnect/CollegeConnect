package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.BvestEventsAdapter
import com.college.collegeconnect.adapters.BvestSocietiesAdapter
import com.college.collegeconnect.databinding.ActivityBvestBinding
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestViewModel
import com.sample.viewbinding.activity.viewBinding

class BvestActivity : AppCompatActivity() {

    private val binding: ActivityBvestBinding by viewBinding()
    lateinit var bvestViewModel: BvestViewModel
    lateinit var adapter: BvestEventsAdapter
    lateinit var adapter2: BvestSocietiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest)

        bvestViewModel = ViewModelProvider(this).get(BvestViewModel::class.java)
        binding.eventSwipeBvest.setOnRefreshListener {
            loadData()
            loadSocieties()
            if (binding.collapse.visibility == View.VISIBLE) {
                binding.collapse.visibility = View.GONE
                binding.viewAll.visibility = View.VISIBLE
            }
        }

        loadData()
        loadSocieties()

        binding.viewAll.setOnClickListener {
            bvestViewModel.getSocietyList().observe(this) { list ->
                binding.societiesRecyclerBvest.layoutManager = GridLayoutManager(this, 3)
                adapter2 = BvestSocietiesAdapter(this, list)
                binding.societiesRecyclerBvest.adapter = adapter2
            }

            binding.collapse.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        binding.collapse.setOnClickListener {
            bvestViewModel.getSocietyList().observe(this) { list ->
                binding.societiesRecyclerBvest.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                adapter2 = BvestSocietiesAdapter(this, list)
                binding.societiesRecyclerBvest.adapter = adapter2
            }

            it.visibility = View.GONE
            binding.viewAll.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        binding.eventSwipeBvest.isRefreshing = true
        bvestViewModel.getEventList().observe(this, {
            binding.eventsRecyclerBvest.layoutManager = GridLayoutManager(this, 2)
            adapter = BvestEventsAdapter(this, it)
            binding.eventsRecyclerBvest.adapter = adapter
            binding.eventSwipeBvest.isRefreshing = false
        })
    }

    private fun loadSocieties() {
        binding.eventSwipeBvest.isRefreshing = true
        bvestViewModel.getSocietyList().observe(this, {
            binding.societiesRecyclerBvest.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter2 = BvestSocietiesAdapter(this, it)
            binding.societiesRecyclerBvest.adapter = adapter2
            binding.eventSwipeBvest.isRefreshing = false
        })
    }
}