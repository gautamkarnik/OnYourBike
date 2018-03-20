package com.example.gautamkarnik.onyourbike;

import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import com.example.gautamkarnik.onyourbike.activities.TimerActivityTest;
import com.example.gautamkarnik.onyourbike.application.ApplicationTest;
import com.example.gautamkarnik.onyourbike.services.TimerServiceTests;
import com.example.gautamkarnik.onyourbike.unit.TimerStateTests;

import junit.framework.TestSuite;

/**
 * Created by gautamkarnik on 2015-07-14.
 */
public class OnYourBikeInstrumentationTestRunner extends InstrumentationTestRunner {

    // see the following links about creating your own test runner
    // this class is not needed, but I am curious in the event that tests want to be omitted
    // http://zutubi.com/source/projects/android-junit-report/
    // http://www.alittlemadness.com/category/android


    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

        suite.addTestSuite(TimerStateTests.class);
        suite.addTestSuite(TimerActivityTest.class);
        suite.addTestSuite(TimerServiceTests.class);
        suite.addTestSuite(ApplicationTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return OnYourBikeInstrumentationTestRunner.class.getClassLoader();
    }
}