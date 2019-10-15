package com.bvcoe.bvpconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register=findViewById(R.id.button2);
        final Intent i = new Intent(this,SignUp.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(i);
            }
        });
    }

    public void LogIn(View view)
    {
        EditText username = findViewById(R.id.editText);
        String text = username.getText().toString();
        Intent intent = new Intent(this,navigation.class);
        intent.putExtra(navigation.USERNAME,text);
        startActivity(intent);
    }
}
