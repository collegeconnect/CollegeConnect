package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.college.collegeconnect.activities.OnBoardingScreen;
import com.college.collegeconnect.datamodels.DatabaseHelper;
import com.college.collegeconnect.utils.DividerItemDecoration;
import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.SettingsAdapter;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.activities.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    GoogleSignInClient mgoogleSignInClient;
    private Button logout;
    private RecyclerView recyclerView;
    private SettingsAdapter settingsAdapter;
    CircleImageView prfileImage;
    TextDrawable drawable;
    TextView nameField;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getColor(R.color.latestBlue), PorterDuff.Mode.SRC_ATOP);
        db = new DatabaseHelper(this);
        //Set profile image and name
        prfileImage = findViewById(R.id.settings_dp);
        nameField = findViewById(R.id.textView16);
        String name = SaveSharedPreference.getUser(this);
        nameField.setText(name);
        File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
        //Load dp if already exits in storage
        if (file.exists()) {
            SettingsActivity.this.uri = Uri.fromFile(file);
            Picasso.get().load(uri).into(prfileImage);
        } else {
            try {
                assert name != null;
                int space = name.indexOf(" ");
                int color = Navigation.generateColor();
                drawable = TextDrawable.builder().beginConfig()
                        .width(150)
                        .height(150)
                        .bold()
                        .endConfig()
                        .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
                prfileImage.setImageDrawable(drawable);
            } catch (Exception e) {

            }

        }
        ArrayList<String> options = new ArrayList<>();
//        options.add("Update Profile");
//        options.add("Theme");
        options.add("Set Attendance Criteria");
        options.add("My Files");
        options.add("Work Profile");
        options.add("Rate Us");
        options.add("Contact Us");
        options.add("About");
        options.add("Export Attendance");

        recyclerView = findViewById(R.id.settings_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        settingsAdapter = new SettingsAdapter(options, this);
        recyclerView.setAdapter(settingsAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, 80, 0);
        recyclerView.addItemDecoration(decoration);
        //Edit user details
        findViewById(R.id.profile_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SettingsActivity.this, HomeEditActivity.class),10);
            }
        });
        //Logout
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> logOutDialog());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && data!=null){
            nameField.setText(data.getStringExtra("NAME"));
        }
    }

    public void logOutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        // Setting Dialog Title
        builder.setTitle("Confirm SignOut");
        // Setting Dialog Message
        builder.setMessage("Are you sure you want to Signout?\nAll your saved data wil be lost!");

        builder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        FirebaseAuth.getInstance().signOut();
                        FirebaseFirestore.getInstance().clearPersistence();
                        signOut();
                        Intent i = new Intent(SettingsActivity.this, OnBoardingScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        db.deleteall();
                        File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
                        if (file.exists())
                            file.delete();
                        SaveSharedPreference.clearUserName(SettingsActivity.this);
                        startActivity(i);
                        finish();
                    }
                });

        // Setting Negative "NO" Btn
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void signOut() {
        mgoogleSignInClient.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gitHub) {
            String url = "https://github.com/collegeconnect/CollegeConnect";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getColor(R.color.latestBlue));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveSharedPreference.getClearall(this)) {
            File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
            if (file.exists()) {
                SettingsActivity.this.uri = Uri.fromFile(file);
                Picasso.get().invalidate(uri);
                SaveSharedPreference.setClearall(SettingsActivity.this, false);
                Picasso.get().load(uri).into(prfileImage);
            }
        }
    }
}
