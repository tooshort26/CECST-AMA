package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.User;

public class UserRepository {
    public static boolean isUserAlreadyRegister(Context context)
    {
        User user  = DB.getInstance(context).userDao().getUser();
        return user != null;
    }
}
