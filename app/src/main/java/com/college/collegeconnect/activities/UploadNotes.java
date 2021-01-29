package com.college.collegeconnect.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.datamodels.Upload;
import com.college.collegeconnect.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class UploadNotes extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    private Intent Data = null;
    private TextInputLayout fileName, author;
    private Button upload;
    Intent receivedIntent;
    private TextView tv8;
    private ImageView imageView;
    private Uri recievedUri;
    //these are the views
    private TextView textViewStatus;
    private ProgressBar progressBar;
    private Spinner semester, branch, course, unit;
    //the firebase objects for storage and database
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        tv8 = findViewById(R.id.textView11);
        imageView = findViewById(R.id.imageView11);

        //getting firebase objects
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseUtil.getDatabase().getReference(Constants.DATABASE_PATH_UPLOADS);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv_title = findViewById(R.id.tvtitle);
        tv_title.setText("Upload Notes");
//        if (SaveSharedPreference.getCheckedItem(this) == 0)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        else if (SaveSharedPreference.getCheckedItem(this) == 1)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else if (SaveSharedPreference.getCheckedItem(this) == 2)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //getting the views
        textViewStatus = findViewById(R.id.textViewStatus);
        semester = findViewById(R.id.spinnerSem);
        branch = findViewById(R.id.spinnerBranch);
        course = findViewById(R.id.spinnerCourse);
        unit = findViewById(R.id.spinnerUnit);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (semester.getSelectedItem().toString().equals("Syllabus")) {
                    unit.setVisibility(View.INVISIBLE);
                    tv8.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    unit.setSelection(0);
                } else {
                    unit.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        editTextFilename = findViewById(R.id.FileName);
        progressBar = findViewById(R.id.UploadNotesProgressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        Button upload = findViewById(R.id.selectNotes);
        if (onSharedIntent() && recievedUri != null) {
            Log.i("Upload Notes", "onCreate: " + recievedUri);
            UploadNotes.this.Data = receivedIntent;
            UploadNotes.this.Data.setData(recievedUri);
            upload.setText("Upload");
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();

            }
        });

    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(UploadNotes.this, new String[]{"Manifest.permission.READ_EXTERNAL_STORAGE"},100);
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            return;
//        }
        if (UploadNotes.this.Data == null) {
            if (ContextCompat.checkSelfPermission(UploadNotes.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(UploadNotes.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            } else {

                //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
            }
        } else {
            StringBuilder filename = new StringBuilder();
            String str;
            if (recievedUri != null)
                str = recievedUri.getLastPathSegment();
            else
                str = UploadNotes.this.Data.getData().getLastPathSegment();
            int slash = -1;
            if (str.contains("/")) {
                slash = str.indexOf("/");
                filename.append(str.substring(slash, str.length() - 1));
            }
            String str1 = str.substring(slash + 1, str.length() - 1);
            if (str1.contains(".")) {
                int dot = str1.indexOf(".");
                filename.append(str1.substring(0, dot));
            }
            alertDialog(filename.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPDF();

        } else {
            Toast.makeText(UploadNotes.this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            StringBuilder filename = new StringBuilder();
            String str = data.getData().getLastPathSegment();
            int slash = -1;
            if (str.contains("/")) {
                slash = str.indexOf("/");
                filename.append(str.substring(slash, str.length() - 1));
            }
            String str1 = str.substring(slash + 1, str.length() - 1);
            if (str1.contains(".")) {
                int dot = str1.indexOf(".");
                filename.append(str1.substring(0, dot));
            }

            //uploading the file
            UploadNotes.this.Data = data;
            textViewStatus.setText("File Selected!");
            alertDialog(filename.toString());


//                findViewById(R.id.uploadNotes).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        uploadFile(data.getData());
//                    }
//                });
        } else {
            Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialog(String filename) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNotes.this);
        LayoutInflater inflater = LayoutInflater.from(UploadNotes.this);
        final View view = inflater.inflate(R.layout.layout_dialog_notes, null);

        builder.setView(view);
        fileName = view.findViewById(R.id.fileName);
        author = view.findViewById(R.id.authorName);
        Objects.requireNonNull(fileName.getEditText()).setText(filename);
        Objects.requireNonNull(author.getEditText()).setText(SaveSharedPreference.getUser(this));
        author.getEditText().setEnabled(false);
        upload = view.findViewById(R.id.btn_upload_notes);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (UploadNotes.this.Data != null)
                    upload.setText("Upload");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                    upload.setText("Upload");
            }
        });
        fileName.getEditText().setText(filename);
        upload.setOnClickListener(v -> {
            String file = fileName.getEditText().getText().toString();
            String authorName = author.getEditText().getText().toString();

            if (file.isEmpty() && authorName.isEmpty()) {
                fileName.setError("Filename cannot be empty");
                author.setError("Author name cannot be empty");
                return;
            }
            if (file.isEmpty()) {
                fileName.setError("Filename cannot be empty");
                fileName.requestFocus();
                return;
            }
            if (authorName.isEmpty()) {
                author.setError("Author name cannot be empty");
                author.requestFocus();
                return;
            }
            applyTexts(file, authorName);
            UploadNotes.this.Data = null;
            dialog.dismiss();
        });
        fileName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fileName.setError(null);
            }
        });
        author.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                author.setError(null);
            }
        });
    }


    //this method is uploading the file
    private void uploadFile(Uri data, final String filename, final String authorname) {

        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + course.getSelectedItem().toString() + "/" + branch.getSelectedItem().toString() + "/" + semester.getSelectedItem().toString() + "/" + unit.getSelectedItem().toString() + "/" + System.currentTimeMillis());
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
//                        final Uri downloadi;
                        textViewStatus.setText("File Uploaded Successfully");
                        Task<Uri> downlaoduri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Upload upload = new Upload(filename,
                                        course.getSelectedItem().toString(),
                                        semester.getSelectedItem().toString(),
                                        branch.getSelectedItem().toString(),
                                        unit.getSelectedItem().toString(),
                                        authorname, 0, uri.toString(),
                                        System.currentTimeMillis(),
                                        SaveSharedPreference.getUserName(UploadNotes.this), new ArrayList() {
                                });
                                mDatabaseReference.child(upload.getTimestamp() + "").setValue(upload);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setProgress(0);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        textViewStatus.setText("");
                        progressBar.setProgress((int) progress);

                    }
                });

    }

    public void applyTexts(String filename, String authorname) {

        if (UploadNotes.this.Data.getData() == null) {
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
        } else
            uploadFile(UploadNotes.this.Data.getData(), filename, authorname);
    }

    public boolean onSharedIntent() {
        receivedIntent = getIntent();
        Bundle bundle = receivedIntent.getExtras();
        if (bundle != null) {
            String receivedAction = receivedIntent.getAction();
            String receivedType = receivedIntent.getType();
            if (receivedIntent != null) {
                Log.i("Upload Notes", "onSharedIntent: " + receivedType + "::::" + receivedAction);
                if (receivedType.contains("pdf")) {
                    recievedUri = receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (recievedUri != null) {
                        Log.i("Upload Notes", "onSharedIntent: " + recievedUri.toString());
                        textViewStatus.setText("File Selected!");
                        return true;
                    }

                }
            }
        }
        return false;
    }

}

