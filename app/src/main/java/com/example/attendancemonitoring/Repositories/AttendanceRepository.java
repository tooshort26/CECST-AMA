package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;

public class AttendanceRepository {

    public static void create(Context context, int activity_id, String name, String student_id, String device_id)
    {
        DB.getInstance(context).attendanceDao().create(
                new Attendance(activity_id, name, student_id, device_id)
        );
    }
}
