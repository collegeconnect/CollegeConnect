package com.bvcoe.bvpconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LogIn(View view)
    {
        EditText username = findViewById(R.id.editText);
        String text = username.getText().toString();
        Intent intent = new Intent(this,LoggedIn.class);
        intent.putExtra(LoggedIn.USERNAME,text);
        startActivity(intent);
    }
}
