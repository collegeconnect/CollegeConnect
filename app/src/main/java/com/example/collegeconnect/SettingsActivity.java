package com.example.collegeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.collegeconnect.ui.SettingsMenu;

import java.util.zip.Inflater;

public class SettingsActivity extends AppCompatActivity {
    Fragment fragment = new SettingsMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.settingbar);
        setSupportActionBar(toolbar);
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
