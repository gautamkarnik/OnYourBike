package com.example.gautamkarnik.onyourbike.activities;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Coordinate;
import com.example.gautamkarnik.onyourbike.model.Coordinates;
import com.example.gautamkarnik.onyourbike.model.Route;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends BaseActivity {

    static private String CLASS_NAME;
    private GoogleMap map;
    //private WhereAmI whereAmI;

    private Route route;
    private Trip trip;

    private SQLiteHelper helper;
    private SQLiteDatabase database;

    private long trip_id = -1;

    public MapActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("trip_id")) {
            trip_id = extras.getLong("trip_id");
        } else {
            trip_id = -1;
        }

        Log.d(CLASS_NAME, "Trip ID: " + trip_id);

        if (available == ConnectionResult.SERVICE_MISSING
                || available == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
                || available == ConnectionResult.SERVICE_DISABLED) {

            Log.e(CLASS_NAME, "Unable to access Google Play services");

            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(available,
                    this, 0);

            dialog.show();

        } else {
            setContentView(R.layout.activity_map);

            map = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (map != null) {
                //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                map.setMyLocationEnabled(true);
            }
            setupActionBar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        helper = ((IOnYourBike) getApplication()).getSQLiteHelper();
        database = helper.open();

        if (trip_id == -1) {
            trackMe();
        } else {
            showTrip();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //if (whereAmI != null) {
        //    whereAmI.stopSearching();
        //    whereAmI = null;
        //}
        IOnYourBike application = ((IOnYourBike) getApplication());
        application.stopSearching();

    }

    private void trackMe() {
        route = new Route();
        Time time = new Time();
        time.setToNow();
        route.name = "Route: " + time.toString().hashCode();
        trip = new Trip(route.getId());
        trip.timeStarted = System.currentTimeMillis();
        route.addTrip(trip);
        route.insert(database);

        /*whereAmI = new WhereAmI();
        whereAmI.setMap(map);
        whereAmI.startSearching(this, trip);
        */
        IOnYourBike application = (IOnYourBike) getApplication();
        application.setMap(map);
        application.startSearching(trip);
    }

    private void showTrip() {

        Log.d(CLASS_NAME, "showTrip()");

        ArrayList<Coordinate> coordinates = Coordinates.getAllForTrip(helper, database, trip_id);
        PolylineOptions points = new PolylineOptions();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        points.color(Color.RED);

        Log.d(CLASS_NAME, "Coordinate: " + coordinates);

        for (Coordinate coordinate : coordinates) {
            LatLng latLong = new LatLng(coordinate.latitude,
                    coordinate.longitude);

            points.add(latLong);
            builder.include(latLong);
        }

        if (map != null && coordinates.size() > 0) {
            //map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
            map.setOnCameraChangeListener((new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    // 20 is padding in px
                    map.moveCamera((CameraUpdateFactory.newLatLngBounds(builder.build(), 20)));
                    map.setOnCameraChangeListener(null);
                }
            }));
            map.addPolyline(points);
        }


    }

}
