package com.example.gautamkarnik.onyourbike.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gautamkarnik.onyourbike.IOnYourBike;
import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.SQLiteHelper;
import com.example.gautamkarnik.onyourbike.model.Trip;
import com.example.gautamkarnik.onyourbike.model.Trips;

import java.util.List;

public class TripsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private long route_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SQLiteHelper helper = ((IOnYourBike) getApplication()).getSQLiteHelper();
        SQLiteDatabase database = helper.open();

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            route_id = extras.getLong("route_id");
        }

        setContentView(R.layout.activity_trips);
        setupActionBar();

        List<Trip> trips = Trips.getAllInRoute(helper, database, route_id);

        ArrayAdapter<Trip> adapter = new ArrayAdapter<Trip>(this,
                android.R.layout.simple_list_item_1,
                trips);

        ListView listview = (ListView) findViewById(R.id.trips_listview);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
        //setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trips, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trip trip = (Trip) parent.getItemAtPosition(position);

        if (trip != null) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("trip_id", trip.getId());
            startActivity(intent);
        }

    }
}
