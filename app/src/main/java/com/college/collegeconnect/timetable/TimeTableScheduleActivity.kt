package com.college.collegeconnect.timetable

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.collegeconnect.R
import kotlinx.android.synthetic.main.activity_time_table_schedule.*
import java.util.*

class TimeTableScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var c = Calendar.getInstance()
        setContentView(R.layout.activity_time_table_schedule)
        textView26.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this,
                    { view, hourOfDay, minute ->
                        val hour = if (hourOfDay < 10)
                            "0$hourOfDay"
                        else
                            hourOfDay.toString()
                        val min = if (minute < 10)
                            "0$minute"
                        else
                            minute.toString()
                        textView26.text = "$hour:$min"
                        c = Calendar.getInstance()
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        c.set(Calendar.MINUTE, minute)
                        c.set(Calendar.SECOND, 0)
                    }, 0, 0, false)
            timePickerDialog.show()
        }

        button6.setOnClickListener {
            setUpAlarm(c)
        }
        button.setOnClickListener {
            cancelAlarm()
        }
    }

    private fun setUpAlarm(c: Calendar){
//        val cal = Calendar.getInstance()
        // add 30 seconds to the calendar object
        // add 30 seconds to the calendar object
//        cal.add(Calendar.SECOND, 10)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        Toast.makeText(this, "notification in 10 secs", Toast.LENGTH_SHORT).show()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }
    fun cancelAlarm(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}