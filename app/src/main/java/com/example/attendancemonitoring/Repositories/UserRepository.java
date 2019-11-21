package com.example.attendancemonitoring.Repositories;

import android.content.Context;

import com.example.attendancemonitoring.DatabaseModules.DB;
import com.example.attendancemonitoring.DatabaseModules.Models.User;
import com.example.attendancemonitoring.Helpers.SharedPref;
import com.example.attendancemonitoring.Helpers.Strings;

public class UserRepository {
    public static boolean isUserAlreadyRegister(Context context)
    {
        User user  = DB.getInstance(context).userDao().getUser();
        return user != null;
    }

    public static String getUserRole(Context context)
    {
        return SharedPref.getSharedPreferenceString(context, "user_role", "employee");
    }

    public static String getUserFullname(Context context)
    {
        User user = DB.getInstance(context).userDao().getUser();
        return Strings.capitalize(user.getFirstname()) + " " + Strings.capitalize(user.getMiddlename()) + " " + Strings.capitalize(user.getLastname());
    }


    public static String getIdNumber(Context context)
    {
        return SharedPref.getSharedPreferenceString(context,"student_id_number", null);
    }

}
