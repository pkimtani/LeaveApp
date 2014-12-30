package com.developer.gdgvit.leaveapp.dataHandlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.developer.gdgvit.leaveapp.LeaveAppClass;

/**
 * Created by pk on 26/12/14.
 *
 * Utility class to check for id and pass data present in the settings.
 *
 */
public class CheckIdPass {

    Context context;

    public CheckIdPass(Context context)
    {
        this.context = context;
    }

    public boolean checkIdPas()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String reg = pref.getString("reg_no","");  //Registration Number
        String pas = pref.getString("pass", ""); //Password

        return !(reg.equals("") || pas.equals(""));

    }

    public String getData(String key)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return pref.getString(key, "");
    }

}
