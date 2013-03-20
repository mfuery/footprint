package com.jaam.footprint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gms.maps.model.LatLng;
import com.jaam.footprint.fragments.FootprintMapFragment;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.PushService;

public class FootprintActivity extends FragmentActivity implements LocationListener {
    //private static ActionBar actionBar;
    private FootprintMapFragment mMapFragment;
    private static Location mCurrentLocation;

    private LocationManager mLocationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(GlobalConstants.APP_NAME, "FootprintActivity.onCreate!");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
        this.requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setContentView(R.layout.main_screen);

        Parse.initialize(this, GlobalConstants.PARSE_APP_ID, GlobalConstants.PARSE_CLIENT_KEY);
        PushService.subscribe(this, "", FootprintActivity.class);
        PushService.setDefaultPushCallback(this, FootprintActivity.class);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //actionBar = getActionBar();
            // Show the Up button in the action bar.
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        // However, if we're being restored from a previous state, then we don't need to do anything
        // and should return or else we could end up with overlapping fragments.
        if (findViewById(R.id.fragment_container) == null) {
            Log.d(GlobalConstants.APP_NAME, "fragment_container or savedInstanceState is null!");
            return;
        }

        mMapFragment = new FootprintMapFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        initLocationListener();
        showMapFragment();
    }

    public static Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public static LatLng getCurrentLatLng() {
        return new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    private void initLocationListener() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        // Specify provider explicitly or get best available?
        //String provider = LocationManager.NETWORK_PROVIDER;
        String provider = mLocationManager.getBestProvider(new Criteria(), false);

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // start location settings activity
            //AlertDialog.Builder
            //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            //mCurrentLocation = mLocationManager.getLastKnownLocation(provider);
        }

        // Define the criteria how to select the location provider -> use default
        mCurrentLocation = mLocationManager.getLastKnownLocation(provider);

        mLocationManager.requestLocationUpdates(provider, GlobalConstants.LOCATION_PROVIDER_TIME,
                GlobalConstants.LOCATION_PROVIDER_DISTANCE, this);

        if (mCurrentLocation != null) {
            Log.d(GlobalConstants.APP_NAME, "mCurrentLocation (last known): " + mCurrentLocation.getLatitude()
                    + ", " + mCurrentLocation.getLongitude());
        }
    }

    /***********************************************************************************
     * Implements LocationListener methods
     */

    @Override
    public void onLocationChanged(Location loc) {
        mCurrentLocation = loc;
        //Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
        Log.d(GlobalConstants.APP_NAME, "onLocationChanged");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(GlobalConstants.APP_NAME, "onProviderDisabled");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(GlobalConstants.APP_NAME, "onProviderEnabled");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(this, "onStatusChanged", Toast.LENGTH_SHORT).show();
        Log.d(GlobalConstants.APP_NAME, "onStatusChanged");

    }

    /***********************************************************************************
     * 
     */

    @Override
    protected void onResume() {
        Log.d(GlobalConstants.APP_NAME, "FootprintActivity.onResume!");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(GlobalConstants.APP_NAME, "FootprintActivity.onPause!");

        // Remove the locationlistener updates when Activity is paused
        mLocationManager.removeUpdates(this);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(GlobalConstants.APP_NAME, "FootprintActivity.onDestroy!");
        super.onDestroy();
    }

    /**
     * Initiating Menu XML file (main_menu.xml)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.actionbar, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Single menu item is selected do something
        // Ex: launching new activity/screen or show alert message
        case R.id.menu_refresh:
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
            mMapFragment.refreshMapView();
            return true;

        case R.id.menu_compose:
            Toast.makeText(this, "Compose selected", Toast.LENGTH_SHORT).show();
            startComposeMessageActivity();
            return true;

            /*
        case R.id.menu_map:
            Toast.makeText(this, "Map Selected", Toast.LENGTH_SHORT).show();
            showMapFragment();
            return true;
             */

        case R.id.menu_logout:
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(FootprintActivity.this, "Logging you out...", Toast.LENGTH_SHORT).show();
                        fbLogout();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
            return true;

        case R.id.menu_preferences:
            Toast.makeText(this, "Preferences selected", Toast.LENGTH_SHORT).show();
            return true;

        case R.id.menu_feedback:
            //Toast.makeText(this, "feedback selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:footprintsf@gmail.com?subject=Footprint User Feedback&body="
                    + GlobalConstants.DEFAULT_FEEDBACK_EMAIL_BODY);
            intent.setData(data);
            startActivity(intent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /***********************************************************************************
     * Methods
     */

    public void fbLogout() {
        Session fbSession = ParseFacebookUtils.getSession();
        if (!fbSession.isClosed()) {
            Log.d(GlobalConstants.APP_NAME, "fbLogout: closed");
            //MainActivity.saveTransaction("user-fb-logout");

            fbSession.closeAndClearTokenInformation();
            ParseUser.logOut();

            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Log.d(GlobalConstants.APP_NAME, "fbLogout: session already closed");

        }
    }

    public void showMapFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(mMapFragment.isAdded()) {
            Log.d(GlobalConstants.APP_NAME, "showMapFragment... show()");
            fragmentTransaction.show(mMapFragment);
        } else {
            Log.d(GlobalConstants.APP_NAME, "showMapFragment... replace()");
            fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
        }

        fragmentTransaction.commit();

    }

    public void startComposeMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble(MessageActivity.KEY_LAT, getCurrentLocation().getLatitude());
        bundle.putDouble(MessageActivity.KEY_LON, getCurrentLocation().getLongitude());
        bundle.putBoolean(MessageActivity.KEY_EDITABLE, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
