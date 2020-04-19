package com.example.collegeconnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.collegeconnect.ui.SettingsMenu;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        Toolbar toolbar = findViewById(R.id.settings_bar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new SettingsMenu();
        loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment)
    {
        if (fragment!=null)
        {
            Log.d("Settings", "loadFragmentsInSettings: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_frag_container,fragment)
                    .commit();

            return true;
        }
        return false;
    }
}
