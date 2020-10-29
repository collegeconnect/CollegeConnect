package com.college.collegeconnect.timetable;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.college.collegeconnect.R;
import com.college.collegeconnect.activities.Navigation;

public class BackgroundService2 extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), Navigation.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_stat_call_white)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon_round))
                .setContentTitle("title")
                .setContentText("body")
                .setColor(Color.parseColor("#138FF7"))
//                .setContentIntent(pendingIntent)
//                .setVibrate(100, 200, 300, 400, 500, 400, 300, 200, 400)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.WHITE, 500, 500)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat mNotificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        mNotificationManager.notify(0, mBuilder.build());
    }

}