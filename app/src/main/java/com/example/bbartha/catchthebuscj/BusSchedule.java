package com.example.bbartha.catchthebuscj;

/**
 * Created by Bartha on 11/18/2014.
 */
public class BusSchedule {

    private int hours;
    private int minutes;

    public BusSchedule(final int hours, final int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getTimeInMinutes() {
        return this.hours * 60 + this.minutes;
    }
}
