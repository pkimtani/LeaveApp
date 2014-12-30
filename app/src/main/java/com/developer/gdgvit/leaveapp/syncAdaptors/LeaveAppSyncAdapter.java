package com.developer.gdgvit.leaveapp.syncAdaptors;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.LeaveAppClass;
import com.developer.gdgvit.leaveapp.R;
import com.developer.gdgvit.leaveapp.dataHandlers.CheckIdPass;
import com.developer.gdgvit.leaveapp.dataHandlers.DBContract;
import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;
import com.developer.gdgvit.leaveapp.dataHandlers.Utility;
import com.developer.gdgvit.leaveapp.fragments.Leave_List_Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by pk on 25/12/14.
 *
 * Leave App Sync Adapter
 *
 */
public class LeaveAppSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int LEAVE_NOTIFICATION_ID = 3004;

    //For Notification
    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[] {
            LeaveEntry.COL_EXIT_ON,
            LeaveEntry.COL_STATUS
    };
    // these indices must match the projection
    private static final int IND_EXIT = 0;
    private static final int IND_STATUS = 1;

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;


    public LeaveAppSyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult)
    {
        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Synchronization Start");

        //new Leave_List_Fragment().showToast("Synchronization started...please be patient..");

        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        //String reg = pref.getString("reg_no", "");  //Registration Number
        //String pas = pref.getString("pass", ""); //Password

        String reg = new CheckIdPass(getContext()).getData("reg_no");
        String pas = new CheckIdPass(getContext()).getData("pass");

        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Reg No: " + reg);
        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Pass: " + pas);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String leaveJSONString;

        //Url to the account authentication and the leave api
        final String BASE_URL_LOGIN = "http://levstud.appspot.com/login";
        final String BASE_URL_LEAVE_API = "http://levstud.appspot.com/leave";

        InputStream inputStream;
        StringBuffer buffer;

        //JSON tags
        final String SL_NO = "Sl No";
        final String APPLY_TO = "Apply To";
        final String APPLY_ON = "Apply On";
        final String PLACE = "Place";
        final String LEAVE_TYPE = "Leave type";
        final String EXIT_ON = "Exit On";
        final String ENTRY_ON= "Entry On";
        final String N_O_D = "No of Days";
        final String STATUS = "Status";
        final String APPROVED_BY = "Approved By";
        final String APPROVED_ON = "Approved On";

        ContentValues values;

        //Strings
        String slno, appliedTo, appliedOn, leave, place, exit, entry, nod, status, approvedBy, approvedOn;

        try {

            Uri loginUri = Uri.parse(BASE_URL_LOGIN).buildUpon()
                    .appendPath(reg).appendPath(pas)
                    .build();

            Log.i(LeaveAppClass.Log_Tag, loginUri.toString());

            URL url = new URL(loginUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();

            if (inputStream == null) {
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                //ContentResolver.cancelSync(account, "com.developer.gdgvit.leaveapp.app");
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                return;
            }

            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Buffer Data 1 " + buffer.toString());

            if(buffer.toString().contentEquals("invalid"))
            {
                Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Buffer Data 2 " + buffer.toString());
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                notifyStatus("Error", "Invalid data");
                //Log.i(LeaveAppClass.Log_Tag, buffer.toString());
                //ContentResolver.cancelSync(getSyncAccount(getContext()), "com.developer.gdgvit.leaveapp.app");
                return;
            }
            else
                Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Buffer Data 3 " + buffer.toString());
            //Login Successful....lets fetch the json from leave api

            Uri leaveUri = Uri.parse(BASE_URL_LEAVE_API).buildUpon()
                    .appendPath(reg).build();

            Log.i(LeaveAppClass.Log_Tag, leaveUri.toString());

            url = new URL(leaveUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();

            if (inputStream == null) {
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                return;
            }

            if(buffer.toString().equals("[]"))
            {
                //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");

                getContext().getContentResolver().delete(LeaveEntry.Content_Uri, null, null);

                values = new ContentValues();

                values.put(LeaveEntry.COL_APPLIED_TO, Leave_List_Fragment.applied_to);
                values.put(LeaveEntry.COL_APPLIED_ON, Leave_List_Fragment.applied_on);
                values.put(LeaveEntry.COL_LEAVE_TYPE, Leave_List_Fragment.leave_type);
                values.put(LeaveEntry.COL_PLACE, Leave_List_Fragment.place);
                values.put(LeaveEntry.COL_EXIT_ON, Leave_List_Fragment.exit_on);
                values.put(LeaveEntry.COL_ENTRY_ON, Leave_List_Fragment.entry_on);
                values.put(LeaveEntry.COL_NO_OF_DAYS, Leave_List_Fragment.no_of_days);
                values.put(LeaveEntry.COL_STATUS, Leave_List_Fragment.status);
                values.put(LeaveEntry.COL_APPROVED_BY, Leave_List_Fragment.approved_by);
                values.put(LeaveEntry.COL_APPROVED_ON, Leave_List_Fragment.approved_on);

                getContext().getContentResolver().insert(LeaveEntry.Content_Uri, values);

                return;
            }
            //JSON fetched successfully

            leaveJSONString = buffer.toString();

            //Parse the JSON now...
            //JSONObject leaveJson = new JSONObject(leaveJSONString);
            JSONArray leaveJSONArray = new JSONArray(leaveJSONString);

            if(!LeaveAppClass.dbpref.GetPreferences(LeaveAppClass.dbDataKey))
                getContext().getContentResolver().delete(LeaveEntry.Content_Uri, null, null);

            for(int i=0; i<leaveJSONArray.length(); i++)
            {
                JSONObject leaveJSON = leaveJSONArray.getJSONObject(i);

                slno = leaveJSON.getString(SL_NO);
                appliedTo = leaveJSON.getString(APPLY_TO);
                appliedOn = leaveJSON.getString(APPLY_ON);
                leave = leaveJSON.getString(LEAVE_TYPE);
                place = leaveJSON.getString(PLACE);
                exit = leaveJSON.getString(EXIT_ON);
                entry = leaveJSON.getString(ENTRY_ON);
                nod = leaveJSON.getString(N_O_D);
                status = leaveJSON.getString(STATUS);
                approvedBy = leaveJSON.getString(APPROVED_BY);
                approvedOn = leaveJSON.getString(APPROVED_ON);

               exit = Utility.deformDateE(exit);
                entry = Utility.deformDateE(entry);

                appliedOn = Utility.deformDateE(appliedOn);

                if(!approvedOn.equals("-"))
                    approvedOn = Utility.deformDateE(approvedOn);

                Log.i(LeaveAppClass.Log_Tag, "Exit: " + exit);
                Log.i(LeaveAppClass.Log_Tag, "Entry: " + entry);
                Log.i(LeaveAppClass.Log_Tag, "Applied: " + appliedOn);
                Log.i(LeaveAppClass.Log_Tag, "Approved: " + approvedOn);

                values = new ContentValues();
                values.put(LeaveEntry._ID, slno);
                values.put(LeaveEntry.COL_APPLIED_TO, appliedTo);
                values.put(LeaveEntry.COL_APPLIED_ON, appliedOn);
                values.put(LeaveEntry.COL_LEAVE_TYPE, leave);
                values.put(LeaveEntry.COL_PLACE, place);
                values.put(LeaveEntry.COL_EXIT_ON, exit);
                values.put(LeaveEntry.COL_ENTRY_ON, entry);
                values.put(LeaveEntry.COL_NO_OF_DAYS, nod);
                values.put(LeaveEntry.COL_STATUS, status);
                values.put(LeaveEntry.COL_APPROVED_BY, approvedBy);
                values.put(LeaveEntry.COL_APPROVED_ON, approvedOn);

                if(checkData(exit, status))
                {
                    Log.i(LeaveAppClass.Log_Tag, "Present");
                    getContext().getContentResolver()
                            .update(
                                    LeaveEntry.Content_Uri,
                                    values,
                                    LeaveEntry.COL_EXIT_ON + " = " + exit,
                                    null
                            );
                }
                else
                {
                    Log.i(LeaveAppClass.Log_Tag, "Absent");
                    getContext().getContentResolver().insert(LeaveEntry.Content_Uri, values);
                }

            }

            //fetch complete
            LeaveAppClass.dbpref.SavePreferences(LeaveAppClass.dbDataKey, true);

            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | DB Key: " + LeaveAppClass.dbpref.GetPreferences(LeaveAppClass.dbDataKey));

        } catch (MalformedURLException e) {
            //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
            Log.i(LeaveAppClass.Log_Tag, "URL Exception: "+e.toString());
        } catch (ProtocolException e) {
            //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
            Log.i(LeaveAppClass.Log_Tag, "Protocol Exception: "+e.toString());
        } catch (IOException e) {
            //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
            Log.i(LeaveAppClass.Log_Tag, "IO Exception: "+e.toString());
        } catch (JSONException e) {
            //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
            Log.i(LeaveAppClass.Log_Tag, "JSON Exception: " + e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //new Leave_List_Fragment().showToast("OOPS! Something went wrong!! :(");
                    Log.e(LeaveAppClass.Log_Tag, "Error closing stream: " + e);
                }
            }
        }

        //new Leave_List_Fragment().showToast("Synchronization finished successfully... :)");
        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Synchronization Complete");

    }

    private boolean checkData(String e, String s)
    {
        Cursor c = getContext().getContentResolver()
                .query(
                        DBContract.LeaveEntry.Content_Uri,//Uri
                        null,//Projection columns..cols to be selected..null means all..
                        DBContract.LeaveEntry.COL_EXIT_ON + " = " + e,//select stmt..where col = value
                        null,//select argument...replace a ? in select stmt
                        null//sort order
                );

        if(c.getCount()>0)
        {
            c.moveToFirst();
            if(!c.getString(c.getColumnIndex(LeaveEntry.COL_STATUS)).equals(s))
            {
                Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Status changed");
                //notify the user
                notifyStatus(e, s);
            }
        }

        return c.getCount() > 0;
    }


    public void notifyStatus(String exit, String status) {

        Context context = getContext();

        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);

        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Notification");

        if ( displayNotifications )
        {
            // Define the text of the forecast.
            String contentText = String.format(context.getString(R.string.format_notification),
                    Utility.formDate(exit),
                    status);

            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications. Just throw in some data.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText(contentText);

            // Make something interesting happen when the user clicks on the notification.
            // In this case, opening the app is sufficient.
            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Notification | Build");

            Intent resultIntent = new Intent(context, Home.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            // LEAVE_NOTIFICATION_ID allows you to update the notification later on.
            mNotificationManager.notify(LEAVE_NOTIFICATION_ID, mBuilder.build());
            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Notification | DONE :)");
        }

        Log.i(LeaveAppClass.Log_Tag, "SyncAdapter | Notification | Hide :(");
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet. If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        // If the password doesn't exist, the account doesn't exist
        if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
            * If you don't set android:syncable="true" in
            * in your <provider> element in the manifest,
            * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
            * here.
            */
            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter Get Account | In If");
            ContentResolver.setSyncAutomatically(newAccount, "com.developer.gdgvit.leaveapp.app", true);
            ContentResolver.addPeriodicSync(newAccount, "com.developer.gdgvit.leaveapp.app", Bundle.EMPTY, 60*3);
        }
        else
        {
            Log.i(LeaveAppClass.Log_Tag, "SyncAdapter Get Account | In Else");
        }

            //onAccountCreated(newAccount, context);

        return newAccount;
    }

    public static void initializeSyncAdapter(Context context)
    {
        getSyncAccount(context);

    }
}
