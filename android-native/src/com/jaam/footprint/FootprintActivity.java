package com.jaam.footprint;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.facebook.Session;
import com.jaam.footprint.fragments.FootprintMapFragment;
import com.jaam.footprint.fragments.MsgEditFragment;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FootprintActivity extends FragmentActivity implements LocationListener {
    private static ActionBar actionBar;
    private FootprintMapFragment mMapFragment;
    private MsgEditFragment mMessageEditFragment;
    public static LocationManager mLocationManager;
    public static Location mCurrentLocation;
    public static String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
        this.requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        setContentView(R.layout.main_screen);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getActionBar();
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        // However, if we're being restored from a previous state, then we don't need to do anything
        // and should return or else we could end up with overlapping fragments.
        if (findViewById(R.id.fragment_container) == null) {
            Log.d(MainActivity.APP_NAME, "fragment_container or savedInstanceState is null!");
            return;
        }

        // Get the location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        provider = mLocationManager.getBestProvider(criteria, false);
        mCurrentLocation = mLocationManager.getLastKnownLocation(provider);
        Log.d(MainActivity.APP_NAME, "mCurrentLocation (last known): " + mCurrentLocation.getLatitude()
                + ", " + mCurrentLocation.getLongitude());

        mMapFragment = new FootprintMapFragment();
        mMessageEditFragment = new MsgEditFragment();

        showMapFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // start location settings activity
            //AlertDialog.Builder
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            mCurrentLocation = mLocationManager.getLastKnownLocation(provider);
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(provider, 400, 10, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
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
            mMapFragment.getNearbyMessages();
            return true;
        case R.id.menu_compose:
            Toast.makeText(this, "Compose selected", Toast.LENGTH_SHORT).show();
            showMessageEditFragment();
            getMessageEditFragment().setPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            return true;

        case R.id.menu_map:
            Toast.makeText(this, "Map Selected", Toast.LENGTH_SHORT).show();
            showMapFragment();
            return true;

        case R.id.menu_logout:
            Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
            fbLogout();
            return true;

        case R.id.menu_preferences:
            Toast.makeText(this, "Preferences selected", Toast.LENGTH_SHORT).show();
            return true;

        case R.id.menu_feedback:
            Toast.makeText(this, "feedback selected", Toast.LENGTH_SHORT).show();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }


    public void fbLogout() {
        Session fbSession = ParseFacebookUtils.getSession();
        if (!fbSession.isClosed()) {
            Log.d(MainActivity.APP_NAME, "fbLogout: closed");
            MainActivity.saveTransaction("user-fb-logout");

            fbSession.closeAndClearTokenInformation();
            ParseUser.logOut();

            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Log.d(MainActivity.APP_NAME, "fbLogout: session already closed");

        }
    }

    public void showMapFragment() {
        showMapFragment(false);
    }

    public void showMapFragment(boolean addToBackStack) {

        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(mMapFragment.isAdded()) {
            Log.d(MainActivity.APP_NAME, "showMapFragment... show()");
            fragmentTransaction.show(mMapFragment);
        } else {
            Log.d(MainActivity.APP_NAME, "showMapFragment... replace()");
            fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
        }

        if (addToBackStack) {
            // Keep the transaction in the back stack so it will be reversed when backbutton is pressed
            fragmentTransaction.addToBackStack(null);
        }

        // Commit transaction
        fragmentTransaction.commit();

    }

    @Override
    public void onLocationChanged(Location loc) {
        mCurrentLocation = loc;
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
        Log.d(MainActivity.APP_NAME, "onLocationChanged");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "onProviderDisabled", Toast.LENGTH_SHORT).show();
        Log.d(MainActivity.APP_NAME, "onProviderDisabled");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
        Log.d(MainActivity.APP_NAME, "onProviderEnabled");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "onStatusChanged", Toast.LENGTH_SHORT).show();
        Log.d(MainActivity.APP_NAME, "onStatusChanged");

    }

    /**
     * 
     * @param view
     */
    public void onClickSaveMessage(View view) {
        getMessageEditFragment().saveMessage();
    }

    public void showMessageEditFragment() {
        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(getMessageEditFragment().isAdded()) {
            Log.d(MainActivity.APP_NAME, "showMessageEditFragment... show()");
            fragmentTransaction.show(getMessageEditFragment());
        } else {
            Log.d(MainActivity.APP_NAME, "showMessageEditFragment... replace()");
            fragmentTransaction.replace(R.id.fragment_container, getMessageEditFragment());
        }

        // Keep the transaction in the back stack so it will be reversed when backbutton is pressed
        fragmentTransaction.addToBackStack(null);

        // Commit transaction
        fragmentTransaction.commit();

    }

    public MsgEditFragment getMessageEditFragment() {
        return mMessageEditFragment;
    }


}
