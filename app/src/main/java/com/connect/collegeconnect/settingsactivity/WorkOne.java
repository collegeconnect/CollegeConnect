package com.connect.collegeconnect.settingsactivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.connect.collegeconnect.R;
import com.connect.collegeconnect.datamodels.SaveSharedPreference;
import com.connect.collegeconnect.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.squareup.picasso.Picasso;


public class WorkOne extends Fragment {

    private EditText name, aboutMe, personalWebsite, resumeLink;
    private TextView tv;
    private Fragment workTwo = new WorkTwo();
    private ImageButton next;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_one, container, false);

        if (getActivity() != null)
            tv = getActivity().findViewById(R.id.tvtitle);

        name = view.findViewById(R.id.enterWorkName);
        aboutMe = view.findViewById(R.id.editText4);
        personalWebsite = view.findViewById(R.id.enterWorkWebsite);
        resumeLink = view.findViewById(R.id.enterResumeUrl);
        next = view.findViewById(R.id.button3);

        //Get user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        documentReference = firebaseFirestore.collection("resume").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        setValuesFirestore();

        name.setText(SaveSharedPreference.getUser(getActivity()));

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

        return view;
    }

    private void setValuesFirestore() {

        documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                String strName = documentSnapshot.getString("name");
                String strAboutMe = documentSnapshot.getString("aboutMe");
                String strWebsite = documentSnapshot.getString("personalWebsite");
                String strResume = documentSnapshot.getString("resumeLink");
                name.setText(strName);
                aboutMe.setText(strAboutMe);
                personalWebsite.setText(strWebsite);
                resumeLink.setText(strResume);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        tv.setText("Work Profile");
    }
}