package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestEventViewModel
import com.college.collegeconnect.utils.ImageHandler
import kotlinx.android.synthetic.main.activity_bvest_event.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BvestEventActivity : AppCompatActivity() {
    private lateinit var bvestEventViewModel: BvestEventViewModel
    private lateinit var event: Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest_event)

        if (intent != null) event = intent.getSerializableExtra("list") as Events
        bvestEventViewModel = ViewModelProvider(this).get(BvestEventViewModel::class.java)

        ImageHandler().getSharedInstance(this)?.load(event.imageUrl[0])
        tvEventPageTitle.text = event.eventName
        tvEventPageDate.text = date(event.date)
        tvEventDescription.text = event.eventDescription
    }
    private fun date(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
        val datetext: Date
        var str: String? = null
        try {
            datetext = inputFormat.parse(date)
            str = outputFormat.format(datetext)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.toString()
    }

}