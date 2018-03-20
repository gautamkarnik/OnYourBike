package com.example.gautamkarnik.onyourbike.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Handler;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.Camera;
import com.example.gautamkarnik.onyourbike.model.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TimerActivity extends BaseActivity {

    static String CLASS_NAME;
    private static long UPDATE_EVERY = 200;
    protected TextView counter;
    protected Button start;
    protected Button stop;
    //private static TimerState timer;

    private Camera camera;
    private Button takePhoto;

    protected Handler handler;
    protected UpdateTimer updateTimer;

    private IOnYourBike application;

    private AdView adView;

    public TimerActivity() {
        CLASS_NAME = getClass().getName();
        canGoHome = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(CLASS_NAME, "onCreate");

        setContentView(R.layout.activity_timer);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        counter = (TextView) findViewById(R.id.timer);
        start = (Button) findViewById(R.id.start_button);
        stop = (Button) findViewById(R.id.stop_button);

        //timer = ((OnYourBike) getApplication()).getTimerState();
        application = (IOnYourBike) getApplication();

        stayAwakeOrNot();

        takePhoto = (Button) findViewById(R.id.photo_button);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(CLASS_NAME, "Showing menu.");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(CLASS_NAME, "onStart");

        camera = new Camera(this);

        if (application.isTimerRunning()) {
            handler = new Handler();
            updateTimer = new UpdateTimer(this);
            handler.postDelayed(updateTimer, UPDATE_EVERY);
        }

        if (!camera.hasCamera()) {
            takePhoto.setVisibility(View.GONE);
        }

        if(!camera.hasCameraApplication()) {
            takePhoto.setEnabled(false);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(CLASS_NAME, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(CLASS_NAME, "onResume");

        enableButtons();
        setTimeDisplay();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(CLASS_NAME, "onStop");

        Settings settings = ((IOnYourBike) getApplication()).getSettings();

        camera = null;

        if (application.isTimerRunning()) {
            handler.removeCallbacks(updateTimer);
            updateTimer = null;
            handler = null;
        }

        if(settings.isCaffeinated(this)) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,CLASS_NAME);

            if (wakeLock.isHeld()){
                wakeLock.release();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(CLASS_NAME, "onDestroy");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(CLASS_NAME, "onRestart");
    }


    protected void enableButtons() {
        Log.d(CLASS_NAME, "Set buttons enabled/disabled");
        start.setEnabled(!application.isTimerRunning());
        stop.setEnabled(application.isTimerRunning());
    }

    protected void setTimeDisplay() {
        counter.setText(application.timerDisplay());
    }

    public void clickedStart(View view) {
        Log.d(CLASS_NAME, "Clicked start button");

        application.startTimer(null);
        //application.startTimer(new Trip());
        enableButtons();

        handler = new Handler();
        updateTimer = new UpdateTimer(this);
        handler.postDelayed(updateTimer, UPDATE_EVERY);

    }

    public void clickedStop(View view) {
        Log.d(CLASS_NAME, "Clicked stop button");

        application.stopTimer();
        enableButtons();

        handler.removeCallbacks(updateTimer);
        handler = null;
    }


    protected void stayAwakeOrNot() {
        Log.d(CLASS_NAME, "stayAwakeOrNot");
        Settings settings = ((IOnYourBike) getApplication()).getSettings();

        if (settings.isCaffeinated(this)) {
            Log.i(CLASS_NAME, "Staying awake");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            Log.i(CLASS_NAME, "Not staying awake");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    class UpdateTimer implements Runnable {

        Activity activity;

        public UpdateTimer (Activity activity) {
            this.activity = activity;
        }

        public void run() {
            // Log.d(CLASS_NAME, "run");

            Settings settings = ((IOnYourBike)getApplication()).getSettings();

            setTimeDisplay();
            if (application.isTimerRunning()) {
                if (settings.isVibrateOn(activity)) {
                    vibrateCheck();
                }
                notifyCheck();
            }

            stayAwakeOrNot();

            if(handler!=null) {
                handler.postDelayed(this, UPDATE_EVERY);
            }
        }

        private void notifyCheck() {

            String message = ((IOnYourBike) getApplication()).notifyCheck();

            if (message != null) {
                String title = getResources().getString(R.string.time_title);
                notification(title, message);
            }

        }

        protected void vibrateCheck() {
            ((IOnYourBike) getApplication()).vibrateCheck();
        }

    }

    public void takePhoto(View view) {
        Log.d(CLASS_NAME, "takePhoto");
        Intent photo = new Intent(this, PhotoActivity.class);
        startActivity(photo);
    }

    // Methods used for testing
    public boolean isCameraNull() {
        return camera == null;
    }

    public boolean isHandlerNull() {
        return handler == null;
    }

    public boolean isUpdateTimerNull() {
        return updateTimer == null;
    }
}
