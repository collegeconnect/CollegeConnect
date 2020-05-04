package com.example.collegeconnect.ui.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.collegeconnect.EventWebView;
import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EventDetailsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ImageView banner;
    private TextView evntName, startingDate, endingDate;
    private EditText description;
    private Button register;
    String registrationurl;
    private FloatingActionButton floatingActionButton;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        if(getActivity()!=null) {
            floatingActionButton = getActivity().findViewById(R.id.createEvent);
            TextView tv = getActivity().findViewById(R.id.tvtitle);
            tv.setText("Event Details");
        }


        banner = view.findViewById(R.id.eventBanner);
        evntName = view.findViewById(R.id.eventName);
        startingDate = view.findViewById(R.id.StartingDate);
        endingDate = view.findViewById(R.id.EndingDate);
        description = view.findViewById(R.id.eventDescription);
        register = view.findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (registrationurl!=null) {
                    Bundle arguments = new Bundle();
                    arguments.putString("Url", registrationurl);

                    EventWebView eventWebView = new EventWebView();
                    eventWebView.setArguments(arguments);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameupcomingevents, eventWebView)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

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
                registrationurl = (String)map.get("registrationUrl");
                String date = (String)map.get("date");
                String endDate = (String)map.get("endDate");

                Picasso.get().load(imageurl).into(banner);
                evntName.setText(name);
                description.setText(Description);
                startingDate.setText(date);
                endingDate.setText(endDate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        floatingActionButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }
}
