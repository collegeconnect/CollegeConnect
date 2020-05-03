package com.example.collegeconnect.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Events;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button create;
    private EditText name, description, url, eventDate, organizer, endeventDate;
    private ImageButton addImage;
    private static final int GET_FROM_GALLERY = 1;
    private String date, endDate;
    private LinearLayout blurr;
    private Uri filePath ;
    public String imageUrl;
    private ImageView imageView;
    private long millis = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_events);
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tvtitle);
        tv.setText("Create Event");
        storageRef = storage.getReference();

        create = findViewById(R.id.createEventButton);
        name = findViewById(R.id.addEventName);
        description = findViewById(R.id.addEventDescription);
        url = findViewById(R.id.addEventUrl);
        eventDate = findViewById(R.id.addEventDate);
        addImage = findViewById(R.id.addEventImage);
        imageView = findViewById(R.id.viewEventImage);
        organizer = findViewById(R.id.addOrganizer);
        blurr = findViewById(R.id.blurrScreenEvent);
        endeventDate = findViewById(R.id.addendEventDate);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date dates = simpleDateFormat.parse(date);
                            millis = dates.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        eventDate.setText(date);
                    }
                }, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
            }
        });
        endeventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
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


                        endDate = day+"/"+mon+"/"+year;
                        endeventDate.setText(endDate);
                    }
                }, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if(millis == 0)
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                else
                    datePickerDialog.getDatePicker().setMinDate(millis);
                datePickerDialog.show();

            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Ename = name.getText().toString();
                final String Edescription = description.getText().toString();
                final String Eurl = url.getText().toString();
                final String Eorganizer = organizer.getText().toString();
                final String Edate = eventDate.getText().toString();
                final String EndDate = endeventDate.getText().toString();

                if (Ename.isEmpty() || Edescription.isEmpty() || Eurl.isEmpty() || Edate.isEmpty() || EndDate.isEmpty() || Eorganizer.isEmpty() ){
                    if (Ename.isEmpty())
                        name.setError("Field cannot be empty");
                    if (Edescription.isEmpty())
                        description.setError("Field cannot be empty");
                    if (Eurl.isEmpty())
                        url.setError("Field cannot be empty");
                    if (Edate.isEmpty())
                        eventDate.setError("Field cannot be empty");
                    if(EndDate.isEmpty())
                        endeventDate.setError("Field cannot be empty");
                    if (Eorganizer.isEmpty())
                        organizer.setError("Field cannot be empty");
                }
                else {
                   getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    blurr.setVisibility(View.VISIBLE);
                    uploadEvent();
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name.setError(null);
            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                description.setError(null);
            }
        });
        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                url.setError(null);
            }
        });
        organizer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                organizer.setError(null);
            }
        });
        eventDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                eventDate.setError(null);
            }
        });
        endeventDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                endeventDate.setError(null);
            }
        });
    }

    public void imageadd(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE )
                == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this,
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
            Toast.makeText(getApplicationContext(),
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap;
                if(Build.VERSION.SDK_INT<28)
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                else
                   bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),filePath));
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
                                    organizer.getText().toString(),endDate);

                            databaseReference.child(name.getText().toString()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    blurr.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Event not created!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error in uploading image!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
