package com.developer.gdgvit.leaveapp.dataHandlers;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.LeaveAppClass;
import com.developer.gdgvit.leaveapp.syncAdaptors.LeaveAppSyncAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by pk on 29/12/14.
 *
 * Service used to submit data for new leave applied..
 *
 */
public class Apply_Leave_Service extends Service {

    String auth, leave, place, exit, entry, reason, reg, pas;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(LeaveAppClass.Log_Tag, "Apply Service | On Start");

        auth = intent.getStringExtra(Home.AUTH_TAG);
        leave = intent.getStringExtra(Home.LEAVE_TYPE_TAG);
        place = intent.getStringExtra(Home.PLACE_TAG);
        exit = Utility.deformDateA(intent.getStringExtra(Home.EXIT_ON_TAG));
        entry = Utility.deformDateA(intent.getStringExtra(Home.ENTRY_ON_TAG));
        reason = intent.getStringExtra(Home.REASON_TAG);

        if(leave.equals("Home Town/Semester Leave"))
            leave = "0";

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        reg = pref.getString("reg_no","");  //Registration Number
        pas = pref.getString("pass", ""); //Password

        if(reg.equals("") || pas.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please provide reg no and pass in the app setting.. :(", Toast.LENGTH_LONG).show();
            return 0;
        }

        Log.i(LeaveAppClass.Log_Tag, "Apply Service | Acquired Values");

        new SubmitTask().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    public void showToast(String r)
    {
        Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();
    }

    private class SubmitTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            InputStream inputStream;
            StringBuffer buffer;

            final String BASE_URL_LOGIN = "http://levstud.appspot.com/login";
            final String BASE_URL_APPLY = "http://levstud.appspot.com/apply";

            Uri sUri = Uri.parse(BASE_URL_APPLY).buildUpon()
                    .appendPath(reg)
                    .appendPath(auth)
                    .appendPath(leave)
                    .appendPath(exit)
                    .appendPath(entry)
                    .appendPath(place)
                    .appendPath(reason)
                    .build();

            try {

                Uri loginUri = Uri.parse(BASE_URL_LOGIN).buildUpon()
                        .appendPath(reg).appendPath(pas)
                        .build();

                URL url = new URL(loginUri.toString());

                Log.i(LeaveAppClass.Log_Tag, "Login: " + loginUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();

                if (inputStream == null) {
                    //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
                    //stopSelf();
                    return "OOPS! Something went wrong :(";
                    //nothing to do
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
                    //stopSelf();
                    return "OOPS! Something went wrong :(";
                    //nothing to do
                }

                if(buffer.toString().equals("invalid"))
                {
                    //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                    //Toast.makeText(getApplicationContext(), "Incorrect Regno and password :(", Toast.LENGTH_LONG).show();
                    //stopSelf();
                    return "Incorrect Regno and password :(";
                }

                Log.i(LeaveAppClass.Log_Tag, "Login Success");

                url = new URL(sUri.toString());

                Log.i(LeaveAppClass.Log_Tag, "Apply: " + sUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();

                if (inputStream == null) {
                    //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
                    //stopSelf();
                    return "OOPS! Something went wrong :(";
                    //nothing to do
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
                    //stopSelf();
                    return "OOPS! Something went wrong :(";
                    //nothing to do
                }

                Log.i(LeaveAppClass.Log_Tag, "All Done | " + buffer.toString());


            } catch (MalformedURLException e) {
                Log.i(LeaveAppClass.Log_Tag, "Apply Service | URL Exception: " + e);
                return "OOPS! Something went wrong :(";
                //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
            } catch (ProtocolException e) {
                Log.i(LeaveAppClass.Log_Tag, "Apply Service | Protocol Exception: " + e);
                return "OOPS! Something went wrong :(";
                //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.i(LeaveAppClass.Log_Tag, "Apply Service | IO Exception: " + e);
                return "OOPS! Something went wrong :(";
                //Toast.makeText(getApplicationContext(), "OOPS! Something went wrong :(", Toast.LENGTH_LONG).show();
            }

            return "Leave Applied Successfully... :)";
        }

        @Override
        protected void onPostExecute(String res) {

            if(res.equals("Leave Applied Successfully... :)"))
                LeaveAppSyncAdapter.syncImmediately(getApplicationContext());

            showToast(res);

            super.onPostExecute(res);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
