package com.bvcoe.bvpconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;import java.io.IOException;

public class TimeTable extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 1;
    private ImageView imageView;
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

        imageView = findViewById(R.id.imageView2);
        progressBar = findViewById(R.id.progressBarTT);
        storageRef = storage.getReference();
        databaseReference = firebaseDatabase.getReference();
        progressBar.setVisibility(View.VISIBLE);

        //Load TimeTable
        storageRef.child(SaveSharedPreference.getUserName(getApplicationContext())+"/TimeTable/timetable.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(imageView);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(TimeTable.this, "No Time Table found!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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


    }

    public void uploaddp(View view)
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"),GET_FROM_GALLERY);
    }

    private void uploadImage()
    {
        if (filePath!=null){

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
