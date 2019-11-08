package com.example.attendancemonitoring;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.attendancemonitoring.Adapters.ActivityAdapter;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;

import java.util.ArrayList;
import java.util.List;



public class ActivityFragment extends Fragment  {

    private RecyclerView recyclerView;
    private ActivityAdapter attendanceAdapater;
    private LinearLayoutManager layoutManager;
    private List<Attendance> attendanceList = new ArrayList<>();


    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Attendance attendance = new Attendance();
        attendance.setName("Sample");
        attendance.setDescription("Sample for description");
        attendance.setStart("01/06/1997");
        attendance.setEnd("01/06/1997");

        attendanceList.add(0, attendance);

//        this.buildRecyclerView(view);
    }

    private void buildRecyclerView(View view) {

        attendanceAdapater = new ActivityAdapter(attendanceList);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(),
                        LinearLayoutManager.VERTICAL)
        );


        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(attendanceAdapater);

    }



}
