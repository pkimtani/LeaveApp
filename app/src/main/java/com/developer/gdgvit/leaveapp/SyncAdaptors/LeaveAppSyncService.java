package com.developer.gdgvit.leaveapp.SyncAdaptors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.developer.gdgvit.leaveapp.Home;

/**
 * Created by pk on 25/12/14.
 *
 * Leave App sync adapter service
 *
 */
public class LeaveAppSyncService extends Service
{

    private static final Object syncAdapterLock = new Object();
    private static LeaveAppSyncAdapter leaveAppSyncAdapter = null;

    @Override
    public void onCreate()
    {
        Log.i(Home.Log_Tag, "OnCreate | Leave App SyncAdapterService");
        if(leaveAppSyncAdapter==null)
            leaveAppSyncAdapter = new LeaveAppSyncAdapter(getApplicationContext(), true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return leaveAppSyncAdapter.getSyncAdapterBinder();
    }
}
