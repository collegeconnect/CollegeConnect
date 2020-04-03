package com.example.collegeconnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class DownloadNotes extends AppCompatActivity {

    public static final String EXTRA_COURSE = "course";
    public static final String EXTRA_BRANCH = "branch";
    public static final String EXTRA_SEMESTER = "semester";
    public static final String EXTRA_UNIT = "unit";
    public static ArrayList<Upload> uploadList;
    static DatabaseReference mDatabaseReference;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_notes);

        Intent intent = getIntent();
        final String receivedCourse = intent.getStringExtra(EXTRA_COURSE);
        final String receivedBranch = intent.getStringExtra(EXTRA_BRANCH);
        final String receivedSemester = intent.getStringExtra(EXTRA_SEMESTER);
        final String receivedUnit = intent.getStringExtra(EXTRA_UNIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        Toast.makeText(this, receivedSemester, Toast.LENGTH_SHORT).show();

        uploadList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.orderByChild("download").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getCourse().equals(receivedCourse)) {
                        if(upload.getBranch().equals(receivedBranch)) {
                            if(upload.getSemester().equals(receivedSemester)) {
                                if(upload.getUnit().equals(receivedUnit)) {
                                    uploadList.add(upload);
//                                    Toast.makeText(DownloadNotes.this, upload.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
                Collections.reverse(uploadList);
                recyclerView = findViewById(R.id.downloadRecycler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(DownloadNotes.this));
                notesAdapter = new NotesAdapter(DownloadNotes.this, uploadList);
                recyclerView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
