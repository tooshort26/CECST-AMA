package com.example.attendancemonitoring.DatabaseModules;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.attendancemonitoring.DatabaseModules.Daos.ActivityDao;
import com.example.attendancemonitoring.DatabaseModules.Daos.AttendanceDao;
import com.example.attendancemonitoring.DatabaseModules.Daos.UserDao;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.DatabaseModules.Models.User;


@Database(entities = {User.class, Attendance.class, Activity.class},version = 1)
public abstract class DB extends RoomDatabase {

    private static DB appDatabase;
    private Context context;
    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
    public abstract ActivityDao activityDao();


    public synchronized  static DB getInstance(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), DB.class, "attendance_monitoring")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

    public void destroyInstance() {
        appDatabase = null;
    }
}

