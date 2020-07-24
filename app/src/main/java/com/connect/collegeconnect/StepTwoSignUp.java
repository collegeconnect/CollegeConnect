package com.connect.collegeconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.connect.collegeconnect.datamodels.SaveSharedPreference;
import com.connect.collegeconnect.datamodels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StepTwoSignUp extends AppCompatActivity {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_PREV = "previous";
    private static String TAG = "Step Two Sign Up";

    private TextInputLayout rollno, branchanme;
    private FirebaseAuth mAuth;
    private Button signup;
    private String receivedPRev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two_sign_up);

        mAuth = FirebaseAuth.getInstance();
        rollno = findViewById(R.id.textuser);
        branchanme = findViewById(R.id.textbranch);
        signup = findViewById(R.id.stepTwoButton);


        Intent intent = getIntent();
        final String receivedName = intent.getStringExtra(EXTRA_NAME);
        final String receivedEmail = intent.getStringExtra(EXTRA_EMAIL);
        final String receivedPassword = intent.getStringExtra(EXTRA_PASSWORD);
        receivedPRev = intent.getStringExtra(EXTRA_PREV);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                final String roll = rollno.getEditText().getText().toString();
                final String branch = branchanme.getEditText().getText().toString();

                if (roll.isEmpty() && branch.isEmpty()) {
                    rollno.setError("Enter Roll Number");
                    branchanme.setError("Enter College Name");
                } else if (branch.isEmpty()) {
                    branchanme.setError("Enter College Name");
                } else if (roll.isEmpty()) {
                    rollno.setError("Enter Roll Number");
                } else {

                    if (receivedPRev == null) {//google
                        User.addUser(roll, mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getDisplayName(), null, branch);
                        SaveSharedPreference.setUserName(getApplicationContext(), mAuth.getCurrentUser().getEmail());
                        startActivity(new Intent(getApplicationContext(), navigation.class));
                        finish();
                    } else {//email

                        User.addUser(roll, receivedEmail, receivedName, receivedPassword, branch);
                        mAuth.createUserWithEmailAndPassword(receivedEmail, receivedPassword).addOnCompleteListener(StepTwoSignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(StepTwoSignUp.this, "Registered! Email Verification sent", Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(StepTwoSignUp.this, task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT);
                                        }
                                    });
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Intent intent = new Intent(StepTwoSignUp.this, MainActivity.class);
                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            }
                        });

                    }


                }

                rollno.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        rollno.setError(null);
                    }
                });

                branchanme.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        branchanme.setError(null);
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (receivedPRev != null)
            super.onBackPressed();
        else
            Toast.makeText(this, "Please fill in the details and click submit!", Toast.LENGTH_SHORT).show();
    }
}

