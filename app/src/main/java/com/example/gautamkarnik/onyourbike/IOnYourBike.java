package com.example.gautamkarnik.onyourbike;

import android.content.ComponentCallbacks2;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Settings;
import com.example.gautamkarnik.onyourbike.model.TimerState;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by gautamkarnik on 2015-07-12.
 */
public interface IOnYourBike {
    void startTimer(Trip trip);
    void startSearching(Trip trip);
    void stopTimer();
    void stopSearching();
    void setMap(GoogleMap map);
    boolean isTimerRunning();
    String timerDisplay();
    Settings getSettings();
    void setSettings(Settings settings);
    SQLiteHelper getSQLiteHelper();
    String notifyCheck();
    void vibrateCheck();
    void checkBattery();
}
