package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R

class BvestActivity : AppCompatActivity() {
    lateinit var bvestViewModel:BvestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest)

        bvestViewModel = ViewModelProvider(this).get(BvestViewModel::class.java)

    }
}