package com.example.gautamkarnik.onyourbike.unit;

import android.test.suitebuilder.annotation.SmallTest;

import com.example.gautamkarnik.onyourbike.unit.TimerStateTestable;

import junit.framework.TestCase;

/**
 * Created by gautamkarnik on 2015-07-09.
 */
public class TimerStateTests extends TestCase {

    private TimerStateTestable timer;

    public TimerStateTests() {

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        timer = new TimerStateTestable();
    }

    private void delay() {
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
             // do nothing
         }
    }

    @SmallTest
    public void testInitialState() throws Exception {
        assertTrue("ElapsedTime is 0", timer.elapsedTime() == 0);
        assertTrue("Seconds is 0", timer.seconds() == 0);
        assertTrue("Minutes is 0", timer.minutes() == 0);
        assertTrue("Hours is 0", timer.hours() == 0);
        assertTrue("IsRunning is false", timer.isRunning() == false);
    }

    @SmallTest
    public void testTimerStarted() throws Exception {
        timer.start();
        assertTrue("IsRunning is true", timer.isRunning() == true);
        timer.setElapsedTime(1000);
        assertTrue("ElapsedTime is not 0", timer.elapsedTime() > 0);
    }

    @SmallTest
    public void testTimerStopped() throws Exception {
        timer.start();
        assertTrue("IsRunning is true", timer.isRunning() == true);
        timer.setElapsedTime(1000);
        assertTrue("ElapsedTime is not 0", timer.elapsedTime() > 0);
        timer.stop();
        assertTrue("IsRunning is true", timer.isRunning() == false);
        assertTrue("ElapsedTime is not 0", timer.elapsedTime() > 0);

    }

    @SmallTest
    public void testTimerReset() throws Exception {
        timer.start();
        assertTrue("IsRunning is true", timer.isRunning() == true);
        timer.setElapsedTime(1000);
        assertTrue("ElapsedTime is not 0", timer.elapsedTime() > 0);
        timer.reset();
        assertTrue("ElapsedTime is 0", timer.elapsedTime() == 0);
        assertTrue("Seconds is 0", timer.seconds() == 0);
        assertTrue("Minutes is 0", timer.minutes() == 0);
        assertTrue("Hours is 0", timer.hours() == 0);
        assertTrue("IsRunning is false", timer.isRunning() == false);
    }

    @SmallTest
    public void testDisplayTime() {
        timer.start();
        timer.setElapsedTime(2*1000*60*60 + 25*1000*60 + 7*1000);
        timer.stop();
        assertTrue("Time is correct", timer.display().equals("2:25:07"));
    }

    @SmallTest
    public void testOneMinute() {
        timer.start();
        timer.setElapsedTime(1000 * 60);
        timer.stop();
        assertTrue("One minute has passed", timer.display().equals("0:01:00"));
    }

    @SmallTest
    public void testOneHour() {
        timer.start();
        timer.setElapsedTime(1000 * 60 * 60);
        timer.stop();
        assertTrue("One hour has passed", timer.display().equals("1:00:00"));
    }

    @SmallTest
    public void testTenHours() {
        timer.start();
        timer.setElapsedTime(10 * 1000 * 60 * 60);
        timer.stop();
        assertTrue("Ten hours has passed", timer.display().equals("10:00:00"));
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        timer = null;
    }

}
