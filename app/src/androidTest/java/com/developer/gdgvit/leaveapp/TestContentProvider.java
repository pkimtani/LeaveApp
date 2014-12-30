package com.developer.gdgvit.leaveapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;
import com.developer.gdgvit.leaveapp.dataHandlers.DBHelper;

/**
 * Created by pk on 21/12/14.
 *
 * Class to perform J Unit Test on Database
 *
 */
public class TestContentProvider extends AndroidTestCase {

    public void testDelDb() throws Throwable
    {

        mContext.deleteDatabase(DBHelper.DB_Name);

    }

    public void testInsertProvider()
    {

        final String applied_to = "faculty 1";
        final String applied_on = "19/12/2014";
        final String leave_type = "hometown";
        final String place = "Guwahati";
        final String exit_on = "20122014";
        final String entry_on = "30122014";
        final String no_of_days = "11";
        final String status = "pending";
        final String approved_on = "NA";
        final String approved_by = "NA";

        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(LeaveEntry.COL_APPLIED_TO, applied_to);
        values.put(LeaveEntry.COL_APPLIED_ON, applied_on);
        values.put(LeaveEntry.COL_LEAVE_TYPE, leave_type);
        values.put(LeaveEntry.COL_PLACE, place);
        values.put(LeaveEntry.COL_EXIT_ON, exit_on);
        values.put(LeaveEntry.COL_ENTRY_ON, entry_on);
        values.put(LeaveEntry.COL_NO_OF_DAYS, no_of_days);
        values.put(LeaveEntry.COL_STATUS, status);
        values.put(LeaveEntry.COL_APPROVED_BY, approved_by);
        values.put(LeaveEntry.COL_APPROVED_ON, approved_on);

        Uri ins = mContext.getContentResolver().insert(LeaveEntry.Content_Uri, values);

        Cursor cursor = mContext.getContentResolver().query(LeaveEntry.Content_Uri,
                null,//leave column null to return all columns
                null,//Columns for where clause
                null,//values for where clause
                null//columns to sort order
        );

        if(cursor.moveToFirst())
        {
            int id_index = cursor.getColumnIndex(LeaveEntry._ID);
            int id_val = cursor.getInt(id_index);

            assertEquals(1, id_val);
            Log.i(Home.Log_Tag, ""+id_val);
        }
        else
        {
            fail("No Values");
        }

        String dateS = "20122014";

        Cursor cursorS = mContext.getContentResolver().query(LeaveEntry.buildLeaveUriWithDate(dateS),
                null,//leave column null to return all columns
                null,//Columns for where clause
                null,//values for where clause
                null//columns to sort order
        );

        if(cursor.moveToFirst())
        {
            int id_index = cursor.getColumnIndex(LeaveEntry._ID);
            int id_val = cursor.getInt(id_index);

            assertEquals(1, id_val);
            Log.i(Home.Log_Tag, ""+id_val);
        }
        else
        {
            fail("No Values");
        }

    }


    public void testGetType()
    {
        //uri: content://com.developer.gdgvit.leaveapp.app/leave
        String type = mContext.getContentResolver().getType(LeaveEntry.Content_Uri);
        assertEquals(LeaveEntry.Content_Type_Dir, type);

        String date = "12122014";//dummy data
        //uri: content://com.developer.gdgvit.leaveapp.app/leave/student1/12122014
        type = mContext.getContentResolver().getType(LeaveEntry.buildLeaveUriWithDate(date));
        assertEquals(LeaveEntry.Content_Type_Item, type);

    }

}
