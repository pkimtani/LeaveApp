package com.developer.gdgvit.leaveapp.dataHandlers;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pk on 21/12/14.
 *
 * Database contract class
 *
 */
public class DBContract {

    public static final String Content_Auth = "com.developer.gdgvit.leaveapp.app";

    public static final Uri Base_Content_Uri = Uri.parse("content://"+Content_Auth);

    public static final String Path_LEAVE_TB = "leave";

    public static final class LeaveEntry implements BaseColumns
    {

        public static final Uri Content_Uri = Base_Content_Uri.buildUpon().appendPath(Path_LEAVE_TB).build();

        public static final String Content_Type_Dir = "vnd.android.cursor.dir/"+Content_Auth+"/"+Path_LEAVE_TB;
        public static final String Content_Type_Item = "vnd.android.cursor.item/"+Content_Auth+"/"+Path_LEAVE_TB;

        public static final String TB_NAME = "leave";
        public static final String COL_APPLIED_TO = "applied_to";//text not null
        public static final String COL_APPLIED_ON = "applied_on";//text
        public static final String COL_LEAVE_TYPE = "leave_type";//text not null
        public static final String COL_PLACE = "place";//text not null
        public static final String COL_EXIT_ON = "exit_on";//text not null
        public static final String COL_ENTRY_ON = "entry_on";//text not null
        public static final String COL_NO_OF_DAYS = "no_of_days";//text not null
        public static final String COL_STATUS = "status";//text not null
        public static final String COL_APPROVED_BY = "approved_by";//text null
        public static final String COL_APPROVED_ON = "approved_on";//text null

        public static Uri buildLeaveUriFromId(long _id)
        {
            return ContentUris.withAppendedId(Content_Uri, _id);
        }

        public static Uri buildLeaveUriWithDate(String exitDate)
        {
            return Content_Uri.buildUpon().appendPath(exitDate).build();
        }

        public static String getExitDateFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

    }

}
