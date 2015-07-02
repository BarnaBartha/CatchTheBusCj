package com.example.bbartha.catchthebuscj;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.bbartha.catchthebuscj.directions.BusStationDirection;
import com.example.bbartha.catchthebuscj.directions.BusStationDirectionUtil;
import com.example.bbartha.catchthebuscj.directions.DirectionResult;
import com.example.bbartha.catchthebuscj.directions.GMapV2Direction;
import com.example.bbartha.catchthebuscj.directions.GetDirectionsAsyncTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

//    private LocationManager mLocationManager;

    private String busRouteNumber;
    private String direction;

    private List<BusStation> busStations;
    private BusRouteContainer busRouteContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.busRouteNumber = extras.getString("busRouteNumber");
            this.direction = extras.getString("direction");
        }
        handleMapInitialization();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handleMapInitialization();
    }

    private void handleMapInitialization() {
        final AssetManager assetManager = getAssets();
        final Calendar calendar = Calendar.getInstance();

        try {
            busRouteContainer = new BusRouteContainer(busRouteNumber, direction, calendar,
                    assetManager);
            busStations = busRouteContainer.getBusStations();
            final BusStationDirectionUtil busStationDirectionUtil = new BusStationDirectionUtil();

            setUpMapIfNeeded();

            if (googleMap != null) {

//                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                final Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
//                    // Do something with the recent location fix
//                    //  otherwise wait for the update below
//                } else {
//                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
//                }

                final BusStationDirection busStationDirection = busStationDirectionUtil
                        .getDirectionToClosestBusStation(busStations, googleMap.getMyLocation());
                final GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this, busStationDirection.getFrom(),
                        busStationDirection.getTo(), GMapV2Direction.MODE_WALKING,
                        busStationDirectionUtil.getClosestBusStationName());
                asyncTask.execute(new HashMap<String, String>());
//                addBusRouteLines(busStations, busRouteContainer.getBusRouteCoordinatesListLatLng(), busStationDirection.getTo());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.clear();
            }
        }
    }

    public void handleGetDirectionsResult(final DirectionResult directionResult) {

        googleMap
                .addPolyline((new PolylineOptions())
                        .addAll(this.busRouteContainer.getBusRouteCoordinatesListLatLng()).width(5).color(Color.BLUE)
                        .geodesic(true));

        for (BusStation station : this.busStations) {
            final String snippet = station.getName().equals(directionResult.getToStationName()) ? station
                    .getTimeInMinutesAndSeconds() + "\n" + directionResult.getDuration() : station
                    .getTimeInMinutesAndSeconds();

            googleMap.addMarker(new MarkerOptions()
                    .position(station.getLatLng())
                    .title(station.getName())
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        final PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.GREEN);
        // move camera to zoom on map
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < directionResult.getDirectionPoints().size(); i++) {
            rectLine.add(directionResult.getDirectionPoints().get(i));
            builder.include(directionResult.getDirectionPoints().get(i));
        }

        // move camera to zoom on map
        LatLngBounds bounds = builder.build();
        // bound of points and offset from edges in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 20);
        googleMap.moveCamera(cu);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(directionResult.getDirectionPoints().get(0),
                13));

        googleMap.addPolyline(rectLine);
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if (location != null) {
//            mLocationManager.removeUpdates((android.location.LocationListener) this);
//        }
//    }
}
