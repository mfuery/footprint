package com.jaam.footprint.fragments;

import android.content.Context;
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
import com.jaam.footprint.MainActivity;

public class FootprintMapFragment extends SupportMapFragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMapOptions mOptions;
    private GoogleMap mMap;
    static final LatLng SAN_FRANCISCO = new LatLng(37.771224,-122.404883);
    //    public static ArrayList<Message> mMessages = new ArrayList<Message>();


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

    private void setupMap() {
        Log.d(MainActivity.APP_NAME, "SetupMap");
        if (mMap == null) {
            mMap = super.getMap();

            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(SAN_FRANCISCO, 15f, 30f, 0f)));
                mMap.setMyLocationEnabled(true);
                UiSettings settings = mMap.getUiSettings();
                //    			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                //    					new CameraPosition(SAN_FRANCISCO, 13.5f, 30f, 112.5f))); // zoom, tilt, bearing
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
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Context context = getActivity();

        CharSequence text = "Marker clicked!";
        if (marker.getTitle() == "Buy Milk @ Whole Foods") {
            text = "Put a long note here!";
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return false;
    }

    @Override
    public void onMapLongClick(LatLng point) {
        //        MsgEditFragment newFragment = new MsgEditFragment();
        //        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //        transaction.replace(R.id.fragments_container, newFragment);
        //        transaction.addToBackStack(null);
        //        transaction.commit();
        //
        //		Context context = getActivity();
        //		CharSequence text = "Long Press!";
        //		int duration = Toast.LENGTH_SHORT;
        //
        //		Toast toast = Toast.makeText(context, text, duration);
        //		toast.show();
    }
}
