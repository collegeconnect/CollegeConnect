package com.college.collegeconnect.ui.event.bvest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.ImageAdapter
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestEventViewModel
import com.college.collegeconnect.utils.ImageHandler
import kotlinx.android.synthetic.main.activity_bvest_event.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BvestEventActivity : AppCompatActivity() {

    private lateinit var event: Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest_event)

        if (intent != null) event = intent.getSerializableExtra("list") as Events

        //Set values
        ImageHandler().getSharedInstance(this)?.load(event.imageUrl[0])
        tvEventPageTitle.text = event.eventName
        tvEventPageDate.text = date(event.date)
        tvEventDescription.text = event.eventDescription
        val imageAdapter = ImageAdapter(event.imageUrl)
        ivEventBanner.adapter = imageAdapter
    }
    private fun date(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
        val datetext: Date
        var str: String? = null
        try {
            datetext = inputFormat.parse(date) as Date
            str = outputFormat.format(datetext)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.toString()
    }

}