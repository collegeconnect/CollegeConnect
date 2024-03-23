package com.college.collegeconnect.timetable;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.app.Service;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.college.collegeconnect.R;
import com.college.collegeconnect.activities.Navigation;

import android.app.AlarmManager;
import android.app.PendingIntent;

public class BackgroundService extends Service {

    private final String TAG = getClass().getName();

    private Boolean isRunning;
    private BackgroundService context;
    private Thread backgroundThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "THE BACKGROUND SERVICE IS RUNNING");
            //Run actions
            showNotification(context);
            //
            stopSelf();
        }
    };

    @SuppressLint("ServiceCast")
    private void showNotification(Context context) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Navigation.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_stat_call_white)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_round))
                .setContentTitle("title")
                .setContentText("body")
                .setColor(Color.parseColor("#138FF7"))
//                .setContentIntent(pendingIntent)
//                .setVibrate(100, 200, 300, 400, 500, 400, 300, 200, 400)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.WHITE, 500, 500)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat mNotificationManager =
                NotificationManagerCompat.from(context);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved()");

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

}