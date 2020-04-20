package com.example.collegeconnect.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeconnect.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AboutFragment extends Fragment {
    TextView tv;
    private StringBuilder text = new StringBuilder();

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        tv = getActivity().findViewById(R.id.settingTitle);


        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getActivity().getAssets().open("about_text.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

            EditText output = view.findViewById(R.id.about_text);
            output.setText((CharSequence) text);
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        tv = getActivity().findViewById(R.id.settingTitle);
        tv.setText("About");
        tv.setPadding(0,0,0,0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
    }
}
