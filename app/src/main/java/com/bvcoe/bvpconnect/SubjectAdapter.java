package com.bvcoe.bvpconnect;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bvcoe.bvpconnect.ui.attendance.AttendanceFragment;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<String> subjects;
    private Context context;
    private DatabaseHelper dB;

    public SubjectAdapter(ArrayList<String> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
//        this.dB = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,parent,false);

        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String current = subjects.get(position);

        //Setting details of cards on loading
        holder.heading.setText(current);
        final int[] attended = {0};
        final int[] missed = {0};
        dB = new DatabaseHelper(context);
        final Cursor res = dB.getClasses(current);

        if (res.moveToFirst())
        {
            attended[0] = Integer.parseInt(res.getString(0));
            missed[0] = Integer.parseInt(res.getString(1));
        }
        holder.ratio.setText(Integer.toString(attended[0])+"/"+Integer.toString(missed[0]+attended[0]));

        String percentage = String.format("%.2f",(float)attended[0]/(attended[0]+missed[0])*100);
        holder.percecntage.setText(percentage+"%");

        //Button functionality
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attended[0]++;
                dB.updateData(Integer.toString(position+1),current,Integer.toString(attended[0]),Integer.toString(missed[0]));
//                Toast.makeText(context, res.getString(0)+" "+res.getString(1) , Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                holder.ratio.setText(Integer.toString(attended[0])+"/"+Integer.toString(missed[0]+attended[0]));
                String percentage = String.format("%.2f",(float)attended[0]/(attended[0]+missed[0])*100);
                holder.percecntage.setText(percentage+"%");
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missed[0]++;
                dB.updateData(Integer.toString(position+1),current,Integer.toString(attended[0]),Integer.toString(missed[0]));
//                Toast.makeText(context, res.getString(0)+" "+res.getString(1) , Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                holder.ratio.setText(Integer.toString(attended[0])+"/"+Integer.toString(missed[0]+attended[0]));
                String percentage = String.format("%.2f",(float)attended[0]/(attended[0]+missed[0])*100);
                holder.percecntage.setText(percentage+"%");
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dB.deleteData(subjects.get(position));
                subjects.remove(position);
                AttendanceFragment.notifyChange();
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton increase, decrease, delete;
        public TextView ratio, percecntage, heading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            delete = itemView.findViewById(R.id.deleteSubject);
            ratio = itemView.findViewById(R.id.qtyTextview);
            percecntage = itemView.findViewById(R.id.percentage);
            heading = itemView.findViewById(R.id.subjectHeading);
        }
    }
}