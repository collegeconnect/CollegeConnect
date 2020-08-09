package com.college.collegeconnect.ui.attendance;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.datamodels.DatabaseHelper;
import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.SubjectAdapter;
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
    Context mCtx;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        subjectRecycler = view.findViewById(R.id.subjectRecyclerView);
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


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = getActivity().findViewById(R.id.navTitle);
        tv.setText("ATTENDANCE");

        mydb = new DatabaseHelper(getContext());

        if (getActivity() != null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        subjectList = new ArrayList<>();
        subjectRecycler.setHasFixedSize(true);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(subjectList, mCtx);
        subjectRecycler.setAdapter(subjectAdapter);
        loadData();
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubject();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_attendance).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.nav_attendance).setChecked(true);
    }

    public void loadData() {
        Cursor res = mydb.viewAllData();

        while (res.moveToNext()) {
            subjectList.add(res.getString(1));
            subjectAdapter.notifyDataSetChanged();
        }
    }

    public void addSubject() {
        if (subject.getEditText().getText().toString().isEmpty() || subject.getEditText().getText().toString().equals(""))
            subject.setError("Enter a Subject");
        else {
            boolean res = mydb.insetData(subject.getEditText().getText().toString(), "0", "0");
            if (res == true) {
                Toast.makeText(getContext(), "Subject added successfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Data not added", Toast.LENGTH_SHORT).show();
            try {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }

            subjectList.add(subject.getEditText().getText().toString());
            subjectAdapter.notifyDataSetChanged();
            subject.getEditText().setText("");
            subject.clearFocus();
        }
    }

    public static void notifyChange() {
        subjectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCtx = context;
    }
}