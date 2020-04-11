package com.example.collegeconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.collegeconnect.ui.attendance.AttendanceFragment;
import com.example.collegeconnect.ui.home.HomeFragment;
import com.example.collegeconnect.ui.notes.NotesFragment;
import com.example.collegeconnect.ui.tools.ToolsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;
import java.util.Random;

public class navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    GoogleSignInClient mgoogleSignInClient;
    BottomNavigationView bottomNavigationView;
    static int color;
    private  DatabaseHelper db;
    Fragment homefrag = new HomeFragment();
    Fragment attenfrag = new AttendanceFragment();
    Fragment notefrag = new NotesFragment();
    Fragment toolsfrag = new ToolsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null)
            SaveSharedPreference.setUser(navigation.this,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else {
            int dot = SaveSharedPreference.getUserName(navigation.this).indexOf(".");
            databaseReference = firebaseDatabase.getReference("users/" + SaveSharedPreference.getUserName(navigation.this).substring(0, dot));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    String name = (String) map.get("Name");
                    SaveSharedPreference.setUser(navigation.this, name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

//        Toast.makeText(this, "Welcome "+firebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, SaveSharedPreference.getUserName(this), Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setColorFilter(getResources().getColor(R.color.colorwhite));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timetable();
            }
        });
        Random random  = new Random();
        color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        db = new DatabaseHelper(this);

        loadFragments(homefrag);

        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);  */
    }

    public static int generatecolor(){

        return color;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_logout :
                Dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void Dialog(){
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
                        signOut();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        db.deleteall();
                        SaveSharedPreference.clearUserName(getApplication());

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
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void timetable()
    {
        Intent intent1 = new Intent(this, TimeTable.class);
        startActivity(intent1);
    }
    private void signOut() {
        mgoogleSignInClient.signOut();
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);
        super.onStart();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                fragment = homefrag;
                break;
            case R.id.nav_attendance:
                fragment = attenfrag;
                break;
            case R.id.nav_notes:
                fragment = notefrag;
                break;
            case R.id.nav_tools:
                fragment = toolsfrag;
                break;
        }
        return loadFragments(fragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof HomeFragment) {
            finish();
        }

        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
        }

        else
            super.onBackPressed();
    }

    private boolean loadFragments(Fragment fragment)
    {
        if (fragment!=null)
        {
            Log.d("navigation", "loadFragments: Frag is loaded");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer,fragment)
                    .addToBackStack(null)
                    .commit();

            return true;
        }
        return false;
    }
}
