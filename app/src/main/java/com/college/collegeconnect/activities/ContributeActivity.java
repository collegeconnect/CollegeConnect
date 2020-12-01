package com.college.collegeconnect.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.college.collegeconnect.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class ContributeActivity extends AppCompatActivity {

    private CheckBox yes, no;
    private TextInputLayout college, course, mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);

        yes = findViewById(R.id.develoepr_check);
        no = findViewById(R.id.nondeveloper_check);
        college = findViewById(R.id.college_contri);
        course = findViewById(R.id.course_contri);
        mobile = findViewById(R.id.mobile_contri);

        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    no.setEnabled(false);
                else
                    no.setEnabled(true);
            }
        });

        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    yes.setEnabled(false);
                else
                    yes.setEnabled(true);
            }
        });

        //Send email to developers
        findViewById(R.id.submit_contri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (college.getEditText().getText().toString() == null || course.getEditText().getText().toString() == null || mobile.getEditText().getText().toString() == null)
                    Toast.makeText(ContributeActivity.this, "Please ensure all the fields are not empty.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = getIntent();
                    String name = intent.getStringExtra("Name");
                    String dev_status;
                    if (yes.isChecked())
                        dev_status = "Yes";
                    else
                        dev_status = "No";

                    //Forming email body
                    String mesaage = "My name is " + name + "\n\nAm I a developer?: " + dev_status +
                            "\n\nCollege Name: " + college.getEditText().getText().toString() + "\n\nCourse: " + course.getEditText().getText().toString() +
                            "\n\nContact\nMobile Number: " + mobile.getEditText().getText().toString() +
                            "\n\nType below this line for any extra message" +
                            "\n---------------------\n";

                    final Intent intent_email = new Intent(android.content.Intent.ACTION_SEND);
                    intent_email.setType("text/plain");
                    String[] recipients = {"college.connect8@gmail.com"};
                    intent_email.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent_email.putExtra(android.content.Intent.EXTRA_TEXT, mesaage);
                    final PackageManager pm = getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(intent_email, 0);
                    ResolveInfo best = null;
                    for (final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                    if (best != null)
                        intent_email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(intent_email);
                }
            }
        });

    }
}
