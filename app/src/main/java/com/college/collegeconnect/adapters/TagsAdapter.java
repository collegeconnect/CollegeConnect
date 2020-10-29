package com.college.collegeconnect.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tags, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tags.get(position);
//        Toast.makeText(context, tag, Toast.LENGTH_SHORT).show();
        if (tag.equalsIgnoreCase("long")) {
            holder.textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6A6A")));
            holder.textView.setBackgroundResource(R.drawable.button_design);
        }
        if (tag.toLowerCase().equals("easy to understand")) {
            holder.textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6FFF6F")));
            holder.textView.setBackgroundResource(R.drawable.button_design);
        }
        if (tag.toLowerCase().equals("short")) {
            holder.textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBFF61")));
            holder.textView.setBackgroundResource(R.drawable.button_design);
        }
        if (tag.toLowerCase().equals("to the point")) {
            holder.textView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6AFFEC")));
            holder.textView.setBackgroundResource(R.drawable.button_design);
        }
        holder.textView.setText(tag);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tag);
        }
    }
}
