package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.college.collegeconnect.R
import kotlinx.android.synthetic.main.activity_team_details.*

class TeamDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        textView28.text = intent.getStringExtra("name")
    }
}