package com.example.collegeconnect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes,parent,false);
        return new UploadlistAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final UploadlistAdapter.ViewHolder holder, int position) {

        //Set Values in card
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads - " + String.valueOf(notes.getDownload()));

        ArrayList<String> selectedTags = new ArrayList<>();
        if (notes.getTags()!=null)
            selectedTags = (ArrayList<String>) notes.getTags().clone();

        //Tags Recycler View
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        TagsAdapter recyclerAdapter = new TagsAdapter(context, selectedTags);
        holder.recyclerView.setAdapter(recyclerAdapter);

        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(notes.getUrl()), "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, "Choose an Application:"));
            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context,v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.notes_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
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

                                                StorageReference delete = FirebaseStorage.getInstance().getReferenceFromUrl(notes.getUrl());
                                                delete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(context,"Notes deleted successfully",Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context,"Unable to delete file\nContact the Devs",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                DatabaseReference mDatabaserefernce = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS).child(String.valueOf(notes.getTimestamp()));
                                                mDatabaserefernce.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d("UploadlistAdapter", "onComplete: database entry removed");
                                                    }
                                                });

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


    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author,noOfDown;
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
            if(constraint == null || constraint.length()==0){
                filteredList.addAll(noteslistfull);
            }
            else
            {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Upload item: noteslistfull){
                    if(item.getName().toLowerCase().contains(filterpattern)){
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
