package com.example.gautamkarnik.onyourbike;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.helpers.Battery;
import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Settings;
import com.example.gautamkarnik.onyourbike.model.TimerState;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.example.gautamkarnik.onyourbike.receivers.BatteryCheck;
import com.example.gautamkarnik.onyourbike.services.TimerService;
import com.example.gautamkarnik.onyourbike.services.WhereAmIService;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by gautamkarnik on 2015-04-01.
 */
public class OnYourBike extends Application implements IOnYourBike {
    private static String CLASS_NAME;
    protected Settings settings;
    private SQLiteHelper helper;

    private Vibrator vibrate;
    private long lastSeconds = -1;

    private TimerService timerService;
    private WhereAmIService whereAmIService;

    private BatteryCheck batteryCheck;

    public OnYourBike() {
        CLASS_NAME = getClass().getName();
    }

    public TimerState getTimerState() {
        return timerService.getTimer();
    }

    @Override
    public void startTimer(Trip trip) {
        if (timerService != null) {
            timerService.getTimer().start();
        }

        if (trip != null) {
            startSearching(trip);
        }
    }

    @Override
    public void startSearching(Trip trip) {
        if(whereAmIService != null) {
            whereAmIService.getWhereAmI().startSearching(this, trip);
        }
    }

    @Override
    public void stopTimer() {
        if (timerService != null) {
            timerService.getTimer().stop();
        }
        stopSearching();
    }

    @Override
    public void stopSearching() {
        if (whereAmIService != null) {
            whereAmIService.getWhereAmI().stopSearching();
        }
    }

    @Override
    public void setMap(GoogleMap map) {
        if (whereAmIService != null) {
            whereAmIService.getWhereAmI().setMap(map);
        }
    }

    @Override
    public boolean isTimerRunning() {
        if (timerService != null) {
            return timerService.getTimer().isRunning();
        } else {
            return false;
        }
    }

    @Override
    public String timerDisplay() {
        if (timerService != null) {
            return timerService.getTimer().display();
        }
        return "0:00:00";
    }

    @Override
    public Settings getSettings() {
        if (settings == null){
            settings = new Settings();
        }
        return settings;
    }

    @Override
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void onCreate() {
        Log.d(CLASS_NAME, "Application: onCreate");

        super.onCreate();

        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrate == null) {
            Log.w(CLASS_NAME, "No vibration service exists.");
        }

        getSQLiteHelper().create();

        Intent service = new Intent(getApplicationContext(), TimerService.class);
        bindService(service, new TimerServiceConnection(), Context.BIND_AUTO_CREATE);

        Intent whereAmI = new Intent(getApplicationContext(), WhereAmIService.class);
        bindService(whereAmI, new WhereAmIServiceConnection(), Context.BIND_AUTO_CREATE);

        batteryCheck = new BatteryCheck(getApplicationContext());
        //checkBattery();
    }

    @Override
    public SQLiteHelper getSQLiteHelper() {
        if (helper == null) {
            helper = new SQLiteHelper(this);
        }
        return helper;
    }

    @Override
    public String notifyCheck() {
        long seconds;
        long minutes;
        long hours;

        Log.d(CLASS_NAME, "notifyCheck()");

        TimerState timer = timerService.getTimer();
        timer.elapsedTime();
        seconds = timer.seconds();
        minutes = timer.minutes();
        hours = timer.hours();

        if (seconds == 30) {
            String title = getResources().getString(R.string.time_title);
            String message = getResources().getString(R.string.time_running_message);

            if (hours == 0 && minutes == 0) {
                message = getResources().getString(R.string.time_start_message);
            }

            message = String.format(message, hours, minutes);
            return message;
        }

        lastSeconds = seconds;
        return null;

    }

    @Override
    public void vibrateCheck() {

        long seconds;
        long minutes;

        Log.d(CLASS_NAME, "vibrateCheck");

        TimerState timer = timerService.getTimer();
        timer.elapsedTime();
        seconds = timer.seconds();
        minutes = timer.minutes();

        try {
            if (vibrate != null && seconds == 0 && seconds != lastSeconds) {

                long[] once = {0, 100};
                long[] twice = {0, 100, 400, 100};
                long[] thrice = {0, 100, 400, 100, 400, 100};

                //every hour
                if (minutes == 0) {
                    Log.i(CLASS_NAME, "Vibrate 3 times");
                    vibrate.vibrate(thrice, -1);
                }
                // every 15 minutes
                else if (minutes % 15 == 0) {
                    Log.i(CLASS_NAME, "Vibrate 2 times");
                    vibrate.vibrate(twice, -1);
                }
                // every 1 minutes
                else if (minutes % 1 == 0) {
                    Log.i(CLASS_NAME, "Vibrate once");
                    vibrate.vibrate(once, -1);
                }
            }
        } catch (Exception e) {
            Log.w(CLASS_NAME, "Exception: " + e.getMessage());
        }

        lastSeconds = seconds;
    }

    private class TimerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(CLASS_NAME, "onServiceConnected");

            TimerService.TimerBinder binder = (TimerService.TimerBinder) service;
            timerService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(CLASS_NAME, "onServiceDisconnected");
            timerService = null;
        }
    }

    private class WhereAmIServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(CLASS_NAME, "onServiceConnected");

            WhereAmIService.WhereAmIBinder binder = (WhereAmIService.WhereAmIBinder) service;
            whereAmIService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(CLASS_NAME, "onServiceDisconnected");
            whereAmIService = null;
        }
    }

    @Override
    public void checkBattery() {
        Battery battery = new Battery();
        Context context = getApplicationContext();

        // if battery is critical shut off location tracking
        if (battery.isCritical(context)) {
            Intent whereAmIIntent = new Intent(getApplicationContext(), WhereAmIService.class);

            if (whereAmIService != null) {
                whereAmIService.getWhereAmI().stopSearching();
                whereAmIService.stopService(whereAmIIntent);
            }
        // if battery is low ltrack less often
        } else if (battery.isLow(context)) {
            if (whereAmIService != null) {
                whereAmIService.getWhereAmI().savePower();
            }
        // otherwise track normally
        } else {
            if (whereAmIService != null) {
                whereAmIService.getWhereAmI().normalPower();
            }
        }
    }

    // Methods used for testing
    public boolean isTimerServiceNull() {
        return timerService == null;
    }

    public boolean isWhereAmIServiceNull() {
        return whereAmIService == null;
    }

}
