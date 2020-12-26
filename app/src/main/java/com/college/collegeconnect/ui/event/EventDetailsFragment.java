package com.college.collegeconnect.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.ImageAdapter;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.utils.FirebaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EventDetailsFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase = FirebaseUtil.getDatabase();
    private TextView eventName, startingDate, endingDate;
    private TextView description;
    private Button register, calendar;
    String registrationurl;
    private FloatingActionButton floatingActionButton;
    private ViewPager imagesViewpager;
    private TabLayout viewpagerIndicator;
    private ArrayList<String> eventImages = new ArrayList<>();
    private Context mContext;
    ValueEventListener listener, listener2;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        String desired_string = arguments.getString("Name");
        if (desired_string != null)
            databaseReference = firebaseDatabase.getReference(Constants.EVENTS_PATH_UPLOAD).child(desired_string);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
//        banner = view.findViewById(R.id.eventBanner);
        imagesViewpager = view.findViewById(R.id.eventBanner);
        viewpagerIndicator = view.findViewById(R.id.viewPager_indicator);
        eventName = view.findViewById(R.id.eventName);
        startingDate = view.findViewById(R.id.StartingDate);
        endingDate = view.findViewById(R.id.EndingDate);
        description = view.findViewById(R.id.eventDescription);
        register = view.findViewById(R.id.registerButton);
        calendar = view.findViewById(R.id.btn_add_to_calendar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            floatingActionButton = getActivity().findViewById(R.id.createEvent);
            TextView tv = getActivity().findViewById(R.id.tvtitle);
            tv.setText("Event Details");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (registrationurl != null) {
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

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToCalendar();
            }
        });

        loadDetails();
    }

    private void addEventToCalendar() {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, eventName.getText().toString());
//        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
        intent.putExtra(CalendarContract.Events.DTSTART,getMilli(startingDate.getText().toString()));
        intent.putExtra(CalendarContract.Events.DTEND,getMilli(endingDate.getText().toString()));
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);

        if(intent.resolveActivity(mContext.getPackageManager()) != null){
            startActivity(intent);
        }else{
            Toast.makeText(mContext, "There is no app that support this action", Toast.LENGTH_SHORT).show();
        }

    }

    private Long getMilli(String mDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault());
        Date time = null;
        try {
            time = simpleDateFormat.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "getMilli: "+time);
        Calendar cali = Calendar.getInstance();
        cali.setTime(time);
        return cali.getTimeInMillis();
    }

    private void loadDetails() {
        databaseReference.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    String name = (String) map.get("eventName");
                    String Description = (String) map.get("eventDescription");
                    String organiser = (String) map.get("organizer");
                    eventImages = (ArrayList<String>) map.get("imageUrl");
                    registrationurl = (String) map.get("registrationUrl");
                    String date = (String) map.get("date");
                    String endDate = (String) map.get("endDate");

//                Picasso.get().load(imageurl).into(banner);
                    Log.d("change", "onDataChange: " + eventImages.get(0));

                    eventName.setText(name);
                    description.setText(Description);
                    startingDate.setText(date(date));
                    endingDate.setText(date(endDate));
                    ImageAdapter imageAdapter = new ImageAdapter(eventImages);
                    imagesViewpager.setAdapter(imageAdapter);
                    if (eventImages.size() == 1)
                        viewpagerIndicator.setVisibility(View.INVISIBLE);
                    viewpagerIndicator.setupWithViewPager(imagesViewpager, true);
                }
                catch(Exception ignored){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String date(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM yy");
        Date datetext = null;
        String str = null;

        try {
            datetext = inputFormat.parse(date);
            assert datetext != null;
            str = outputFormat.format(datetext);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStop() {

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        databaseReference = firebaseDatabase.getReference("EventAdmin");
        databaseReference.addListenerForSingleValueEvent(listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.getValue();
                if (arrayList.contains(SaveSharedPreference.getUserName(mContext)))
                    floatingActionButton.setVisibility(View.VISIBLE);
                else
                    floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStop();

    }


    @Override
    public void onStart() {
        super.onStart();
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        if (listener != null)
            databaseReference.removeEventListener(listener);
        if (listener2 != null)
            databaseReference.removeEventListener(listener2);
        super.onDestroyView();
    }
}
