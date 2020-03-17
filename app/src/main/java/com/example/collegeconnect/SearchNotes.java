package com.example.collegeconnect;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchNotes {

    public static ArrayList<Upload> uploadList;
    static DatabaseReference mDatabaseReference;

    public static void getNotes(final String course, final String Branch, final String Sem, final String Unit) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getCourse().equals(course) && upload.getBranch().equals(Branch) && upload.getSemester().equals(Sem) && upload.getUnit().equals(Unit)) {
                        uploadList.add(upload);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<Upload> getUploadList() {
        return uploadList;
    }
}
