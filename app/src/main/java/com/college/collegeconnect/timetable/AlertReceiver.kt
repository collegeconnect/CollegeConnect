package com.college.collegeconnect.timetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.college.collegeconnect.Notification

class AlertReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Notification.displayNotification(context,"","")
        Toast.makeText(context,"alert receive",Toast.LENGTH_LONG).show()
    }

}