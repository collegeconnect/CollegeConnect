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
import com.college.collegeconnect.databinding.ActivityMyFilesBinding
import com.college.collegeconnect.settingsActivity.models.MyFilesViewModel
import com.google.android.gms.ads.AdRequest
import com.sample.viewbinding.activity.viewBinding

class   MyFilesActivity : AppCompatActivity() {
    private val binding: ActivityMyFilesBinding by viewBinding()
    private lateinit var myFilesViewModel: MyFilesViewModel
    private lateinit var adapterUpload:MyUploadedFilesAdapter
    private lateinit var adapterDownload:MyDownloadedFilesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_files)

        val adRequest = AdRequest.Builder().build()
        binding.adMyFiles.loadAd(adRequest)

        myFilesViewModel = ViewModelProvider(this).get(MyFilesViewModel::class.java)

        binding.viewAll.setOnClickListener {
            startActivity(Intent(this, MyUploadsActivity::class.java))
        }

        myFilesViewModel.getUploads().observe(this) {
            binding.recyclerViewUploads.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapterUpload = MyUploadedFilesAdapter(this, it)
            binding.recyclerViewUploads.adapter = adapterUpload
            if (it.isNullOrEmpty())
                binding.txtNoUploads.visibility = View.VISIBLE
            else
                binding.txtNoUploads.visibility = View.GONE
        }

        DownloadDatabase(application).getDownloadsDao().getDownloadFiles().observe(this, {
            binding.recyclerViewDownloads.layoutManager = LinearLayoutManager(this)
            adapterDownload = MyDownloadedFilesAdapter(this, it, myFilesViewModel)
            Log.d(localClassName, "onCreate: ${it.toString()}")
            binding.recyclerViewDownloads.adapter = adapterDownload
            if (it.isNullOrEmpty())
                binding.txtNoDownloads.visibility = View.VISIBLE
            else
                binding.txtNoDownloads.visibility = View.GONE
        })
    }
}