package com.example.collegeconnect.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeconnect.R;
import com.example.collegeconnect.datamodels.Events;
import com.example.collegeconnect.ui.EventsFragment;
import com.example.collegeconnect.ui.event.EventDetailsFragment;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Events> eventsArrayList;

    public EventsAdapter(Context context, ArrayList<Events> eventsArrayList) {
        this.context = context;
        this.eventsArrayList = eventsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_events_card,parent,false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Events event = eventsArrayList.get(position);
        holder.textView.setText(event.getEventName());
        holder.organiser.setText("By " + event.getOrganizer());
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString( "Name",event.getEventName());

                EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                eventDetailsFragment.setArguments(arguments);

                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameupcomingevents,eventDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,organiser;
        View itv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView15);
            textView = itemView.findViewById(R.id.eventName);
            organiser = itemView.findViewById(R.id.organiser);
            itv=itemView;
        }
    }
}
