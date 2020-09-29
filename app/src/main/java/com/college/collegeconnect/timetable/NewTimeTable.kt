package com.college.collegeconnect.timetable

import android.app.TimePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.SectionsPagerAdapter
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.entity.MondayEntity
import com.college.collegeconnect.database.TimeTableDatabse
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_new_time_table.*
import java.util.*

class NewTimeTable : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_time_table)

        //Setup viewpager and tablayout
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
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


        val start = view.findViewById<TextView>(R.id.start_time)
        val end = view.findViewById<TextView>(R.id.end_time)

        start.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->

                    start.text = "$selectedHour:$selectedMinute"
                    startTime = "$selectedHour:$selectedMinute"

            }, hour, minute, true) //Yes 24 hour time
            mTimePicker.show()

        }

        end.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->

                    end.text = "$selectedHour:$selectedMinute"
                    endTime = "$selectedHour:$selectedMinute"

            }, hour, minute, true) //Yes 24 hour time
            mTimePicker.show()

        }

        builder.setPositiveButton("Done") { dialog, which ->
            saveClass(MondayEntity(spinner.selectedItem.toString(), startTime.toString(), endTime.toString()))
            Toast.makeText(this, "$startTime : $endTime", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
        }

        builder.setView(view)
        val dialog = builder.create()

        dialog.show()
    }

    private fun saveClass(entity: MondayEntity) {
        class SaveClass : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                TimeTableDatabse(applicationContext).getMondayDao().add(entity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(applicationContext, "Class added", Toast.LENGTH_SHORT).show()
            }
        }
        SaveClass().execute()
    }
}