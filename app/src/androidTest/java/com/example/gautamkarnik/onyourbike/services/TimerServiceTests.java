package com.example.gautamkarnik.onyourbike.services;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.example.gautamkarnik.onyourbike.activities.MockOnYourBike;
import com.example.gautamkarnik.onyourbike.services.TimerService;

/**
 * Created by gautamkarnik on 2015-07-13.
 */
public class TimerServiceTests extends ServiceTestCase<TimerService> {

    public TimerServiceTests() {
        super(TimerService.class);
    }

    public TimerServiceTests(Class<TimerService> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setApplication(new MockOnYourBike());
    }

    @MediumTest
    public void testStart() {
        Intent intent = new Intent(getContext(), TimerService.class);

        assertNull(getService());

        startService(intent);
        assertTrue(getService().isRunning());

        startService(intent); // no effect
        assertTrue(getService().isRunning());

        assertNotNull(getService().getTimer());
    }

    @MediumTest
    public void testShutdown() {
        Intent intent = new Intent(getContext(), TimerService.class);

        assertNull(getService());

        startService(intent);
        assertTrue(getService().isRunning());

        shutdownService();
        assertFalse(getService().isRunning());

    }



    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
