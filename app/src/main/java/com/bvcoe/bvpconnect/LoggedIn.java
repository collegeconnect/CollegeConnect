package com.bvcoe.bvpconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class LoggedIn extends AppCompatActivity {

    public static final String USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        Intent intent = getIntent();
        Toast.makeText(this, "Welcome "+intent.getStringExtra(USERNAME), Toast.LENGTH_SHORT).show();
    }
}
