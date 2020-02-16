package com.bvcoe.bvpconnect;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bvcoe.bvpconnect.ui.attendance.AttendanceFragment;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private ArrayList<String> subjects;
    private Context context;
    private DatabaseHelper dB;
    public float percent;
    public int per;

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
//        holder.progressBar.setProgress(0);
//        holder.progressBar.setSecondaryProgress(100);
//        holder.progressBar.setMax(100);
//        holder.progressBar.setProgressDrawable(ContextCompat.getDrawable(context,R.drawable.circular));
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
        if(per <= 77)
            holder.tv_bunk.setText("You CAN\'T BUNK any more classes");
        else if(per > 78)
            holder.tv_bunk.setText("You CAN BUNK Classes");


//        if(percentage.equals("NaN")) {
//            holder.percecntage.setText("0%");
//        }
//         else
//            holder.percecntage.setText(percentage+"%");


        //Button functionality
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attended[0]++;
                dB.updateData(Integer.toString(position+1),current,Integer.toString(attended[0]),Integer.toString(missed[0]));
//                Toast.makeText(context, res.getString(0)+" "+res.getString(1) , Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "position: " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                holder.ratio.setText(Integer.toString(attended[0])+"/"+Integer.toString(missed[0]+attended[0]));
                String percentage = String.format("%.0f",(float)attended[0]/(attended[0]+missed[0])*100);
//                holder.percecntage.setText(percentage+"%");
                per = (int) Float.parseFloat(percentage);

//                String s = holder.percecntage.getText().toString();
//                percent = Float.parseFloat(s.substring(0,s.length()-1));
                if(per <= 77)
                    holder.tv_bunk.setText("You CAN\'T BUNK any more classes");
                else if(per > 78)
                    holder.tv_bunk.setText("You CAN BUNK Classes");
                holder.circleProgress.setProgress(per);
//                holder.progressBar.setProgress((int)Float.parseFloat(percentage));

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
//                per = (attended[0]/(attended[0]+missed[0])*100);

                String percentage = String.format("%.0f",(float)attended[0]/(attended[0]+missed[0])*100);
                per = (int) Float.parseFloat(percentage);

//                holder.percecntage.setText(percentage+"%");
//                String s = holder.percecntage.getText().toString();
//                percent = Float.parseFloat(s.substring(0,s.length()-1));
                if(per <= 77)
                    holder.tv_bunk.setText("You CAN\'T BUNK any more classes");
                else if(per > 78)
                    holder.tv_bunk.setText("You CAN BUNK Classes");
//                holder.progressBar.setProgress((int)Float.parseFloat(percentage));
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
//
//     dB.deleteData(subjects.get(position));
//                subjects.remove(position);
//                AttendanceFragment.notifyChange();

    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton increase, decrease, delete;
        public TextView ratio, percecntage, heading, tv_bunk;
        public ProgressBar progressBar;
        public ArcProgress circleProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            increase = itemView.findViewById(R.id.increase);
            decrease = itemView.findViewById(R.id.decrease);
            delete = itemView.findViewById(R.id.pop);
            ratio = itemView.findViewById(R.id.qtyTextview);
//            percecntage = itemView.findViewById(R.id.percentage);
            heading = itemView.findViewById(R.id.subjectHeading);
            tv_bunk = itemView.findViewById(R.id.tv_bunk);
//            progressBar = itemView.findViewById(R.id.circularProgressbar);
            circleProgress = itemView.findViewById(R.id.arc_progress);
        }
    }
}
