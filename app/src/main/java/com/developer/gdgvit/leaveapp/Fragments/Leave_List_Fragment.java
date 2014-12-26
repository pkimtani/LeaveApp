package com.developer.gdgvit.leaveapp.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.developer.gdgvit.leaveapp.DataHandlers.DBContract.LeaveEntry;
import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.R;

public class Leave_List_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView leaveList;

    private static final String[] LeaveEntryCols_Array = {
            LeaveEntry._ID,
            LeaveEntry.COL_APPLIED_ON,
            LeaveEntry.COL_APPLIED_TO,
            LeaveEntry.COL_LEAVE_TYPE,
            LeaveEntry.COL_PLACE,
            LeaveEntry.COL_EXIT_ON,
            LeaveEntry.COL_ENTRY_ON,
            LeaveEntry.COL_NO_OF_DAYS,
            LeaveEntry.COL_STATUS,
            LeaveEntry.COL_APPROVED_BY,
            LeaveEntry.COL_APPROVED_ON
    };

    //Indices tied to each column in column array
    public static final int IND_COL_ID = 0;
    public static final int IND_COL_APPLIED_ON = 1;
    public static final int IND_COL_APPLIED_TO = 2;
    public static final int IND_COL_LEAVE_TYPE = 3;
    public static final int IND_COL_PLACE = 4;
    public static final int IND_COL_EXIT_ON = 5;
    public static final int IND_COL_ENTRY_ON = 6;
    public static final int IND_COL_STATUS = 7;
    public static final int IND_COL_APPROVED_BY = 8;
    public static final int IND_COL_APPROVED_ON = 9;

    //Dummy data to show in case if no data is fetched and stored in the database
    public static final String applied_to = "Applied To";
    public static final String applied_on = "Applied Date";
    public static final String leave_type = "Leave Type";
    public static final String place = "Leave Place";
    public static final String exit_on = "Exit Date";
    public static final String entry_on = "Entry Date";
    public static final String no_of_days = "No of Days";
    public static final String status = "Status";
    public static final String approved_on = "Approved By";
    public static final String approved_by = "Approved On";
    //Dummy Data till here

    SimpleCursorAdapter mLeaveAdapter;

    private static final int LeaveLoader = 0;

    public Leave_List_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LeaveLoader, null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_home, container, false);

        leaveList = (ListView) rootView.findViewById(R.id.leaveList);

        mLeaveAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.leave_list_item_deafult,
                null,
                new String[] {LeaveEntry.COL_PLACE, LeaveEntry.COL_NO_OF_DAYS, LeaveEntry.COL_EXIT_ON, LeaveEntry.COL_STATUS},
                new int[] {R.id.placeTextView, R.id.nODTextView, R.id.extDateTextView, R.id.statusTextView},
                0
        );

        leaveList.setAdapter(mLeaveAdapter);

        return rootView;
    }

    //Loader implementation from here....

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {

        if(Home.login_data)
        {
            if(Home.db_data)
            {
                return new CursorLoader(
                        getActivity(),
                        LeaveEntry.Content_Uri,
                        LeaveEntryCols_Array,
                        null,
                        null,
                        null
                );
            }
            else
            {
                getActivity().getContentResolver().delete(LeaveEntry.Content_Uri, null, null);

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

                getActivity().getContentResolver().insert(LeaveEntry.Content_Uri, values);

                Home.db_data = true;

                return new CursorLoader(
                        getActivity(),
                        LeaveEntry.Content_Uri,
                        LeaveEntryCols_Array,
                        null,
                        null,
                        null
                );

            }
        }
        else
        {
            getActivity().getContentResolver().delete(LeaveEntry.Content_Uri, null, null);

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

            getActivity().getContentResolver().insert(LeaveEntry.Content_Uri, values);

            Home.db_data = true;

            return new CursorLoader(
                    getActivity(),
                    LeaveEntry.Content_Uri,
                    LeaveEntryCols_Array,
                    null,
                    null,
                    null
            );
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mLeaveAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mLeaveAdapter.swapCursor(null);
    }

}
