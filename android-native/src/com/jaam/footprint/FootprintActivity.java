package com.jaam.footprint;

import android.app.ActionBar;
import android.content.Intent;
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
import com.jaam.footprint.fragments.FootprintMapFragment;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FootprintActivity extends FragmentActivity {
    private static ActionBar actionBar;
    private FootprintMapFragment mMapFragment;

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


        //TextView welcome = (TextView) findViewById(R.id.hello_text);
        //welcome.setText("Hello " + MainActivity.fbUser.getFirstName() + "!");

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        // However, if we're being restored from a previous state, then we don't need to do anything
        // and should return or else we could end up with overlapping fragments.
        if (findViewById(R.id.fragment_container) == null) {
            Log.d(MainActivity.APP_NAME, "fragment_container or savedInstanceState is null!");
            return;
        }

        mMapFragment = new FootprintMapFragment();

        showMapFragment();
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
        case R.id.menu_compose:
            Toast.makeText(this, "Compose selected", Toast.LENGTH_SHORT).show();
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

    private void showMapFragment() {

        // Start a new FragmentTransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(mMapFragment.isAdded()) {
            Log.d(MainActivity.APP_NAME, "showMapFragment... show()");
            fragmentTransaction.show(mMapFragment);
        } else {
            Log.d(MainActivity.APP_NAME, "showMapFragment... replace()");
            fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
        }

        // Keep the transaction in the back stack so it will be reversed when backbutton is pressed
        //fragmentTransaction.addToBackStack(null);

        // Commit transaction
        fragmentTransaction.commit();

    }

}
