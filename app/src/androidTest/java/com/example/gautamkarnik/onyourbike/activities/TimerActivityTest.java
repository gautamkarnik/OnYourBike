package com.example.gautamkarnik.onyourbike.activities;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.mock.MockApplication;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.TextView;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.activities.TimerActivity;

/**
 * Created by gautamkarnik on 2015-07-09.
 */
public class TimerActivityTest extends ActivityInstrumentationTestCase2<TimerActivity>  {

    private TimerActivity activity;

    public TimerActivityTest() {
        super("com.example.gautamkarnik.onyourbike.activities", TimerActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        //setApplication(new MockOnYourBike());
        //Intent intent = new Intent(getInstrumentation().getTargetContext(),
        //        TimerActivity.class);
        //startActivity(intent, null, null);
        activity = getActivity();
    }

    @SmallTest
    public void testInitialUI() {
        TextView counter = (TextView) activity.findViewById(R.id.timer);
        Button start = (Button) activity.findViewById(R.id.start_button);
        Button stop = (Button) activity.findViewById(R.id.stop_button);
        Button takePhoto = (Button) activity.findViewById(R.id.photo_button);

        assertNotNull(counter);
        assertNotNull(start);
        assertNotNull(stop);
        assertNotNull(takePhoto);
    }

    @UiThreadTest
    public void testOnStart() {
        getInstrumentation().callActivityOnStart(activity);

        assertFalse(activity.isCameraNull());
        assertTrue(activity.isHandlerNull());
        assertTrue(activity.isUpdateTimerNull());
    }

    @UiThreadTest
    public void testOnResume() {
        TextView counter = (TextView) activity.findViewById(R.id.timer);
        Button start = (Button) activity.findViewById(R.id.start_button);
        Button stop = (Button) activity.findViewById(R.id.stop_button);
        Button takePhoto = (Button) activity.findViewById(R.id.photo_button);

        getInstrumentation().callActivityOnResume(activity);

        assertTrue("Counter is correct", counter.getText().equals("0:00:00"));

        assertTrue("Start button is enabled", start.isEnabled());
        assertFalse("Stop button is not enabled", stop.isEnabled());
        assertTrue("Photo button is enabled", takePhoto.isEnabled());
    }

    @SmallTest
    public void testOnStop() {
        getInstrumentation().callActivityOnStop(activity);

        assertTrue(activity.isCameraNull());
        assertTrue(activity.isHandlerNull());
        assertTrue(activity.isUpdateTimerNull());
    }

    @SmallTest
    public void testOnPause() {
        getInstrumentation().callActivityOnPause(activity);

        // Nothing to test just check on RTE or the like
    }

    @SmallTest
    public void testOnDestroy() {
        //getInstrumentation().callActivityOnDestroy(activity);
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                activity.finish();
            }
        });
        // Nothing to test just check on RTE or the like
    }

    @SmallTest
    public void testOnRestart() {
        getInstrumentation().callActivityOnRestart(activity);

        // Nothing to test just check on RTE or the like
    }

    @UiThreadTest
    public void testTimerRunning() {
        IOnYourBike application = ((IOnYourBike) activity.getApplication());

        application.startTimer(null);
        assertTrue(application.isTimerRunning());

        getInstrumentation().callActivityOnStart(activity);

        assertTrue(application.isTimerRunning());
        assertFalse(activity.isCameraNull());
        assertFalse(activity.isHandlerNull());
        assertFalse(activity.isUpdateTimerNull());

        getInstrumentation().callActivityOnStop(activity);

        assertTrue(application.isTimerRunning());
        assertTrue(activity.isCameraNull());
        assertTrue(activity.isHandlerNull());
        assertTrue(activity.isUpdateTimerNull());

        getInstrumentation().callActivityOnStart(activity);
        assertTrue(application.isTimerRunning());
    }

    @MediumTest
    public void testStopStartButtons() {
        Button start = (Button) activity.findViewById(R.id.start_button);
        Button stop = (Button) activity.findViewById(R.id.stop_button);
        IOnYourBike application = ((IOnYourBike) activity.getApplication());

        //getInstrumentation().callActivityOnStart(activity);
        //getInstrumentation().runOnMainSync(new Runnable() {
        //    public void run() {
        //        activity.startActivity(new Intent(activity, TimerActivity.class));
        //    }
        //});

        assertFalse(application.isTimerRunning());

        // need to make sure the device is not locked when running this test
        // Here is some code that can be called to disable the screen lock,
        // and can be called in the activity onCreate()
        // Make sure following permission is set
        //<uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
        //KeyguardManager keyguardManager= (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        //KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("TimerActivityTest");
        //lock.disableKeyguard();

        TouchUtils.clickView(this, start);
        assertTrue(application.isTimerRunning());
        assertFalse("Start button is not enabled", start.isEnabled());
        assertTrue("Stop button is enabled", stop.isEnabled());

        TouchUtils.clickView(this, stop);
        assertFalse(application.isTimerRunning());
        assertTrue("Start button is enabled", start.isEnabled());
        assertFalse("Stop button is not enabled", stop.isEnabled());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        activity = null;
    }
}
