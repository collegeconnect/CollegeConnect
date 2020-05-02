package com.example.collegeconnect.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Events;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateEvent extends Fragment {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private Button create;
    private EditText name, description, url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        if(getActivity()!=null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            fab = getActivity().findViewById(R.id.fab);
        }

        create = view.findViewById(R.id.createEventButton);
        name = view.findViewById(R.id.addEventName);
        description = view.findViewById(R.id.addEventDescription);
        url = view.findViewById(R.id.addEventUrl);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = firebaseDatabase.getReference("Events");
                Events event = new Events(name.getText().toString(), description.getText().toString(), url.getText().toString());
                databaseReference.child(name.getText().toString()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Event Created Successfully!", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        description.setText("");
                        url.setText("");
                        url.clearFocus();
                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
        fab.setVisibility(View.INVISIBLE);
        bottomNavigationView.getMenu().findItem(R.id.nav_tools).setChecked(true);
    }
}
