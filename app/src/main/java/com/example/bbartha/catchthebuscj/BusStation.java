package com.example.bbartha.catchthebuscj;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bartha on 11/19/2014.
 */
public class BusStation {

    private LatLng latLng;
    private float timeInMinutes;
    private String name;

    public BusStation(final LatLng latLng, final float timeInMinutes, final String name) {
        this.latLng = latLng;
        this.timeInMinutes = timeInMinutes;
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTimeInMinutesAndSeconds() {
        int hours = (int) (timeInMinutes / 60);
        String hoursString = hours > 0 ? String.valueOf(hours) + "h " : "";
        float timeInMinutesLessThenAHour = timeInMinutes % 60;
        String minutes = timeInMinutesLessThenAHour > 1 ? String.valueOf((int) Math.floor(timeInMinutesLessThenAHour)) +
                "min " : "";
        String seconds = String.valueOf((int) Math.floor((timeInMinutesLessThenAHour % 1) * 60)) + "sec";
        return hoursString + minutes + seconds;
    }

    public String getName() {
        return name;
    }
}
