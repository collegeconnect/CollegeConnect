package com.example.collegeconnect.ui.Download;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.collegeconnect.Constants;
import com.example.collegeconnect.NotesAdapter;
import com.example.collegeconnect.R;
import com.example.collegeconnect.SearchNotes;
import com.example.collegeconnect.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Download extends Fragment {

    NotesAdapter notesAdapter;
    RecyclerView recyclerView;
    static ArrayList<Upload> uploadList;
    static DatabaseReference mDatabaseReference;
    SearchNotes searchNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);

//        uploadList = new ArrayList<>();
//        searchNotes = new SearchNotes();
//        recyclerView = view.findViewById(R.id.downloadRecycler);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        notesAdapter = new NotesAdapter(getContext(), SearchNotes.uploadList);
//        recyclerView.setAdapter(notesAdapter);

        return view;

    }

}
