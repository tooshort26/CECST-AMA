package com.example.attendancemonitoring.DatabaseModules.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.attendancemonitoring.DatabaseModules.Models.Activity;
import com.example.attendancemonitoring.DatabaseModules.Models.Attendance;

import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(Attendance attendance);

    @Query("SELECT * FROM activity_attendance")
    List<Attendance> getAttendance();

    @Query("SELECT COUNT(id) FROM activity_attendance WHERE activity_id = :activity_id AND device_id = :device_id")
    int check(int activity_id, String device_id);

    @Query("SELECT activities.* FROM activity_attendance LEFT JOIN activities ON activity_attendance.activity_id = activities.id " +
            "WHERE  student_id = :student_id ")
    List<Activity> getByStudent(String student_id);

    @Query("SELECT * FROM activity_attendance WHERE activity_id = :activity_id")
    List<Attendance> getAttendanceById(int activity_id);

}
