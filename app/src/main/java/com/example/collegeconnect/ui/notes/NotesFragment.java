package com.example.collegeconnect.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.collegeconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotesFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        tv=getActivity().findViewById(R.id.tvTitle);
        tv.setText("Notes");
        View view = inflater.inflate(R.layout.fragment_notes,null);
        view.findViewById(R.id.fab_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),UploadNotes.class));
            }
        });

        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_notes).setChecked(true);
    }
}