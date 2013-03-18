/**
 * Created with IntelliJ IDEA.
 * User: julian
 * Date: 3/4/13
 * Time: 1:54 AM
 */
package com.jaam.footprint.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.jaam.footprint.MainActivity;
import com.jaam.footprint.R;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

public class MsgEditFragment extends Fragment {
    public final static String MESSAGE = "message";
    public final static String MESSAGE_TITLE = "message_title";
    public final static String MESSAGE_LAT = "message_lat";
    public final static String MESSAGE_LON = "message_lon";
    String mCurrentMessage = "";
    String mCurrentMessageTitle = "";
    double mLat = 0.0;
    double mLon = 0.0;

    private EditText mEditMsgTitle;
    private EditText mEditMsg;
    private EditText mEditLat;
    private EditText mEditLon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        return inflater.inflate(R.layout.msg_edit_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        // during startup test are there any arguments passed to the fragment
        Bundle args = getArguments();

        if (args != null) {
            // set the message based on argument passed in
            updateMessageView(args.getString(MESSAGE_TITLE), args.getString(MESSAGE), args.getDouble(MESSAGE_LAT), args.getDouble(MESSAGE_LON));
        } else {
            // set message based on save instance state defined
            updateMessageView(mCurrentMessageTitle, mCurrentMessage, mLat, mLon);
        }

    }

    public void updateMessageView(final String messageTitle, final String message, final double lat, final double lon) {
        if (mEditMsgTitle == null) {
            mEditMsgTitle = (EditText) getActivity().findViewById(R.id.edit_message_title);
            mEditMsg = (EditText) getActivity().findViewById(R.id.edit_message);
            mEditLat = (EditText) getActivity().findViewById(R.id.edit_lat);
            mEditLon = (EditText) getActivity().findViewById(R.id.edit_lon);
            Log.d("ffo", "isnull");
        } else {
            Log.d("ffo", "good");
        }

        mCurrentMessage = message;
        mCurrentMessageTitle = messageTitle;
        mLat = lat;
        mLon = lon;

        mEditMsgTitle.setText(mCurrentMessageTitle);
        mEditMsg.setText(mCurrentMessage);
        mEditLat.setText(Double.toString(mLat));
        mEditLon.setText(Double.toString(mLon));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // save the current message in case we need to recreate the fragment
        outState.putString(MESSAGE, mCurrentMessage);
        outState.putString(MESSAGE_TITLE, mCurrentMessageTitle);
        outState.putDouble(MESSAGE_LAT, mLat);
        outState.putDouble(MESSAGE_LON, mLon);

        super.onSaveInstanceState(outState);
    }

    /**
     * 
     */
    public void saveMessage() {
        mCurrentMessageTitle = mEditMsgTitle.getText().toString();
        mCurrentMessage = mEditMsg.getText().toString();
        mLat = Double.parseDouble(mEditLat.getText().toString());
        mLon = Double.parseDouble(mEditLon.getText().toString());


        ParseObject msg = new ParseObject("Message");
        msg.put("title", mCurrentMessageTitle);
        msg.put("message", mCurrentMessage);

        if (mLat != 0.0) {
            msg.put("location", new ParseGeoPoint(mLat, mLon));
        }

        if (MainActivity.user != null && MainActivity.user.getObjectId() != null) {
            ParseRelation rel = msg.getRelation("creatorUserId");
            rel.add(MainActivity.user);
        }

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    setArguments(null);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Uh oh, an error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void setPoint(final double lat, final double lon) {
        mLat = lat;
        mLon = lon;

    }


}
