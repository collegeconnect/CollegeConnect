package com.connect.collegeconnect.settingsactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.connect.collegeconnect.R;
import com.connect.collegeconnect.WorkProfile;
import com.connect.collegeconnect.datamodels.Resume;
import com.connect.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;


public class WorkTwo extends Fragment {

    private ImageButton back;
    private EditText email, linkedIn, github, behance, medium;
    private ImageButton upload;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_two, container, false);

        Bundle args = getArguments();

        back = view.findViewById(R.id.button5);
        email = view.findViewById(R.id.enterWorkEmail);
        linkedIn = view.findViewById(R.id.enterWorkLinkedIn);
        github = view.findViewById(R.id.enterWorkGitHub);
        behance = view.findViewById(R.id.enterWorkBehance);
        medium = view.findViewById(R.id.enterWorkMedium);
        upload = view.findViewById(R.id.button4);

        //Get user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        documentReference = firebaseFirestore.collection("resume").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        setValuesFirestore();

        email.setText(SaveSharedPreference.getUserName(getActivity()));

        final String aboutMe = args.getString("aboutMe");
        final String website = args.getString("website");
        final String resumeLink = args.getString("resume");

        Log.i("TAG", "onCreateView: "+aboutMe+" "+website+" "+resumeLink);

        final String email = SaveSharedPreference.getUserName(getActivity());

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String strlinkedIn = linkedIn.getText().toString();
                final String strGitHub = github.getText().toString();
                final String strBehance = behance.getText().toString();
                String strMedium = medium.getText().toString();
                final Resume resume = new Resume(SaveSharedPreference.getUser(getActivity()),aboutMe,website,resumeLink,email,strlinkedIn,strGitHub,strBehance,strMedium);
                Log.i("TAG2", "onCreateView: "+strBehance+" "+strGitHub+" "+strlinkedIn);
                firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = firebaseFirestore.collection("resume");
                collectionReference.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(resume).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Resume", "onSuccess: Resume Uploaded");
                        Toast.makeText(getContext(), "Resume Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        getActivity().startActivity(new Intent(getContext(),SettingsActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Resume", "onFailure: Resume failed :"+e.getMessage());
                        Toast.makeText(getContext(), "An error occurred. Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }

    private void setValuesFirestore() {

        documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                String strLinkedIn = documentSnapshot.getString("linkedIn");
                String strGitHub = documentSnapshot.getString("github");
                String strBehance = documentSnapshot.getString("behance");
                String strMedium = documentSnapshot.getString("medium");
                linkedIn.setText(strLinkedIn);
                github.setText(strGitHub);
                behance.setText(strBehance);
                medium.setText(strMedium);
            }
        });
    }
}
