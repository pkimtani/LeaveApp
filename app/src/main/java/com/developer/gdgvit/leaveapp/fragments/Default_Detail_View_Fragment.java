package com.developer.gdgvit.leaveapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.gdgvit.leaveapp.R;

/**
 * Created by pk on 30/12/14.
 *
 * Fragment to show default view
 *
 */
public class Default_Detail_View_Fragment extends Fragment {

    public Default_Detail_View_Fragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.default_detail_view,container,false);

        return rootView;
    }
}
