package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Event;

public class EventRepository {
    public static void create(Context context, String name, String date)
    {
        Event event = new Event(name, date);
        DB.getInstance(context).eventsDao().create(event);
    }
}
