package com.jaam.footprint.fragments;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaam.footprint.FootprintActivity;
import com.jaam.footprint.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FootprintMapFragment extends SupportMapFragment
implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMapOptions mOptions;
    private GoogleMap mMap;
    private List<ParseObject> mNearbyMessagesList = null;
    private List<Marker> mMapMarkers = null;
    static final LatLng SAN_FRANCISCO = new LatLng(37.771224,-122.404883);
    static final double DEFAULT_NEARBY_MILES = 15.0;

    @Override
    public void onStart() {
        super.onStart();
        setupMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMap();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mMap.clear();
        //mMap = null;
    }

    private void setupMap() {
        Log.d(MainActivity.APP_NAME, "setupMap() was called");
        if (mMap == null) {
            mMap = super.getMap();

            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(
                                new LatLng(FootprintActivity.mCurrentLocation.getLatitude(),
                                        FootprintActivity.mCurrentLocation.getLongitude()
                                        ), 15f, 30f, 0f))); // zoom, tilt, bearing

                //new CameraPosition(SAN_FRANCISCO, 15f, 30f, 0f))); // zoom, tilt, bearing
                mMap.setMyLocationEnabled(true);
                UiSettings settings = mMap.getUiSettings();
                settings.setAllGesturesEnabled(true);
                settings.setCompassEnabled(true);
                settings.setMyLocationButtonEnabled(true);
                settings.setRotateGesturesEnabled(true);
                settings.setScrollGesturesEnabled(true);
                settings.setTiltGesturesEnabled(true);
                settings.setZoomControlsEnabled(true);
                settings.setZoomGesturesEnabled(true);

                mMap.setOnMarkerClickListener(this);
                mMap.setOnMapLongClickListener(this);
                mMap.setOnInfoWindowClickListener(this);

                getNearbyMessages();
            }

        }
    }

    private void updateMapMessages() {
        if (mMap == null) {
            return;
        }
        if (mMapMarkers == null) {
            mMapMarkers = new ArrayList<Marker>();
        }

        if (mMapMarkers != null && mMapMarkers.size() > 0) {
            for (Marker mark : mMapMarkers) {
                mark.remove();
            }
            mMapMarkers.clear();
        }

        for (ParseObject msg : mNearbyMessagesList) {
            ParseGeoPoint geo = msg.getParseGeoPoint("location");
            Log.d(MainActivity.APP_NAME, "Msg Geo: " + geo.getLatitude() + ", "
                    + geo.getLongitude() + " for: " + msg.getString("message"));
            mMapMarkers.add(
                    mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(geo.getLatitude(), geo.getLongitude()))
                    .title(msg.getString("message"))
                    .draggable(false)
                    //.icon(BitmapDescriptorFactory.fromAsset("marker.png"))
                            )
                    );
        }

    }


    @Override
    public void onInfoWindowClick(Marker mark) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(), "You clicked an info marker! Wow, good for you!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String text = "Marker clicked!";
        if (marker.getTitle() == "Buy Milk @ Whole Foods") {
            text = "Put a long note here!";
        }

        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        ((FootprintActivity) getActivity()).showMessageEditFragment();
        ((FootprintActivity) getActivity()).getMessageEditFragment().setPoint(point.latitude, point.longitude);

        Toast.makeText(getActivity(), "Long Press!", Toast.LENGTH_SHORT).show();
    }

    public void getNearbyMessages() {
        Log.d(MainActivity.APP_NAME, "getting nearby messages!");
        ParseGeoPoint userLocation = new ParseGeoPoint(FootprintActivity.mCurrentLocation.getLatitude(),
                FootprintActivity.mCurrentLocation.getLongitude());
        ParseQuery query = new ParseQuery("Message");
        query.whereWithinMiles("location", userLocation, DEFAULT_NEARBY_MILES);
        query.setLimit(10);

        // TODO: implement ACL vs public filtering
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    mNearbyMessagesList = list;
                    Log.d(MainActivity.APP_NAME, "Retrieved " + list.size() + " location(s)");
                    Toast.makeText(getActivity(), "Retrieved " + list.size() + " location(s)", Toast.LENGTH_SHORT).show();
                    updateMapMessages();
                } else {
                    Log.d(MainActivity.APP_NAME, "Error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Error retrieving nearby locations!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
