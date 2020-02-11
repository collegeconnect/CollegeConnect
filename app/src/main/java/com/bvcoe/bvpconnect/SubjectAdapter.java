package com.bvcoe.bvpconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bvcoe.bvpconnect.ui.attendance.AttendanceFragment;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<Subject> subjects;
    private Context context;

    public SubjectAdapter(ArrayList<Subject> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,parent,false);

        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject current = subjects.get(position);
        holder.heading.setText(current.getSubjectName());
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Increase", Toast.LENGTH_SHORT).show();
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Decrease", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton increase, decrease;
        public TextView attended, missed, percecntage, heading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            attended = itemView.findViewById(R.id.qtyTextview);
            missed = itemView.findViewById(R.id.textView8);
            percecntage = itemView.findViewById(R.id.textView7);
            heading = itemView.findViewById(R.id.subjectHeading);
        }
    }
}

