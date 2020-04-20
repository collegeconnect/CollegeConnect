package com.example.collegeconnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ContactFragment extends Fragment {
    TextView tv;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        tv = getActivity().findViewById(R.id.settingTitle);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        tv = getActivity().findViewById(R.id.settingTitle);
        tv.setText("Contact Us");
        tv.setPadding(0,0,0,0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
    }
}
