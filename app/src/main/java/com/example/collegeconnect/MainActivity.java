package com.example.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

public class MainActivity extends AppCompatActivity {
//    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Button register;
    int RC_SIGN_IN = 1  ;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG= "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        final Intent i = new Intent(this, SignUp.class);
        register=findViewById(R.id.button2);
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

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser()!=null) {
//                    Intent intent = new Intent(MainActivity.this,navigation.class);
//                    startActivity(intent);
//                }
//            }
//        };
        findViewById(R.id.googleSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinintent= mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signinintent,RC_SIGN_IN);
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
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
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
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            SaveSharedPreference.setUserName(getApplicationContext(),user.getEmail());
                            startActivity(new Intent(getApplicationContext(), navigation.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            Snackbar.make(findViewById(R.id.googleSign), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
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
        if(currentUser!=null){
            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
        if(GoogleSignIn.getLastSignedInAccount(this)!=null) {
            startActivity(new Intent(this, navigation.class));
            finish();
        }
//        firebaseAuth.addAuthStateListener(authStateListener );
    }

}
