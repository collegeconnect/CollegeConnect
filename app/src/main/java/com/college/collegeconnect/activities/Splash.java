package com.college.collegeconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.college.collegeconnect.datamodels.SaveSharedPreference;


public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if (SaveSharedPreference.getCheckedItem(Splash.this) == 0)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        else if (SaveSharedPreference.getCheckedItem(Splash.this) == 1)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else if (SaveSharedPreference.getCheckedItem(Splash.this) == 2)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Intent i = new Intent(Splash.this, OnBoardingScreen.class);
        startActivity(i);
        finish();


    }
}
