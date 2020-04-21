package com.example.collegeconnect.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.collegeconnect.Constants;
import com.example.collegeconnect.DividerItemDecoration;
import com.example.collegeconnect.DownloadNotes;
import com.example.collegeconnect.NotesAdapter;
import com.example.collegeconnect.R;
import com.example.collegeconnect.Upload;
import com.example.collegeconnect.UploadlistAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class UploadListFragment extends Fragment {

    TextView tv;
    public static ArrayList<Upload> uploadList;
    static DatabaseReference mDatabaseReference;
    RecyclerView recyclerView;
    UploadlistAdapter notesAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public UploadListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_upload_list, container, false);
        tv = getActivity().findViewById(R.id.settingTitle);
        setHasOptionsMenu(true);
        uploadList = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if(upload.getUploaderMail().equals(user.getEmail()))
                        uploadList.add(upload);
                }
                if(uploadList.isEmpty()){
//                    Toast.makeText(getApplicationContext(),"No PDFs Found",Toast.LENGTH_LONG).show();
                    Snackbar.make(view,"You have not uploaded anything!",Snackbar.LENGTH_LONG).show();
                }
                else {

                    recyclerView = view.findViewById(R.id.uploadrecyler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    notesAdapter = new UploadlistAdapter(getContext(), uploadList);
                    recyclerView.setAdapter(notesAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view,menu);
        Drawable drawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        drawable.mutate();
        drawable.setColorFilter(getResources().getColor(R.color.newBlue), PorterDuff.Mode.SRC_IN);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by Name");
        EditText searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchedittext.setTextColor(Color.BLACK);
        searchedittext.setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notesAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        tv = getActivity().findViewById(R.id.settingTitle);
        tv.setText("My Uploads");
        tv.setPadding(140,0,0,0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
    }
}
