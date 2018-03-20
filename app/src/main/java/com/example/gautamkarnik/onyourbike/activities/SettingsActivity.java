package com.example.gautamkarnik.onyourbike.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.Toaster;
import com.example.gautamkarnik.onyourbike.model.Settings;


public class SettingsActivity extends BaseActivity {

    private static String CLASS_NAME;

    private CheckBox vibrate;
    private CheckBox stayAwake;

    public SettingsActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CLASS_NAME, "onCreate");
        setContentView(R.layout.activity_settings);
        vibrate = (CheckBox) findViewById(R.id.vibrate_checkbox);
        stayAwake = (CheckBox) findViewById(R.id.awake_checkbox);
        Settings settings = ((IOnYourBike)getApplication()).getSettings();
        vibrate.setChecked(settings.isVibrateOn(this));
        stayAwake.setChecked(settings.isCaffeinated(this));
        setupActionBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(CLASS_NAME, "onStop");
        Settings settings = ((IOnYourBike)getApplication()).getSettings();
        settings.setVibrate(this, vibrate.isChecked());
        settings.setCaffeinated(this, stayAwake.isChecked());
    }

    public void vibrateChanged(View view) {
        Toaster toast = new Toaster(getApplicationContext());

        if (vibrate.isChecked()) {
            toast.make(R.string.vibrate_on);
        } else {
            toast.make(R.string.vibrate_off);
        }
    }

    public void awakeChanged(View view) {
        Toaster toast = new Toaster(getApplicationContext());

        if (stayAwake.isChecked()) {
            toast.make(R.string.awake_on);
        } else {
            toast.make(R.string.awake_off);
        }
    }

}
