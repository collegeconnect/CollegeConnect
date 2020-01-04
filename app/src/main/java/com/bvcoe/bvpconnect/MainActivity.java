package com.bvcoe.bvpconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        Button register=findViewById(R.id.button2);
        final Intent i = new Intent(this,SignUp.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(i);
            }
        });


//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser()!=null) {
//                    Intent intent = new Intent(MainActivity.this,navigation.class);
//                    startActivity(intent);
//                }
//            }
//        };
    }

    public void LogIn(View view)
    {
        EditText email = findViewById(R.id.editText);
        EditText password = findViewById(R.id.editText2);
        String Stremail = email.getText().toString();
        String Strpass= password.getText().toString();

        if (Stremail.isEmpty()||Strpass.isEmpty())
            Toast.makeText(MainActivity.this, "Fields Empty", Toast.LENGTH_SHORT).show();
        else {
            firebaseAuth.signInWithEmailAndPassword(Stremail, Strpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                        Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    else {
                        Intent intent = new Intent(MainActivity.this,navigation.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener );
//    }
}
