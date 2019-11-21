package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.Activity;

public class ActivityRepository {

    public static long create(Context context, String name, String description)
    {
        Activity activity = new Activity();
        activity.setName(name.toLowerCase());
        activity.setDescription(description);
       return DB.getInstance(context).activityDao().create(activity);
    }

    public static long create(Context context,int id, String name, String description)
    {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name.toLowerCase());
        activity.setDescription(description);
        return DB.getInstance(context).activityDao().create(activity);
    }

    public static boolean exists(Context context, String name)
    {
        return DB.getInstance(context).activityDao().find(name.toLowerCase()) >= 1;
    }
}
