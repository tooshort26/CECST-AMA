package com.example.attendancemonitoring.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.R;

import java.util.List;

public class ServerViewActivityAdapter extends RecyclerView.Adapter<ServerViewActivityAdapter.ServerViewActivityHolder>  {
    List<Attendance> list_students;
    private Context context;
    public ServerViewActivityAdapter(List<Attendance> studentList, Context context) {
        this.list_students = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServerViewActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_student_layout, parent, false);
        return new ServerViewActivityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServerViewActivityHolder holder, int position) {
        Attendance attendance = list_students.get(position);
        holder.name.setText(String.format("%s: %s", attendance.getStudent_id(), attendance.getStudent_name()));
    }

    @Override
    public int getItemCount() {
        return list_students.size();
    }

    public class ServerViewActivityHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ServerViewActivityHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.record);
        }
    }
}
