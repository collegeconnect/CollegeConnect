package com.example.collegeconnect.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Events;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;


public class CreateEvent extends DialogFragment {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button create;
    private EditText name, description, url, eventDate, organizer;
    private ImageButton addImage;
    private static final int GET_FROM_GALLERY = 1;
    private static String date;
    private Uri filePath;
    public String imageUrl;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        if(getActivity()!=null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            fab = getActivity().findViewById(R.id.fab);
        }

        storageRef = storage.getReference();

        create = view.findViewById(R.id.createEventButton);
        name = view.findViewById(R.id.addEventName);
        description = view.findViewById(R.id.addEventDescription);
        url = view.findViewById(R.id.addEventUrl);
        eventDate = view.findViewById(R.id.addEventDate);
        addImage = view.findViewById(R.id.addEventImage);
        imageView = view.findViewById(R.id.viewEventImage);
        organizer = view.findViewById(R.id.addOrganizer);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE )
                        == PackageManager.PERMISSION_DENIED) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            100);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select an image"), GET_FROM_GALLERY);
                }
            }
        });

        Calendar myCalendar = Calendar.getInstance();
        final int year = myCalendar.get(Calendar.YEAR);
        final int month = myCalendar.get(Calendar.MONTH);
        final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        eventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        date = dayOfMonth+"/"+month+"/"+year;
                        eventDate.setText(date);
                    }
                }, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadEvent();

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void uploadEvent()
    {
        if (filePath!=null){

            StorageReference unique = storageRef.child("Event/");
            final StorageReference timeTableref = unique.child( name.getText().toString()+"/Poster.jpeg");
            timeTableref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downlaoduri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            CreateEvent.this.imageUrl = uri.toString();

                            databaseReference = firebaseDatabase.getReference("Events");

//                            Toast.makeText(getActivity(), date+" "+organizer.getText().toString(), Toast.LENGTH_SHORT).show();
                            Events event = new Events(name.getText().toString(),
                                    description.getText().toString(),
                                    imageUrl,
                                    url.getText().toString(),
                                    date,
                                    organizer.getText().toString());

                            databaseReference.child(name.getText().toString()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new UpcomingEvents();
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragmentContainer,fragment)
                                            .commit();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Event not created!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error in uplaoding image!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
        fab.setVisibility(View.INVISIBLE);
        bottomNavigationView.getMenu().findItem(R.id.nav_tools).setChecked(true);
    }
}
