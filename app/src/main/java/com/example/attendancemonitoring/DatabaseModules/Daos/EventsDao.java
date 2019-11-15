package com.example.attendancemonitoring.DatabaseModules.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.attendancemonitoring.DatabaseModules.Models.Event;

import java.util.List;

@Dao
public interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(Event event);

    @Query("SELECT * FROM events")
    List<Event> getEvents();

}
