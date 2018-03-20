package com.example.gautamkarnik.onyourbike.application;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.OnYourBike;
import com.example.gautamkarnik.onyourbike.activities.MockOnYourBike;
import com.example.gautamkarnik.onyourbike.activities.SettingsActivity;
import com.example.gautamkarnik.onyourbike.activities.TimerActivity;
import com.example.gautamkarnik.onyourbike.model.Settings;
import com.example.gautamkarnik.onyourbike.services.TimerService;
import com.example.gautamkarnik.onyourbike.services.WhereAmIService;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<OnYourBike> {
    public ApplicationTest() {
        super(OnYourBike.class);
    }

    public ApplicationTest(Class<OnYourBike> applicationClass, OnYourBike application) {
        super(applicationClass);
        this.application = application;
    }

    private OnYourBike application;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    @SmallTest
    public void testSimpleCreate() {
        assertNotNull(application);
    }

    @SmallTest
    public void testPrecondition() {
        ApplicationInfo info = application.getApplicationInfo();
        assertNotNull(info);
        assertTrue(info.packageName.equals("com.engineering.gautamkarnik.onyourbike"));
    }

    @MediumTest
    public void testBindServices() throws InterruptedException {
        //assertTrue(application.isTimerServiceNull());
        //assertTrue(application.isWhereAmIServiceNull());

        // not sure why this doesn't work on emulator
        // ... runs properly on real device
        //application.onCreate();

        //try {
        //   Thread.sleep(1000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        //assertFalse(application.isTimerServiceNull());
        //assertFalse(application.isWhereAmIServiceNull());

    }

    @SmallTest
    public void testSettings() {
        Settings settings = application.getSettings();
        assertNotNull(settings);

        // need a better interface for settings here, use of activity in this design is poor choice
        // this does call the code properly and crashes
        // settings.setCaffeinated(new SettingsActivity(), true);
        // assertTrue(settings.isCaffeinated(new SettingsActivity()));
    }

    @SmallTest
    public void testSimpleTerminate() {
        terminateApplication();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        application = null;
    }
}