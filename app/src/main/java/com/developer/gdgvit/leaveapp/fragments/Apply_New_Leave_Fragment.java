package com.developer.gdgvit.leaveapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.developer.gdgvit.leaveapp.Home;
import com.developer.gdgvit.leaveapp.R;
import com.developer.gdgvit.leaveapp.dataHandlers.Apply_Leave_Service;

/**
 * Created by pk on 29/12/14.
 *
 * New leave apply fragment..
 *
 */
public class Apply_New_Leave_Fragment extends Fragment {

    Spinner applySpinner, leaveSpinner;

    ArrayAdapter<CharSequence> applyAdapter, leaveAdapter;

    EditText exit, entry, place, reason;

    Button apply;

    public Apply_New_Leave_Fragment()
    {
        //Required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_apply, container, false);

        applySpinner = (Spinner) rootView.findViewById(R.id.applyToValueSpinner);
        leaveSpinner = (Spinner) rootView.findViewById(R.id.leaveTypeValueSpinner);

        applyAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.applyToList,
                android.R.layout.simple_spinner_item
        );

        applyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        leaveAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.leaveTypeList,
                android.R.layout.simple_spinner_item
        );

        leaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        applySpinner.setAdapter(applyAdapter);
        leaveSpinner.setAdapter(leaveAdapter);

        exit = (EditText) rootView.findViewById(R.id.exitOnValue);
        entry = (EditText) rootView.findViewById(R.id.entryOnValue);
        place = (EditText) rootView.findViewById(R.id.placeToValue);
        reason = (EditText) rootView.findViewById(R.id.reasonValue);

        apply = (Button) rootView.findViewById(R.id.applyNowBtn);

        apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(exit.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please provide the exit date... :(", Toast.LENGTH_LONG).show();
                else if(entry.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please provide the entry date... :(", Toast.LENGTH_LONG).show();
                else if(place.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please provide the address... :(", Toast.LENGTH_LONG).show();
                else if(reason.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Please provide the reason for leave... :(", Toast.LENGTH_LONG).show();
                else if(checkDate(exit.getText().toString()) && checkDate(entry.getText().toString()))
                    Toast.makeText(getActivity(), "Please provide a valid date... :(", Toast.LENGTH_LONG).show();
                else
                {
                    Intent intent = new Intent(getActivity(), Apply_Leave_Service.class);

                    intent.putExtra(Home.AUTH_TAG, applySpinner.getSelectedItem().toString());
                    intent.putExtra(Home.LEAVE_TYPE_TAG, leaveSpinner.getSelectedItem().toString());
                    intent.putExtra(Home.EXIT_ON_TAG, exit.getText().toString());
                    intent.putExtra(Home.ENTRY_ON_TAG, entry.getText().toString());
                    intent.putExtra(Home.PLACE_TAG, place.getText().toString());
                    intent.putExtra(Home.REASON_TAG, reason.getText().toString());
                    getActivity().startService(intent);
                    Toast.makeText(getActivity(), "Applying leave...please be patient..", Toast.LENGTH_LONG).show();

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, new Leave_List_Fragment(), "home");
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        return rootView;
    }

    public boolean checkDate(String s)
    {

        if(s.length()>10)
            return true;

        int d = Integer.parseInt(s.substring(0, 2));
        int m = Integer.parseInt(s.substring(3, 5));
        int y = Integer.parseInt(s.substring(6, 10));

        if(m<=0 || m>=13)
            return true;
        else if(m==2)
            if(d<=0 || d>=29)
                return true;
        else if(d<=0 || d>=31)
                return true;
        else if(y<=2000 || y>=2500)
                return true;

        return false;
    }
}
