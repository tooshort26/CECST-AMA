package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;

public class AttendanceRepository {
    public static void create(Context context,Attendance attendance)
    {
        DB.getInstance(context).attendanceDao().create(attendance);
    }

    public static boolean hasAttend(Context context, String name)
    {
        Attendance attendance = DB.getInstance(context).attendanceDao().find(name);
        return attendance != null && attendance.getName().toLowerCase().equals(name.toLowerCase());
    }
}
