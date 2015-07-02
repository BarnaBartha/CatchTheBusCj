package com.example.bbartha.catchthebuscj.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bartha on 11/19/2014.
 */
public class LatLngUtils {

    public static float distanceBetween(LatLng latLng, LatLng latLngList) {
        float[] dist = new float[1];
        Location.distanceBetween(latLng.latitude, latLng.longitude, latLngList.latitude, latLngList.longitude, dist);
        return dist[0];
    }
}
