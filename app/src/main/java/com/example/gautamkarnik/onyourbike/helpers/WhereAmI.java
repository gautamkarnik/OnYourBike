package com.example.gautamkarnik.onyourbike.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by gautamkarnik on 2015-05-26.
 */
public class WhereAmI implements LocationListener, GpsStatus.Listener {

    private static final long MIN_TIME = 10000; // 10 seconds
    private static final float MIN_DISTANCE = 20.0F;
    private static final float INITIAL_ZOOM = 15.0F;
    private static final float INACCURATE = 10; // 10 meters
    private static final float OLD_LOCATION_IMPORTANCE = 1;
    private static final float NEW_LOCATION_IMPORTANCE = 3; // weight of 2 originally
    private static final long TOO_OLD = 60000; // 60 seconds
    private static String CLASS_NAME;

    protected LocationManager locationManager;
    private Application application;
    private GoogleMap map;
    private Trip trip;
    private Marker you;
    private PolylineOptions rawPoints;
    private PolylineOptions correctedPoints;
    private boolean firstLocation;

    private double longitude = -1;
    private double latitude = -1;
    private long lastLocationTime;

    private SQLiteHelper helper;
    private SQLiteDatabase database;

    private int factor = 1;

    public WhereAmI() {
        CLASS_NAME = getClass().getName();
    }

    public void setMap(GoogleMap map) {
        Log.d(CLASS_NAME, "setMap");
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(CLASS_NAME, "onLocationChanged");
        Log.d(CLASS_NAME, "latitude : " + Double.toString(location.getLatitude()));
        Log.d(CLASS_NAME, "longitude : " + Double.toString(location.getLongitude()));
        Log.d(CLASS_NAME, "accuracy : " + Float.toString(location.getAccuracy()));

        long now = System.currentTimeMillis();
        LatLng rawLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng oldLatLong = new LatLng(latitude, longitude);

        longitude = calcLongitude(location, longitude);
        latitude = calcLatitude(location, latitude);

        LatLng newLatLong = new LatLng(latitude, longitude);

        rawPoints.add(rawLatLong);
        correctedPoints.add(newLatLong);

        if (map != null) {
            if (firstLocation) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLong, INITIAL_ZOOM));
                firstLocation = false;
            } else {
                map.animateCamera(CameraUpdateFactory.newLatLng(newLatLong));
            }
        }

        if (you != null) {
            you.remove();
        }

        if (map != null) {
            you = map.addMarker(new MarkerOptions().position(newLatLong)
                    .title("I am here").draggable(false));
            map.addPolyline(rawPoints);
            map.addPolyline(correctedPoints);
        }

        if (!database.isOpen()) {
            database = helper.open();
        }

        trip.addCoordinate(database, newLatLong.latitude, newLatLong.longitude, now);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(CLASS_NAME, "onStatusChanged");
        switch(status) {
            case LocationProvider.AVAILABLE:
                Log.d(CLASS_NAME, provider + " available");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d(CLASS_NAME, provider + " unavailable");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d(CLASS_NAME, provider + " out of service");
                break;
            default:
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(CLASS_NAME, "onProviderEnabled " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(CLASS_NAME, "onProviderDisabled " + provider);
    }

    public void startSearching(Application application, Trip trip) {
        Log.d (CLASS_NAME, "startSearching");
        this.application = application;
        this.trip = trip;

        firstLocation = true;
        rawPoints = new PolylineOptions();
        rawPoints.color(Color.RED);
        correctedPoints = new PolylineOptions();
        correctedPoints.color(Color.GREEN);

        if (application != null) {
            locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME * factor,
                        MIN_DISTANCE * factor,
                        this);
                locationManager.addGpsStatusListener(this);
            } else {
                Log.w(CLASS_NAME, "GPS Provider not enabled");
                // monkey testing found a bug here, can't be application, has to be activity
                // due to the integrated nature of this class, don't feel like fixing it.
                //AlertDialog.Builder alert = new AlertDialog.Builder(application);
                //alert.setIcon(android.R.attr.alertDialogIcon)
                //            .setTitle(application.getResources().getText(R.string.gps_title))
                //            .setMessage(application.getResources().getText(R.string.gps_settings))
                //            .setPositiveButton(android.R.string.yes, new DialogClick())
                //            .setNegativeButton(android.R.string.no, null).show();

                // the the onDialog Click here instead as a quick fix
                Toaster toaster = new Toaster((application.getApplicationContext()));
                toaster.make(R.string.gps_settings);
                Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                application.startActivity(settings);

            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME * factor,
                        MIN_DISTANCE * factor,
                        this);
            } else {
                Log.w(CLASS_NAME, "Network provider not enabled");
            }

            helper = ((IOnYourBike) application).getSQLiteHelper();
            database = helper.open();
        }
    }

    public void stopSearching() {
        firstLocation = false;

        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
        }

        application = null;
        map = null;

        trip = null;
    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.d(CLASS_NAME, "GPS First Fix");
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                Log.d(CLASS_NAME, "GPS Started");
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.d(CLASS_NAME, "GPS Stopped");
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                // too many, don't log
                break;
            default:
                break;
        }
    }

    private class DialogClick implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            application.startActivity(settings);
        }
    }

    private double calcLongitude(Location location, double oldLongitude) {
        Log.d(CLASS_NAME, "calcLongitude");

        double newLongitude = oldLongitude;

        if (newLongitude == -1) {
            newLongitude = location.getLongitude();
        }

        if (location.getAccuracy() <= INACCURATE * factor) {
            newLongitude = (OLD_LOCATION_IMPORTANCE * oldLongitude
                    + NEW_LOCATION_IMPORTANCE * location.getLongitude())
                    / (OLD_LOCATION_IMPORTANCE + NEW_LOCATION_IMPORTANCE);
            lastLocationTime = System.currentTimeMillis();
        } else {
            Log.d(CLASS_NAME, "longitude discarded");
        }

        if ((System.currentTimeMillis() - lastLocationTime) >= TOO_OLD * factor) {
            Log.d(CLASS_NAME, "longitude too old");
            newLongitude = location.getLongitude();
        }

        return newLongitude;
    }

    private double calcLatitude(Location location, double oldLatitude) {
        Log.d(CLASS_NAME, "calcLatitude");

        double newLatitude = oldLatitude;

        if (newLatitude == -1 ) {
            newLatitude = location.getLatitude();
        }

        if (location.getAccuracy() <= INACCURATE * factor) {
            newLatitude = (OLD_LOCATION_IMPORTANCE * oldLatitude
                    + NEW_LOCATION_IMPORTANCE * location.getLatitude())
                    / (OLD_LOCATION_IMPORTANCE + NEW_LOCATION_IMPORTANCE);
            lastLocationTime = System.currentTimeMillis();
        } else {
            Log.d(CLASS_NAME, "latitude discarded");
        }

        if ((System.currentTimeMillis() - lastLocationTime) >= TOO_OLD * factor) {
            Log.d(CLASS_NAME, "latitude too old");
            newLatitude = location.getLatitude();
        }

        return newLatitude;
    }

    public void savePower() {
        Log.d(CLASS_NAME, "savePower");
        setFactor(5);
    }

    public void normalPower() {
        Log.d(CLASS_NAME, "normalPower");
        setFactor(1);
    }

    private void setFactor(int newFactor) {
        Log.d(CLASS_NAME, "setFactor");
        if (factor != newFactor ) {
            factor = newFactor;
            stopSearching();
            startSearching(application, trip);
        }
    }
}
