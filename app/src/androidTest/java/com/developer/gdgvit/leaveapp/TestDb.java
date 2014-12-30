package com.developer.gdgvit.leaveapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.developer.gdgvit.leaveapp.dataHandlers.DBHelper;
import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;

/**
 * Created by pk on 21/12/14.
 *
 * Class to perform J Unit Test on Database
 *
 */
public class TestDb extends AndroidTestCase {

    public void testCreateDb() throws Throwable
    {

        mContext.deleteDatabase(DBHelper.DB_Name);
        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertDb()
    {

        final String applied_to = "faculty 1";
        final String applied_on = "19/12/2014";
        final String leave_type = "hometown";
        final String place = "Guwahati";
        final String exit_on = "20/12/2014";
        final String entry_on = "30/12/2014";
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

        long returnRowNum;
        returnRowNum = db.insert(LeaveEntry.TB_NAME, null, values);

        assertTrue(returnRowNum != -1);

        Log.i(Home.Log_Tag, ""+returnRowNum);

        String[] cols = {LeaveEntry._ID, LeaveEntry.COL_APPLIED_TO, LeaveEntry.COL_APPLIED_ON, LeaveEntry.COL_LEAVE_TYPE,
                LeaveEntry.COL_PLACE, LeaveEntry.COL_EXIT_ON, LeaveEntry.COL_ENTRY_ON, LeaveEntry.COL_NO_OF_DAYS,
                LeaveEntry.COL_STATUS, LeaveEntry.COL_APPROVED_BY, LeaveEntry.COL_APPROVED_ON};

        Cursor cursor = db.query(
                LeaveEntry.TB_NAME,
                cols,
                null,//Columns for where clause
                null,//values for where clause
                null,//columns to group by
                null,//columns to filter by row group
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

}
