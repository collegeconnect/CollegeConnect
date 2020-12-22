package com.college.collegeconnect.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.NotesAdapter;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.viewmodels.DownloadNotesViewModel;
import com.college.collegeconnect.utils.FirebaseUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.firebase.database.DatabaseReference;

import java.util.Collections;

public class DownloadNotes extends AppCompatActivity {

    public static final String EXTRA_COURSE = "course";
    public static final String EXTRA_BRANCH = "branch";
    public static final String EXTRA_SEMESTER = "semester";
    public static final String EXTRA_UNIT = "unit";
    //    public static ArrayList<Upload> uploadList;
    DatabaseReference mDatabaseReference;
    public RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    public AdView mAdView;
    TextView tv;
    //    String receivedCourse;
//    String receivedBranch;
//    String receivedSemester;
//    String receivedUnit;
    SwipeRefreshLayout swiperefreshlayout;
    //    ValueEventListener listener;
    DownloadNotesViewModel downloadNotesViewModel;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_notes);

        bundle = getIntent().getExtras();
//        assert bundle != null;
//        receivedCourse = bundle.getString(EXTRA_COURSE);
//        receivedBranch = bundle.getString(EXTRA_BRANCH);
//        receivedSemester = bundle.getString(EXTRA_SEMESTER);
//        receivedUnit = bundle.getString(EXTRA_UNIT);

        swiperefreshlayout = findViewById(R.id.SwipeRefreshLayout);
        downloadNotesViewModel = new ViewModelProvider(this).get(DownloadNotesViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv = findViewById(R.id.tvtitle);
        tv.setText("Notes");

        //AdMob
        MobileAds.initialize(this, initializationStatus -> {

        });
        mAdView = findViewById(R.id.adViewNotes);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        uploadList = new ArrayList<>();
        mDatabaseReference = FirebaseUtil.getDatabase().getReference(Constants.DATABASE_PATH_UPLOADS);
        mDatabaseReference.keepSynced(true);
        loadData();
        swiperefreshlayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        swiperefreshlayout.setRefreshing(true);
        downloadNotesViewModel.downloadNotes(bundle).observe(this, uploadList -> {
            Collections.reverse(uploadList);
            recyclerView = findViewById(R.id.downloadRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(DownloadNotes.this));
            notesAdapter = new NotesAdapter(DownloadNotes.this, uploadList, downloadNotesViewModel);
            recyclerView.setAdapter(notesAdapter);
        });
        swiperefreshlayout.setRefreshing(false);
//        mDatabaseReference.orderByChild("download").addValueEventListener(listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                uploadList.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    if (upload.getCourse().equals(receivedCourse)) {
//                        if (upload.getBranch().equals(receivedBranch)) {
//                            if (upload.getSemester().equals(receivedSemester)) {
//                                if (upload.getUnit().equals(receivedUnit)) {
//                                    uploadList.add(upload);
////                                    Toast.makeText(DownloadNotes.this, upload.getName(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    }
//                }
//                if (uploadList.isEmpty()) {
////                    Toast.makeText(getApplicationContext(),"No PDFs Found",Toast.LENGTH_LONG).show();
//                    Snackbar.make(findViewById(R.id.relativeshit), "No PDFs Found", Snackbar.LENGTH_LONG).show();
//                } else {
//
//                    Collections.reverse(uploadList);
//                    recyclerView = findViewById(R.id.downloadRecycler);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(DownloadNotes.this));
//                    notesAdapter = new NotesAdapter(DownloadNotes.this, uploadList);
//                    recyclerView.setAdapter(notesAdapter);
//                }
//                swiperefreshlayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                swiperefreshlayout.setRefreshing(false);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 95){
            assert data != null;
            rateDialog(data.getLongExtra("timestamp",0));
        }
    }

    private void rateDialog(long timestamp) {
        //TODO(rating Dialog pops up)
        //TODO(use timestamp tp create a database reference to store ratings and tags
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by Name or Author");
        EditText searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchedittext.setTextColor(Color.WHITE);
        searchedittext.setHintTextColor(Color.parseColor("#50F3F9FE"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    notesAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                }
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tv.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tv.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return true;
    }

//    @Override
//    protected void onDestroy() {
//        if (listener != null)
//            mDatabaseReference.removeEventListener(listener);
//        super.onDestroy();
//    }
}
