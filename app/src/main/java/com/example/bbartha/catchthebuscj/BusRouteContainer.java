package com.example.bbartha.catchthebuscj;

import android.content.res.AssetManager;

import com.example.bbartha.catchthebuscj.util.DataAccessor;
import com.example.bbartha.catchthebuscj.util.LatLngUtils;
import com.google.android.gms.maps.model.LatLng;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Bartha on 11/19/2014.
 */
public class BusRouteContainer {

    private int currentTimeInMinutes;

    private List<BusRouteCoordinates> busRouteCoordinatesList;
    private List<BusSchedule> busScheduleList;
    private float busRouteHalfTripDuration;
    private boolean nextDayScheduleUsed;


    public BusRouteContainer(final String busRouteNumber, final String direction, final Calendar calendar,
                             final AssetManager assetManager)
            throws IOException, SAXException, ParserConfigurationException {
        this.currentTimeInMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

        final DataAccessor dataAccessor = new DataAccessor(assetManager, busRouteNumber, direction);

        final BusRouteReader busRouteReader = new BusRouteReader();
        this.busRouteCoordinatesList = busRouteReader.read(dataAccessor.getRouteCoordinatesStream());

        final BusScheduleReader busScheduleReader = new BusScheduleReader();

        this.busScheduleList = getBusScheduleList(dataAccessor, busScheduleReader, calendar.get(Calendar.DAY_OF_WEEK)
                , true);
        this.busRouteHalfTripDuration = busScheduleReader.getHalfOfRoundTripTime(dataAccessor.getRouteDurationStream());
    }

    private List<BusSchedule> getBusScheduleList(final DataAccessor dataAccessor,
                                                 final BusScheduleReader busScheduleReader,
                                                 final int dayOfWeek, final boolean nextDayScheduleCheckNeeded)
            throws IOException {
        final InputStream scheduleStream;
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            scheduleStream = dataAccessor.getRouteScheduleForWeekendStream();
        } else {
            scheduleStream = dataAccessor.getRouteScheduleStream();
        }
        final List<BusSchedule> busSchedules = busScheduleReader.readSchedule(scheduleStream);
        if (currentTimeInMinutes > busSchedules.get(busSchedules.size() - 1).getTimeInMinutes() &&
                nextDayScheduleCheckNeeded) {
            final int nextDayOfWeek = dayOfWeek == 7 ? 1 : dayOfWeek + 1;
            nextDayScheduleUsed = true;
            return getBusScheduleList(dataAccessor, busScheduleReader, nextDayOfWeek, false);
        } else {
            return busSchedules;
        }
    }


    public List<BusStation> getBusStations() {
        List<BusStation> busStations = new ArrayList<>();

        float totalDistance = 0;
        int count = 0;
        for (BusRouteCoordinates busRouteCoordinates : busRouteCoordinatesList) {
            if (count > 0) {
                totalDistance += LatLngUtils.distanceBetween(busRouteCoordinatesList.get(count - 1).getLatLng(),
                        busRouteCoordinates.getLatLng());
            }
            count++;
        }

        float distance = 0;
        int counter = 0;
        for (BusRouteCoordinates busRouteCoordinates : busRouteCoordinatesList) {
            if (counter == 0) {
                if (busRouteCoordinates.isStation()) {

                    busStations.add(new BusStation(busRouteCoordinates.getLatLng(),
                            BusScheduleCalculator.getMinutesTillNextBus(busScheduleList, currentTimeInMinutes,
                                    0, nextDayScheduleUsed), busRouteCoordinates.getStationName()));
                }
            }
            if (counter > 0) {
                distance += LatLngUtils.distanceBetween(busRouteCoordinatesList.get(counter - 1).getLatLng(),
                        busRouteCoordinates.getLatLng());
                if (busRouteCoordinates.isStation()) {
                    float timeToGetToStation = (distance / totalDistance) * this.busRouteHalfTripDuration;

                    busStations.add(new BusStation(busRouteCoordinates.getLatLng(),
                            BusScheduleCalculator.getMinutesTillNextBus(busScheduleList, currentTimeInMinutes,
                                    timeToGetToStation, nextDayScheduleUsed), busRouteCoordinates.getStationName()));
                }
            }

            counter++;
        }

        return busStations;
    }

    public List<LatLng> getBusRouteCoordinatesListLatLng() {
        List<LatLng> latLngs = new ArrayList<>();
        for (BusRouteCoordinates busRouteCoordinates : this.busRouteCoordinatesList) {
            latLngs.add(busRouteCoordinates.getLatLng());
        }
        return latLngs;
    }
}
