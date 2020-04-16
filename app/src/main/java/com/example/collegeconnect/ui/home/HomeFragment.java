package com.example.collegeconnect.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.amulyakhare.textdrawable.TextDrawable;
import com.example.collegeconnect.DatabaseHelper;
import com.example.collegeconnect.R;
import com.example.collegeconnect.SaveSharedPreference;
import com.example.collegeconnect.User;
import com.example.collegeconnect.navigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    TextDrawable drawable;
    TextView tv, totalAttendance;
    EditText nameField,enrollNo, branch;
    ImageButton imageButton;
    CircleImageView prfileImage;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private DatabaseHelper mydb;
    Uri uri;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private Uri filePath;
    FloatingActionButton editDetails,submitDetails;
    DatabaseHelper databaseHelper;
    private static final int GET_FROM_GALLERY = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getActivity()!=null)
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);

        tv=getActivity().findViewById(R.id.tvTitle);
        tv.setText("HOME");

        View view =  inflater.inflate(R.layout.fragment_home,null);

        storageRef = storage.getReference();
        int dot = SaveSharedPreference.getUserName(getContext()).indexOf(".");
        databaseReference = firebaseDatabase.getReference("users/"+SaveSharedPreference.getUserName(getContext()).substring(0,dot));
        prfileImage = view.findViewById(R.id.imageView3);
        nameField = view.findViewById(R.id.nameField);
        enrollNo = view.findViewById(R.id.textView3);
        branch = view.findViewById(R.id.textView4);
        imageButton = view.findViewById(R.id.imageButton);
        editDetails = view.findViewById(R.id.editDetails);
        submitDetails = view.findViewById(R.id.submitDetails);
        totalAttendance = view.findViewById(R.id.aggregateAttendance);
//        circleprog = view.findViewById(R.id.cicleprog);
        totalAttendance.setEnabled(false);
        nameField.setEnabled(false);
        enrollNo.setEnabled(false);
        branch.setEnabled(false);
        imageButton.setEnabled(false);
//        circleprog.setMax(100);
//        circleprog.setProgress(0);
        submitDetails.setColorFilter(getResources().getColor(R.color.colorwhite));

//        nameField.setText(firebaseAuth.getCurrentUser().getDisplayName());
//        try {
//            String name = SaveSharedPreference.getUser(getActivity());
//            int space = name.indexOf(" ");
//            int color = navigation.generatecolor();
//            drawable = TextDrawable.builder().beginConfig()
//                    .width(150)
//                    .height(150)
//                    .bold()
//                    .endConfig()
//                    .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
//            prfileImage.setImageDrawable(drawable);
//        }
//        catch (Exception e){
//
//        }
        datachange();

        storageRef.child("User/"+SaveSharedPreference.getUserName(getActivity())+"/DP.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                HomeFragment.this.uri = uri;
                if (uri!=null)
                Picasso.get().load(uri).into(prfileImage);
//                progressBar.setVisibility(View.GONE);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(getActivity(), "No DP!", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
            }

        });
        if (uri!=null)
            Picasso.get().load(uri).into(prfileImage);

        mydb= new DatabaseHelper(getContext());
        String pecentage = mydb.calculateTotal();
        if (!pecentage.equals("NaN")){
            totalAttendance.setText("Aggregate\nAttendance: "+pecentage+"%");
//            circleprog.setProgress((int)Float.parseFloat(pecentage));
        }
        else {
            totalAttendance.setText("Aggregate\nAttendance: 0.00%");
//            circleprog.setProgress(0);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select an image"),GET_FROM_GALLERY);
            }
        });

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameField.setEnabled(true);
                enrollNo.setEnabled(true);
                branch.setEnabled(true);
                nameField.setTextColor(Color.parseColor("#000000"));
                enrollNo.setTextColor(Color.parseColor("#000000"));
                branch.setTextColor(Color.parseColor("#000000"));
                imageButton.setEnabled(true);
                imageButton.setVisibility(View.VISIBLE);
                editDetails.setEnabled(false);
                editDetails.setVisibility(View.GONE);
                submitDetails.setEnabled(true);
                submitDetails.setVisibility(View.VISIBLE);
                //                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//                if (editDetails.getIm)
//                if(editDetails.getAlpha() == 0.9){
//                    nameField.setEnabled(true);
//                    enrollNo.setEnabled(true);
//                    branch.setEnabled(true);
//                    editDetails.setAlpha((float) 1.0);
//                    editDetails.setBackgroundColor(Color.parseColor("#ff99cc00"));
//                }
//                else if(editDetails.getAlpha() == 1.0){
//                    String name = nameField.getText().toString();
//                    String enroll = enrollNo.getText().toString();
//                    String clg = branch.getText().toString();
//                    User.addUser(enroll,firebaseAuth.getCurrentUser().getEmail(),name,null,clg);
//                    nameField.setEnabled(false);
//                    enrollNo.setEnabled(false);
//                    branch.setEnabled(false);
//                    editDetails.setAlpha((float) 0.9);
//                    datachange();
//                }
            }
        });

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String enroll = enrollNo.getText().toString();
                String clg = branch.getText().toString();
                User.addUser(enroll,firebaseAuth.getCurrentUser().getEmail(),name,null,clg);
                nameField.setEnabled(false);
                imageButton.setEnabled(false);
                imageButton.setVisibility(View.GONE);
                enrollNo.setEnabled(false);
                branch.setEnabled(false);
                nameField.setTextColor(Color.parseColor("#138FF7"));
                enrollNo.setTextColor(Color.parseColor("#138FF7"));
                branch.setTextColor(Color.parseColor("#138FF7"));
                submitDetails.setEnabled(false);
                submitDetails.setVisibility(View.GONE);
                editDetails.setEnabled(true);
                editDetails.setVisibility(View.VISIBLE);
            }
        });

        return view;

    }

    private void datachange() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Object> map= (Map<String,Object>)dataSnapshot.getValue();
                String name = (String) map.get("Name");
                String rollNo = (String) map.get("Username");
                String college = (String) map.get("Clgname");
                nameField.setText(name);
                enrollNo.setText(rollNo);
                branch.setText(college);
                try {
                    int space = name.indexOf(" ");
                    int color = navigation.generatecolor();
                    drawable = TextDrawable.builder().beginConfig()
                            .width(150)
                            .height(150)
                            .bold()
                            .endConfig()
                            .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
                    prfileImage.setImageDrawable(drawable);
                }
                catch (Exception e){

                }
                if (uri!=null)
                    Picasso.get().load(uri).into(prfileImage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                prfileImage.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath!=null){
//            progressBar.setVisibility(View.VISIBLE);
            StorageReference unique = storageRef.child("User/");
            final StorageReference timeTableref = unique.child( SaveSharedPreference.getUserName(getContext())+"/DP.jpeg");
            timeTableref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBar.setVisibility(View.GONE);

//                    Toast.makeText(getActivity(), "DP updated!", Toast.LENGTH_SHORT).show();

//                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
////                        Toast.makeText(TimeTable.this, uri.toString(), Toast.LENGTH_SHORT).show();
//
//                        int dot = SaveSharedPreference.getUserName(getContext()).indexOf(".");
//                        databaseReference.child(SaveSharedPreference.getUserName(getActivity()).substring(0,dot)).child("TimeTable").setValue(uri.toString());
//                        }
//                    });
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
//        if (HomeFragment.this.uri!=null)
//        Picasso.get().load(HomeFragment.this.uri).into(prfileImage);
    }
}