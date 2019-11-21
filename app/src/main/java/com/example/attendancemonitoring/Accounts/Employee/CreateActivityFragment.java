package com.example.attendancemonitoring.Accounts.Employee;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendancemonitoring.AttendanceActivity;
import com.example.attendancemonitoring.R;
import com.example.attendancemonitoring.Repositories.ActivityRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateActivityFragment extends Fragment {


    public CreateActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText activityName = view.findViewById(R.id.activityName);

        Button btnGenerateActivity = view.findViewById(R.id.btnGenerateActivity);

        btnGenerateActivity.setOnClickListener(view1 -> {
            if (!activityName.getText().toString().trim().isEmpty()) {
                if(!ActivityRepository.exists(getContext(), activityName.getText().toString()))
                {
                    long activityId = ActivityRepository.create(getContext(), activityName.getText().toString(), "activity_description");
                    Intent intent  = new Intent(getActivity(), AttendanceActivity.class);
                    intent.putExtra("ACTIVITY_NAME", activityName.getText().toString());
                    intent.putExtra("ACTIVITY_ID", String.valueOf(activityId));
                    activityName.setText("");
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Activity already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter the activity name.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
