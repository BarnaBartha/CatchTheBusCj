package com.example.bbartha.catchthebuscj;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartha on 11/18/2014.
 */
public class BusScheduleReader {

    public List<BusSchedule> readSchedule(final InputStream inputStream) throws IOException {

        List<BusSchedule> busSchedules = new ArrayList<>();

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while ((line = in.readLine()) != null) {
            String[] time;
            if (line.contains(".")) {
                time = line.split("\\.");
            } else {
                time = line.split(",");
            }
            if (time.length == 2) {
                int hours = Integer.parseInt(time[0].trim());
                int minutes = Integer.parseInt(time[1].trim());
                busSchedules.add(new BusSchedule(hours, minutes));
            }
        }

        in.close();
        return busSchedules;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public float getHalfOfRoundTripTime(final InputStream inputStream) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {

            String line = null;
            while ((line = in.readLine()) != null) {
                return Float.parseFloat(line);
            }
            return 1;
        }
    }
}
