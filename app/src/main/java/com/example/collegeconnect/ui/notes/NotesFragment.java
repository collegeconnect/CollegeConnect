package com.example.collegeconnect.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;import com.example.collegeconnect.DownloadNotes;
import com.example.collegeconnect.R;
import com.example.collegeconnect.UploadNotes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotesFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    TextView tv,tv8;
    ImageView imageView;
    Spinner course, branch, semester, unit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        final View view = inflater.inflate(R.layout.fragment_notes,null);

        tv=getActivity().findViewById(R.id.tvTitle);
        tv.setText("NOTES");

        course = view.findViewById(R.id.CourseN);
        branch = view.findViewById(R.id.BranchN);
        semester = view.findViewById(R.id.SemesterN);
        unit = view.findViewById(R.id.UnitN);
        tv8 = view.findViewById(R.id.textView8);
        imageView=view.findViewById(R.id.imageView10);

        view.findViewById(R.id.fab_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UploadNotes.class));
            }
        });

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(semester.getSelectedItem().toString().equals("Syllabus")){
                    unit.setVisibility(View.INVISIBLE);
                    tv8.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
                else{
                    unit.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.viewnotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selected_course = course.getSelectedItem().toString();
                String selected_branch = branch.getSelectedItem().toString();
                String selected_semester = semester.getSelectedItem().toString();
                String selected_unit = unit.getSelectedItem().toString();

//                Toast.makeText(getActivity(), selected_branch, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DownloadNotes.class);
                intent.putExtra(DownloadNotes.EXTRA_COURSE,selected_course);
                intent.putExtra(DownloadNotes.EXTRA_BRANCH,selected_branch);
                intent.putExtra(DownloadNotes.EXTRA_SEMESTER,selected_semester);
                intent.putExtra(DownloadNotes.EXTRA_UNIT,selected_unit);
                startActivity(intent);
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