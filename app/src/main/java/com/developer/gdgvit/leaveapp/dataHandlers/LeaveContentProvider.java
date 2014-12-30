package com.developer.gdgvit.leaveapp.dataHandlers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;

/**
 * Created by pk on 21/12/14.
 *
 * Content Provider of the LeaveApp
 *
 */
public class LeaveContentProvider extends ContentProvider {

    public static final int LEAVE = 100;
    public static final int LEAVE_WITH_DATE = 101;

    private DBHelper db;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.Content_Auth;

        matcher.addURI(authority, DBContract.Path_LEAVE_TB, LEAVE);
        matcher.addURI(authority, DBContract.Path_LEAVE_TB + "/*", LEAVE_WITH_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        db = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {

        Cursor retCur;

        switch(mUriMatcher.match(uri))
        {
            case LEAVE:
                retCur = db.getReadableDatabase().query(
                        LeaveEntry.TB_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LEAVE_WITH_DATE:
                String[] args = {LeaveEntry.getExitDateFromUri(uri)};
                retCur = db.getReadableDatabase().query(
                        LeaveEntry.TB_NAME,
                        projection,
                        LeaveEntry.COL_EXIT_ON + " = ?;",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCur.setNotificationUri(getContext().getContentResolver(), uri);

        return retCur;
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch(match)
        {
            case LEAVE:
                return LeaveEntry.Content_Type_Dir;
            case LEAVE_WITH_DATE:
                return LeaveEntry.Content_Type_Item;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" +uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri retUri;
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();

        switch(mUriMatcher.match(uri))
        {
            case LEAVE:
                long _id = db.insert(LeaveEntry.TB_NAME, null, contentValues);
                    if(_id > 0)
                        retUri = LeaveEntry.buildLeaveUriFromId(_id);
                    else
                        throw new SQLException("Failed to insert row to " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" +uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int rowAffected;

        switch(mUriMatcher.match(uri))
        {
            case LEAVE:
                rowAffected = db.delete(LeaveEntry.TB_NAME, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //because a null deletes all rows, so we notify change only if any or all rows are affected
        if(selection == null || rowAffected != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowAffected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int rowAffected;

        switch(mUriMatcher.match(uri))
        {
            case LEAVE:
                rowAffected = db.update(LeaveEntry.TB_NAME, contentValues, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //because a null deletes all rows, so we notify change only if any or all rows are affected
        if(rowAffected != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowAffected;
    }
}
