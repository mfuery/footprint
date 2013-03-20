package com.jaam.footprint.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaam.footprint.FootprintActivity;
import com.jaam.footprint.GlobalConstants;
import com.jaam.footprint.MessageActivity;
import com.jaam.footprint.domain.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FootprintMapFragment extends SupportMapFragment
implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    //private GoogleMapOptions mOptions;
    private GoogleMap mMap;
    // Parse.objectId, Message
    private HashMap<String,Message> mNearbyMessages = null;
    private List<Marker> mMapMarkers = null;
    private CameraPosition cameraPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Location currLocation = FootprintActivity.getCurrentLocation();
        cameraPosition = new CameraPosition(new LatLng(currLocation.getLatitude(),
                currLocation.getLongitude()), 15, 30, 0); // zoom, tilt, bearing

    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(GlobalConstants.APP_NAME, "FootprintMapFragment.onActivityResult(): " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case GlobalConstants.RESULT_CREATE_MESSAGE:
            //if (resultCode == Activity.RESULT_OK) {
            refreshMapView();
            //}
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(GlobalConstants.APP_NAME, marker.getTitle() + " : " + marker.getPosition().toString());
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker mark) {
        Toast.makeText(getActivity(), "You clicked an info marker!\nWow, good for you!", Toast.LENGTH_LONG).show();
        LatLng point = mark.getPosition();
        Message msg = getMessageByLocation(point);
        if (msg != null) {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra(MessageActivity.KEY_TITLE, msg.getTitle());
            intent.putExtra(MessageActivity.KEY_MESSAGE, msg.getMessage());
            intent.putExtra(MessageActivity.KEY_LAT, msg.getLatitude());
            intent.putExtra(MessageActivity.KEY_LON, msg.getLongitude());
            intent.putExtra(MessageActivity.KEY_EDITABLE, false);
            startActivity(intent);
        } else {
            Log.d(GlobalConstants.APP_NAME, "Null msg: " + point.toString());
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //Toast.makeText(getActivity(), "Long Press!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(MessageActivity.KEY_LAT, point.latitude);
        intent.putExtra(MessageActivity.KEY_LON, point.longitude);
        intent.putExtra(MessageActivity.KEY_EDITABLE, true);
        startActivityForResult(intent, GlobalConstants.RESULT_CREATE_MESSAGE);
    }


    private Message getMessageByLocation(LatLng location) {
        for (Entry<String, Message> el : mNearbyMessages.entrySet()) {
            Message msg = el.getValue();
            double lat = msg.getLatitude();
            double lon = msg.getLongitude();
            //Log.d(GlobalConstants.APP_NAME, "Compare msgs: " + location.toString() + " -> " + msg.getLatLng().toString());
            if (lat == location.latitude && lon == location.longitude) {
                return msg;
            }
        }

        return null;
    }

    private void setupMap() {
        Log.d(GlobalConstants.APP_NAME, "setupMap() was called");
        if (mMap == null) {
            mMap = super.getMap();

            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

                refreshMapView();
            }

        }
    }

    public void refreshMapView() {
        if (mMap == null) {
            setupMap();
        }

        Log.d(GlobalConstants.APP_NAME, "getting nearby messages!");
        Location currLocation = FootprintActivity.getCurrentLocation();
        ParseGeoPoint userLocation = new ParseGeoPoint(currLocation.getLatitude(),
                currLocation.getLongitude());
        ParseQuery query = new ParseQuery("Message");
        query.whereWithinMiles("location", userLocation, GlobalConstants.DEFAULT_NEARBY_MILES);
        query.orderByAscending("location");
        query.whereNotEqualTo("isDeleted", true);
        query.setLimit(GlobalConstants.DEFAULT_NEARBY_LOCATIONS);

        // TODO: implement ACL vs public filtering
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    setMessages(list);
                    Log.d(GlobalConstants.APP_NAME, "Retrieved " + list.size() + " location(s)");
                    //Toast.makeText(getActivity(), "Retrieved " + list.size() + " location(s)", Toast.LENGTH_SHORT).show();
                    drawMapMarkers();
                } else {
                    Log.d(GlobalConstants.APP_NAME, "Error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Error retrieving nearby locations!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Draws GMap markers from in-memory Message hashmap
     */
    private void drawMapMarkers() {
        if (mMap == null) {
            return;
        }

        if (mMapMarkers == null) {
            mMapMarkers = new ArrayList<Marker>();
        } else if (mMapMarkers.size() > 0) {
            mMapMarkers.clear();
        }

        mMap.clear();

        for (Entry<String, Message> el : mNearbyMessages.entrySet()) {
            Message msg = el.getValue();
            final double lat = msg.getLatitude();
            final double lon = msg.getLongitude();
            Log.d(GlobalConstants.APP_NAME, "Add map marker for: " + el.getKey() + ": " + lat + ", " + lon + " for: " + msg.getTitle());

            MarkerOptions mOpt = new MarkerOptions()
            .position(new LatLng(lat, lon))
            .title(msg.getTitle())
            .snippet(msg.getMessage())
            .draggable(false);

            mMapMarkers.add(mMap.addMarker(mOpt));
        }
    }

    /**
     * Takes ParseObjects and sets up in-memory hashmap
     * @param list of ParseObjects
     */
    private void setMessages(List<ParseObject> list) {
        if (mNearbyMessages == null) {
            mNearbyMessages = new HashMap<String, Message>();
        }

        mNearbyMessages.clear();

        if (list == null || list.isEmpty()) {
            return;
        }

        for (ParseObject msg : list) {
            ParseGeoPoint geo = msg.getParseGeoPoint("location");
            mNearbyMessages.put(msg.getObjectId(),
                    new Message(msg.getString("title"), msg.getString("message"),
                            geo.getLatitude(), geo.getLongitude()));
        }
    }

}
