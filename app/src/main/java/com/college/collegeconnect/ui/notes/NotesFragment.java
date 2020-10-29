package com.college.collegeconnect.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.college.collegeconnect.activities.DownloadNotes;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.activities.UploadNotes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    private TextView tv8;
    ImageView imageView;
    Spinner course, branch, semester, unit;
    private FloatingActionButton upload;
    private Button viewnotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_notes, container, false);
        course = view.findViewById(R.id.CourseN);
        branch = view.findViewById(R.id.BranchN);
        semester = view.findViewById(R.id.SemesterN);
        unit = view.findViewById(R.id.UnitN);
        tv8 = view.findViewById(R.id.textView8);
        imageView = view.findViewById(R.id.imageView10);
        upload = view.findViewById(R.id.fab_upload);
        viewnotes = view.findViewById(R.id.viewnotes);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
        TextView tv = getActivity().findViewById(R.id.navTitle);
        tv.setText("NOTES");
        course.setSelection(SaveSharedPreference.getCourse(getContext()));
        branch.setSelection(SaveSharedPreference.getBranch(getContext()));
        semester.setSelection(SaveSharedPreference.getSemester(getContext()));
        unit.setSelection(SaveSharedPreference.getUnit(getContext()));

        upload.setOnClickListener(view -> startActivity(new Intent(getContext(), UploadNotes.class)));

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (semester.getSelectedItem().toString().equals("Syllabus")) {
                    unit.setVisibility(View.INVISIBLE);
                    tv8.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    unit.setSelection(0);

                } else {
                    unit.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selected_course = course.getSelectedItem().toString();
                String selected_branch = branch.getSelectedItem().toString();
                String selected_semester = semester.getSelectedItem().toString();
                String selected_unit = unit.getSelectedItem().toString();

                SaveSharedPreference.setCourse(getContext(), course.getSelectedItemPosition());
                SaveSharedPreference.setBranch(getContext(), branch.getSelectedItemPosition());
                SaveSharedPreference.setSemester(getContext(), semester.getSelectedItemPosition());
                SaveSharedPreference.setUnit(getContext(), unit.getSelectedItemPosition());
//                Toast.makeText(getActivity(), selected_branch, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DownloadNotes.class);
                Bundle bundle = new Bundle();
                bundle.putString(DownloadNotes.EXTRA_COURSE, selected_course);
                bundle.putString(DownloadNotes.EXTRA_BRANCH, selected_branch);
                bundle.putString(DownloadNotes.EXTRA_SEMESTER, selected_semester);
                bundle.putString(DownloadNotes.EXTRA_UNIT, selected_unit);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_notes).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.nav_notes).setChecked(true);
    }
}