package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautamkarnik on 2015-04-04.
 */
public class Route {
    private long _id;
    public String name;
    public String notes;
    public List<Trip> trips = new ArrayList<Trip>();

    public static void create(SQLiteDatabase database) {
        String createTable = "create table if not exists routes ("
                + "_id integer primary key autoincrement, "
                + "name text not null, "
                + "notes text not null)";
        String createIndex = "create unique index if not exists "
                + "pk_routes on routes(_id);";

        database.execSQL(createTable);
        database.execSQL(createIndex);
    }

    public static void drop(SQLiteDatabase database) {
        String dropTable = "drop table if exists routes;";
        String dropIndex = "drop index if exists pk_routes;";

        database.execSQL(dropIndex);
        database.execSQL(dropTable);
    }

    public void insert(SQLiteDatabase database) {
        String insertRow = "insert into routes "
                + "(name,notes) values "
                + "( '" + name + "', '" + notes + "' );";

        database.beginTransaction();
        try {
            database.execSQL(insertRow);

            Cursor cursor = database.rawQuery(
                    "select last_insert_rowid() from routes;", null);
            cursor.moveToFirst();
            _id = cursor.getLong(0);
            cursor.close();

            for(Trip trip : trips) {
                trip.insert(database, _id);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void update(SQLiteHelper helper, SQLiteDatabase database) {
        String updateRow = "update routes set name = '"
                + name + "', notes = '" + notes
                + "' where _id = " + _id + ";";

        database.beginTransaction();
        try {
            //database.execSQL(updateRow);
            helper.execAsyncSQL(database, updateRow);

            for (Trip trip : trips) {
                trip.update(helper, database);
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void delete(SQLiteHelper helper, SQLiteDatabase database) {
        String removeRow = "delete from routes where _id = " + _id + ";";

        //database.execSQL(removeRow);
        helper.execAsyncSQL(database, removeRow);

        _id = -1;
    }

    public void setId(int id) {
        _id = id;
    }

    public long getId() {
        return _id;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }
}
