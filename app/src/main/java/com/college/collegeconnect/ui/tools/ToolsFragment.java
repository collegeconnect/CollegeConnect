package com.college.collegeconnect.ui.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.college.collegeconnect.R;
import com.college.collegeconnect.ui.event.UpcomingEvents;
import com.college.collegeconnect.ui.event.bvest.BvestActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class ToolsFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    TextView tv;
    CardView cardView, events, bvest;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tools, container, false);
        cardView = view.findViewById(R.id.roomLocator);
        events = view.findViewById(R.id.events);
        bvest = view.findViewById(R.id.bvest);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        tv = getActivity().findViewById(R.id.navTitle);
        tv.setText("TOOLS");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d("Tools", "Config params updated: " + updated);
                            if (mFirebaseRemoteConfig.getBoolean("bvest_visibilty")){
                                bvest.setVisibility(View.VISIBLE);
                            }
                            else
                                bvest.setVisibility(View.GONE);
                        } else {
                            Log.d("Tools", "Config params  not updated");
                        }
                    }
                });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RoomLocFragment();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UpcomingEvents.class));
            }
        });

//        if (mFirebaseRemoteConfig.getBoolean("bvest_visibilty")){
//            bvest.setVisibility(View.VISIBLE);
//        }
//        else
//            bvest.setVisibility(View.GONE);

        bvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "BVEST", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), BvestActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_tools).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.nav_tools).setChecked(true);
    }
}