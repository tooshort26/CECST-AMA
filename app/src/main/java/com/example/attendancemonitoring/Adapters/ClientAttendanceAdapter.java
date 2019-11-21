package com.example.attendancemonitoring.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.R;

import java.util.List;

public class ClientAttendanceAdapter extends RecyclerView.Adapter<ClientAttendanceAdapter.ClientAttendanceHolder> {

    private List<Activity> list_attendance;


    public ClientAttendanceAdapter(List<Activity> attendance_list) {
        this.list_attendance = attendance_list;
    }

    @Override
    public ClientAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_attendance_layout, parent, false);
        return new ClientAttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientAttendanceHolder holder, int position) {
        Activity activity = list_attendance.get(position);
        holder.name.setText(String.format("%s", activity.getName()));
    }

    @Override
    public int getItemCount() {
        return list_attendance.size();
    }



    class ClientAttendanceHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ClientAttendanceHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}