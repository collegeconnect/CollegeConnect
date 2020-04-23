package com.example.collegeconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private ArrayList<String> options;
    String selection;
    private Context context;
    List act_list;
    int checked_item = 0;
    Class homeedit = HomeEditActivity.class;
    Class myuploads = MyUploadsActivity.class;
    Class contactus = ContactActivity.class;
    Class about = AboutActivity.class;
    int images[] ={R.drawable.ic_profile ,R.drawable.ic_brightness_24dp ,R.drawable.ic_uploadlist, R.drawable.ic_contactus, R.drawable.ic_about};

    public SettingsAdapter(ArrayList<String> options, Context context) {
        this.options = options;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_card,parent,false);
        return new SettingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        checked_item = 0;
        holder.textView.setText(options.get(position));
        holder.imageView.setBackgroundResource(images[position]);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(position==1)
                            dialog();
                        else
                        context.startActivity(new Intent(context,(Class) act_list.get(position)));
                    }
                },165);

            }
        });
    }

    private void dialog() {
        final String theme[] = context.getResources().getStringArray(R.array.themes);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle("Select Theme");
        builder.setSingleChoiceItems(R.array.themes, SaveSharedPreference.getCheckedItem(context), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection = theme[which];
                checked_item = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveSharedPreference.setCheckedItem(context,checked_item);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        View relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.option);
            imageView = itemView.findViewById(R.id.setting_icon);
            relativeLayout = itemView;
            act_list = new ArrayList<>();
            act_list.add(homeedit);
            act_list.add(1);
            act_list.add(myuploads);
            act_list.add(contactus);
            act_list.add(about);
        }
    }
}
