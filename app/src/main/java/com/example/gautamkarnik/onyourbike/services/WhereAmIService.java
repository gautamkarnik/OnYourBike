package com.example.gautamkarnik.onyourbike.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.helpers.WhereAmI;

/**
 * Created by gautamkarnik on 2015-06-30.
 */
public class WhereAmIService extends Service {
    private static String CLASS_NAME;

    private final IBinder binder;
    private WhereAmI whereAmI;

    public WhereAmIService() {
        CLASS_NAME = getClass().getName();
        whereAmI = new WhereAmI();
        binder = new WhereAmIBinder();
    }

    public WhereAmI getWhereAmI() {
        return whereAmI;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(CLASS_NAME, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(CLASS_NAME, "OnCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(CLASS_NAME, "onDestroy");
        whereAmI = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class WhereAmIBinder extends Binder {
        public WhereAmIService getService() {
            return WhereAmIService.this;
        }
    }

}
