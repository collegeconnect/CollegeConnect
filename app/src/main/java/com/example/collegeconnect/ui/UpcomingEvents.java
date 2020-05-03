package com.example.collegeconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.signature.ObjectKey;
import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UpcomingEvents extends AppCompatActivity {
    private FloatingActionButton createEvent;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Fragment upcomingevents = new EventsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = firebaseDatabase.getReference("EventAdmin");
        setContentView(R.layout.activity_upcoming_event);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tvtitle);
        tv.setText("Upcoming Events");
        createEvent = findViewById(R.id.createEvent);
        createEvent.setVisibility(View.GONE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.getValue();
                if(arrayList.contains(SaveSharedPreference.getUserName(UpcomingEvents.this)))
                    createEvent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(UpcomingEvents.this, CreateEvent.class));
            }
        });

        loadFragments(upcomingevents);
    }
    private boolean loadFragments(Fragment fragment)
    {
        if (fragment!=null)
        {
            Log.d("navigation", "loadFragments: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameupcomingevents,fragment)
                    .commit();

            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                FragmentManager mgr = getSupportFragmentManager();
                if (mgr.getBackStackEntryCount() == 0) {
                    super.onBackPressed();
                    finish();
                }
                else
                    mgr.popBackStack();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
