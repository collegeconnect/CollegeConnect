package com.example.collegeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.example.collegeconnect.ui.SettingsMenu;

public class SettingsActivity extends AppCompatActivity {
    Fragment fragment = new SettingsMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
