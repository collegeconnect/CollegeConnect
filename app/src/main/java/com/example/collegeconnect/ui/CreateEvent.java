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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.collegeconnect.R;
import com.example.collegeconnect.TimeTable;
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

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button create;
    private EditText name, description, url, eventDate, organizer;
    private ImageButton addImage;
    private static final int GET_FROM_GALLERY = 1;
    private String date;
    private LinearLayout blurr;
    private Uri filePath;
    public String imageUrl;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        storageRef = storage.getReference();

        create = view.findViewById(R.id.createEventButton);
        name = view.findViewById(R.id.addEventName);
        description = view.findViewById(R.id.addEventDescription);
        url = view.findViewById(R.id.addEventUrl);
        eventDate = view.findViewById(R.id.addEventDate);
        addImage = view.findViewById(R.id.addEventImage);
        imageView = view.findViewById(R.id.viewEventImage);
        organizer = view.findViewById(R.id.addOrganizer);
        blurr = view.findViewById(R.id.blurrScreenEvent);
        imageUrl = "";
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageadd();
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
                        String mon;
                        String day;
                        if(month<10)
                            mon = "0"+month;
                        else
                            mon = String.valueOf(month);
                        if(dayOfMonth<10)
                            day = "0"+dayOfMonth;
                        else
                            day = String.valueOf(dayOfMonth);


                        date = day+"/"+mon+"/"+year;
                        eventDate.setText(date);
                    }
                }, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        final String Ename = name.getText().toString();
        final String Edescription = description.getText().toString();
        final String Eurl = url.getText().toString();
        final String Eorganizer = organizer.getText().toString();
        final String Edate = eventDate.getText().toString();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Ename.isEmpty() || Edescription.isEmpty() || Eurl.isEmpty() || Edate.isEmpty() || Eorganizer.isEmpty() || imageUrl.equals("")){
                    if (Ename.isEmpty())
                        name.setError("Field cannot be empty");
                    if (Edescription.isEmpty())
                        description.setError("Field cannot be empty");
                    if (Eurl.isEmpty())
                        url.setError("Field cannot be empty");
                    if (Edate.isEmpty())
                        eventDate.setError("Field cannot be empty");
                    if (Eorganizer.isEmpty())
                        organizer.setError("Field cannot be empty");
                    if (imageUrl.equals(""))
                        Toast.makeText(getActivity(), "Please upload event poster!", Toast.LENGTH_SHORT).show();
                }
                else {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    blurr.setVisibility(View.VISIBLE);
                    uploadEvent();
                }
            }
        });

        return view;
    }
    public void imageadd(){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageadd();

        } else {
            Toast.makeText(getContext(),
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
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

                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    blurr.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();

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
//        getActivity().getSupportFragmentManager().popBackStack();
    }
}
