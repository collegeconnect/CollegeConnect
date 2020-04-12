package com.example.collegeconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NotesDialog extends AppCompatDialogFragment {

    private EditText fileName, author;
    private uploadFileListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog_notes,null);

        builder.setView(view)
                .setTitle("Set File Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String file = fileName.getText().toString();
                        String authorName = author.getText().toString();
                        listener.applyTexts(file,authorName);
                    }
                });

        fileName = view.findViewById(R.id.fileName);
        author = view.findViewById(R.id.authorName);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (uploadFileListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+"must implement listener");
        }

    }

    public interface uploadFileListener{
        void applyTexts(String filename, String authorname);
    }
}
