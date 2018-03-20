package com.example.gautamkarnik.onyourbike.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gautamkarnik.onyourbike.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by gautamkarnik on 2015-07-28.
 */
public class TimerFragment extends Fragment {

    private final String CLASS_NAME = getClass().getName();
    private AdView adView;

    public TimerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(CLASS_NAME, "onCreateView");
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(CLASS_NAME, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // add the Ad Mob Banner
        adView = (AdView) getView().findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("F31D2E99526F8EDDA8A46EEEEF221E3F")
                .build();
        if(adView != null) {
            Log.d(CLASS_NAME, "AdView is not null ... loading ad");
            adView.loadAd(adRequest);
        } else {
            Log.d(CLASS_NAME, "AdView is null ...");
        }
    }
}