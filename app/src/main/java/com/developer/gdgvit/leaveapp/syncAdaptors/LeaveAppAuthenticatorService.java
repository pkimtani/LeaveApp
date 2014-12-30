package com.developer.gdgvit.leaveapp.syncAdaptors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by pk on 25/12/14.
 *
 * A bound Service that instantiates the authenticator
 * when started.
 */
public class LeaveAppAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private LeaveAppAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new LeaveAppAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}