package com.example.gautamkarnik.onyourbike.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gautamkarnik.onyourbike.model.Coordinate;
import com.example.gautamkarnik.onyourbike.model.Route;
import com.example.gautamkarnik.onyourbike.model.Trip;

/**
 * Created by gautamkarnik on 2015-04-04.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static String CLASS_NAME;

    private static final String DATABASE_NAME = "OnYourBike.db";
    private static final int DATABASE_VERSION = 20;
    static private int numAsyncCalls = 0;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CLASS_NAME = getClass().getName();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(CLASS_NAME, "onCreate");

        database.beginTransaction();
        try {
            onConfigure(database); //might be wrong or might not get executed
            Route.create(database);
            Trip.create(database);
            Coordinate.create(database);
            database.setTransactionSuccessful();
        } catch (SQLiteException error) {
            Log.e(CLASS_NAME, "SQLite error " + error.getMessage());
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d(CLASS_NAME, "onUpgrade");

        database.beginTransaction();
        try {
            Coordinate.drop(database);
            Trip.drop(database);
            Route.drop(database);
            onCreate(database);
            database.setTransactionSuccessful();
        } catch (SQLiteException error) {
            Log.e(CLASS_NAME, "SQLite error " + error.getMessage());
        } finally {
            database.endTransaction();
        }

    }

    public void onConfigure(SQLiteDatabase database) {
        Log.d(CLASS_NAME, "onConfigure");
        database.execSQL("pragma foreign_keys=ON;");
    }

    public SQLiteDatabase open() {
        Log.d(CLASS_NAME, "open");
        SQLiteDatabase database = getWritableDatabase();
        return database;

    }

    public void create() {
        Log.d(CLASS_NAME, "create");
        open();
    }

    public void execAsyncSQL(SQLiteDatabase database, String sql) {
        Log.d(CLASS_NAME, "execAsyncSQL");

        numAsyncCalls++;
        new AsyncDatabase(database).execute(sql);
    }

    public void execAsyncSQL(SQLiteDatabase database, String... sqls) {
        Log.d(CLASS_NAME, "execAsyncSQL");

        numAsyncCalls++;
        new AsyncDatabase(database).execute(sqls);
    }

    private class AsyncDatabase extends AsyncTask<String, Void, Void> {
        private final SQLiteDatabase database;

        public AsyncDatabase(SQLiteDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(String... sqls) {
            Log.d(CLASS_NAME, "doInBackground");

            int length = sqls.length;

            if (length == 1) {
                database.execSQL(sqls[0]);
            } else {
                database.beginTransaction();
                try {
                    for (int i = 0; i < length; i++) {
                        database.execSQL(sqls[i]);
                    }
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(CLASS_NAME, "onPostExecute");

            numAsyncCalls--;
            if (numAsyncCalls == 0) {
                database.close();
            }
        }
    }


}
