package com.college.collegeconnect.timetable

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.college.collegeconnect.R
import com.college.collegeconnect.activities.MainActivity
import com.college.collegeconnect.activities.Navigation


class AlertReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent1: Intent?) {
//        Notification.displayNotification(context,"","")
//        val intent = Intent(context,MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val builder = NotificationCompat.Builder(context!!, Navigation.CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_stat_call_white)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_round))
//                .setContentTitle("title")
//                .setContentText("body")
//                .setColor(Color.parseColor("#138FF7"))
//                .setContentIntent(pendingIntent)
//                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setLights(Color.WHITE, 500, 500)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        val notificationManagerCompat = NotificationManagerCompat.from(context)
//        notificationManagerCompat.notify(1, builder.build())
//        Toast.makeText(context, "alert receive", Toast.LENGTH_LONG).show()

//        val background = Intent(context, BackgroundService2::class.java)
//        context!!.startService(background)

        // Create an explicit intent for an Activity in your app
        val intent = Intent(context?.applicationContext, Navigation::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context?.applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context?.applicationContext!!, "Notification")
                .setSmallIcon(R.mipmap.ic_stat_call_white)
                .setLargeIcon(BitmapFactory.decodeResource(context.applicationContext.resources, R.mipmap.icon_round))
                .setContentTitle("title")
                .setContentText("body")
                .setColor(Color.parseColor("#138FF7"))
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.WHITE, 500, 500)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                        NotificationCompat.BigTextStyle()
                                .bigText("long notification content")
                )
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context.applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

}