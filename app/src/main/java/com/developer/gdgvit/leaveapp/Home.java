package com.developer.gdgvit.leaveapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.gdgvit.leaveapp.DataHandlers.CheckIdPass;
import com.developer.gdgvit.leaveapp.Fragments.Leave_List_Fragment;
import com.developer.gdgvit.leaveapp.SyncAdaptors.LeaveAppSyncAdapter;

import static com.developer.gdgvit.leaveapp.SyncAdaptors.LeaveAppSyncAdapter.getSyncAccount;


public class Home extends FragmentActivity {

    public static boolean login_data = false;//login id and password is available
    public static boolean db_data = false;//data is available in the database
    public static final String Log_Tag = "LeaveApp";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Leave_List_Fragment()).commit();
        }

        LeaveAppSyncAdapter.initializeSyncAdapter(this);

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
                LeaveAppSyncAdapter.syncImmediately(this);
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
