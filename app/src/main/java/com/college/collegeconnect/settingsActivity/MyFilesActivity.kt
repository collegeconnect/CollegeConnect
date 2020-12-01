package com.college.collegeconnect.settingsActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.MyDownloadedFilesAdapter
import com.college.collegeconnect.adapters.MyUploadedFilesAdapter
import com.college.collegeconnect.database.DownloadDatabase
import com.college.collegeconnect.settingsActivity.models.MyFilesViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_my_files.*

class   MyFilesActivity : AppCompatActivity() {
    private lateinit var myFilesViewModel: MyFilesViewModel
    private lateinit var adapterUpload:MyUploadedFilesAdapter
    private lateinit var adapterDownload:MyDownloadedFilesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_files)

        val adRequest = AdRequest.Builder().build()
        adMyFiles.loadAd(adRequest)

        myFilesViewModel = ViewModelProvider(this).get(MyFilesViewModel::class.java)

        viewAll.setOnClickListener {
            startActivity(Intent(this, MyUploadsActivity::class.java))
        }

        myFilesViewModel.getUploads().observe(this, {
            recyclerViewUploads.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapterUpload = MyUploadedFilesAdapter(this, it)
            recyclerViewUploads.adapter = adapterUpload
            if (it.isNullOrEmpty())
                txt_no_uploads.visibility = View.VISIBLE
            else
                txt_no_uploads.visibility = View.GONE
        })

        DownloadDatabase(application).getDownloadsDao().getDownloadFiles().observe(this, {
            recyclerViewDownloads.layoutManager = LinearLayoutManager(this)
            adapterDownload = MyDownloadedFilesAdapter(this, it, myFilesViewModel)
            Log.d(localClassName, "onCreate: ${it.toString()}")
            recyclerViewDownloads.adapter = adapterDownload
            if (it.isNullOrEmpty())
                txt_no_downloads.visibility = View.VISIBLE
            else
                txt_no_downloads.visibility = View.GONE
        })
    }
}