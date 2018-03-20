package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautamkarnik on 2015-04-04.
 */
public class Trip {
    private long _id;
    public long timeStarted;
    public long timeTaken;
    private final List<Coordinate> coordinates = new ArrayList<Coordinate>();

    public Trip() {
        _id = -1;
    }

    public Trip(long id) {
        _id = id;
    }

    public void setId(int id) {
        _id = id;
    }

    public long getId() {
        return _id;
    }

    @Override
    public String toString() {
        //return DateFormat.getDateInstance().format(timeStarted) + " "
        //        + DateFormat.getTimeInstance().format(timeStarted);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return df.format(timeStarted);
    }

    public static void create(SQLiteDatabase database) {
        String createTable = "create table if not exists trips ("
                + "_id integer primary key autoincrement, "
                + "route_id integer references routes(_id), "
                + "timeStarted integer not null, "
                + "timeTaken integer not null)";
        String createIndex = "create unique index if not exists "
                + "pk_trips on trips(_id);";
        String createForeignIndex = "create index if not exists "
                + "fk_trips_routes on trips(route_id);";

        database.execSQL(createTable);
        database.execSQL(createIndex);
        database.execSQL(createForeignIndex);
    }

    public static void drop(SQLiteDatabase database) {
        String dropTable = "drop table if exists trips;";
        String dropIndex = "drop index if exists pk_trips;";
        String dropForeignIndex = "drop index if exists fk_trips_routes;";

        database.execSQL(dropForeignIndex);
        database.execSQL(dropIndex);
        database.execSQL(dropTable);
    }


    public void insert(SQLiteDatabase database, long route_id) {
        String insertRow = "insert into trips"
                + "(route_id, timeStarted, timeTaken) values " + "(" + route_id
                + ", " + timeStarted + ", " + timeTaken + " );";

        database.beginTransaction();
        try {
            database.execSQL(insertRow);

            Cursor cursor = database.rawQuery(
                    "select last_insert_rowid() from trips;", null);
            cursor.moveToFirst();
            _id = cursor.getLong(0);
            cursor.close();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

    }

    public void update(SQLiteHelper helper, SQLiteDatabase database) {
        String updateRow = "update trips set timeStarted = " + timeStarted
                + ", timeTaken = " + timeTaken + " where _id = " + _id + ";";
        //database.execSQL(updateRow);
        helper.execAsyncSQL(database, updateRow);
    }

    public void delete(SQLiteHelper helper, SQLiteDatabase database) {
        String removeRow = "delete from trips where _id = " + _id + ";";
        //database.execSQL(removeRow);
        helper.execAsyncSQL(database, removeRow);
        _id = -1;
    }

    public void addCoordinate(SQLiteDatabase database,
                              double latitude,
                              double longitude,
                              long timeAt) {
        Coordinate coordinate = new Coordinate(latitude, longitude, timeAt);
        coordinate.insert(database, _id);
        coordinates.add(coordinate);
    }

}
