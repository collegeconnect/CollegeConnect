package com.connect.collegeconnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.connect.collegeconnect.settingsactivity.WorkOne;

public class WorkProfile extends AppCompatActivity {

    private Fragment workone = new WorkOne();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_profile);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        loadFragments(workone);
    }

    private boolean loadFragments(Fragment fragment) {
        if (fragment != null) {
            Log.d("WorkProfile", "loadFragments: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.workprofilecontainer, fragment)
                    .commit();

            return true;
        }
        return false;
    }
}
