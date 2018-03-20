package com.example.gautamkarnik.onyourbike.model;

import android.util.Log;

import com.example.gautamkarnik.onyourbike.utils.Time;

/**
 * Created by gautamkarnik on 2015-04-02.
 */
public class TimerState {
    public static String CLASS_NAME;
    private static boolean timerRunning;
    private static long startedAt;
    private static long lastStopped;
    private static long lastTime;

    protected Time time;

    public TimerState() {
        CLASS_NAME = getClass().getName();
        time = new Time();
        reset();
    }

    public void reset() {
        timerRunning = false;
        lastStopped = time.now();
        startedAt = time.now();
    }

    public void start() {
        timerRunning = true;
        startedAt = time.now();
    }

    public void stop() {
        timerRunning = false;
        lastStopped = time.now();
    }

    public boolean isRunning() {
        return timerRunning;
    }

    public String display() {
        String display;

        long diff;
        long seconds;
        long minutes;
        long hours;

        Log.d(CLASS_NAME, "Setting time display");

        diff = elapsedTime();

        // no negative time
        if (diff < 0 ) { diff = 0; }

        seconds = diff / 1000;
        minutes = seconds / 60;
        hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;

        display = String.format("%d", hours) + ":"
                + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);

        return display;
    }

    public long elapsedTime() {
        long timeNow;

        if(this.isRunning()) {
            timeNow = time.now();
        } else {
            timeNow = lastStopped;
        }
        lastTime = timeNow - startedAt;
        return lastTime;
    }

    public long seconds() {
        return (lastTime/1000) % 60;
    }

    public long minutes () {
        return (lastTime/1000/60) % 60;
    }

    public long hours() {
        return(lastTime/1000/60/60);
    }
}
