package com.example.collegeconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.collegeconnect.ui.AboutFragment;
import com.example.collegeconnect.ui.ContactFragment;
import com.example.collegeconnect.ui.UploadListFragment;
import com.example.collegeconnect.ui.home.Home1Fragment;
import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private ArrayList<String> options;
    private Context context;
    List frag_list;
    Fragment homegfrag = new Home1Fragment();
    Fragment uploadlistfrag = new UploadListFragment();
    Fragment contactfrag = new ContactFragment();
    Fragment aboutfrag = new AboutFragment();
    int images[] ={R.drawable.ic_profile ,R.drawable.ic_uploadlist, R.drawable.ic_contactus, R.drawable.ic_about};

    public SettingsAdapter(ArrayList<String> options, Context context) {
        this.options = options;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_card,parent,false);
        return new SettingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(options.get(position));
        holder.imageView.setBackgroundResource(images[position]);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppCompatActivity activity = (AppCompatActivity)context;
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.settings_frag_container, (Fragment) frag_list.get(position))
                                .addToBackStack(null)
                                .commit();
                    }
                },165);

            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        View relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.option);
            imageView = itemView.findViewById(R.id.setting_icon);
            relativeLayout = itemView;
            frag_list = new ArrayList<Fragment>();
            frag_list.add(homegfrag);
            frag_list.add(uploadlistfrag);
            frag_list.add(contactfrag);
            frag_list.add(aboutfrag);
        }
    }
}
