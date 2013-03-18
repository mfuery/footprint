package com.jaam.footprint.domain;

import com.google.android.gms.maps.model.LatLng;

public class Message {
    public LatLng geoCoordinate;
    public String title;
    public String text;

    public Message() {
        geoCoordinate = new LatLng(0,0);
        title = "New Title";
        text = "Type messages...";
    }

    public Message(LatLng arg0, String arg1, String arg2) {
        geoCoordinate = arg0;
        title = arg1;
        text = arg2;
    }

}
