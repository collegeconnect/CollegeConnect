package com.connect.collegeconnect.settingsactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.connect.collegeconnect.R;

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
        TextView tv = findViewById(R.id.tvtitle);
        tv.setText("Contact Us");
        findViewById(R.id.email_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String[] recipients = {"bvp.connect@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
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

        findViewById(R.id.sendMail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = findViewById(R.id.message_body);
                String mesaage = textView.getText().toString();
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String[] recipients = {"bvp.connect@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, mesaage);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
