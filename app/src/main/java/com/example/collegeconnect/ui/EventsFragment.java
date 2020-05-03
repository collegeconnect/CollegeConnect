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

import com.example.collegeconnect.DatabaseHelper;
import com.example.collegeconnect.DownloadNotes;
import com.example.collegeconnect.R;
import com.example.collegeconnect.adapters.EventsAdapter;
import com.example.collegeconnect.adapters.NotesAdapter;
import com.example.collegeconnect.datamodels.Constants;
import com.example.collegeconnect.datamodels.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EventsFragment extends Fragment {

    private FloatingActionButton createEvent;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    static EventsAdapter eventsAdapter;
    ArrayList<Events> eventsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        recyclerView = view.findViewById(R.id.eventsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadEvents();
        return view;
    }

    public void loadEvents(){
        databaseReference = firebaseDatabase.getReference("Events");
        databaseReference.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pattern = "dd/MM/yyyy";
                String dateInString = new SimpleDateFormat(pattern).format(new Date());
                eventsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Events events = postSnapshot.getValue(Events.class);
//                    Toast.makeText(getContext(), events.getEventName(), Toast.LENGTH_SHORT).show();
                    if(events.getDate().compareTo(dateInString)>=0)
                        eventsList.add(events);
                    if(events.getDate().compareTo(dateInString) < 0) {
                        DatabaseReference mDatabaserefernce = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_PATH_UPLOAD).child(events.getEventName());
                        mDatabaserefernce.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("EventsFragment", "onComplete: Event Removed");
                            }
                        });
                    }
                }
                eventsAdapter = new EventsAdapter(getActivity(),eventsList);
                recyclerView.setAdapter(eventsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
