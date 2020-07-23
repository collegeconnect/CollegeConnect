package com.connect.collegeconnect.adapters;

import android.content.Context;
import android.database.Cursor;
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

import com.connect.collegeconnect.DatabaseHelper;
import com.connect.collegeconnect.R;
import com.connect.collegeconnect.datamodels.SaveSharedPreference;
import com.connect.collegeconnect.ui.attendance.AttendanceFragment;
import com.github.lzyzsd.circleprogress.ArcProgress;
import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<String> subjects;
    private Context context;
    private DatabaseHelper dB;
    public int per;
    int criteria;

    public SubjectAdapter(ArrayList<String> subjects, Context context) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        criteria = SaveSharedPreference.getAttendanceCriteria(context);
        final String current = subjects.get(position);

        holder.circleProgress.setMax(100);
        holder.circleProgress.setProgress(25);

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

        String percentage = String.format("%.0f",(float)attended[0]/(attended[0]+missed[0])*100);
        if(percentage.equals("NaN"))
            per=0;
        else
            per = (int) Float.parseFloat(percentage);

        holder.circleProgress.setProgress(per);
        if(per <= criteria+2 && !percentage.equals("NaN"))
            holder.tv_bunk.setText("You can\'t miss any more classes.");
        else
            holder.tv_bunk.setText("You can miss the next class.");

        //Button functionality
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attended[0]++;
                dB.updateData(Integer.toString(position+1),current,Integer.toString(attended[0]),Integer.toString(missed[0]));

                holder.ratio.setText(attended[0] +"/"+ (missed[0] + attended[0]));
                String percentage = String.format("%.0f",(float)attended[0]/(attended[0]+missed[0])*100);
                per = (int) Float.parseFloat(percentage);

                if(per <= criteria+2)
                    holder.tv_bunk.setText("You can\'t miss any more classes.");
                else
                    holder.tv_bunk.setText("You can miss the next class.");
                holder.circleProgress.setProgress(per);

            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missed[0]++;
                dB.updateData(Integer.toString(position+1),current,Integer.toString(attended[0]),Integer.toString(missed[0]));
                holder.ratio.setText(attended[0] +"/"+ (missed[0] + attended[0]));

                String percentage = String.format("%.0f",(float)attended[0]/(attended[0]+missed[0])*100);
                per = (int) Float.parseFloat(percentage);

                if(per <= criteria+2)
                    holder.tv_bunk.setText("You can\'t miss any more classes.");
                else
                    holder.tv_bunk.setText("You can miss the next class.");
                holder.circleProgress.setProgress(per);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context,view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
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
