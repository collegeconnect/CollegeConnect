package com.example.collegeconnect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> tags;

    public TagsAdapter(Context context, ArrayList<String> tags) {
        this.context = context;
        this.tags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tags,parent,false);
        return new TagsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tags.get(position);
//        Toast.makeText(context, tag, Toast.LENGTH_SHORT).show();
        if(tag.toLowerCase().equals("long"))
            holder.textView.setBackgroundColor(Color.RED);
        if(tag.toLowerCase().equals("easy to understand"))
            holder.textView.setBackgroundColor(Color.GREEN);
        if(tag.toLowerCase().equals("short"))
            holder.textView.setBackgroundColor(Color.YELLOW);
        if(tag.toLowerCase().equals("to the point"))
            holder.textView.setBackgroundColor(Color.CYAN);
        holder.textView.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tag);
        }
    }
}
