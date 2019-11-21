package com.example.attendancemonitoring.Helpers;

import android.content.Context;



public class UserHelper {
    public static boolean isUserAlreadyRegister(Context context)
    {
        String user_role = SharedPref.getSharedPreferenceString(context, "user_role", null);
        return user_role != null;
    }


}
