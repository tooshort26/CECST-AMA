package com.example.attendancemonitoring.DatabaseModules.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="activities")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String name;
    private String description;



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
}
