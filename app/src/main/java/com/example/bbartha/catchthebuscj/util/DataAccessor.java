package com.example.bbartha.catchthebuscj.util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Bartha on 12/1/2014.
 */
public class DataAccessor {

    private final static String uS = "_";
    private final static String kmlExt = ".kml";
    private final static String txtExt = ".txt";
    private final static String orar = "orar";
    private final static String weekend = "weekend";
    private final static String duration = "duration";


    private final String number;
    private final String direction;
    private final AssetManager assetManager;

    public DataAccessor(final AssetManager assetManager, final String number, final String direction) {
        this.assetManager = assetManager;
        this.number = number;
        this.direction = direction;
    }

    private InputStream openStream(final String path) throws IOException {
        return this.assetManager.open(path);
    }

    public InputStream getRouteCoordinatesStream() throws IOException {
        return openStream(this.number + uS + this.direction + kmlExt);
    }

    public InputStream getRouteScheduleStream() throws IOException {
        return openStream(this.number + uS + this.direction + uS + orar + txtExt);
    }

    public InputStream getRouteScheduleForWeekendStream() throws IOException {
        return openStream(this.number + uS + this.direction + uS + orar + uS + weekend +
                txtExt);
    }

    public InputStream getRouteDurationStream() throws IOException {
        return openStream(this.number + uS + this.duration + txtExt);
    }

}
