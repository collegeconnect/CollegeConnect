package com.example.collegeconnect.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.collegeconnect.DatabaseHelper;
import com.example.collegeconnect.DownloadNotes;
import com.example.collegeconnect.MainActivity;
import com.example.collegeconnect.R;
import com.example.collegeconnect.SaveSharedPreference;
import com.example.collegeconnect.navigation;
import com.example.collegeconnect.ui.home.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsMenu extends Fragment {

    private DatabaseHelper db;
    GoogleSignInClient mgoogleSignInClient;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_menu, container, false);

        ListView listView = view.findViewById(R.id.settings_options);
        db = new DatabaseHelper(getActivity());


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.settings_frag_container,new HomeFragment())
//                            .commit();
//                    Intent intent = new Intent(getActivity(), navigation.class);
//                    intent.putExtra(navigation.SETTINGS,"settings");
//                    startActivity(intent);
//                    getActivity().finish();
                }
            }
        };
        listView.setOnItemClickListener(itemClickListener);

        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutDialog();
            }
        });

        return view;
    }

    public void logOutDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        // Setting Dialog Title
        builder.setTitle("Confirm SignOut");
        // Setting Dialog Message
        builder.setMessage("Are you sure you want to Signout?\nAll your saved data wil be lost!");

        builder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        FirebaseAuth.getInstance().signOut();
                        signOut();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        db.deleteall();
                        SaveSharedPreference.clearUserName(getActivity());

                        startActivity(i);
                        getActivity().finish();
                    }
                });

        // Setting Negative "NO" Btn
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        builder.show();
    }

    private void signOut() {
        mgoogleSignInClient.signOut();
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(getActivity(),gso);
        super.onStart();
    }
}
