package com.college.collegeconnect.settingsActivity;

import  android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

public class WorkOne extends Fragment {

    private EditText name, aboutMe, personalWebsite, resumeLink;
    private TextView tv;
    private final Fragment workTwo = new WorkTwo();
    private ImageButton next;
    private DocumentReference documentReference;
    ListenerRegistration listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_one, container, false);

        name = view.findViewById(R.id.enterWorkName);
        aboutMe = view.findViewById(R.id.editText4);
        personalWebsite = view.findViewById(R.id.enterWorkWebsite);
        resumeLink = view.findViewById(R.id.enterResumeUrl);
        next = view.findViewById(R.id.button3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            tv = getActivity().findViewById(R.id.tvtitle);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        documentReference = firebaseFirestore.collection("resume").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        setValuesFirestore();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String about = aboutMe.getText().toString();
                String website = personalWebsite.getText().toString();
                String resume = resumeLink.getText().toString();

                if (about == null || website == null || resume == null) {

                    if (about == null) {
                        aboutMe.setError("Description cannot be empty");
                    }
                } else {
                    Bundle argumnets = new Bundle();
                    argumnets.putString("aboutMe", about);
                    argumnets.putString("website", website);
                    argumnets.putString("resume", resume);
                    workTwo.setArguments(argumnets);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.workprofilecontainer, workTwo)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    private void setValuesFirestore() {

        listener = documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                try {
                    assert documentSnapshot != null;
                    String strAboutMe = documentSnapshot.getString("aboutMe");
                    String strWebsite = documentSnapshot.getString("personalWebsite");
                    String strResume = documentSnapshot.getString("resumeLink");
                    name.setText(SaveSharedPreference.getUser(getActivity()));
                    aboutMe.setText(strAboutMe);
                    personalWebsite.setText(strWebsite);
                    resumeLink.setText(strResume);
                } catch (Exception ignored) {

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        tv.setText("Work Profile");
    }

    @Override
    public void onDestroyView() {
        if (listener != null)
            listener.remove();
        super.onDestroyView();
    }
}