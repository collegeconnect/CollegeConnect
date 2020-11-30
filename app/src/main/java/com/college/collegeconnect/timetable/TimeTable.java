package com.college.collegeconnect.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.DatabaseHelper;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ortiz.touchview.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TimeTable extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    DatabaseHelper db;
    private TouchImageView imageView;
    private ProgressBar progressBar;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private StorageReference storageRef;
    private int dot;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tvtitle);
        tv.setText("Timetable");

        imageView = findViewById(R.id.imageView2);
        progressBar = findViewById(R.id.progressBarTT);
        storageRef = storage.getReference();
        databaseReference = firebaseDatabase.getReference();
//        progressBar.setVisibility(View.VISIBLE);
        db = new DatabaseHelper(this);

        //Load TimeTable
//        storageRef.child(SaveSharedPreference.getUserName(getApplicationContext())+"/TimeTable/timetable.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                Picasso.get().load(uri).into(imageView);
//                progressBar.setVisibility(View.GONE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(TimeTable.this, "No Time Table found!", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });
        byte[] image;
        Bitmap bit;
        //load from sqlite
        Cursor res = db.viewAllImage();
        if (res.getCount() == 0)
            Toast.makeText(getApplicationContext(), "No time table found", Toast.LENGTH_LONG).show();
        else {
            while (res.moveToNext()) {
                image = res.getBlob(1);
                bit = getImage(image);
                imageView.setImageBitmap(bit);
                imageView.setRotateImageToFitScreen(true);
//                imageView.setImageBitmap(bit);
//                imageView.setMaxZoom(3);
            }
        }


//        dot = SaveSharedPreference.getUserName(this).indexOf(".");
//        databaseReference.child(SaveSharedPreference.getUserName(this).substring(0,dot)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("TimeTable").getValue()!=null) {
//                    Toast.makeText(TimeTable.this, dataSnapshot.child("TimeTable").getValue().toString(), Toast.LENGTH_SHORT).show();
//                    Picasso.get().load(dataSnapshot.child("TimeTable").getValue().toString()).into(imageView);
//                }
//                else {
//                    Toast.makeText(TimeTable.this, "No Time Table Found", Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaddp();
            }
        });


    }

    public void uploaddp() {
        if (ContextCompat.checkSelfPermission(TimeTable.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(TimeTable.this,
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
            uploaddp();

        } else {
            Toast.makeText(TimeTable.this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void uploadImage() {
        if (filePath != null) {

            progressBar.setVisibility(View.VISIBLE);
            StorageReference unique = storageRef.child(SaveSharedPreference.getUserName(this));
            final StorageReference timeTableref = unique.child("TimeTable/timetable.jpeg");
            timeTableref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TimeTable.this, "TimeTable updated!", Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                        Toast.makeText(TimeTable.this, uri.toString(), Toast.LENGTH_SHORT).show();
//                        dot = SaveSharedPreference.getUserName(getApplicationContext()).indexOf(".");
//                        databaseReference.child(SaveSharedPreference.getUserName(getApplicationContext()).substring(0,dot)).child("TimeTable").setValue(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TimeTable.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void UploadImage() {
        if (filePath != null) {
//            progressBar.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                byte[] image = getBytes(bitmap);
                boolean insert = db.insertImage(image);
                if (insert)
                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT < 28)
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                else
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), filePath));
                imageView.setImageBitmap(bitmap);
                imageView.setRotateImageToFitScreen(true);
//                UploadImage();
                byte[] image1 = getBytes(bitmap);
                boolean insert = db.insertImage(image1);
                db.updateImage(image1);
//                if(insert)
//                    Toast.makeText(getApplicationContext(),"Image uploaded",Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(getApplicationContext(),"Image not uploaded",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}