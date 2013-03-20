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
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends Activity {

    public static ParseUser user;
    public static GraphUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        Parse.initialize(this, GlobalConstants.PARSE_APP_ID, GlobalConstants.PARSE_CLIENT_KEY);
        user = ParseUser.getCurrentUser(); // if user login info is cached on the device already

        saveTransaction("app-started");

        ParseFacebookUtils.initialize(getString(R.string.app_id));

        if (user != null && user.getObjectId() != null) {
            Session fbSession = ParseFacebookUtils.getSession();
            if (fbSession.getAccessToken() != null) {
                // User has valid session cached already. skip login screen and start main activity.
                fbLogin(); // Log user into FB and goto main screen
            }
        } else {
            // replace progress bar with login button
            ProgressBar bar = (ProgressBar) findViewById(R.id.splash_progress_bar);
            bar.setVisibility(View.INVISIBLE);
            Button btn = (Button) findViewById(R.id.login_button);
            btn.setVisibility(View.VISIBLE);
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalConstants.FB_INIT_RESULT) {
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
            public void done(ParseUser u, ParseException err) {
                user = u;
                if (user == null) {
                    Log.d(GlobalConstants.APP_NAME, "Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(getApplicationContext(), "Why did you cancel Facebook login?", Toast.LENGTH_LONG).show();

                    saveTransaction("user-fb-cancelled");

                    //} else if (user.isNew()) {
                    // This doesn't work properly b/c isNew() is true even when user is already on Facebook,
                    // but ParseUser needs to be created.
                    //Log.d(APP_NAME, "User signed up and logged in through Facebook!");
                    //Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_LONG).show();
                    //
                    //saveTransaction("user-fb-signup");
                    //
                    //drawMainScreen();

                } else {
                    Log.d(GlobalConstants.APP_NAME, "User logged in through Facebook");
                    Toast.makeText(getApplicationContext(), "User logged in through Facebook", Toast.LENGTH_LONG).show();

                    saveTransaction("user-fb-login");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.link(user, MainActivity.this, GlobalConstants.FB_LINK_RESULT, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Facebook and linked to Parse!");
                                    saveTransaction("user-fb-linked");
                                }
                            }
                        });

                    }

                    Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser, Response response) {
                            if (graphUser != null) {
                                fbUser = graphUser;
                                if (MainActivity.user.getString("fbId") == null) {
                                    MainActivity.user.put("fbId", fbUser.getId());
                                    MainActivity.user.put("firstName", fbUser.getFirstName());
                                    MainActivity.user.put("lastName", fbUser.getLastName());
                                    MainActivity.user.saveInBackground();
                                    saveTransaction("user-fb-saved-fbId");
                                }
                            }
                        }
                    });

                    startMainScreen();
                }
            }
        });

    }


    /**
     * Boilerplate user activity logging: logs a transaction to parse
     * @param transactionType
     */
    public static void saveTransaction(final String transactionType) {
        ParseObject transaction = new ParseObject("Transaction");
        transaction.put("type", transactionType);

        if (user != null && user.getObjectId() != null) {
            ParseRelation rel = transaction.getRelation("user");
            rel.add(user);
        }

        transaction.saveEventually();
    }

    private void startMainScreen() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        bar.setVisibility(View.INVISIBLE);
        Button btn = (Button) findViewById(R.id.login_button);
        btn.setVisibility(View.INVISIBLE);

        //finish();
        //popBackStack();
        startActivity(new Intent(this, FootprintActivity.class));

    }
}
