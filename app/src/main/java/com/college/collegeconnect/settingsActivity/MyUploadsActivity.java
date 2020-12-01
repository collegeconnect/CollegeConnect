package com.college.collegeconnect.settingsActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.college.collegeconnect.adapters.UploadlistAdapter;
import com.college.collegeconnect.datamodels.Upload;
import com.college.collegeconnect.viewmodels.UploadViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MyUploadsActivity extends AppCompatActivity {

    TextView tv;
    public static ArrayList<Upload> uploadList;
    RecyclerView recyclerView;
    UploadlistAdapter notesAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    public AdView mAdView;
    UploadViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);

        model = new ViewModelProvider(this).get(UploadViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv = findViewById(R.id.tvtitle);
        tv.setText("My Uploads");

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adMyNotes);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        uploadList = new ArrayList<>();
        fetchMvvm();
        swipeRefreshLayout.setOnRefreshListener(this::fetchMvvm);
    }

    private void fetchMvvm(){
        swipeRefreshLayout.setRefreshing(true);
        model.getList().observe(this, new Observer<List<Upload>>() {
            @Override
            public void onChanged(List<Upload> uploads) {
                if (uploads.isEmpty()){
                    Snackbar.make(findViewById(R.id.recycle), "You have not uploaded anything!", Snackbar.LENGTH_LONG).show();
                }
                recyclerView = findViewById(R.id.uploadrecyler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MyUploadsActivity.this));
                notesAdapter = new UploadlistAdapter(MyUploadsActivity.this, (ArrayList<Upload>) uploads);
                recyclerView.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

//    private void fetch() {
//        swipeRefreshLayout.setRefreshing(true);
//        mDatabaseReference.addValueEventListener(listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                uploadList.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    if (upload.getUploaderMail().equals(user.getEmail()))
//                        uploadList.add(upload);
//                }
//                if (uploadList.isEmpty()) {
////                    Toast.makeText(getApplicationContext(),"No PDFs Found",Toast.LENGTH_LONG).show();
//                    Snackbar.make(findViewById(R.id.recycle), "You have not uploaded anything!", Snackbar.LENGTH_LONG).show();
//                } else {
//
//                    recyclerView = findViewById(R.id.uploadrecyler);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(MyUploadsActivity.this));
//                    notesAdapter = new UploadlistAdapter(MyUploadsActivity.this, uploadList);
//                    recyclerView.setAdapter(notesAdapter);
//                    notesAdapter.notifyDataSetChanged();
//                }
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by Name or Author");
        searchView.setIconifiedByDefault(true);
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
}
