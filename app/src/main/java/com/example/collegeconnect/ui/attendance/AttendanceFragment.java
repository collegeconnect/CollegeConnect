package com.example.collegeconnect.ui.attendance;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeconnect.DatabaseHelper;
import com.example.collegeconnect.R;
import com.example.collegeconnect.SubjectAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    DatabaseHelper mydb;
    TextInputLayout subject;
    Button addSubject;
    private static ArrayList<String> subjectList;
    private RecyclerView subjectRecycler;
    static SubjectAdapter subjectAdapter;
    TextView tv;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance,null);
        tv = getActivity().findViewById(R.id.navTitle);
        tv.setText("ATTENDANCE");

        mydb= new DatabaseHelper(getContext());

        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        subjectList = new ArrayList<>();
        subjectRecycler = view.findViewById(R.id.subjectRecyclerView);
        subjectRecycler.setHasFixedSize(true);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(subjectList,getContext());
        subjectRecycler.setAdapter(subjectAdapter);
        loadData();

        subject = view.findViewById(R.id.subjectNamemas);
        addSubject = view.findViewById(R.id.addSubject);

        subject.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                subject.setError(null);
            }
        });

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubject();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_attendance).setChecked(true);
    }

    public void loadData()
    {
        Cursor res = mydb.viewAllData();

        while (res.moveToNext()) {
            subjectList.add(res.getString(1));
            subjectAdapter.notifyDataSetChanged();
        }
    }

    public void addSubject()
    {
        if(subject.getEditText().getText().toString().isEmpty() || subject.getEditText().getText().toString().equals(""))
            subject.setError("Enter a Subject");
        else {
            boolean res = mydb.insetData(subject.getEditText().getText().toString(), "0", "0");
            if (res == true) {
                Toast.makeText(getContext(), "Subject added successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Data not added", Toast.LENGTH_SHORT).show();

            subjectList.add(subject.getEditText().getText().toString());
            subjectAdapter.notifyDataSetChanged();
            subject.getEditText().setText("");
            subject.clearFocus();
        }
    }

    public static void notifyChange()
    {
        subjectAdapter.notifyDataSetChanged();
    }
}