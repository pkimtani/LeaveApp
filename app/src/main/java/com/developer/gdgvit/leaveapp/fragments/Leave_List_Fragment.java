package com.developer.gdgvit.leaveapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.gdgvit.leaveapp.LeaveAppClass;
import com.developer.gdgvit.leaveapp.dataHandlers.CheckInternet;
import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;
import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.R;
import com.developer.gdgvit.leaveapp.dataHandlers.Utility;

public class Leave_List_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView leaveList;
    Button applyBtn;

    Detail_View_Fragment df;

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
    public static final int IND_COL_NOD = 7;
    public static final int IND_COL_STATUS = 8;
    public static final int IND_COL_APPROVED_BY = 9;
    public static final int IND_COL_APPROVED_ON = 10;

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

        mLeaveAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if(columnIndex==IND_COL_EXIT_ON)
                {
                    ((TextView) view).setText(Utility.formDate(cursor.getString(cursor.getColumnIndex(LeaveEntry.COL_EXIT_ON))));
                    return true;
                }
                if(columnIndex==IND_COL_NOD)
                {
                    ((TextView) view).setText("No of days: " + cursor.getString(cursor.getColumnIndex(LeaveEntry.COL_NO_OF_DAYS)));
                    return true;
                }

                return false;
            }
        });

        leaveList.setAdapter(mLeaveAdapter);

        leaveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Cursor c = mLeaveAdapter.getCursor();
                c.moveToPosition(position);
                String slNo = c.getString(c.getColumnIndex(LeaveEntry._ID));
                String exit = c.getString(c.getColumnIndex(LeaveEntry.COL_EXIT_ON));

                if(exit.equals(exit_on))
                    Toast.makeText(getActivity(), "Provide the login details in setting and refresh first... :(", Toast.LENGTH_LONG).show();
                else
                {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString(Home.Sl_NO_TAG, slNo);
                    dataBundle.putString(Home.EXIT_DATE_TAG, exit);
                    df = new Detail_View_Fragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    df.setArguments(dataBundle);
                    if(LeaveAppClass.TWO_PANE_UI)
                        ft.replace(R.id.detailContainer, df, "detail");
                    else
                        ft.replace(R.id.container, df, "detail");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }

        });

        applyBtn = (Button) rootView.findViewById(R.id.newLeaveBtn);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new CheckInternet(getActivity()).state())
                {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    if(LeaveAppClass.TWO_PANE_UI)
                        ft.replace(R.id.detailContainer, new Apply_New_Leave_Fragment(), "apply");
                    else
                        ft.replace(R.id.container, new Apply_New_Leave_Fragment(), "apply");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Please connect to internet first.. :(",Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }

    //Loader implementation from here....

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {

        if(LeaveAppClass.dbpref.GetPreferences(LeaveAppClass.loginDataKey))
        {
            if(LeaveAppClass.dbpref.GetPreferences(LeaveAppClass.dbDataKey))
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

                LeaveAppClass.dbpref.SavePreferences(LeaveAppClass.dbDataKey, false);

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

            LeaveAppClass.dbpref.SavePreferences(LeaveAppClass.dbDataKey, false);

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
