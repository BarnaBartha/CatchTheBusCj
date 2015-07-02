package com.example.bbartha.catchthebuscj.directions;

import android.location.Location;

import com.example.bbartha.catchthebuscj.BusStation;
import com.example.bbartha.catchthebuscj.util.LatLngUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Bartha on 12/7/2014.
 */
public class BusStationDirectionUtil {

    private final static LatLng MY_LOC_HARD_CODED = new LatLng(46.779197, 23.558225);

    public String getClosestBusStationName() {
        return closestBusStationName;
    }

    private String closestBusStationName = "";

    public BusStationDirection getDirectionToClosestBusStation(final List<BusStation> busStations,
                                                               final Location myLocation) {

        BusStation closestBusStation = busStations.get(0);

        final LatLng myLocationLatLng = myLocation != null ? new LatLng(myLocation.getLatitude(),
                myLocation.getLongitude()) : MY_LOC_HARD_CODED;
        float distance = Float.MAX_VALUE;
        for (BusStation busStation : busStations) {
            if (LatLngUtils.distanceBetween(busStation.getLatLng(), myLocationLatLng) < distance) {
                closestBusStation = busStation;
                distance = LatLngUtils.distanceBetween(busStation.getLatLng(), myLocationLatLng);
            }
        }

        closestBusStationName = closestBusStation.getName();
        return new BusStationDirection(myLocationLatLng, closestBusStation.getLatLng());
    }
}
