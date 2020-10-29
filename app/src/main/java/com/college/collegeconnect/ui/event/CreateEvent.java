package com.college.collegeconnect.ui.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.ImageCreateAdapter;
import com.college.collegeconnect.datamodels.Events;
import com.college.collegeconnect.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseUtil.getDatabase();
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private EditText name, description, url, eventDate, organizer, endeventDate;
    private static final int GET_FROM_GALLERY = 1;
    private String date, endDate;
    private LinearLayout blurr;
    private Uri filePath;
    public String imageUrl;
    private ImageView imageView;
    ViewPager viewPagerImage;
    TabLayout viewPagerIndicator;
    ArrayList<Uri> images = new ArrayList<>();
    ArrayList<String> imageurl = new ArrayList<>();
    ProgressBar progressBar;
    private long millis = 0;
    ValueEventListener listener;

    @SuppressLint("RestrictedApi")
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
        progressBar = findViewById(R.id.progloading);
        progressBar.setVisibility(View.GONE);
        viewPagerImage = findViewById(R.id.viewEventImage);
        viewPagerIndicator = findViewById(R.id.tabCreateEvent);
        Button create = findViewById(R.id.createEventButton);
        name = findViewById(R.id.addEventName);
        description = findViewById(R.id.addEventDescription);
        url = findViewById(R.id.addEventUrl);
        eventDate = findViewById(R.id.addEventDate);
        ImageButton addImage = findViewById(R.id.addEventImage);
        organizer = findViewById(R.id.addOrganizer);
        blurr = findViewById(R.id.blurrScreenEvent);
        endeventDate = findViewById(R.id.addendEventDate);
        imageUrl = "";

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageadd();
                v.bringToFront();
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
                        month = month + 1;
                        String mon;
                        String day;
                        if (month < 10)
                            mon = "0" + month;
                        else
                            mon = String.valueOf(month);
                        if (dayOfMonth < 10)
                            day = "0" + dayOfMonth;
                        else
                            day = String.valueOf(dayOfMonth);


                        date = day + "/" + mon + "/" + year;
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
                        month = month + 1;
                        String mon;
                        String day;
                        if (month < 10)
                            mon = "0" + month;
                        else
                            mon = String.valueOf(month);
                        if (dayOfMonth < 10)
                            day = "0" + dayOfMonth;
                        else
                            day = String.valueOf(dayOfMonth);


                        endDate = day + "/" + mon + "/" + year;
                        endeventDate.setText(endDate);
                    }
                }, year, month, day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (millis == 0)
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

                if (Ename.isEmpty()) {
                    name.setError("Field cannot be empty");
                    return;
                }
                if (Edescription.isEmpty()) {
                    description.setError("Field cannot be empty");
                    return;
                }
                if (Eurl.isEmpty()) {
                    url.setError("Field cannot be empty");
                    return;
                }
                if (Edate.isEmpty()) {
                    eventDate.setError("Field cannot be empty");
                    return;
                }
                if (EndDate.isEmpty()) {
                    endeventDate.setError("Field cannot be empty");
                    return;
                }
                if (Eorganizer.isEmpty()) {
                    organizer.setError("Field cannot be empty");
                    return;
                }
                if (images.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please upload event poster!", Toast.LENGTH_SHORT).show();
                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    blurr.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
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

    public void imageadd() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select an image"), GET_FROM_GALLERY);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK) {

//            filePath = data.getData();
            images.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    filePath = data.getClipData().getItemAt(i).getUri();
                    images.add(filePath);
                }
            } else if (data.getData() != null) {
                filePath = data.getData();
                images.add(filePath);
            }
            ImageCreateAdapter imageCreateAdapter = new ImageCreateAdapter(images, getApplicationContext());
            viewPagerImage.setAdapter(imageCreateAdapter);
            if (images.size() == 1)
                viewPagerIndicator.setVisibility(View.GONE);
            viewPagerIndicator.setupWithViewPager(viewPagerImage, true);
        }
    }

    private void uploadEvent() {
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                filePath = images.get(i);
                StorageReference unique = storageRef.child("Event/");
                final StorageReference timeTableref = unique.child(name.getText().toString() + "/Poster" + i + ".jpeg");
                timeTableref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> downlaoduri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                CreateEvent.this.imageUrl = uri.toString();


                                databaseReference = firebaseDatabase.getReference("Events");
                                databaseReference.child(name.getText().toString()).child("imageUrl").addValueEventListener(listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.getValue();
                                        if (arrayList != null)
                                            imageurl = (ArrayList<String>) arrayList.clone();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                imageurl.add(imageUrl);

                                Toast.makeText(getApplicationContext(), date + " " + organizer.getText().toString(), Toast.LENGTH_SHORT).show();
                                Events event = new Events(name.getText().toString(),
                                        description.getText().toString(),
                                        imageurl,
                                        url.getText().toString(),
                                        date,
                                        organizer.getText().toString(), endDate);

                                databaseReference.child(name.getText().toString()).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        blurr.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (listener != null)
            databaseReference.removeEventListener(listener);
        super.onDestroy();
    }
}
