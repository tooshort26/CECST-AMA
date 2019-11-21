package com.example.attendancemonitoring.DatabaseModules.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="activity_attendance")
public class Attendance {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int activity_id;
    public String student_name;
    public String student_id;
    public String device_id;

    public Attendance(int activity_id, String student_name, String student_id, String device_id) {
        this.activity_id = activity_id;
        this.student_name = student_name;
        this.student_id = student_id;
        this.device_id = device_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
