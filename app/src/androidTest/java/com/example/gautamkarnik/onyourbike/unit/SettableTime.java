package com.example.gautamkarnik.onyourbike.unit;

import com.example.gautamkarnik.onyourbike.utils.Time;

/**
 * Created by gautamkarnik on 2015-07-11.
 */
public class SettableTime extends Time {

    public long time;

    @Override
    public long now() {
        return time;
    }
}
