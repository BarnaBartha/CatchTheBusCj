package com.example.bbartha.catchthebuscj;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bartha on 11/17/2014.
 */
public class BusRouteCoordinates {

    private LatLng latLng;
    private boolean isStation;
    private String stationName;

    public BusRouteCoordinates(LatLng latLng, boolean isStation, String stationName) {
        this.latLng = latLng;
        this.isStation = isStation;
        this.stationName = stationName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getStationName() {
        return stationName;
    }

    public boolean isStation() {
        return isStation;
    }
}
