package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by gautamkarnik on 2015-06-11.
 */
public class Coordinates {

    public static ArrayList<Coordinate> getAllForTrip(SQLiteHelper helper,
                                                      SQLiteDatabase database,
                                                      long trip_id) {
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

        Cursor cursor = database.rawQuery(
                "select * from coordinates where trip_id = " + trip_id
                        + " order by _id;", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Coordinate coordinate = cursorToCoordinate(cursor);
            coordinates.add(coordinate);
            cursor.moveToNext();
        }

        cursor.close();

        return coordinates;
    }

    private static Coordinate cursorToCoordinate(Cursor cursor) {
        double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        long timeAt = cursor.getLong(cursor.getColumnIndex("timeAt"));

        Coordinate coordinate = new Coordinate(latitude, longitude, timeAt);

        return coordinate;
    }
}
