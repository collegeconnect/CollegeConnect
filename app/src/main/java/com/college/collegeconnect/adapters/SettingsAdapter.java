package com.college.collegeconnect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.college.collegeconnect.R;
import com.college.collegeconnect.settingsActivity.MyFilesActivity;
import com.college.collegeconnect.settingsActivity.SettingsActivity;
import com.college.collegeconnect.settingsActivity.WorkProfile;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.settingsActivity.AboutActivity;
import com.college.collegeconnect.settingsActivity.ContactActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private ArrayList<String> options;
    String selection;
    private final Context context;
    List act_list;
    int checked_item = 0;
    TextInputLayout placeholder;
    Button set;
    Class myfiles = MyFilesActivity.class;
    Class workprofile = WorkProfile.class;
    Class contactus = ContactActivity.class;
    Class about = AboutActivity.class;
    int[] images = {R.drawable.ic_addchart_24px, R.drawable.ic_uploadlist, R.drawable.ic_work_24px, R.drawable.ic_baseline_star_rate, R.drawable.ic_contactus, R.drawable.ic_about, R.drawable.ic_export};

    public SettingsAdapter(ArrayList<String> options, Context context) {
        this.options = options;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_card, parent, false);
        return new SettingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        checked_item = 0;
        holder.textView.setText(options.get(position));
        holder.imageView.setBackgroundResource(images[position]);
        holder.relativeLayout.setOnClickListener(v -> v.postDelayed(() -> {
            if (position == 0)
                dialogAttend();
            else if (position==3){
                ReviewManager manager;
                manager = ReviewManagerFactory.create(context);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // We can get the ReviewInfo object
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow((Activity)context , reviewInfo);
                        flow.addOnCompleteListener(task1 -> {
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.
                        });
                    }
                });
            }
            else if(position==6){
                exportData();
            }
            else
                context.startActivity(new Intent(context, (Class) act_list.get(position)));
        }, 165));
    }

    private void exportData() {
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(context, "AttendanceDatabase", "/storage/emulated/0/Download/");
        sqliteToExcel.exportSingleTable("SubjectDetails", "attendance.xls" , new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                Toast.makeText(context, "Processing!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCompleted(String filePath) {
                Uri uri = FileProvider.getUriForFile(context,  context.getPackageName() + ".provider", new File(filePath));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setDataAndType(uri, "application/vnd.ms-excel");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                Log.i("TAG", "onCompleted: ${uri.toString()}");
                context.startActivity(Intent.createChooser(intent, "Select"));
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogAttend() {
        final AlertDialog.Builder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_atttendance_criteria, null);
        builder.setView(view);
        set = view.findViewById(R.id.set_dialog_button);
        placeholder = view.findViewById(R.id.placeholder);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(placeholder.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            placeholder.requestFocus();
        } catch (Exception ignored) {
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        set.setOnClickListener(v -> {
            if (!placeholder.getEditText().getText().toString().equals(""))
                SaveSharedPreference.setAttendanceCriteria(context, Integer.parseInt(Objects.requireNonNull(placeholder.getEditText()).getText().toString()));
            dialog.dismiss();
        });
    }

    private void dialog() {
        final String theme[] = context.getResources().getStringArray(R.array.themes);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle("Select Theme");
        builder.setSingleChoiceItems(R.array.themes, SaveSharedPreference.getCheckedItem(context), (dialog, which) -> {
            selection = theme[which];
            checked_item = which;
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selection == null) {
                checked_item = SaveSharedPreference.getCheckedItem(context);
                selection = theme[checked_item];

            }
            switch (selection) {
                case "System Default":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case "Dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case "Light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
            SaveSharedPreference.setCheckedItem(context, checked_item);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        View relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.option);
            imageView = itemView.findViewById(R.id.setting_icon);
            relativeLayout = itemView;
            act_list = new ArrayList<>();
            act_list.add(1);
            act_list.add(myfiles);
            act_list.add(workprofile);
            act_list.add(4);
            act_list.add(contactus);
            act_list.add(about);
            act_list.add(7);
        }
    }
}
