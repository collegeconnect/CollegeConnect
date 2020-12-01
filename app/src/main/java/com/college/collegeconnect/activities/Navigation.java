package com.college.collegeconnect.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.settingsActivity.SettingsActivity;
import com.college.collegeconnect.ui.attendance.AttendanceFragment;
import com.college.collegeconnect.ui.home.HomeFragment;
import com.college.collegeconnect.ui.notes.NotesFragment;
import com.college.collegeconnect.ui.tools.ToolsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Random;

public class Navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    static int color;
    Fragment homefrag = new HomeFragment();
    Fragment attenfrag = new AttendanceFragment();
    Fragment notefrag = new NotesFragment();
    Fragment toolsfrag = new ToolsFragment();
    public static String CHANNEL_ID = "Notification";
    private static String CHANNEL_NAME = "Notification Channel";
    private static String CHANNEL_DESC = "app notification";
    AlertDialog.Builder builder;
    public static Activity act;
    public Boolean visible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //For test notifications
        /*
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("navigation", msg);
//                        Toast.makeText(navigation.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
           */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.WHITE);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (getIntent().getStringExtra("fragment") != null && getIntent().getStringExtra("fragment").equals("attenfrag")) {
            Log.d("navigation", "onCreate: attenfrag called from notificaion");
            loadFragments(new AttendanceFragment());
        }
        builder = new MaterialAlertDialogBuilder(this);
        act = this;

        SaveSharedPreference.setUserName(this, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Set initials and dp
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null)
            SaveSharedPreference.setUser(Navigation.this, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else {
            String name = SaveSharedPreference.getUser(Navigation.this);
        }

        Random random = new Random();
        color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Toolbar toolbar = findViewById(R.id.toolbarnav);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragments(homefrag);

    }


    public static int generateColor() {

        return color;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(Navigation.this, SettingsActivity.class));
//                Dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    public void feedbackPop() {
        builder.setTitle("Feedback");
        builder.setMessage("Consider taking a one minute feedback?");
        builder.setPositiveButton("Sure", (dialog, which) -> startActivity(new Intent(Navigation.this, FeedbackActivity.class)));
        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = homefrag;
                break;
            case R.id.nav_attendance:
                fragment = attenfrag;
                break;
            case R.id.nav_notes:
                fragment = notefrag;
                break;
            case R.id.nav_tools:
                fragment = toolsfrag;
                break;
        }
        return loadFragments(fragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof HomeFragment) {
//            if (SaveSharedPreference.getPop(this) % 7 == 0) {
//                feedbackPop();
//            } else
                finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else
            super.onBackPressed();
    }

    private boolean loadFragments(Fragment fragment) {
        if (fragment != null) {
            Log.d("navigation", "loadFragments: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        SaveSharedPreference.setPop(this, SaveSharedPreference.getPop(this) + 1);
        super.onDestroy();
    }
}
