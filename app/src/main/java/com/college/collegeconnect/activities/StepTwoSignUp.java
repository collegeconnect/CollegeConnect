package com.college.collegeconnect.activities;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.ContributeActivity;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.FirebaseUserInfo;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.datamodels.User;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import kotlin.Unit;

public class StepTwoSignUp extends AppCompatActivity {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_PREV = "previous";
    private static String TAG = "Step Two Sign Up";
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private TextInputLayout rollno, branchanme, collegeName;
    private FirebaseAuth mAuth;
    private Button signup;
    private Spinner collegeSpinner;
    private TextView contribute;
    private String receivedPRev;
    private ListenerRegistration listener;
    ValueEventListener valueListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two_sign_up);

        mAuth = FirebaseAuth.getInstance();
        rollno = findViewById(R.id.textuser);
        branchanme = findViewById(R.id.textbranch);
        collegeName = findViewById(R.id.otherCollege);
        signup = findViewById(R.id.stepTwoButton);
        collegeSpinner = findViewById(R.id.collegeSpinner);
        contribute = findViewById(R.id.textView21);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Other");
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(StepTwoSignUp.this, android.R.layout.simple_spinner_item, arrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner.setAdapter(spinnerArrayAdapter);

        databaseReference = firebaseDatabase.getReference("Colleges");
        databaseReference.addListenerForSingleValueEvent(valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList2 = (ArrayList<String>) dataSnapshot.getValue();
                arrayList.addAll(arrayList2);
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent intent = getIntent();
        final String receivedName = intent.getStringExtra(EXTRA_NAME);
        final String receivedEmail = intent.getStringExtra(EXTRA_EMAIL);
        final String receivedPassword = intent.getStringExtra(EXTRA_PASSWORD);
        receivedPRev = intent.getStringExtra(EXTRA_PREV);

        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepTwoSignUp.this, ContributeActivity.class);
                intent.putExtra("Name", receivedName);
                startActivity(intent);
            }
        });

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    collegeName.setVisibility(View.VISIBLE);
                else
                    collegeName.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    branchanme.setError("Enter Branch Name");
                } else if (branch.isEmpty()) {
                    branchanme.setError("Enter Branch Name");
                } else if (roll.isEmpty()) {
                    rollno.setError("Enter Roll Number");
                } else {

                    if (collegeSpinner.getSelectedItem().toString().equals("Other") && collegeName.getEditText().getText().toString().isEmpty())
                        collegeName.setError("Enter College Name");
                    else {


                        String college;
                        if (collegeSpinner.getSelectedItem().toString().equals("Other")) {
                            college = collegeName.getEditText().getText().toString();
                            try {
                                databaseReference = firebaseDatabase.getReference("CollegesInputFromUsers");
                                databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(collegeName.getEditText().getText().toString());
                            } catch (Exception e) {
                                Log.d(TAG, "onClick: " + e.getMessage());
                            }
                        } else
                            college = collegeSpinner.getSelectedItem().toString();

                        if (receivedPRev == null) {//google
                            uploadUserInfo(roll, mAuth.getCurrentUser().getDisplayName(), branch, college);
                            SaveSharedPreference.setUserName(getApplicationContext(), mAuth.getCurrentUser().getEmail());
                            startActivity(new Intent(getApplicationContext(), Navigation.class));
                            finish();
                        } else {//email
                            SaveSharedPreference.setUploaded(StepTwoSignUp.this, true);
                            if (SaveSharedPreference.getUser(StepTwoSignUp.this).equals("")) {
                                listener = FirebaseUserInfo.INSTANCE.getUserInfo(StepTwoSignUp.this, user -> {
                                    try {
                                        SaveSharedPreference.setUser(StepTwoSignUp.this, user.getName());
                                        Intent intent = new Intent(StepTwoSignUp.this, MainActivity.class);
                                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    } catch (Exception ignored) { }
                                    return Unit.INSTANCE;
                                });
                            } else {
                                uploadUserInfo(roll, SaveSharedPreference.getUser(StepTwoSignUp.this), branch, college);
                                Log.d(TAG, "Details Uploaded!");
                                Intent intent = new Intent(StepTwoSignUp.this, MainActivity.class);
                                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        }
                    }
                }


                collegeName.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        collegeName.setError(null);
                    }
                });

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

    @Override
    protected void onDestroy() {
        if (valueListener != null)
            databaseReference.removeEventListener(valueListener);
        if(listener != null)
            listener.remove();
        super.onDestroy();
    }

    private void uploadUserInfo(String roll, String name, String branch, String college) {
        if (mAuth.getCurrentUser() == null) return;
        User user = new User(roll, mAuth.getCurrentUser().getEmail(), name, branch, college);
        FirebaseUserInfo.INSTANCE.uploadUserInfo(user, this);
    }
}

