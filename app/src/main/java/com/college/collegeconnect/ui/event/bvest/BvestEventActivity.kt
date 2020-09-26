package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestEventViewModel

class BvestEventActivity : AppCompatActivity() {
    lateinit var bvestEventViewModel: BvestEventViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest_event)

        bvestEventViewModel = ViewModelProvider(this).get(BvestEventViewModel::class.java)

    }
}