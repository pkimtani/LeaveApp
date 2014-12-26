package com.developer.gdgvit.leaveapp;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/**
 * Created by pk on 21/12/14.
 *
 * Full Test Suite class to initiate junit test..
 *
 */
public class FullTestSuite {

    public static Test suite()
    {
        return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();
    }

    public FullTestSuite()
    {
        super();
    }

}
