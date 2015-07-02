package com.example.bbartha.catchthebuscj;

import java.util.List;

/**
 * Created by Bartha on 11/19/2014.
 */
public class BusScheduleCalculator {
    public static float getMinutesTillNextBus(List<BusSchedule> busScheduleList, int currentTimeInMinutes,
                                              float timeToGetToStation, final boolean nextDayScheduleUsed) {

        float returnValue = 0;
        for (BusSchedule busSchedule : busScheduleList) {
            float possibleTimeToGetToStation = busSchedule.getTimeInMinutes() + timeToGetToStation;
            if (nextDayScheduleUsed) {
                possibleTimeToGetToStation += 24 * 60;
            }
            if (possibleTimeToGetToStation > currentTimeInMinutes) {
                returnValue = possibleTimeToGetToStation - currentTimeInMinutes;
                break;
            }
        }
        return returnValue;
    }
}
