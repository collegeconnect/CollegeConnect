package com.college.collegeconnect.ui.home;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.college.collegeconnect.BuildConfig;
import com.college.collegeconnect.datamodels.DatabaseHelper;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.Navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    TextDrawable drawable;
    TextView tv, totalAttendance;
    EditText nameField, enrollNo, branch;
    CircleImageView prfileImage;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private DatabaseHelper mydb;
    public Uri uri;
    private StorageReference storageRef;
    private Context mcontext;
    private FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    ListenerRegistration registered;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        prfileImage = view.findViewById(R.id.imageView3);
        nameField = view.findViewById(R.id.nameField);
        enrollNo = view.findViewById(R.id.textView3);
        branch = view.findViewById(R.id.textView4);
        totalAttendance = view.findViewById(R.id.aggregateAttendance);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        tv = getActivity().findViewById(R.id.navTitle);
        tv.setText("HOME");
        storageRef = storage.getReference();

        //Get user id
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        databaseReference = firebaseDatabase.getReference("users/" + userId);
        firebaseFirestore = FirebaseFirestore.getInstance();
        totalAttendance.setEnabled(false);
        nameField.setEnabled(false);
        enrollNo.setEnabled(false);
        branch.setEnabled(false);

//        loadData();

        documentReference = firebaseFirestore.collection("users").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);

        loadDataFirestore();

        File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
        if (file.exists()) {
            HomeFragment.this.uri = Uri.fromFile(file);
            Picasso.get().load(uri).into(prfileImage);
            Log.d("HomeFrag", "onClick: already exists");
        } else {

            storageRef.child("User/" + SaveSharedPreference.getUserName(getActivity()) + "/DP.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    HomeFragment.this.uri = uri;
                    download_dp();

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }

            });
        }

        if (uri != null)
            Picasso.get().load(uri).into(prfileImage);

        mydb = new DatabaseHelper(getContext());
        String pecentage = mydb.calculateTotal();
        if (!pecentage.equals("NaN")) {
            totalAttendance.setText("Aggregate\nAttendance: " + pecentage + "%");
        } else {
            totalAttendance.setText("Aggregate\nAttendance: 0.00%");
        }
    }

    private void download_dp() {
        final DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(getContext().DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(HomeFragment.this.uri);
        request.setDestinationInExternalFilesDir(getContext(), "", "dp.jpeg");
        final long id = downloadManager.enqueue(request);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                if (c != null) {
                    c.moveToFirst();
                    try {
                        String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        HomeFragment.this.uri = Uri.parse(fileUri);
                        Picasso.get().load(uri).into(prfileImage);
                        copyFile("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files", "/dp.jpeg", getContext().getFilesDir().getAbsolutePath());
                        new File("/storage/emulated/0/Android/data/com.college.collegeconnect/files/dp.jpeg").delete();
                    } catch (Exception e) {
                        Log.e("error", "Could not open the downloaded file");
                    }
                }
            }
        };
        getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void loadDataFirestore() {
        registered = documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                try {
                    String name = documentSnapshot.getString("name");
                    String rollNo = documentSnapshot.getString("rollno");
                    String strbranch = documentSnapshot.getString("branch");
                    SaveSharedPreference.setUser(mcontext, name);
                    nameField.setText(SaveSharedPreference.getUser(mcontext));
                    enrollNo.setText(rollNo);
                    branch.setText(strbranch);

                    assert name != null;
                    int space = name.indexOf(" ");
                    int color = Navigation.generatecolor();
                    drawable = TextDrawable.builder().beginConfig()
                            .width(150)
                            .height(150)
                            .bold()
                            .endConfig()
                            .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
                    prfileImage.setImageDrawable(drawable);
                } catch (Exception e) {
                    Log.d("Home", "onEvent: " + e.getMessage());
                }
                if (uri != null)
                    Picasso.get().load(uri).into(prfileImage);
            }
        });
    }

    private void loadData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String name = (String) map.get("Name");
                String rollNo = (String) map.get("Username");
                String college = (String) map.get("branch");
                SaveSharedPreference.setUser(mcontext, name);
                nameField.setText(SaveSharedPreference.getUser(getContext()));
                enrollNo.setText(rollNo);
                branch.setText(college);
                try {
                    int space = name.indexOf(" ");
                    int color = Navigation.generatecolor();
                    drawable = TextDrawable.builder().beginConfig()
                            .width(150)
                            .height(150)
                            .bold()
                            .endConfig()
                            .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
                    prfileImage.setImageDrawable(drawable);
                } catch (Exception e) {

                }
                if (uri != null)
                    Picasso.get().load(uri).into(prfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        if (SaveSharedPreference.getClearall1(mcontext)) {
            File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
            if (file.exists()) {
                HomeFragment.this.uri = Uri.fromFile(file);
                Picasso.get().invalidate(uri);
                SaveSharedPreference.setClearall1(getContext(), false);
                Picasso.get().load(uri).into(prfileImage);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if(registered != null)
            registered.remove();
        super.onDestroyView();
    }
}