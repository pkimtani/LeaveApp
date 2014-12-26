package com.developer.gdgvit.leaveapp.DataHandlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.R;

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

        if(reg=="" || pas == "")
        {
            //Set boolean login data to false..

            Home.login_data = false;

            return false;
        }
        else
        {
            //Set the login data boolean to true.
            Home.login_data = true;

            return true;
        }
    }

}
