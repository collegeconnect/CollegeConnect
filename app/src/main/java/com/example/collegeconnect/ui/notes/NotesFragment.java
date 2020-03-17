package com.example.collegeconnect.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.collegeconnect.Constants;
import com.example.collegeconnect.NotesAdapter;
import com.example.collegeconnect.R;
import com.example.collegeconnect.Upload;
import com.example.collegeconnect.UploadNotes;
import com.example.collegeconnect.ui.Download.Download;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    TextView tv;
    Spinner course, branch, semester, unit;
    ListView listView;
//    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    public static List<Upload> uploadList;
    static DatabaseReference mDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        View view = inflater.inflate(R.layout.fragment_notes,null);

        tv=getActivity().findViewById(R.id.tvTitle);
        tv.setText("Notes");

//        recyclerView = view.findViewById(R.id.downloadRecycler);
//        listView = view.findViewById(R.id.);
        course = view.findViewById(R.id.CourseN);
        branch = view.findViewById(R.id.BranchN);
        semester = view.findViewById(R.id.SemesterN);
        unit = view.findViewById(R.id.UnitN);

        view.findViewById(R.id.fab_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UploadNotes.class));
            }
        });

        final Download download = new Download();

//        final String courses = course.getSelectedItem().toString();
//        final String branches = branch.getSelectedItem().toString();
//        final String semesters = semester.getSelectedItem().toString();
//        final String units = unit.getSelectedItem().toString();

        view.findViewById(R.id.viewnotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadList = new ArrayList<>();
                Toast.makeText(getActivity(), branch.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Upload upload = postSnapshot.getValue(Upload.class);
                            if (upload.getBranch().equals(branch.getSelectedItem().toString())) {
                                uploadList.add(upload);
                                Toast.makeText(getActivity(), upload.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                String[] uploads = new String[uploadList.size()];
//
//                for (int i = 0; i < uploads.length; i++) {
//                    uploads[i] = uploadList.get(i).getName();
//                }
//
//                //displaying it to list
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, uploads);
//                listView.setAdapter(adapter);

//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                notesAdapter = new NotesAdapter(getActivity(), uploadList);
//                recyclerView.setAdapter(notesAdapter);
                Toast.makeText(getActivity(), "Works", Toast.LENGTH_SHORT).show();
//                SearchNotes.getNotes(courses,branches,semesters,units);
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmentContainer, download)
//                        .addToBackStack(null)
//                        .commit();
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