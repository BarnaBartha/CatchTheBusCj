package com.example.bbartha.catchthebuscj.directions;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Bartha on 12/11/2014.
 */
public class DirectionResult {

    private ArrayList<LatLng> directionPoints;
    private String duration;
    private String toStationName;

    public DirectionResult(ArrayList<LatLng> directionPoints, String duration, String toStationName) {
        this.directionPoints = directionPoints;
        this.duration = duration;
        this.toStationName = toStationName;
    }

    public ArrayList<LatLng> getDirectionPoints() {
        return directionPoints;
    }

    public String getDuration() {
        return duration;
    }

    public String getToStationName() {
        return toStationName;
    }
}
