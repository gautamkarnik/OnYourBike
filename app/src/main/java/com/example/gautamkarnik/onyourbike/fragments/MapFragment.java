package com.example.gautamkarnik.onyourbike.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gautamkarnik.onyourbike.R;

/**
 * Created by gautamkarnik on 2015-07-28.
 */
public class MapFragment extends Fragment {

    private static String CLASS_NAME;

    public MapFragment() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
}