package com.bvcoe.bvpconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    Button register;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        final Intent i = new Intent(this, SignUp.class);
        register=findViewById(R.id.button2);
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

    public void LogIn(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){

        }
        final ProgressBar progressBar = findViewById(R.id.progressbar);
        final Button login =findViewById(R.id.button);
        login.setEnabled(false);
        register.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        TextInputLayout email = findViewById(R.id.editText);
        TextInputLayout password = findViewById(R.id.editText2);
        final String Stremail = email.getEditText().getText().toString();
        String Strpass = password.getEditText().getText().toString();

        if (Stremail.isEmpty() && Strpass.isEmpty()) {
            email.setError("Enter your Email address");
            password.setError("Enter a Valid password");
            progressBar.setVisibility(View.GONE);
            login.setEnabled(true);
            register.setEnabled(true);
        } else if (Stremail.isEmpty()) {
            email.setError("Enter your Email address");
            progressBar.setVisibility(View.GONE);
            login.setEnabled(true);
            register.setEnabled(true);
        } else if (Strpass.isEmpty()) {
            password.setError("Enter a valid password");
            progressBar.setVisibility(View.GONE);
            login.setEnabled(true);
            register.setEnabled(true);
        } else {
            firebaseAuth.signInWithEmailAndPassword(Stremail, Strpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {

                        firebaseAuth.fetchSignInMethodsForEmail(Stremail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean b = !task.getResult().getSignInMethods().isEmpty();

                                if (b) {
                                    Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "User not Registered!", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                login.setEnabled(true);
                                register.setEnabled(true);
                            }
                        });

                    } else {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            SaveSharedPreference.setUserName(MainActivity.this, Stremail);
                            Intent intent = new Intent(MainActivity.this, navigation.class);
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        } else
                            Toast.makeText(MainActivity.this, "Email Not Verified! Please verify before continuing", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        login.setEnabled(true);
                        register.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SaveSharedPreference.getUserName(MainActivity.this).length() != 0) {
            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
//        firebaseAuth.addAuthStateListener(authStateListener );
    }

}
