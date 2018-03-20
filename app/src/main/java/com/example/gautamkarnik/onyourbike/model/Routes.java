package com.example.gautamkarnik.onyourbike.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautamkarnik on 2015-04-05.
 */
public class Routes {
    public static String CLASS_NAME;

    public Routes() {
        CLASS_NAME = getClass().getName();
    }

    public static List<Route> getAll(SQLiteHelper helper, SQLiteDatabase database) {
        List<Route> routes = new ArrayList<Route>();
        Cursor cursor = database.rawQuery("select * from routes;", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Route route = cursorToRoute(cursor);
            routes.add(route);
            cursor.moveToNext();
        }

        cursor.close();
        return routes;
    }

    private static Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        route.name = cursor.getString(cursor.getColumnIndex("name"));
        route.notes = cursor.getString(cursor.getColumnIndex("notes"));
        return route;
    }
}
