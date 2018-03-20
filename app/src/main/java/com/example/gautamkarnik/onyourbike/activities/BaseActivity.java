package com.example.gautamkarnik.onyourbike.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.BuildConfig;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.Camera;
import com.example.gautamkarnik.onyourbike.helpers.NightMode;
import com.example.gautamkarnik.onyourbike.helpers.Notify;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.ServerManagedPolicy;

/**
 * Created by gautamkarnik on 2015-05-26.
 */
public class BaseActivity extends ActionBarActivity {
// ActionBar activity extends Fragment Activity so only need to extend ActionBarActivity

    private static String CLASS_NAME;

    // strictly for licensing
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjy/GUIDgR6Pcf30HzC6ckpJIWFJdPKpkniSijLuwKUOo5vSqd1NnV2GeOYgby1416Lz0m0Qa6Y73IYmD7WssbUCVuTlcbstPSf3cMoEp7QGG7ICkrNHT1ti7sft29lKKHBpRh4C4EL6SBdnWlnP4DLnqxOil05/TntMTdlPMvGBEQ05pAc7GdtUEF7A7E0cCD2YiB+uwmwZUIuWzt/jVDU8du+/WbOkEHrod7VKhpwB8pO7e1wqh2AQ5y56f8K6M9qJNJ8+zPB/Rpag9MJfDy8ld7OJ8kWI0SZrzG3WaoqNTVR8o2ptgXmx3zYvrsPI157CkqPKHk6D85/e7wTkZ8wIDAQAB";
    private static final byte[] SALT = new byte[] {94,80,88,97,93,12,04,84,62,05,56,18,83,85,70,78,51,51,94,73};
    private Handler mHandler;
    private LicenseChecker mChecker;
    private LicenseCheckerCallback mLicenseCheckerCallback;
    boolean licensed;
    boolean checkingLicense;
    boolean didCheck;

    static boolean licenseOk;

    // for application
    private NightMode nightMode;
    private Notify notify;
    public boolean canGoHome = true;

    public BaseActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupStrictMode();

        int themeID = getIntent().getIntExtra("Theme", -1);
        if (themeID != -1) {
            setTheme(themeID);
        }

        boolean isLightTheme = getIntent().getBooleanExtra("isLightTheme", true);

        notify = new Notify(this);

        nightMode = new NightMode(getWindow(), this, true, isLightTheme);

        // disabling the keyguard lock for the emulator in order to run activity tests
        KeyguardManager keyguardManager= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("BaseActivity");
        lock.disableKeyguard();

        // license checking
        if(!com.example.gautamkarnik.onyourbike.BuildConfig.DEBUG) {
            licenseOk = ((IOnYourBike)getApplication()).getSettings().isLicenseOk(this);
            Log.d(CLASS_NAME, "License: " + licenseOk);
            if(licenseOk == false){
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                mHandler = new Handler();  // not needed in this example
                mLicenseCheckerCallback = new MyLicenseCheckerCallback();
                mChecker = new LicenseChecker(this,
                     new ServerManagedPolicy(this,
                            new AESObfuscator(SALT,
                                    getPackageName(),
                                    deviceId)),
                     BASE64_PUBLIC_KEY);

                Toast.makeText(this, "Checking application license...", Toast.LENGTH_SHORT).show();
                doCheck();
                Log.i(CLASS_NAME, "Checking license!");
            }

        } else {
            Log.d(CLASS_NAME, "Debug - skipping license");
            //Toast.makeText(this, "Skipping application license check...", Toast.LENGTH_SHORT).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void setupStrictMode() {
        // Make sure we do nothing silly!
        //if (com.example.gautamkarnik.onyourbike.BuildConfig.DEBUG) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                   .detectAll().penaltyLog().penaltyDeath().build());
        }

    }

    @Override
    public void onPause(){
        super.onPause();

        //nightMode.off();
        //nightMode.stopSensing();
    }

    @Override
    public void onResume() {
        super.onResume();

        //nightMode.on();
        //nightMode.startSensing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home:
                gotoHome();
                return true;
            case R.id.menu_settings:
                gotoSettings(null);
                return true;
            case R.id.menu_routes:
                gotoRoutes();
                return true;
            case R.id.menu_map:
                gotoMap();
                return true;
            case R.id.menu_photo:
                gotoPhoto();
                return true;
            case R.id.menu_share:
                shareActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void gotoSettings(View view) {
        Log.d(CLASS_NAME, "gotoSettings");
        Intent settingsIntent = new Intent(getApplication(), SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void gotoRoutes() {
        Log.d(CLASS_NAME, "gotoRoutes");
        Intent routesIntent = new Intent(getApplication(), RoutesActivity.class);
        startActivity(routesIntent);
    }

    public void gotoMap() {
        Log.d(CLASS_NAME, "gotoMap");
        Intent mapIntent = new Intent(getApplication(), MapActivity.class);
        startActivity(mapIntent);
    }


    public void notification(String title, String message) {
        notify.notify(title, message);
    }

    public void gotoHome() {
        Log.d(CLASS_NAME, "gotoHome");

        Intent timer = new Intent(getApplicationContext(), TimerActivity.class);
        timer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(timer);
    }

    public void goBack(View view) {
        finish();
    }

    //public void setupActionBar() {
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(canGoHome);
    //}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(canGoHome);
            }
        }
    }

    public void gotoPhoto() {
        Log.d(CLASS_NAME, "gotoPhoto");
        Intent photo = new Intent(this, PhotoActivity.class);
        startActivity(photo);
    }

    public void shareActivity() {
        Log.d(CLASS_NAME, "shareActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(CLASS_NAME, "onActivityResult");
        switch(requestCode) {
            case Camera.PHOTO_TAKEN:
                if (resultCode == RESULT_OK) {
                    PhotoActivity photoActivity = (PhotoActivity) this;
                    photoActivity.displayPhoto(intent);
                    //displayPhoto(intent);
                }
                break;
            default:
                Log.d(CLASS_NAME, "Unkown request code " + Integer.toString(requestCode));
                break;
        }
    }

    // Licensing functions

    private void doCheck() {

        didCheck = false;
        checkingLicense = true;
        setProgressBarIndeterminateVisibility(true);

        mChecker.checkAccess(mLicenseCheckerCallback);
    }


    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {

        private String CLASS_NAME = getClass().getName();

        public void allow(int reason) {
            // TODO Auto-generated method stub
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            Log.i(CLASS_NAME,"Accepted!");

            //You can do other things here, like saving the licensed status to a
            //SharedPreference so the app only has to check the license once.
            licensed = true;
            checkingLicense = false;
            didCheck = true;

            ((IOnYourBike)getApplication()).getSettings().setLicenseOk(BaseActivity.this, (licensed && didCheck));
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dontAllow(int reason) {
            // TODO Auto-generated method stub
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            Log.i(CLASS_NAME,"Denied!");
            Log.i(CLASS_NAME,"Reason for denial: "+reason);

            //You can do other things here, like saving the licensed status to a
            //SharedPreference so the app only has to check the license once.

            licensed = false;
            checkingLicense = false;
            didCheck = true;

            ((IOnYourBike)getApplication()).getSettings().setLicenseOk(BaseActivity.this, (licensed && didCheck));

            showDialog(0);

        }

        @SuppressWarnings("deprecation")
        @Override
        public void applicationError(int reason) {
            // TODO Auto-generated method stub
            Log.i(CLASS_NAME, "Error: " + reason);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            licensed = true;
            checkingLicense = false;
            didCheck = false;

            showDialog(0);
        }


    }

    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle("UNLICENSED APPLICATION DIALOG TITLE")
                .setMessage("This application is not licensed, please buy it from the play store.")
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://market.android.com/details?id=" + getPackageName()));
                        startActivity(marketIntent);
                        finish();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("Re-Check", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doCheck();
                    }
                })

                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener(){
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        Log.i("License", "Key Listener");
                        finish();
                        return true;
                    }
                })
                .create();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChecker!= null) {
            mChecker.onDestroy();
        }
    }

}
