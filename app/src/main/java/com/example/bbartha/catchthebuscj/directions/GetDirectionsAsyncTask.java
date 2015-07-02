package com.example.bbartha.catchthebuscj.directions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.bbartha.catchthebuscj.MapsActivity;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.Map;

/**
 * Created by Bartha on 12/7/2014.
 */
public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, DirectionResult> {

    private LatLng fromPosition;
    private LatLng toPosition;
    private String directionsMode;
    private String toStationName;
    private MapsActivity activity;
    private Exception exception;
    private ProgressDialog progressDialog;

    public GetDirectionsAsyncTask(MapsActivity activity, final LatLng fromPosition,
                                  final LatLng toPosition, final String directionsMode, final String toStationName) {
        super();
        this.activity = activity;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.directionsMode = directionsMode;
        this.toStationName = toStationName;
    }

    public void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Calculating directions...");
        progressDialog.show();
    }

    @Override
    public void onPostExecute(DirectionResult result) {
        progressDialog.dismiss();
        if (exception == null) {
            activity.handleGetDirectionsResult(result);
        } else {
            processException();
        }
    }

    @Override
    protected DirectionResult doInBackground(Map<String, String>... params) {
        try {
            GMapV2Direction md = new GMapV2Direction();
            Document doc = md.getDocument(fromPosition, toPosition, directionsMode);
            return new DirectionResult(md.getDirection(doc), md.getDurationText(doc), toStationName);
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    private void processException() {
        Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}