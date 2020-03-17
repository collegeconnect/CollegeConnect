package com.example.collegeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DownloadNotes extends AppCompatActivity {

    public static final String EXTRA_COURSE = "course";
    public static final String EXTRA_BRANCH = "branch";
    public static final String EXTRA_SEMESTER = "semester";
    public static final String EXTRA_UNIT = "unit";
    public static ArrayList<Upload> uploadList;
    static DatabaseReference mDatabaseReference;
    RecyclerView recyclerView;
    ListView listView;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_notes);

        Intent intent = getIntent();
        String receivedCourse = intent.getStringExtra(EXTRA_COURSE);
        final String receivedBranch = intent.getStringExtra(EXTRA_BRANCH);
        String receivedSemester = intent.getStringExtra(EXTRA_SEMESTER);
        String receivedUnit = intent.getStringExtra(EXTRA_UNIT);

        Toast.makeText(this, receivedSemester, Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.list);

        uploadList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getBranch().equals(receivedBranch)) {
                        uploadList.add(upload);
                        Toast.makeText(DownloadNotes.this, upload.getName(), Toast.LENGTH_SHORT).show();
                    }
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        recyclerView = findViewById(R.id.downloadRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, uploadList);
        recyclerView.setAdapter(notesAdapter);
    }
}
