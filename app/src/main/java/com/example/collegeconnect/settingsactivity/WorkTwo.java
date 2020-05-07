package com.example.collegeconnect.settingsactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.SaveSharedPreference;


public class WorkTwo extends Fragment {

    private ImageButton back;
    private EditText email;
//    private Fragment workOne = new WorkOne();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_two, container, false);

//        next = view.findViewById(R.id.button4);
        back = view.findViewById(R.id.button5);
        email = view.findViewById(R.id.enterWorkEmail);
        email.setText(SaveSharedPreference.getUserName(getActivity()));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }
}
