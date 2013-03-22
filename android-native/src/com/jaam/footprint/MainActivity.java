package com.jaam.footprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Initialize Parse service and global subsystems
        Parse.initialize(this, GlobalConstants.PARSE_APP_ID, GlobalConstants.PARSE_CLIENT_KEY);
        ParseUser user = ParseUser.getCurrentUser(); // if user login info is cached on the device already
        ParseAnalytics.trackAppOpened(getIntent());
        Utility.saveTransaction("app-started");
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Begin user auth
        ParseFacebookUtils.initialize(getString(R.string.app_id));

        if (user != null && user.getObjectId() != null) {
            Session fbSession = ParseFacebookUtils.getSession();
            if (fbSession.getAccessToken() != null) {
                // User has valid session cached already. skip login screen and start main activity.
                //fbLogin(); // Log user into FB and goto main screen
                startMainScreen();
            }
        }

        // We get here only if user login session is not cached to disk (like a new user)
        setContentView(R.layout.splash_screen);

        // replace progress bar with login button
        ProgressBar bar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        bar.setVisibility(View.INVISIBLE);
        Button btn = (Button) findViewById(R.id.login_button);
        btn.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        Log.d(GlobalConstants.APP_NAME, "MainActivity.onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalConstants.FB_INIT_RESULT && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);

        }
    }

    public void onClickLoginButton(View view) {
        ProgressBar bar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        bar.setVisibility(View.VISIBLE);
        Button btn = (Button) findViewById(R.id.login_button);
        btn.setVisibility(View.INVISIBLE);
        fbLogin(); // login and go to main screen
    }

    public void fbLogin() {
        ParseFacebookUtils.logIn(this, GlobalConstants.FB_INIT_RESULT, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d(GlobalConstants.APP_NAME, "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(getApplicationContext(), "Why did you cancel Facebook login?", Toast.LENGTH_LONG).show();
                    Utility.saveTransaction("user-fb-cancelled");

                    ((ProgressBar) findViewById(R.id.splash_progress_bar)).setVisibility(View.INVISIBLE);
                    ((Button) findViewById(R.id.login_button)).setVisibility(View.VISIBLE);

                } else {
                    Log.d(GlobalConstants.APP_NAME, "User logged in through Facebook");
                    Toast.makeText(getApplicationContext(), "User logged in through Facebook", Toast.LENGTH_LONG).show();
                    Utility.saveTransaction("user-fb-login");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.link(user, MainActivity.this, GlobalConstants.FB_LINK_RESULT, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.d(GlobalConstants.APP_NAME, "Woohoo, user logged in with Facebook and linked to Parse!");
                                    Utility.saveTransaction("user-fb-linked");
                                }
                            }
                        });

                    }

                    Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response) {
                            if (graphUser != null) {
                                if (user.getString("fbId") == null) {
                                    user.put("fbId", graphUser.getId());
                                    user.put("firstName", graphUser.getFirstName());
                                    user.put("lastName", graphUser.getLastName());
                                    user.saveInBackground();
                                    Utility.saveTransaction("user-fb-saved-fbId");
                                }
                            }
                        }
                    });

                    startMainScreen();
                }
            }
        });

    }


    private void startMainScreen() {
        finish();
        startActivity(new Intent(this, FootprintActivity.class));

    }
}
