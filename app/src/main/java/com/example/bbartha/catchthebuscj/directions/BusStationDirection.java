package com.example.bbartha.catchthebuscj.directions;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bartha on 12/7/2014.
 */
public class BusStationDirection {

    public LatLng getFrom() {
        return from;
    }

    public LatLng getTo() {
        return to;
    }

    final LatLng from;
    final LatLng to;

    public BusStationDirection(LatLng from, LatLng to) {
        this.from = from;
        this.to = to;
    }
}
