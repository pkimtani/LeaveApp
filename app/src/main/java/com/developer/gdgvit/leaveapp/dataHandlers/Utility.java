package com.developer.gdgvit.leaveapp.dataHandlers;

import android.database.Cursor;

/**
 * Created by pk on 28/12/14.
 *
 * The Utility class
 *
 */
public class Utility {

    public static String deformDateE(String s)
    {
        String m = s.substring(0, 2);
        String d = s.substring(3, 5);
        String y = s.substring(6, 10);

        return d+m+y;
    }

    public static String deformDateA(String s)
    {
        String d = s.substring(0, 2);
        String m = s.substring(3, 5);
        String y = s.substring(6, 10);

        return m+d+y;
    }

    public static String formDate(String s)
    {
        String d = s.substring(0, 2);
        String m = s.substring(2, 4);
        String y = s.substring(4);

        return d+ "/" + m + "/" + y;
    }

}
