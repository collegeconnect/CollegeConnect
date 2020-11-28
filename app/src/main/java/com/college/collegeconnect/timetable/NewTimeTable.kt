package com.college.collegeconnect.timetable

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.SectionsPagerAdapter
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.entity.*
import com.college.collegeconnect.utils.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_new_time_table.*
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*

class NewTimeTable : AppCompatActivity() {

    lateinit var newTimeTableViewModel: NewTimeTableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_time_table)

        newTimeTableViewModel = ViewModelProvider(this).get(NewTimeTableViewModel::class.java)
        //Setup viewpager and tablayout
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, newTimeTableViewModel)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        time_table_fab.setOnClickListener {
            add_class()
        }
    }

    private fun add_class() {

        var startTime: String? = null
        var endTime: String? = null
        var startTimeShow: String? = null
        var endTimeShow: String? = null

        val builder = AlertDialog.Builder(this)
        val inflater = (this as AppCompatActivity).layoutInflater
        val view = inflater.inflate(R.layout.add_class_layout, null)

        var spinner = view.findViewById<Spinner>(R.id.spinner2)
        var adapter: ArrayAdapter<String>
        AttendanceDatabase(this).getAttendanceDao().getSubjects().observe(this, androidx.lifecycle.Observer {
            adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        })

        val roomNumber = view.findViewById<EditText>(R.id.roomNumber)
        val start = view.findViewById<TextView>(R.id.start_time)
        val end = view.findViewById<TextView>(R.id.end_time)

        start.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                val am_pm = if (selectedHour < 12) "AM" else "PM"
                val hour = if (selectedHour > 12) {
                    val temp = selectedHour - 12
                    if (temp < 10)
                        "0$temp"
                    else
                        temp.toString()

                } else {
                    if (selectedHour < 10)
                        "0$selectedHour"
                    else
                        selectedHour.toString()
                }
                val min = if (selectedMinute < 10)
                    "0$selectedMinute"
                else
                    selectedMinute.toString()

                val hourStore = if (selectedHour < 10)
                    "0$selectedHour"
                else
                    selectedHour.toString()

                start.text = "$hour:$min $am_pm"
                startTimeShow = "$hour:$min $am_pm"
                startTime = "$hourStore:$min:00"

            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.show()
        }

        end.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                val am_pm = if (selectedHour < 12) "AM" else "PM"
                val hour = if (selectedHour > 12) {
                    val temp = selectedHour - 12
                    if (temp < 10)
                        "0$temp"
                    else
                        temp.toString()

                } else {
                    if (selectedHour < 10)
                        "0$selectedHour"
                    else
                        selectedHour.toString()
                }
                val min = if (selectedMinute < 10)
                    "0$selectedMinute"
                else
                    selectedMinute.toString()

                val hourStore = if (selectedHour < 10)
                    "0$selectedHour"
                else
                    selectedHour.toString()

                end.text = "$hour:$min $am_pm"
                endTimeShow = "$hour:$min $am_pm"
                endTime = "$hourStore:$min:00"

            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.show()
        }

        builder.setPositiveButton("Done") { dialog, which ->
//            saveClass(spinner.selectedItem.toString(), startTime.toString(), endTime.toString())
            if (spinner.adapter.count == 0) {
                toast("Add Subject in Attendance Manager")
                return@setPositiveButton
            }
            startTimeShow?.let { sts -> endTimeShow?.let { ets -> newTimeTableViewModel.addItem(spinner.selectedItem.toString(), startTime.toString(), sts, endTime.toString(), ets, view_pager.currentItem, roomNumber.text.toString()) } }
            Toast.makeText(this, "$startTime : $endTime", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()

        dialog.show()
    }
}