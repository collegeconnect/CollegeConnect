package com.college.collegeconnect.datamodels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    public String rollno;
    public String email;
    public String name;
    public String year;
    public String branch;
    public String college;
    public static FirebaseFirestore firebaseFirestore;
    public static FirebaseDatabase firebaseDatabase;
    public static FirebaseAuth auth;

    public String getYear() {
        return year;
    }

    public String getRollno() {
        return rollno;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getCollege() {
        return college;
    }

    public User() {
        rollno = null;
        email = null;
        name = null;
        branch = null;
        year = null;
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String rollNo, String email, String name, String branch, String college, String year) {
        this.rollno = rollNo;
        this.email = email;
        this.name = name;
        this.branch = branch;
        this.college = college;
        this.year = year;
    }

    public static boolean addUser(String username, String email, String name, String branch, String college, String year) {
        User user = new User(username, email, name, branch, college, year);

        //Get user id
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;

        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("User", "onSuccess: Account Created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("User", "onFailure: Account failed :" + e.getMessage());
            }
        });
//            String userId = firebaseUser.getUid();
//            firebaseDatabase=FirebaseDatabase.getInstance();
//            DatabaseReference myRef = firebaseDatabase.getReference("users");
//            int dot = email.indexOf(".");
//            String str = email.replace(".","@");
//            myRef.child(userId).setValue(user);
        return true;
    }
}
