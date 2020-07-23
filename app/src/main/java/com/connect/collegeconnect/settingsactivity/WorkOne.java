package com.connect.collegeconnect.settingsactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connect.collegeconnect.R;
import com.connect.collegeconnect.datamodels.SaveSharedPreference;


public class WorkOne extends Fragment {

    private EditText name;
    private TextView tv;
    private Fragment workTwo = new WorkTwo();
    private ImageButton next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_work_one, container, false);

        if (getActivity()!=null)
            tv = getActivity().findViewById(R.id.tvtitle);

        name = view.findViewById(R.id.enterWorkName);
        next = view.findViewById(R.id.button3);

        name.setText(SaveSharedPreference.getUser(getActivity()));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.workprofilecontainer,workTwo)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        tv.setText("Work Profile");
    }
}