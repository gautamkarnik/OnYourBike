package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by gautamkarnik on 2015-06-10.
 */
public class Coordinate {
    private long _id;
    public double latitude;
    public double longitude;
    public long timeAt;

    public Coordinate(double latitude, double longitude, long timeAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeAt = timeAt;
    }

    public static void create(SQLiteDatabase database) {
        String createTable = "create table if not exists coordinates ("
                + "_id integer primary key autoincrement, "
                + "trip_id integer references trips(_id), "
                + "latitude real not null, "
                + "longitude real not null, "
                + "timeAt long not null);";

        database.execSQL(createTable);
    }

    public static void drop(SQLiteDatabase database) {
        String dropTable = "drop table if exists coordinates;";

        database.execSQL(dropTable);
    }

    public void update(SQLiteDatabase database) {
        String updateRow = "update coordinates set latitude = " + latitude
                + ", longitude = " + longitude + ", timeAt = " + timeAt
                + " where _id = " + _id + ";";

        database.execSQL(updateRow);
    }

    public void insert(SQLiteDatabase database, long trip_id) {
        String insertRow = "insert into coordinates "
                + "(trip_id, latitude, longitude, timeAt) values " + "( "
                + trip_id + ", " + latitude + ", " + longitude + ", " + timeAt + " );";

        database.beginTransaction();
        try {
            database.execSQL(insertRow);

            Cursor cursor = database.rawQuery(
                    "select last_insert_rowid() from coordinates;", null);
            cursor.moveToFirst();
            _id = cursor.getLong(0);
            cursor.close();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void delete(SQLiteDatabase database) {
        String removeRow = "delete from coordinates where _id = " + _id + ";";
        _id = -1;
        database.execSQL(removeRow);
    }

}
