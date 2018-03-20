package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by gautamkarnik on 2015-06-10.
 */
public class Trips {

    public static ArrayList<Trip> getAll(SQLiteHelper helper,
                                         SQLiteDatabase database) {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        Cursor cursor = database.rawQuery("select * from trips;", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Trip route = cursorToTrip(cursor);
            trips.add(route);
            cursor.moveToNext();
        }

        cursor.close();

        return trips;
    }

    public static ArrayList<Trip> getAllInRoute(SQLiteHelper helper,
                                                SQLiteDatabase database, long route_id) {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        Cursor cursor = database.rawQuery(
                "select * from trips where route_id = " + route_id + ";", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Trip trip = cursorToTrip(cursor);
            trips.add(trip);
            cursor.moveToNext();
        }

        cursor.close();

        Log.d("Trips: ", trips.toString());

        return trips;
    }

    private static Trip cursorToTrip(Cursor cursor) {
        Trip trip = new Trip();
        trip.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        trip.timeStarted = cursor.getLong(cursor.getColumnIndex("timeStarted"));
        trip.timeTaken = cursor.getLong(cursor.getColumnIndex("timeTaken"));

        return trip;
    }

}
