package com.example.collegeconnect.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.collegeconnect.R;

public class UploadListFragment extends Fragment {

    TextView tv;

    public UploadListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload_list, container, false);
        tv = getActivity().findViewById(R.id.settingTitle);
        return view;
    }
    public void onStart() {
        super.onStart();
        tv = getActivity().findViewById(R.id.settingTitle);
        tv.setText("My Uploads");
    }
}
