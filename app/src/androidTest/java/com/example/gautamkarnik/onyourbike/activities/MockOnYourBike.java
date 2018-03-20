package com.example.gautamkarnik.onyourbike.activities;

import android.test.mock.MockApplication;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Settings;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by gautamkarnik on 2015-07-12.
 */
public class MockOnYourBike extends MockApplication implements IOnYourBike {
    private boolean running;

    @Override
    public void checkBattery() {

    }

    @Override
    public void startTimer(Trip trip) {
        running = true;
    }

    @Override
    public void startSearching(Trip trip) {

    }

    @Override
    public void stopTimer() {
        running = false;
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void setMap(GoogleMap map) {

    }

    @Override
    public boolean isTimerRunning() {
        return running;
    }

    @Override
    public String timerDisplay() {
        return "0:00:00";
    }

    @Override
    public Settings getSettings() {
        return new Settings();
    }

    @Override
    public void setSettings(Settings settings) {

    }

    @Override
    public SQLiteHelper getSQLiteHelper() {
        return null;
    }

    @Override
    public String notifyCheck() {
        return null;
    }

    @Override
    public void vibrateCheck() {

    }
}
