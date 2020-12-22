package com.college.collegeconnect.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.BuildConfig;
import com.college.collegeconnect.activities.PdfViewerActivity;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class UploadlistAdapter extends RecyclerView.Adapter<UploadlistAdapter.ViewHolder> implements Filterable {
    private Context context;
    private com.google.firebase.database.DatabaseReference DatabaseReference;
    private ArrayList<Upload> noteslist;
    private ArrayList<Upload> noteslistfull;

    public UploadlistAdapter(Context context, ArrayList<Upload> noteslist) {
        this.context = context;
        this.noteslist = noteslist;
        noteslistfull = new ArrayList<>(noteslist);
    }

    @NonNull
    @Override
    public UploadlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes, parent, false);
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
        return new UploadlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UploadlistAdapter.ViewHolder holder, int position) {

        //Set Values in card
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads: " + String.valueOf(notes.getDownload()));

        ArrayList<String> selectedTags = new ArrayList<>();
        if (notes.getTags() != null)
            selectedTags = (ArrayList<String>) notes.getTags().clone();

        //Tags Recycler View
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        final TagsAdapter recyclerAdapter = new TagsAdapter(context, selectedTags);
        holder.recyclerView.setAdapter(recyclerAdapter);

        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files/Notes/Upload Notes" + File.separator + notes.getName() + ".pdf");
                if (file.exists()) {
                    openfile(file.getAbsolutePath());
                    Log.d("upload", "onClick: already exists");
                } else {
                    downloadfile(notes.getUrl(), notes.getName());
                    Log.d("upload", "onClick: download");
                }
            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.notes_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.deletenotes:
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                                // Setting Dialog Title
                                builder.setTitle("Delete Notes");
                                // Setting Dialog Message
                                builder.setMessage("Are you sure you want to delete this file?\nDeleted notes cannot be recovered");

                                builder.setPositiveButton("Delete",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog
                                                new File("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files/Notes/Upload Notes" + File.separator + notes.getName() + ".pdf").delete();

                                                StorageReference delete = FirebaseStorage.getInstance().getReferenceFromUrl(notes.getUrl());
                                                delete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(context, "Notes deleted successfully", Toast.LENGTH_SHORT).show();
//                                                        noteslist.remove(position);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Unable to delete file\nContact the Devs", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                DatabaseReference mDatabaserefernce = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(String.valueOf(notes.getTimestamp()));
                                                mDatabaserefernce.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("UploadlistAdapter", "onComplete: database entry removed");
                                                    }
                                                });
                                                notifyDataSetChanged();

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
                                alertDialog.show();
                                break;
                            case R.id.details:
                                MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(context);
                                // Setting Dialog Title
                                builder2.setTitle("Details");
                                // Setting Dialog Message
                                builder2.setMessage("\nCourse: " + notes.getCourse() + "\nBranch: " + notes.getBranch() +
                                        "\nSemester: " + notes.getSemester() + "\nUnit: " + notes.getUnit());

                                builder2.setPositiveButton("Dismiss",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog
                                            }
                                        });

                                AlertDialog alertDialog2 = builder2.create();
                                alertDialog2.show();
                                break;
                        }
                        return true;
                    }
                });
                popup.getMenu().findItem(R.id.tagover).setVisible(false);
                popup.getMenu().findItem(R.id.report).setVisible(false);
                popup.show();
            }
        });

    }

    public void downloadfile(String url, String name) {
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/pdf");
        request.setDestinationInExternalFilesDir(context, "Notes/Upload Notes", name + ".pdf");
        request.setVisibleInDownloadsUi(false);
        request.allowScanningByMediaScanner();
        final long id = downloadManager.enqueue(request);
        Toast.makeText(context, "Downloading..... Please Wait!", Toast.LENGTH_LONG).show();
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                if (c != null) {
                    c.moveToFirst();
                    try {
                        String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        File mFile = new File(Uri.parse(fileUri).getPath());
                        String fileName = mFile.getAbsolutePath();
                        openfile(fileName);
                    } catch (Exception e) {
                        Log.e("error", "Could not open the downloaded file");
                    }
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void openfile(String path) {
//        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(path));
//        Log.d("Upload", "openfile:uri being sent in intent "+uri+"\n Actual path: "+uri);
//        context.getApplicationContext().grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent(context, PdfViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("file",path);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d("Upload", "openfile: " + path);
//        intent.setDataAndType(uri, "application/pdf");
        ((Activity) context).startActivityForResult(intent,95);
    }


    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, noOfDown;
        ImageButton report;
        RelativeLayout itv;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authorname);
            report = itemView.findViewById(R.id.reportButton);
            noOfDown = itemView.findViewById(R.id.download);
            itv = itemView.findViewById(R.id.relate);
            recyclerView = itemView.findViewById(R.id.tagsRecycler);
        }
    }

    @Override
    public Filter getFilter() {
        return notesfilter;
    }

    private Filter notesfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Upload> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(noteslistfull);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Upload item : noteslistfull) {
                    if (item.getName().toLowerCase().contains(filterpattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteslist.clear();
            noteslist.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
