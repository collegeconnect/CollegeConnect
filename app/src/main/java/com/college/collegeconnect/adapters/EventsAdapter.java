package com.college.collegeconnect.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.college.collegeconnect.ui.event.EventDetailsFragment;

import java.util.ArrayList;
import java.util.Date;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Events> eventsArrayList;

    public EventsAdapter(Context context, ArrayList<Events> eventsArrayList) {
        this.context = context;
        this.eventsArrayList = eventsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_events_card, parent, false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Events event = eventsArrayList.get(position);

        holder.textView.setText(event.getEventName());
        if (event.getOrganizer().toLowerCase().contains("dsc"))
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.dsc));
        else if (event.getOrganizer().toLowerCase().contains("ieee"))
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.bvpieee));
        else if (event.getOrganizer().toLowerCase().contains("csi"))
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.bvpcsi));

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date strDate = df.parse(event.getDate());
            //If date has passed
            if (new Date().after(strDate)) {

            }
//            else    //If event is yet to occur
//                holder.relativeLayout.setBackgroundColor(R.color.eventPassed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.organiser.setText("By " + event.getOrganizer());
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("Name", event.getEventName());

                final EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                eventDetailsFragment.setArguments(arguments);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((AppCompatActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameupcomingevents, eventDetailsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }, 150);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        RelativeLayout relativeLayout;
        TextView textView, organiser;
        View itv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView15);
            textView = itemView.findViewById(R.id.eventName);
            relativeLayout = itemView.findViewById(R.id.eventCardLayout);
            organiser = itemView.findViewById(R.id.organiser);
            itv = itemView;
        }
    }
}
