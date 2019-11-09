package com.example.attendancemonitoring.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.AttendanceHolder> {

    private List<Attendance> list_attendance;


    public ActivityAdapter(List<Attendance> attendance_list) {
        this.list_attendance = attendance_list;
    }

    @Override
    public AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(AttendanceHolder holder, int position) {
        Attendance attendance = list_attendance.get(position);
        holder.name.setText(String.format("%s\n%s (%s - %s)", attendance.getName(), attendance.getDescription(), attendance.getStart(), attendance.getEnd()));
    }

    @Override
    public int getItemCount() {
        return list_attendance.size();
    }



    class AttendanceHolder extends RecyclerView.ViewHolder {
        TextView name;

        public AttendanceHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}