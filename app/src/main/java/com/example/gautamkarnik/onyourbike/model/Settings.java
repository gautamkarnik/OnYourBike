package com.example.gautamkarnik.onyourbike.model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by gautamkarnik on 2015-04-01.
 */
public class Settings {
    private static String CLASS_NAME;
    private static String VIBRATE = "vibrate";
    private static String STAYAWAKE = "stayawake";
    private static String LICENSE = "license";

    protected boolean vibrateOn;
    protected boolean stayAwake;
    protected boolean licenseOk;

    public Settings () {
        CLASS_NAME = getClass().getName();
    }

    public boolean isVibrateOn(Activity activity) {
        Log.d(CLASS_NAME, "isVibrateOn");
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        if(preferences.contains(VIBRATE)) {
            this.vibrateOn = preferences.getBoolean(VIBRATE, false);
        }
        return this.vibrateOn;
    }

    public void setVibrate(Activity activity, boolean vibrate){
        Log.d(CLASS_NAME, "setVibrate");
        Log.i(CLASS_NAME, "Setting vibrate to " + vibrate);
        this.vibrateOn = vibrate;
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(VIBRATE, this.vibrateOn);
        editor.apply();
    }


    public boolean isCaffeinated(Activity activity) {
        Log.d(CLASS_NAME, "isCaffeinated");

        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        if(preferences.contains(STAYAWAKE)) {
            this.stayAwake = preferences.getBoolean(STAYAWAKE, false);
        }
        return this.stayAwake;
    }

    public void setCaffeinated(Activity activity, boolean stayawake) {
        Log.d(CLASS_NAME, "setCaffeinated");
        Log.i(CLASS_NAME, "Setting stay awake to " + stayawake);
        this.stayAwake = stayawake;
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(STAYAWAKE, this.stayAwake);
        editor.apply();
    }

    public boolean isLicenseOk(Activity activity) {
        Log.d(CLASS_NAME, "isLicenseOk");
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        if(preferences.contains(LICENSE)) {
            this.licenseOk = preferences.getBoolean(LICENSE, false);
        }
        return this.licenseOk;
    }

    public void setLicenseOk(Activity activity, boolean licenseOk) {
        Log.d(CLASS_NAME, "setLicenseOk");
        this.licenseOk = licenseOk;
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(LICENSE, this.licenseOk);
        editor.apply();
    }

}
