package com.college.collegeconnect.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.college.collegeconnect.databinding.ActivityPdfViewerBinding
import java.io.File
import java.lang.Exception

class PdfViewerActivity : AppCompatActivity() {

    private var openTime: Long = 0
    private var closeTime: Long = 0
    private var timeStamp: Long = 0
    private var filePath: String? = null
    private lateinit var binding: ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openTime = System.currentTimeMillis()
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bundle = intent.extras
        filePath = bundle?.get("file") as String
        if (bundle.get("timestamp") != null)
            timeStamp = bundle.get("timestamp") as Long

        val toolbar = binding.toolbarcomPdf
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.tvtitle.text = filePath?.toUri()?.lastPathSegment.toString()

        Log.d(localClassName, "onCreate: $filePath")

//            val file = File(filePath)
        val file = File(filePath.toString())
        binding.pdfViewer.fromFile(file).load()


    }

    override fun onDestroy() {
        closeTime = System.currentTimeMillis()
        if (closeTime - openTime > 60000 && timeStamp != 0.toLong()) {
            setResult(95,Intent().apply {
                putExtra("timestamp",timeStamp)
            })
        }
        super.onDestroy()

    }
}