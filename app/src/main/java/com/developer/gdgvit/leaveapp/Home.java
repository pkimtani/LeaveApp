package com.developer.gdgvit.leaveapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.gdgvit.leaveapp.dataHandlers.CheckIdPass;
import com.developer.gdgvit.leaveapp.dataHandlers.CheckInternet;
import com.developer.gdgvit.leaveapp.fragments.Default_Detail_View_Fragment;
import com.developer.gdgvit.leaveapp.fragments.Leave_List_Fragment;
import com.developer.gdgvit.leaveapp.syncAdaptors.LeaveAppSyncAdapter;


public class Home extends FragmentActivity {

    public static final String EXIT_DATE_TAG = "exit_date";
    public static final String Sl_NO_TAG = "sl_no";

    public static final String AUTH_TAG = "auth";
    public static final String LEAVE_TYPE_TAG = "leave_type";
    public static final String EXIT_ON_TAG = "exit";
    public static final String ENTRY_ON_TAG = "entry";
    public static final String PLACE_TAG = "place";
    public static final String REASON_TAG = "reason";

    public static final String REG_TAG = "reg";
    public static final String PASS_TAG = "pass";

    public static final String AUTHORITY = "com.developer.gdgvit.leaveapp.app";

    public static String reg, pass;

    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        reg = pref.getString("reg_no","");  //Registration Number
        pass = pref.getString("pass", ""); //Password

        if(reg.equals("") || pass.equals(""))
            LeaveAppClass.dbpref.SavePreferences(LeaveAppClass.loginDataKey, false);
        else
            LeaveAppClass.dbpref.SavePreferences(LeaveAppClass.loginDataKey, true);

        Log.i(LeaveAppClass.Log_Tag, "Home | Reg No: " + reg);
        Log.i(LeaveAppClass.Log_Tag, "Home | Pass: " + pass);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Leave_List_Fragment()).commit();
        }

        if (findViewById(R.id.detailContainer) != null)
        {
            LeaveAppClass.TWO_PANE_UI = true;
            if (savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detailContainer, new Default_Detail_View_Fragment()).commit();
            }
        }
        else
        {
            LeaveAppClass.TWO_PANE_UI = false;
        }

        //LeaveAppSyncAdapter.initializeSyncAdapter(this);

        // Get the content resolver for your app
        mResolver = getContentResolver();
        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(
                LeaveAppSyncAdapter.getSyncAccount(this),
                AUTHORITY,
                Bundle.EMPTY,
                60*3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            if(new CheckIdPass(this).checkIdPas())
            {
                if(new CheckInternet(this).state())
                {
                    Toast.makeText(this, "Sync started... :)", Toast.LENGTH_LONG).show();
                    LeaveAppSyncAdapter.syncImmediately(this);
                }
                else
                    Toast.makeText(this, "OOPS! Please connect to internet first... :(",Toast.LENGTH_LONG).show();

            }
            else
                Toast.makeText(this, "Please provide login id and password in the settings..", Toast.LENGTH_LONG).show();

            return true;
        }

        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
