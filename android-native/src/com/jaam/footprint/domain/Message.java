package com.jaam.footprint.domain;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jaam.footprint.GlobalConstants;
import com.jaam.footprint.MainActivity;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

public class Message {
    public LatLng geoCoordinate;
    public String title;
    public String message;
    private String _objectId = null;
    private double lat;
    private double lon;

    public Message() {
    }

    public Message(final String title, final String message, final double lat, final double lon) {
        this.title = title;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
    }

    public String getObjectId() {
        return _objectId;
    }

    public void setObjectId(final String objectId) {
        this._objectId = objectId;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(final double latitude) {
        this.lat = latitude;
    }

    public double getLongitude() {
        return lon;
    }

    public void setLongitude(final double longitude) {
        this.lon = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public LatLng getLatLng() {
        LatLng latLng = new LatLng(lat, lon);
        return latLng;
    }

    public void save() {
        ParseObject msg = new ParseObject("Message");

        msg.put("title", title);
        msg.put("message", message);
        msg.put("location", new ParseGeoPoint(lat, lon));

        if (MainActivity.user != null && MainActivity.user.getObjectId() != null) {
            ParseRelation rel = msg.getRelation("creatorUserId");
            rel.add(MainActivity.user);
        }

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(GlobalConstants.APP_NAME, "Save message to parse successful.");
                    //setArguments(null);
                    //getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.e(GlobalConstants.APP_NAME, "Error saving message to parse: " + e.getMessage());
                    //Toast.makeText(getActivity(), "Uh oh, a save error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
