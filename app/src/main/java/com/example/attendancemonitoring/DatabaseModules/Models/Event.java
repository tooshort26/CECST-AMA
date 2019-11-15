package com.example.attendancemonitoring.DatabaseModules.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    String name;
    String date;

    public Event(String name, String date)
    {
        this.setDate(name);
        this.setName(date);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
