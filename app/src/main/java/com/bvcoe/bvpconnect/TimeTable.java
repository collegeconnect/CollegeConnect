package com.bvcoe.bvpconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class TimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        ImageView imageView = findViewById(R.id.imageView2);
        imageView.setBackgroundResource(R.drawable.timetable);
    }
}
