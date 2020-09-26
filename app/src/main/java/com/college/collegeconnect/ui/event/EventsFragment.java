package com.college.collegeconnect.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.EventsAdapter;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.datamodels.Events;
import com.college.collegeconnect.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EventsFragment extends Fragment {

    private FloatingActionButton createEvent;
    FirebaseDatabase firebaseDatabase = FirebaseUtil.getDatabase();
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    static EventsAdapter eventsAdapter;
    ArrayList<Events> eventsList = new ArrayList<>();
    TextView textView, tv;
    ValueEventListener listener;
    SwipeRefreshLayout swiperefreshlayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        textView = view.findViewById(R.id.tv_noEvent);
        swiperefreshlayout = view.findViewById(R.id.eventSwipe);
        recyclerView = view.findViewById(R.id.eventsRecycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            tv = getActivity().findViewById(R.id.tvtitle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadEvents();
        swiperefreshlayout.setOnRefreshListener(this::loadEvents);
    }

    public void loadEvents() {
        swiperefreshlayout.setRefreshing(true);
        databaseReference = firebaseDatabase.getReference("Events");
        databaseReference.orderByChild("date").addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pattern = "dd/MM/yyyy";
                String dateInString = new SimpleDateFormat(pattern).format(new Date());
                eventsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Events events = postSnapshot.getValue(Events.class);
//                    Toast.makeText(getContext(), events.getEventName(), Toast.LENGTH_SHORT).show();
                    if (events.getEndDate().compareTo(dateInString) >= 0)
                        eventsList.add(events);
                    if (events.getEndDate().compareTo(dateInString) < 0) {

                        for (int i = 0; i < events.getImageUrl().size(); i++) {
                            StorageReference delete = FirebaseStorage.getInstance().getReferenceFromUrl(events.getImageUrl().get(i));
                            Log.d("change", "onDataChange22: " + events.getImageUrl().get(i));
                            delete.delete();
                        }
                        DatabaseReference mDatabaserefernce = FirebaseUtil.getDatabase().getReference(Constants.EVENTS_PATH_UPLOAD).child(events.getEventName());
                        mDatabaserefernce.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("EventsFragment", "onComplete: Event Removed");
                            }
                        });
                    }
                }
                if (eventsList.isEmpty())
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.GONE);
                eventsAdapter = new EventsAdapter(getActivity(), eventsList);
                recyclerView.setAdapter(eventsAdapter);
                swiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swiperefreshlayout.setRefreshing(false);
                Log.d("Events", "onCancelled: "+ databaseError.getMessage());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        tv.setText("Upcoming Events");
    }

    @Override
    public void onDestroyView() {
        if(listener != null)
            databaseReference.removeEventListener(listener);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if(listener != null)
            databaseReference.removeEventListener(listener);
        super.onDestroy();
    }
}
