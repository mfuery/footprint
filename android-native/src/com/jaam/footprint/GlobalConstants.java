package com.jaam.footprint;

import com.google.android.gms.maps.model.LatLng;

public class GlobalConstants {
    // For logging
    public static final String APP_NAME = "Footprint";

    // Activity Intent Codes, must be unique
    public static final int FB_INIT_RESULT = 1;
    public static final int FB_LINK_RESULT = 2;
    public static final int IMAGE_CAPTURE = 100;

    // (Street) Cred.
    public static final String PARSE_APP_ID = "GejzV5ros8VQQleYBVpXRWhlSw4nZMmRbevIpI1g";
    public static final String PARSE_CLIENT_KEY = "GvqMbgX7kCMYTX0PjwHIgMMR3jcxfp0yMMSjitp5";

    public static final LatLng SAN_FRANCISCO = new LatLng(37.771224,-122.404883);

    // Location provider consts
    public final static int LOCATION_PROVIDER_TIME = 2000;
    public final static float LOCATION_PROVIDER_DISTANCE = 10;

    // This determines what markers appear on the map (are pulled from Parse)
    public static final double DEFAULT_NEARBY_MILES = 15.0;

    // Feedback button composes email with this body
    public final static String DEFAULT_FEEDBACK_EMAIL_BODY = "Hi Footprint! I have some feedback for you below...\n\n";
}
