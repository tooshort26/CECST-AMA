package com.example.attendancemonitoring.DatabaseModules.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;

import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(Attendance attendance);

    @Query("SELECT * FROM attendances")
    List<Attendance> getAttendance();

    @Query("SELECT * FROM attendances WHERE name LIKE '%' || :name || '%'")
    Attendance find(String name);
}
