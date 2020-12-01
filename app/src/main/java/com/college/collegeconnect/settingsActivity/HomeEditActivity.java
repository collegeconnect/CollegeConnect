package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.college.collegeconnect.BuildConfig;
import com.college.collegeconnect.R;
import com.college.collegeconnect.customviews.DoneListener;
import com.college.collegeconnect.customviews.EditTextWithEditButton;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.datamodels.User;
import com.college.collegeconnect.activities.Navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeEditActivity extends AppCompatActivity implements DoneListener {

    private TextDrawable drawable;
    private EditTextWithEditButton nameField, enrollNo, branch, college, year;
    private ImageButton imageButton;
    private CircleImageView prfileImage;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri uri;
    private StorageReference storageRef;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LinearLayout blurr;
    private static final int GET_FROM_GALLERY = 1;
    ProgressBar progressBar;
    DocumentReference documentReference;
    ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initialize views
        TextView tv = findViewById(R.id.tvtitle);
        prfileImage = findViewById(R.id.imageView3copy);
        nameField = findViewById(R.id.nameFieldcopy);
        enrollNo = findViewById(R.id.textView3copy);
        branch = findViewById(R.id.textView4copy);
        college = findViewById(R.id.textView5copy);
        year = findViewById(R.id.textView6copy);
        imageButton = findViewById(R.id.edit_dp);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        blurr = findViewById(R.id.blurrScreen);

        tv.setText("Edit Details");
        storageRef = storage.getReference();
        File file = new File("/data/user/0/"+BuildConfig.APPLICATION_ID+"/files/dp.jpeg");
        if (file.exists()) {
            HomeEditActivity.this.uri = Uri.fromFile(file);
        }

        if (uri != null)
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(prfileImage);

        //Get user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

//        nameField.setEnabled(false);
//        enrollNo.setEnabled(false);
//        branch.setEnabled(false);
//        imageButton.setEnabled(false);
//        college.setEnabled(false);
//        year.setEnabled(false);
        setDoneListener(nameField, enrollNo, branch, college, year);

        //Initialize Firebase Firestore
        documentReference = firebaseFirestore.collection("users").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        setValuesFirestore();
//        edit();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePic();
            }
        });
    }

    @Override
    public void onDonePressed() {
        Log.d("HomeEditActivity", "Trying to save user");
        String name = nameField.getText().toString();
        String enroll = enrollNo.getText().toString();
        String branch = HomeEditActivity.this.branch.getText().toString();
        String strCollege = college.getText().toString();
        String strYear = year.getText().toString();
        SaveSharedPreference.setCollege(HomeEditActivity.this, strCollege);
        SaveSharedPreference.setUser(this,name);
        Intent intent = new Intent();
        intent.putExtra("NAME",name);
        setResult(10,intent);
        User.addUser(enroll, firebaseAuth.getCurrentUser().getEmail(), name, branch, strCollege, strYear);
    }

    public void setDoneListener(EditTextWithEditButton... editTexts) {
        for (EditTextWithEditButton editText : editTexts) {
            editText.setDoneListener(this);
        }
    }

    //Download dp to storage
    private void download_dp() {
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(HomeEditActivity.this.uri);
        request.setDestinationInExternalFilesDir(this, "", "dp.jpeg");
        final long id = downloadManager.enqueue(request);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                if (c != null) {
                    c.moveToFirst();
                    try {
                        String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        HomeEditActivity.this.uri = Uri.parse(fileUri);
                        copyFile("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files", "/dp.jpeg", getFilesDir().getAbsolutePath());
                        new File("/storage/emulated/0/Android/data/"+ BuildConfig.APPLICATION_ID + "/files/dp.jpeg").delete();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        blurr.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Log.e("error", "Could not open the downloaded file");
                    }
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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

        } catch (Exception fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }

    }

    //Select dp from storage
    private void getProfilePic() {
        if (ContextCompat.checkSelfPermission(HomeEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(HomeEditActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select an image"), GET_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getProfilePic();

        } else {
            Toast.makeText(this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void edit() {
        nameField.setEnabled(true);
        enrollNo.setEnabled(true);
        branch.setEnabled(true);
        year.setEnabled(true);
        college.setEnabled(true);
        imageButton.setEnabled(true);
    }

    private void setValuesFirestore() {
        listener = documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                try {
                    assert documentSnapshot != null;
                    String name = documentSnapshot.getString("name");
                    String rollNo = documentSnapshot.getString("rollno");
                    String branch = documentSnapshot.getString("branch");
                    String strYear = documentSnapshot.getString("year");
                    String strCollege = documentSnapshot.getString("college");
                    SaveSharedPreference.setUser(getApplicationContext(), name);
                    nameField.setText(SaveSharedPreference.getUser(getApplicationContext()));
                    enrollNo.setText(rollNo);
                    college.setText(strCollege);
                    year.setText(strYear);
                    HomeEditActivity.this.branch.setText(branch);

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
                if (uri != null)
                    Picasso.get().load(uri).into(prfileImage);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            CropImage.activity(filePath).setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    prfileImage.setImageBitmap(bitmap);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.VISIBLE);
                    blurr.setVisibility(View.VISIBLE);
                    uploadImage(resultUri);
                } catch (Exception e) {
                    Log.d("HomeEditActivity", "onActivityResult: CropImage failed");
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }
        }
    }

    //Upload dp to Firebase Storage
    private void uploadImage(Uri resultUri) {
        if (resultUri != null) {

            StorageReference unique = storageRef.child("User/");
            final StorageReference timeTableref = unique.child(SaveSharedPreference.getUserName(this) + "/DP.jpeg");
            timeTableref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    File file = new File("/data/user/0/com.college.collegeconnect/files/dp.jpeg");
                    if (file.exists())
                        if (file.delete())

                            storageRef.child("User/" + SaveSharedPreference.getUserName(HomeEditActivity.this) + "/DP.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    HomeEditActivity.this.uri = uri;
                                    download_dp();
                                    SaveSharedPreference.setClearall(HomeEditActivity.this, true);
                                    SaveSharedPreference.setClearall1(HomeEditActivity.this, true);

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(HomeEditActivity.this,"Unable to Update Details. Try Again Later",Toast.LENGTH_SHORT).show();
                                }

                            });

                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(HomeEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        if(listener != null)
            listener.remove();
        super.onDestroy();
    }

}
