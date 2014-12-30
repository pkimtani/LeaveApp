package com.developer.gdgvit.leaveapp.dataHandlers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.LeaveAppClass;


/**
 * Created by pk on 21/12/14.
 *
 * Class checks for internet connectivity and returns a true boolean in case of connected or connecting..otherwise false
 *
 */
public class CheckInternet {

    Context context;
    boolean connect;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;

    public CheckInternet(Context c){

        this.context = c;

    }

    public boolean state(){

        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&  activeNetwork.isConnected()){
            Log.i(LeaveAppClass.Log_Tag, "Internet Connected");
            return true;
        }
        else{
            Log.i(LeaveAppClass.Log_Tag, "Internet not connected");
        }

        return false;
    }

}
