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
import com.example.collegeconnect.ui.UploadListFragment;
import com.example.collegeconnect.ui.home.Home1Fragment;
import java.util.ArrayList;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>{

    private ArrayList<String> options;
    private Context context;
    List frag_list;

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
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity)context;
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.settings_frag_container, (Fragment) frag_list.get(position))
                            .addToBackStack(null)
                            .commit();

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
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.option);
            imageView = itemView.findViewById(R.id.setting_icon);
            relativeLayout = itemView.findViewById(R.id.relate_settings);
            frag_list = new ArrayList<Fragment>();
            frag_list.add(new Home1Fragment());
            frag_list.add(new UploadListFragment());
            frag_list.add(new AboutFragment());
        }
    }
}
