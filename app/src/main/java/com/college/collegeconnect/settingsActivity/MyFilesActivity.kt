package com.college.collegeconnect.settingsActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.MyFilesBottomAdapter
import com.college.collegeconnect.adapters.MyFilesTopAdapter
import com.college.collegeconnect.database.entity.DownloadEntity
import com.college.collegeconnect.settingsActivity.models.MyFilesViewModel
import kotlinx.android.synthetic.main.activity_my_files.*

class MyFilesActivity : AppCompatActivity() {
    private lateinit var myFilesViewModel: MyFilesViewModel
    private lateinit var adapterUpload:MyFilesTopAdapter
    private lateinit var adapterDownload:MyFilesBottomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_files)

        myFilesViewModel = ViewModelProvider(this).get(MyFilesViewModel::class.java)

        viewAll.setOnClickListener {
            startActivity(Intent(this,MyUploadsActivity::class.java))
        }

        myFilesViewModel.getUploads().observe(this, {
            recyclerViewUploads.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            adapterUpload = MyFilesTopAdapter(this,it)
            recyclerViewUploads.adapter = adapterUpload
        })

        myFilesViewModel.getDownloads().observe(this, {
            recyclerViewDownloads.layoutManager = LinearLayoutManager(this)
            adapterDownload = MyFilesBottomAdapter(this, it as ArrayList<DownloadEntity>,myFilesViewModel)
            recyclerViewDownloads.adapter = adapterDownload
        })



    }
}