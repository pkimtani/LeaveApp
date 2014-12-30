package com.developer.gdgvit.leaveapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by pk on 27/12/14.
 *
 * Application Class of Leave App
 *
 */
public class LeaveAppClass extends Application {

    public static final String dbDataKey = "db";
    public static final String loginDataKey = "login";

    public static final String Log_Tag = "LeaveApp";

    public static DBPref dbpref;

    public static boolean TWO_PANE_UI = false;

    @Override
    public void onCreate() {
        super.onCreate();
        dbpref = new DBPref();
        Toast.makeText(getApplicationContext(), "!!Welcome!! to Leave App", Toast.LENGTH_LONG).show();
    }

    public class DBPref
    {
        private SharedPreferences ini_pref;
        private SharedPreferences.Editor editor;

        public DBPref() {
            this.ini_pref = getSharedPreferences("initial", 0);
            this.editor = ini_pref.edit();
            editor.commit();
        }

        public void SavePreferences(String key, boolean value) {

            editor.putBoolean(key, value);
            editor.commit();

        }

        public boolean GetPreferences(String key) {
            return ini_pref.getBoolean(key, false);

        }
    }

}