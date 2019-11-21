package com.example.attendancemonitoring.Accounts.Employee;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Adapters.ServerViewActivityAdapter;
import com.example.attendancemonitoring.AttendanceActivity;
import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.Helpers.SharedPref;
import com.example.attendancemonitoring.Helpers.Strings;
import com.example.attendancemonitoring.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ViewActivityFragment extends Fragment {

    List<Attendance> attendanceList;
    ServerViewActivityAdapter serverViewActivityAdapter;


    public ViewActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.permissionForStorage();

        TextView activityName = view.findViewById(R.id.txtActivityName);

        int activityId = SharedPref.getSharedPreferenceInt(getContext(), "VIEW_ID", 0);
        if (activityId != 0) {
            Activity activity = DB.getInstance(getContext()).activityDao().find(activityId);
            activityName.setText(Strings.capitalize(activity.getName()));

            this.buildRecyclerView(view, activity.getId());


            view.findViewById(R.id.btnProcessServer).setOnClickListener(btn -> {
                Intent intent  = new Intent(getActivity(), AttendanceActivity.class);
                intent.putExtra("ACTIVITY_NAME", activity.getName());
                intent.putExtra("ACTIVITY_ID", String.valueOf(activity.getId()));
                startActivity(intent);
            });


            view.findViewById(R.id.btnExportCsv).setOnClickListener(btn -> {
                String csv1 = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/students_attendance.csv"); // Here csv file name is MyCsvFile.csv

                CSVWriter writer = null;
                try {
                    writer = new CSVWriter(new FileWriter(csv1));

                    List<String[]> data = new ArrayList<>();
                    data.add(new String[]{"ID Number", "Name"});

                    for(Attendance attendance : attendanceList) {
                        data.add(new String[]{attendance.getStudent_id(), attendance.getStudent_name()});
                    }

                    writer.writeAll(data); // data is adding to csv

                    writer.close();
                    Toast.makeText(getContext(), "Successfully create new csv.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Rebase the view ID
                SharedPref.setSharedPreferenceInt(getContext(),"VIEW_ID", 0);
            });

        } else {
            Toast.makeText(getContext(), "Please select another activity.", Toast.LENGTH_SHORT).show();
        }

    }

    private void permissionForStorage() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "You need to allow the storage permission this will need to write CSV file.", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    private void buildRecyclerView(View view, int id) {

        attendanceList = DB.getInstance(getContext()).attendanceDao().getAttendanceById(id);
        serverViewActivityAdapter = new ServerViewActivityAdapter(attendanceList, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.student_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),1));

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(serverViewActivityAdapter);

    }



}
