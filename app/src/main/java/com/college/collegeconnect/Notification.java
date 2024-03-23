package com.college.collegeconnect;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.college.collegeconnect.activities.MainActivity;
import com.college.collegeconnect.activities.Navigation;

public class Notification {

    public static void displayNotification(Context mCtx, String title, String body) {

        Intent intent = new Intent(mCtx, MainActivity.class);
        intent.putExtra("fragment", "attenfrag");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mCtx, Navigation.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_stat_call_white)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.icon_round))
                .setContentTitle(title)
                .setContentText(body)
                .setColor(Color.parseColor("#138FF7"))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.WHITE, 500, 500)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mCtx);
        notificationManagerCompat.notify(1, builder.build());

    }
}
