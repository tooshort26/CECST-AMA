package com.example.attendancemonitoring.DatabaseModules.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String id_number;
    public String firstname;
    public String middlename;
    public String lastname;
    public String course;



    public User(String id_number, String firstname, String middlename, String lastname, String course) {
        this.setId_number(id_number);
        this.setFirstname(firstname);
        this.setMiddlename(middlename);
        this.setLastname(lastname);
        this.setCourse(course);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
