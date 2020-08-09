package com.college.collegeconnect.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.datamodels.DatabaseHelper;
import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.ui.attendance.AttendanceFragment;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<String> subjects;
    private Context context;
    private DatabaseHelper dB;
    public int per;
    float criteria, predict;
    private static final String TAG = "SubjectAdapter";

    public SubjectAdapter(ArrayList<String> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        criteria = (float) SaveSharedPreference.getAttendanceCriteria(context);
        final String current = subjects.get(position);

        holder.circleProgress.setMax(100);
        holder.circleProgress.setProgress(25);

        //Setting details of cards on loading
        holder.heading.setText(current);
        final int[] attended = {0};
        final int[] missed = {0};
        dB = new DatabaseHelper(context);
        final Cursor res = dB.getClasses(current);

        if (res.moveToFirst()) {
            attended[0] = Integer.parseInt(res.getString(0));
            missed[0] = Integer.parseInt(res.getString(1));
        }
        holder.ratio.setText(attended[0] + "/" + (missed[0] + attended[0]));

        String percentage = String.format("%.0f", (float) attended[0] / (attended[0] + missed[0]) * 100);
        if (percentage.equals("NaN"))
            per = 0;
        else
            per = (int) Float.parseFloat(percentage);

        // (attended / (attended + miss + 1)*100)
        // (missed+1) / (attended + miss + 1)*100)
        holder.circleProgress.setProgress(per);
        predict = ((float) (attended[0]) / (attended[0] + missed[0] + 1) * 100);
        final String[] i = {null};

        if (predict <= criteria && !percentage.equals("NaN")) {
            holder.tv_bunk.setText("You can\'t miss any more lectures");
        } else {
            if (percentage.equals("NaN"))
                holder.tv_bunk.setText("No classes have happened yet");
            else {
                i[0] = "1";
                if (((float) (attended[0]) / (attended[0] + missed[0] + 2) * 100) >= criteria)
                    i[0] = "2";
                else if (((float) (attended[0]) / (attended[0] + missed[0] + 3) * 100) >= criteria)
                    i[0] = "3";
                else if (((float) (attended[0]) / (attended[0] + missed[0] + 4) * 100) >= criteria)
                    i[0] = "4";
                else if (i[0].equals("4"))
                    holder.tv_bunk.setText("You can miss more than 3 lectures");
                else
                    holder.tv_bunk.setText("You can miss " + i[0] + " lecture(s)");
            }

        }
        holder.circleProgress.setProgress(per);


        //Button functionality
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attended[0]++;
                dB.updateData(Integer.toString(position + 1), current, Integer.toString(attended[0]), Integer.toString(missed[0]));

                holder.ratio.setText(attended[0] + "/" + (missed[0] + attended[0]));
                String percentage = String.format("%.0f", (float) attended[0] / (attended[0] + missed[0]) * 100);
                per = (int) Float.parseFloat(percentage);
                predict = ((float) (attended[0]) / (attended[0] + missed[0] + 1) * 100);
//                predict = missed[0] + 1;
                Log.d(TAG, "onClick: increase " + predict);
                if (predict <= criteria && !percentage.equals("NaN")) {
                    holder.tv_bunk.setText("You can\'t miss any more lectures");
                } else {
                    i[0] = "1";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 2) * 100) >= criteria)
                        i[0] = "2";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 3) * 100) >= criteria)
                        i[0] = "3";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 4) * 100) >= criteria)
                        i[0] = "4";
                    if (i[0].equals("4"))
                        holder.tv_bunk.setText("You can miss more than 3 lectures");
                    else
                        holder.tv_bunk.setText("You can miss " + i[0] + " lecture(s)");
                }
                holder.circleProgress.setProgress(per);

            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missed[0]++;
                dB.updateData(Integer.toString(position + 1), current, Integer.toString(attended[0]), Integer.toString(missed[0]));
                holder.ratio.setText(attended[0] + "/" + (missed[0] + attended[0]));

                String percentage = String.format("%.0f", (float) attended[0] / (attended[0] + missed[0]) * 100);
                per = (int) Float.parseFloat(percentage);
                predict = ((float) (attended[0]) / (attended[0] + missed[0] + 1) * 100);
                Log.d(TAG, "onClick: increase " + predict);
                if (predict <= criteria && !percentage.equals("NaN")) {
                    holder.tv_bunk.setText("You can\'t miss any more lectures");
                } else {
                    i[0] = "1";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 2) * 100) >= criteria)
                        i[0] = "2";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 3) * 100) >= criteria)
                        i[0] = "3";
                    if (((float) (attended[0]) / (attended[0] + missed[0] + 4) * 100) >= criteria)
                        i[0] = "4";
                    if (i.equals("4"))
                        holder.tv_bunk.setText("You can miss more than 3 lectures");
                    else
                        holder.tv_bunk.setText("You can miss " + i[0] + " lecture(s)");
                }
                holder.circleProgress.setProgress(per);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                dB.deleteData(subjects.get(position));
                                subjects.remove(position);
                                AttendanceFragment.notifyChange();
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
        return subjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton delete;
        public ImageView decrease, increase;
        public TextView ratio, heading, tv_bunk;
        public ArcProgress circleProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            delete = itemView.findViewById(R.id.pop);
            ratio = itemView.findViewById(R.id.qtyTextview);
            heading = itemView.findViewById(R.id.subjectHeading);
            tv_bunk = itemView.findViewById(R.id.tv_bunk);
            circleProgress = itemView.findViewById(R.id.arc_progress);
        }
    }
}
