package com.example.collegeconnect.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.collegeconnect.DownloadNotes;
import com.example.collegeconnect.R;
import com.example.collegeconnect.adapters.EventsAdapter;
import com.example.collegeconnect.adapters.NotesAdapter;
import com.example.collegeconnect.datamodels.Events;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UpcomingEvents extends Fragment {

    private FloatingActionButton createEvent;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    static EventsAdapter eventsAdapter;
    ArrayList<Events> eventsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        if(getActivity()!=null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            fab = getActivity().findViewById(R.id.fab);
        }

        eventsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsAdapter = new EventsAdapter(getActivity(),eventsList);
        recyclerView.setAdapter(eventsAdapter);
        loadEvents();

        createEvent = view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CreateEvent();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void loadEvents(){
        databaseReference = firebaseDatabase.getReference("Events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Events events = postSnapshot.getValue(Events.class);
//                    Toast.makeText(getContext(), events.getEventName(), Toast.LENGTH_SHORT).show();
                    eventsList.add(events);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        eventsAdapter.notifyDataSetChanged();
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
