package com.college.collegeconnect.settingsActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.settingsActivity.models.MyFilesViewModel
import kotlinx.android.synthetic.main.activity_my_files.*

class MyFilesActivity : AppCompatActivity() {
    lateinit var myFilesViewModel: MyFilesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_files)

        myFilesViewModel = ViewModelProvider(this).get(MyFilesViewModel::class.java)

        viewAll.setOnClickListener {
            startActivity(Intent(this,MyUploadsActivity::class.java))
        }

    }
}