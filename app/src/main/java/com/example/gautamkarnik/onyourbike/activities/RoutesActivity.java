package com.example.gautamkarnik.onyourbike.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Route;
import com.example.gautamkarnik.onyourbike.model.Routes;

import java.util.List;

public class RoutesActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static String CLASS_NAME;

    public RoutesActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteHelper helper = ((IOnYourBike) getApplication()).getSQLiteHelper();
        SQLiteDatabase database = helper.open();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        setupActionBar();

        List<Route> routes = Routes.getAll(helper, database);
        helper.close();

        ArrayAdapter<Route> adapter = new ArrayAdapter<Route>(this,android.R.layout.simple_list_item_1, routes);
        ListView listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
        //setListAdapter(adapter);

    }

    /*
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Route route = (Route) listView.getItemAtPosition(position);

        super.onListItemClick(listView, view, position, id);

        if (route != null) {
            Intent intent = new Intent(this, TripsActivity.class);
            intent.putExtra("route_id", route.getId());
            startActivity(intent);
        }
    }
    */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Route route = (Route) parent.getItemAtPosition(position);

        if (route != null) {
            Intent intent = new Intent(this, TripsActivity.class);
            intent.putExtra("route_id", route.getId());
            startActivity(intent);
        }
    }

}
