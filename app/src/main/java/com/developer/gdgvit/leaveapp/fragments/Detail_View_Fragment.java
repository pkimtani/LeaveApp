package com.developer.gdgvit.leaveapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.LeaveAppClass;
import com.developer.gdgvit.leaveapp.R;
import com.developer.gdgvit.leaveapp.dataHandlers.DBContract.LeaveEntry;
import com.developer.gdgvit.leaveapp.dataHandlers.Utility;

/**
 * Created by pk on 28/12/14.
 *
 * Detail view of the selected leave..
 *
 */
public class Detail_View_Fragment extends Fragment {

    TextView slNo, applOn, applTo, leave, place, exit, entry, nod, status, apprBy, apprOn;

    public Detail_View_Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);

        Bundle data = new Bundle();
        data = this.getArguments();
        String sSlNo = data.getString(Home.Sl_NO_TAG);
        String sExit = data.getString(Home.EXIT_DATE_TAG);

        Log.i(LeaveAppClass.Log_Tag, "Detail Fragment | Extra | Sl No: " + sSlNo);
        Log.i(LeaveAppClass.Log_Tag, "Detail Fragment | Extra | Exit Date: " + sExit);

        slNo = (TextView) rootView.findViewById(R.id.slnoValue);
        applOn = (TextView) rootView.findViewById(R.id.applOnValue);
        applTo = (TextView) rootView.findViewById(R.id.applToValue);
        leave = (TextView) rootView.findViewById(R.id.leaveValue);
        place = (TextView) rootView.findViewById(R.id.placeValue);
        exit = (TextView) rootView.findViewById(R.id.exitValue);
        entry = (TextView) rootView.findViewById(R.id.entryValue);
        nod = (TextView) rootView.findViewById(R.id.nodValue);
        status = (TextView) rootView.findViewById(R.id.statusValue);
        apprBy = (TextView) rootView.findViewById(R.id.apprByValue);
        apprOn = (TextView) rootView.findViewById(R.id.apprOnValue);

        Cursor c = getActivity().getContentResolver().query(
                LeaveEntry.Content_Uri,
                null,
                LeaveEntry._ID
                        + " = "
                        + sSlNo + ";",
                null,
                null
        );

        Log.i(LeaveAppClass.Log_Tag, "Detail Fragment | Cursor | Count: " + c.getCount());

        c.moveToFirst();

        Log.i(LeaveAppClass.Log_Tag, "Detail Fragment | Cursor | Sl No: " + c.getString(c.getColumnIndex(LeaveEntry._ID)) );

        slNo.setText(c.getString(c.getColumnIndex(LeaveEntry._ID)));
        applOn.setText(Utility.formDate(c.getString(c.getColumnIndex(LeaveEntry.COL_APPLIED_ON))));
        applTo.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_APPLIED_TO)));
        leave.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_LEAVE_TYPE)));
        place.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_PLACE)));
        exit.setText(Utility.formDate(c.getString(c.getColumnIndex(LeaveEntry.COL_EXIT_ON))));
        entry.setText(Utility.formDate(c.getString(c.getColumnIndex(LeaveEntry.COL_ENTRY_ON))));
        nod.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_NO_OF_DAYS)));
        status.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_STATUS)));
        apprBy.setText(c.getString(c.getColumnIndex(LeaveEntry.COL_APPROVED_BY)));

        String appr = c.getString(c.getColumnIndex(LeaveEntry.COL_APPROVED_ON));

        if(!appr.equals("-"))
            appr = Utility.formDate(appr);

        apprOn.setText(appr);

        c.close();

        return rootView;
    }
}
