package com.jaam.footprint.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jaam.footprint.GlobalConstants;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Message {
    protected String title;
    protected String message;
    protected String _objectId = null;
    protected double lat;
    protected double lon;
    protected String fileUri;
    protected ParseObject parseMessage;
    protected ParseFile parseFile;

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

    public void setFileUri(String uri) {
        this.fileUri = uri;
    }

    public String getFileUri() {
        return fileUri;
    }

    /**
     * @return path without file:// for a local file:<br/>
     * file:///path/to/file -> becomes -> /path/to/file
     */
    public String getFilePathLocal() {
        return fileUri.replaceAll("^\\w+://", "");
    }

    /**
     * 
     * @return filename without path: imgname.jpg
     */
    public String getFilename() {
        File fn = new File(getFilePathLocal());
        return fn.getName();
    }

    public boolean isFileLocal() {
        return fileUri.startsWith("file://");
    }

    public void save() {
        parseMessage = new ParseObject("Message");

        parseMessage.put("title", title);
        parseMessage.put("message", message);
        parseMessage.put("location", new ParseGeoPoint(lat, lon));
        parseMessage.put("isDeleted", false);

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null && user.getObjectId() != null) {
            ParseRelation rel = parseMessage.getRelation("creatorUserId");
            rel.add(user);
        }

        parseMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(GlobalConstants.APP_NAME, "Save message to parse successful.");
                    // TODO Somehow notify the map to draw this marker?
                } else {
                    // TODO error handling
                    Log.e(GlobalConstants.APP_NAME, "Error saving message to parse: " + e.getMessage());
                    //Toast.makeText(getActivity(), "Uh oh, a save error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Try to upload file to Parse
        if (isFileLocal()) {
            File file = new File(getFilePathLocal());
            byte data[];
            try {
                data = FileUtils.readFileToByteArray(file);

                parseFile = new ParseFile(getFilename(), data);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i(GlobalConstants.APP_NAME, "Save message ParseFile successful.");
                            parseMessage.put("attachment", parseFile);
                            parseMessage.saveInBackground();
                        } else {
                            // TODO error handling
                            Log.e(GlobalConstants.APP_NAME, "Save message ParseFile failed :(");
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(GlobalConstants.APP_NAME, "Could not read file into byte array for ParseFile upload: "
                        + e.getMessage());
            }

        }


    }

    public void log() {
        Log.d(GlobalConstants.APP_NAME, toString());
    }

    @Override
    public String toString() {
        return "MESSAGE //Title: " + title
                + " //Message: " + message
                + " //Lat/Lon: " + lat + ", " + lon
                + " //Uri: " + fileUri;
    }


}
