package com.developer.gdgvit.leaveapp.dataHandlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;

/**
 * Created by pk on 21/12/14.
 *
 * Database class...create table and manage upgrade
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_Name = "leave";
    public static final int DB_Ver = 1;

    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_Ver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String createTB_Qry = "CREATE TABLE IF NOT EXISTS " + LeaveEntry.TB_NAME + " ( " +
                LeaveEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LeaveEntry.COL_APPLIED_TO + " TEXT NOT NULL, " +
                LeaveEntry.COL_APPLIED_ON + " TEXT NOT NULL, " +
                LeaveEntry.COL_LEAVE_TYPE + " TEXT NOT NULL, " +
                LeaveEntry.COL_PLACE + " TEXT NOT NULL, " +
                LeaveEntry.COL_EXIT_ON + " TEXT NOT NULL, " +
                LeaveEntry.COL_ENTRY_ON + " TEXT NOT NULL, " +
                LeaveEntry.COL_NO_OF_DAYS + " TEXT NOT NULL, " +
                LeaveEntry.COL_STATUS + " TEXT NOT NULL, " +
                LeaveEntry.COL_APPROVED_BY + " TEXT NOT NULL, " +
                LeaveEntry.COL_APPROVED_ON + " TEXT  NOT NULL," +
                " UNIQUE (  " + LeaveEntry.COL_EXIT_ON + " ) ON CONFLICT IGNORE );";

        sqLiteDatabase.execSQL(createTB_Qry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LeaveEntry.TB_NAME);
        onCreate(sqLiteDatabase);

    }
}
