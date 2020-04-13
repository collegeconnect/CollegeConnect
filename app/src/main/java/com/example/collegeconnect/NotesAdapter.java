package com.example.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeconnect.ui.attendance.AttendanceFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Upload> noteslist;
    private ArrayList<Upload> noteslistfull;

    public NotesAdapter(Context context, ArrayList<Upload> noteslist) {
        this.context = context;
        this.noteslist = noteslist;
        noteslistfull = new ArrayList<>(noteslist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes,parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads = " + String.valueOf(notes.getDownload()));
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int downloads = notes.getDownload() + 1;

                Upload upload = new Upload(notes.getName(),
                        notes.getCourse(),
                        notes.getSemester(),
                        notes.getBranch(),
                        notes.getUnit(),
                        notes.getAuthor(), downloads, notes.getUrl(), notes.getTimestamp());
                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
                mDatabaseReference.child(notes.getTimestamp()+"").setValue(upload);
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
                inflater.inflate(R.menu.report, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.report:
//                                Toast.makeText(context, "Report Notes", Toast.LENGTH_SHORT).show();
                                ReportsDialog reportsDialog = new ReportsDialog(notes.getTimestamp());
                                reportsDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"Report Dialog");
                        }
                        return true;
                    }
                });
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authorname);
            report = itemView.findViewById(R.id.reportButton);
            noOfDown = itemView.findViewById(R.id.download);
            itv = itemView.findViewById(R.id.relate);
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
                    if(item.getName().toLowerCase().contains(filterpattern) || item.getAuthor().toLowerCase().contains(filterpattern)){
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
