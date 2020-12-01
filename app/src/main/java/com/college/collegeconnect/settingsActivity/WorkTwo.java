package com.college.collegeconnect.settingsActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.college.collegeconnect.BuildConfig;
import com.college.collegeconnect.R;
import com.college.collegeconnect.database.entity.DownloadEntity;
import com.college.collegeconnect.datamodels.Resume;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.viewmodels.DownloadNotesViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class WorkTwo extends Fragment {

    private ImageButton back;
    private EditText email, linkedIn, github, behance, medium;
    private ImageButton upload;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private ProgressBar progressBar;
    private String aboutMe, website, resumeLink;
    private ListenerRegistration listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        assert args != null;
        aboutMe = args.getString("aboutMe");
        website = args.getString("website");
        resumeLink = args.getString("resume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_two, container, false);
        back = view.findViewById(R.id.button5);
        email = view.findViewById(R.id.enterWorkEmail);
        linkedIn = view.findViewById(R.id.enterWorkLinkedIn);
        github = view.findViewById(R.id.enterWorkGitHub);
        behance = view.findViewById(R.id.enterWorkBehance);
        medium = view.findViewById(R.id.enterWorkMedium);
        upload = view.findViewById(R.id.button4);
        progressBar = view.findViewById(R.id.workProgressBar);
        Log.i("TAG", "onCreateView: " + aboutMe + " " + website + " " + resumeLink);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        documentReference = firebaseFirestore.collection("resume").document(userId);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        setValuesFirestore();

        email.setText(SaveSharedPreference.getUserName(getActivity()));

        final String email = SaveSharedPreference.getUserName(getActivity());

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                final String strlinkedIn = linkedIn.getText().toString();
                final String strGitHub = github.getText().toString();
                final String strBehance = behance.getText().toString();
                String strMedium = medium.getText().toString();
                final Resume resume = new Resume(SaveSharedPreference.getUser(getActivity()), aboutMe, website, resumeLink, email, strlinkedIn, strGitHub, strBehance, strMedium);
                Log.i("TAG2", "onCreateView: " + strBehance + " " + strGitHub + " " + strlinkedIn);
                firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = firebaseFirestore.collection("resume");
                collectionReference.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(resume).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("Resume", "onSuccess: Resume Uploaded");
                        Toast.makeText(getContext(), "Resume Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        getActivity().startActivity(new Intent(getContext(), SettingsActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Resume", "onFailure: Resume failed :" + e.getMessage());
                        Toast.makeText(getContext(), "An error occurred. Please try later!", Toast.LENGTH_SHORT).show();
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                //Page 1
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(400,600,1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                canvas.drawText("Resume",40,50,paint);
                pdfDocument.finishPage(page);

                //page 2
                PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(400,600,1).create();
                PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);
                Canvas canvas2 = page2.getCanvas();
                canvas2.drawText("Resume Page 2",40,50,paint);
                pdfDocument.finishPage(page2);

                DownloadNotesViewModel downloadNotesViewModel = new ViewModelProvider(getActivity()).get(DownloadNotesViewModel.class);
                File file = new File("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files/Notes/Download Notes" + File.separator + "Resume" + ".pdf");
                try {
                    DownloadEntity downloadEntity = new DownloadEntity("Resume",SaveSharedPreference.getUserName(getContext()),"");
                    downloadNotesViewModel.addDownload(downloadEntity);
                    pdfDocument.writeTo(new FileOutputStream(file));
                    Log.d("Pdf","Success");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Pdf",e.getMessage());
                }
                pdfDocument.close();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void setValuesFirestore() {

        listener = documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                try {
                    assert documentSnapshot != null;
                    String strLinkedIn = documentSnapshot.getString("linkedIn");
                    String strGitHub = documentSnapshot.getString("github");
                    String strBehance = documentSnapshot.getString("behance");
                    String strMedium = documentSnapshot.getString("medium");
                    linkedIn.setText(strLinkedIn);
                    github.setText(strGitHub);
                    behance.setText(strBehance);
                    medium.setText(strMedium);
                } catch (Exception e) {
                    Log.d("WorkTwo", "onEvent: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (listener != null)
            listener.remove();
        super.onDestroyView();
    }
}
