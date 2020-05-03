package com.example.collegeconnect.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EventDetailsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        Bundle arguments = getArguments();
        String desired_string = arguments.getString("Name");
        if(desired_string != null)
            databaseReference = firebaseDatabase.getReference(Constants.EVENTS_PATH_UPLOAD).child(desired_string);
        loadDetails();


        return view;
    }

    private void loadDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map= (Map<String,Object>)dataSnapshot.getValue();
                String name = (String) map.get("eventName");
                String Description = (String) map.get("eventDescription");
                String organiser = (String) map.get("organizer");
                String imageurl = (String)map.get("imageUrl");
                String registrationurl = (String)map.get("registrationUrl");
                String date = (String)map.get("date");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
