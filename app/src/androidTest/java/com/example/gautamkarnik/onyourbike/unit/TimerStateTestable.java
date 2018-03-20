package com.example.gautamkarnik.onyourbike.unit;

import com.example.gautamkarnik.onyourbike.model.TimerState;

/**
 * Created by gautamkarnik on 2015-07-11.
 */
public class TimerStateTestable extends TimerState {

    public TimerStateTestable() {
        super();
        time = new SettableTime();
    }

    public void setElapsedTime(long value) {
        ((SettableTime) time).time = value;
    }

    /*
    private long fakeTime = -1;

    @Override
    public long elapsedTime() {
        if (fakeTime == -1) {
            return super.elapsedTime();
        } else {
            return fakeTime;
        }
    }

    public void setElapsedTime(long time) {
        fakeTime = time;
    }

    @Override
    public void reset() {
        fakeTime = -1;
        super.reset();
    }
    */
}
