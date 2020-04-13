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

public class ReportsDialog extends AppCompatDialogFragment {

    private EditText answer;
    private long timestamp;
    private reportNotesListener listener;

    public ReportsDialog(long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog_report,null);

        builder.setView(view)
                .setTitle("State Your Concern")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = answer.getText().toString();
                        listener.submitReport(text,timestamp);
                    }
                });

        answer = view.findViewById(R.id.answer);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (reportNotesListener)context;
        } catch (ClassCastException e) {
            throw  new  ClassCastException(context.toString()+"must implement listener");
        }
    }

    public interface reportNotesListener {
        void submitReport(String text, long timeStamp);
    }
}
