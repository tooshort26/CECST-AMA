package com.example.attendancemonitoring.DatabaseModules;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.attendancemonitoring.DatabaseModules.Daos.AttendanceDao;
import com.example.attendancemonitoring.DatabaseModules.Daos.EventsDao;
import com.example.attendancemonitoring.DatabaseModules.Daos.UserDao;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;
import com.example.attendancemonitoring.DatabaseModules.Models.Event;
import com.example.attendancemonitoring.DatabaseModules.Models.User;


@Database(entities = {User.class, Attendance.class, Event.class},version = 1)
public abstract class DB extends RoomDatabase {

    private static DB appDatabase;
    private Context context;
    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
    public abstract EventsDao eventsDao();

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

