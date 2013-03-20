package com.jaam.footprint;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Utility {
    public static final int MILE = 0;
    public static final int KILOMETER = 1;
    public static final int NAUTICAL_MILE = 2;

    /**
     * Calculate geographical distance between two lat/lon points using Haversine.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param unit Unit of distance to return. Either {@link #MILE}, {@link #KILOMETER},
     *   or {@link #NAUTICAL_MILE}.
     * @return distance
     */
    public static final double distance(final double lat1, final double lon1,
            final double lat2, final double lon2, final int unit) {
        final double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == KILOMETER) {
            dist *= 1.609344;
        } else if (unit == NAUTICAL_MILE) {
            dist *= 0.8684;
        }
        return dist;
    }

    // converts decimal degrees to radians
    private static final double deg2rad(double deg) {
        return (deg * Math.PI) / 180.0;
    }

    // converts radians to decimal degrees
    private static final double rad2deg(double rad) {
        return (rad * 180.0) / Math.PI;
    }

    /**
     * http://stackoverflow.com/questions/1910608/android-action-image-capture-intent
     * this is a well documented bug in some versions of android. that is,
     * on google experience builds of android, image capture doesn't work as documented.
     * what i've generally used is something like this in a utilities class.
     * 
     * @return boolean
     */
    public static final boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
                + android.os.Build.DEVICE);
    }

    /**
     * Check if this device has a camera
     * @param context
     * @return boolean
     */
    public static final boolean checkCameraHardware(Context context) {
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    /**
     * Seems problematic.
     * Not sure if I want to use it, but also not sure if I want to delete it yet.
     * @param view
     */
    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    /**
     * Boilerplate user activity logging: logs a transaction to parse
     * @param transactionType
     */
    public static void saveTransaction(final String transactionType) {
        ParseObject transaction = new ParseObject("Transaction");
        transaction.put("type", transactionType);

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null && user.getObjectId() != null) {
            ParseRelation rel = transaction.getRelation("user");
            rel.add(user);
        }

        transaction.saveEventually();
    }

}
