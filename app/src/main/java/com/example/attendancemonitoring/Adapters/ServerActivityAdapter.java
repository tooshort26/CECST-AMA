package com.example.attendancemonitoring.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Accounts.Employee.ViewActivityFragment;
import com.example.attendancemonitoring.DashboardActivity;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.Helpers.SharedPref;
import com.example.attendancemonitoring.Helpers.Strings;
import com.example.attendancemonitoring.R;

import java.util.ArrayList;
import java.util.List;

public class ServerActivityAdapter extends RecyclerView.Adapter<ServerActivityAdapter.ServerActivityHolder> {
    private List<Activity> list_activity;
    private Context context;

    public ServerActivityAdapter(List<Activity> activity_list, Context context) {
        this.list_activity = activity_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ServerActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_activity_layout, parent, false);
        return new ServerActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServerActivityHolder holder, int position) {
        Activity activity = list_activity.get(position);
        holder.name.setText(String.format("%s", Strings.capitalize(activity.getName())));

        holder.view.setOnClickListener(view -> {
            // Display the view fragment.
            ViewActivityFragment viewActivityFragment = new ViewActivityFragment();
            ((DashboardActivity) this.context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.flContent, viewActivityFragment)
                                    .commit();

            SharedPref.setSharedPreferenceInt(this.context, "VIEW_ID", activity.getId());

        });

        /*holder.edit.setOnClickListener(view -> {
            Toast.makeText(holder.view.getContext(), list_activity.get(position).getName() + " Edit", Toast.LENGTH_SHORT).show();
        });*/
    }

    @Override
    public int getItemCount() {
        return list_activity.size();
    }

    public void filterList(ArrayList<Activity> filteredList) {
        list_activity = filteredList;
        notifyDataSetChanged();
    }


    class ServerActivityHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton view;


        public ServerActivityHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            view = itemView.findViewById(R.id.view);
        }
    }


}
