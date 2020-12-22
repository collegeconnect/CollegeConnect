    package com.college.collegeconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout email, password;
    private Button register, login;
    private int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            firebaseAuth = FirebaseAuth.getInstance();
            final Intent i = new Intent(this, SignUp.class);
            register = findViewById(R.id.button2);
            email = findViewById(R.id.editText);
            login = findViewById(R.id.button);
            password = findViewById(R.id.editText2);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(i);
                }
            });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.googleSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinintent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signinintent, RC_SIGN_IN);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn();
            }
        });
        password.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    LogIn();
                return false;
            }
        });
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    Toast.makeText(this, "Google sign in failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (newuser) {

                                startActivity(new Intent(getApplicationContext(), StepTwoSignUp.class));

                            } else {

                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                SaveSharedPreference.setUserName(getApplicationContext(), user.getEmail());
                                startActivity(new Intent(getApplicationContext(), Navigation.class));
                            }
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            Snackbar.make(findViewById(R.id.googleSign), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                    }
                });
    }

    public void LogIn() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ignored) {

        }
        final ProgressBar progressBar = findViewById(R.id.MainProgressBar);
        login.setEnabled(false);
        register.setEnabled(false);


        progressBar.setVisibility(View.VISIBLE);

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

                            if (SaveSharedPreference.getUpload(MainActivity.this)) {
                                startActivity(new Intent(MainActivity.this, Navigation.class));
                                finish();
                            } else {
                                //Set email
                                SaveSharedPreference.setUserName(MainActivity.this, Stremail);
                                Intent intent = new Intent(MainActivity.this, StepTwoSignUp.class);
                                intent.putExtra(StepTwoSignUp.EXTRA_PREV, "MainActivity");
                                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
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
//        if(GoogleSignIn.getLastSignedInAccount(this)!=null) {
//            startActivity(new Intent(this, navigation.class));
//            finish();
//        }
//        else if(currentUser!=null){
//            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            finish();
//        }
//        else if (SaveSharedPreference.getUserName(MainActivity.this).length() != 0) {
//            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            finish();
//        }

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
            startActivity(new Intent(this, Navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

    }

    public void forgotpassword(View view) {
        String Stremail = email.getEditText().getText().toString();
        if (Stremail.isEmpty())
            email.setError("Enter your Email address");
        else
            FirebaseAuth.getInstance().sendPasswordResetEmail(Stremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Password Reset Email sent. Check Inbox", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Email not registered!", Toast.LENGTH_LONG).show();
                }
            });
    }
}
