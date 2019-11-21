package com.example.attendancemonitoring.DatabaseModules.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.attendancemonitoring.DatabaseModules.Models.Activity;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long create(Activity activity);

    @Query("SELECT * FROM activities")
    List<Activity> getActivity();

    @Query("SELECT COUNT(name) FROM activities WHERE name = :name")
    int find(String name);

    @Query("SELECT * FROM activities WHERE id = :id")
    Activity find(long id);
}
