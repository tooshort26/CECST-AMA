package com.example.attendancemonitoring.Accounts.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Adapters.ClientAttendanceAdapter;
import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.R;

import java.util.ArrayList;
import java.util.List;

public  class AttendedActivityFragment extends Fragment {

        private RecyclerView recyclerView;
        private ClientAttendanceAdapter attendanceAdapater;
        private LinearLayoutManager layoutManager;
        private List<Activity> activityList = new ArrayList<>();


        public AttendedActivityFragment() {
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
            String studentId = DB.getInstance(getContext()).userDao().getUser().getId_number();
            activityList = DB.getInstance(getContext()).attendanceDao().getByStudent(studentId);
            Log.d("STUDENT_ATTENDANCE_LIST",activityList.toString());
            this.buildRecyclerView(view);
        }

        private void buildRecyclerView(View view) {

            attendanceAdapater = new ClientAttendanceAdapter(activityList);

            recyclerView = view.findViewById(R.id.recycler_view);

            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),1));

            layoutManager = new LinearLayoutManager(getContext());

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(attendanceAdapater);

        }



    }