package com.example.attendancemonitoring.DatabaseModules.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="attendances")
public class Attendance {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String name;
    private String description;
    private String start;
    private String end;


    public Attendance(String name, String description, String start, String end) {
        this.setName(name);
        this.setDescription(description);
        this.setStart(start);
        this.setEnd(end);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
