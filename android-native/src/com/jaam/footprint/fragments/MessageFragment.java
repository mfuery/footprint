package com.jaam.footprint.fragments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaam.footprint.GlobalConstants;
import com.jaam.footprint.R;
import com.jaam.footprint.Utility;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MessageFragment extends Fragment {
    public final static String MESSAGE = "message";
    public final static String MESSAGE_TITLE = "message_title";
    public final static String MESSAGE_LAT = "lat";
    public final static String MESSAGE_LON = "lon";
    private String mCurrentMessage = "";
    private String mCurrentMessageTitle = "";
    private double mLat = 0.0;
    private double mLon = 0.0;

    private EditText mEditMsgTitle;
    private EditText mEditMsg;
    private EditText mEditLat;
    private EditText mEditLon;

    private final Uri fileUri = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.message, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init() {
        /*
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewMessage();
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonCamera = (Button) findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });

        Button buttonFile = (Button) findViewById(R.id.buttonFile);
        buttonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageActivity.this, "Pick file button clicked", Toast.LENGTH_SHORT).show();
            }
        });


        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        if (!isEditMode) {
            editTextTitle.setEnabled(false);
            editTextMessage.setEnabled(false);
            buttonSave.setVisibility(View.GONE);

            buttonCamera.setVisibility(View.GONE);
            buttonFile.setVisibility(View.GONE);
        }
         */
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Utility.checkCameraHardware(getActivity())) {
            // No device camera - wtf?!
            ((Button) getActivity().findViewById(R.id.camera_button)).setVisibility(View.INVISIBLE);
        }

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

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
        case GlobalConstants.IMAGE_CAPTURE:
            Uri u;
            if (Utility.hasImageCaptureBug()) {
                File fi = new File(Environment.getExternalStorageDirectory().getPath());
                //File fi = new File("/sdcard/tmp");
                try {
                    u = Uri.parse(Images.Media.insertImage(getActivity().getContentResolver(), fi.getAbsolutePath(), null, null));
                    if (!fi.delete()) {
                        Log.i("logMarker", "Failed to delete " + fi);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                u = intent.getData();
            }

            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(getActivity(), "Image saved to:\n", Toast.LENGTH_LONG).show();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(getActivity(), "Why cancel? Don't you want a pic", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(getActivity(), "Error capturing picture", Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;

        }


    }


    public void updateMessageView(final String messageTitle, final String message, final double lat, final double lon) {
        if (mEditMsgTitle == null) {
            mEditMsgTitle = (EditText) getActivity().findViewById(R.id.edit_message_title);
            mEditMsg = (EditText) getActivity().findViewById(R.id.edit_message);
            mEditLat = (EditText) getActivity().findViewById(R.id.edit_lat);
            mEditLon = (EditText) getActivity().findViewById(R.id.edit_lon);
        }

        mCurrentMessage = message;
        mCurrentMessageTitle = messageTitle;
        mLat = lat;
        mLon = lon;

        if (mEditMsgTitle != null) {
            mEditMsgTitle.setText(mCurrentMessageTitle);
            mEditMsg.setText(mCurrentMessage);
            mEditLat.setText(Double.toString(mLat));
            mEditLon.setText(Double.toString(mLon));
        }

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

        if (fileUri != null) {
            // Try to retrieve and save the image file, if one was captured.
            File file = new File(fileUri.toString());
            InputStream inputStream = null;
            byte[] buffer = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                buffer = IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                Log.e(GlobalConstants.APP_NAME, "Error reading image file from camera capture: " + e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        Log.e(GlobalConstants.APP_NAME, "Error reading image file from camera capture: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            }

            if (buffer != null) {
                ParseFile pfile = new ParseFile(file.getName(), buffer);
                pfile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException arg0) {
                        // TODO Auto-generated method stub
                    }
                });
            }
        }

        ParseObject msg = new ParseObject("Message");
        msg.put("title", mCurrentMessageTitle);
        msg.put("message", mCurrentMessage);

        if (mLat != 0.0) {
            msg.put("location", new ParseGeoPoint(mLat, mLon));
        }

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null && user.getObjectId() != null) {
            ParseRelation rel = msg.getRelation("creatorUserId");
            rel.add(user);
        }

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //setArguments(null);
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

    public void startCameraActivity() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //File imgFile = new File(Environment.getDownloadCacheDirectory(), "img_" + UUID.randomUUID() + ".jpg");
        //fileUri = Uri.fromFile(imgFile); // create a file to save the image

        //Log.d(GlobalConstants.APP_NAME, "File uri: " + fileUri);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        if (Utility.hasImageCaptureBug()) {
            Log.d(GlobalConstants.APP_NAME, "HAS THE CAMERA BUG");
            File imgFile = new File(Environment.getExternalStorageDirectory().getPath());
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        } else {
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }

        // start the image capture Intent
        startActivityForResult(intent, GlobalConstants.IMAGE_CAPTURE);
    }
}
