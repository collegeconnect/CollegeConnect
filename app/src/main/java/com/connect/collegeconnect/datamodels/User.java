package com.connect.collegeconnect.datamodels;

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

        public String Rollno;
        public String Email;
        public String Name;
        public String Password;
        public String Branch;
        private static FirebaseFirestore firebaseFirestore;
        private static FirebaseDatabase firebaseDatabase;
        private static FirebaseAuth auth;

    public User() {
            Rollno =null;
            Email =null;
            Name =null;
            Password =null;
            Branch =null;
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String rollNo, String email, String name, String password, String branch) {
            this.Rollno = rollNo;
            this.Email = email;
            this.Name =name;
            this.Password =password;
            this.Branch = branch;
        }

        public static boolean addUser(String username, String email, String name, String password, String branch)
        {
            User user = new User(username,email,name,password,branch);

            //Get user id
            firebaseFirestore = FirebaseFirestore.getInstance();
//
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
                    Log.d("User", "onFailure: Account failed :"+e.getMessage());
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
