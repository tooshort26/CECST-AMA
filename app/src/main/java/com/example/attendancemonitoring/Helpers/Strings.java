package com.example.attendancemonitoring.Helpers;

import java.util.regex.Pattern;

public class Strings {
    public static String capitalize(String text)
    {
        return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String splitAndGetLastElement(String text)
    {
        String[] splitted = text.split(Pattern.quote(","));
        int len = splitted.length - 1;
        return splitted[len];
    }


}
