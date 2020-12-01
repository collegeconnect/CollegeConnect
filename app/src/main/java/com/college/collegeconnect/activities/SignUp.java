package com.college.collegeconnect.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "Step 1/2";
    private TextInputLayout password, name, email;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        password = findViewById(R.id.textpass);
        name = findViewById(R.id.textname);
        email = findViewById(R.id.textemail);
        Button button = findViewById(R.id.stepOneButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                final String Strpassword = password.getEditText().getText().toString();
                final String Strname = name.getEditText().getText().toString();
                final String Stremail = email.getEditText().getText().toString().trim();

                if (Stremail.isEmpty() && Strpassword.isEmpty() && Strname.isEmpty()) {

                    password.setError("Enter a valid password");
                    name.setError("Enter your Full name");
                    email.setError("Enter your email address");
                } else if (Stremail.isEmpty()) {
                    email.setError("Enter your Email address");
                } else if (Strpassword.isEmpty()) {
                    password.setError("Enter a valid password");
                } else if (Strname.isEmpty()) {
                    name.setError("Enter your name");
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    if (isEmailValid(Stremail)) {
                        mAuth.fetchSignInMethodsForEmail(Stremail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean b = !task.getResult().getSignInMethods().isEmpty();
                                if (b) {
                                    Toast.makeText(getApplicationContext(), "Email already Exist! or Verify your email if already registered!", Toast.LENGTH_SHORT).show();
                                } else {
                                    SaveSharedPreference.setUser(SignUp.this, Strname);
                                    mAuth.createUserWithEmailAndPassword(Stremail, Strpassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUp.this, "Registered! Email Verification sent", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(SignUp.this, Strname, Toast.LENGTH_SHORT).show();
                                                        } else
                                                            Toast.makeText(SignUp.this, task.getException().getMessage(),
                                                                    Toast.LENGTH_SHORT);
                                                    }
                                                });
                                                Log.d(TAG, "createUserWithEmail:success");
                                                Intent intent = new Intent(SignUp.this, MainActivity.class);
//                                                intent.putExtra(StepTwoSignUp.EXTRA_NAME, Strname);
//                                                intent.putExtra(StepTwoSignUp.EXTRA_EMAIL, Stremail);
//                                                intent.putExtra(StepTwoSignUp.EXTRA_PASSWORD, Strpassword);
                                                //To indicate the user is signing up using email

                                                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else
                        Toast.makeText(getApplicationContext(), "Enter a Valid Email Id", Toast.LENGTH_LONG).show();
                }
                email.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        email.setError(null);
                    }
                });
                password.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        password.setError(null);
                    }
                });

                name.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        name.setError(null);
                    }
                });

            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
