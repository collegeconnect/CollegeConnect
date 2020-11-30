package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.college.collegeconnect.R;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final CheckBox feedback = findViewById(R.id.feedBack);
        final CheckBox issue = findViewById(R.id.issue);
        TextView tv = findViewById(R.id.tvtitle);
        feedback.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                feedback.setError(null);
                issue.setError(null);
            }
        });
        issue.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                feedback.setError(null);
                issue.setError(null);
            }
        });
        Log.i("TAG", "SERIAL: " + Build.SERIAL);
        Log.i("TAG", "MODEL: " + Build.MODEL);
        Log.i("TAG", "ID: " + Build.ID);
        Log.i("TAG", "Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG", "brand: " + Build.BRAND);
        Log.i("TAG", "type: " + Build.TYPE);
        Log.i("TAG", "user: " + Build.USER);
        Log.i("TAG", "BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG", "INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("TAG", "SDK  " + Build.VERSION.SDK);
        Log.i("TAG", "BOARD: " + Build.BOARD);
        Log.i("TAG", "BRAND " + Build.BRAND);
        Log.i("TAG", "HOST " + Build.HOST);
        Log.i("TAG", "FINGERPRINT: " + Build.FINGERPRINT);
        Log.i("TAG", "Version Code: " + Build.DEVICE);

        tv.setText("Contact Us");
        findViewById(R.id.email_contact).setOnClickListener(v -> {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String[] recipients = {"college.connect8@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            String mess = "Manufacturer: "+Build.MANUFACTURER + "\n"
                    + "Brand: " + Build.BRAND + "\n"
                    + "Model: " + Build.MODEL + "\n"
                    + "Version Code :" + Build.DEVICE + "\n"
                    + "Android " + Build.VERSION.RELEASE + "\n"
                    + "Board: " + Build.BOARD + "\n"
                    + "Incremental: " + Build.VERSION.INCREMENTAL + "\n\n";
            intent.putExtra(Intent.EXTRA_TEXT, mess);
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") ||
                        info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
            if (best != null)
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(intent);
        });

        findViewById(R.id.sendMail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!feedback.isChecked() && !issue.isChecked()) {
                    feedback.setError("Choose on option");
                    issue.setError("Choose an option");
                    feedback.requestFocus();
                    issue.requestFocus();
                    return;
                }

                TextView textView = findViewById(R.id.message_body);
                String message = textView.getText().toString();
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String[] recipients = {"college.connect8@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                if (feedback.isChecked() && issue.isChecked())
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback | Issue");
                else if (issue.isChecked())
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Issue");
                else if (feedback.isChecked())
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

                String mess = "Manufacturer: "+Build.MANUFACTURER + "\n"
                        + "Brand: " + Build.BRAND + "\n"
                        + "Model: " + Build.MODEL + "\n"
                        + "Version Code :" + Build.DEVICE + "\n"
                        + "Android " + Build.VERSION.RELEASE + "\n"
                        + "Board: " + Build.BOARD + "\n"
                        + "Incremental: " + Build.VERSION.INCREMENTAL + "\n\n";
                intent.putExtra(android.content.Intent.EXTRA_TEXT, mess);
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") ||
                            info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                if (best != null)
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(intent);
            }
        });
    }
}
