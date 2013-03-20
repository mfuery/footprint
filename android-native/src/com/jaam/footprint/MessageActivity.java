
package com.jaam.footprint;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaam.footprint.domain.Message;
import com.parse.Parse;

public class MessageActivity extends Activity {
    public interface Closure {
        void done(boolean success);
    }

    // For savedInstance bundle data
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_EDITABLE = "is_editable";

    private boolean isEditMode = true;
    private Message mMessage;

    private EditText editTextTitle;
    private EditText editTextMessage;
    TextView textViewLocation;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Parse.initialize(this, GlobalConstants.PARSE_APP_ID, GlobalConstants.PARSE_CLIENT_KEY);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(GlobalConstants.APP_NAME, "MessageActivity.onCreate with savedBundle");
            mMessage = new Message(bundle.getString(KEY_TITLE), bundle.getString(KEY_MESSAGE),
                    bundle.getDouble(KEY_LAT), bundle.getDouble(KEY_LON));
            isEditMode = bundle.getBoolean(KEY_EDITABLE);
        } else {
            Log.d(GlobalConstants.APP_NAME, "MessageActivity.onCreate no savedBundle");
            mMessage = new Message();
        }

        setContentView(R.layout.message);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        Button buttonCamera = (Button) findViewById(R.id.buttonCamera);
        Button buttonFile = (Button) findViewById(R.id.buttonFile);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (isEditMode) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNewMessage();
                }
            });

            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCameraActivity();
                }
            });

            buttonFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MessageActivity.this, "Pick file button clicked", Toast.LENGTH_SHORT).show();
                }
            });

            buttonSave.setVisibility(View.VISIBLE);
            buttonCancel.setText("Cancel");
            buttonCamera.setVisibility(View.VISIBLE);
            buttonFile.setVisibility(View.VISIBLE);

        } else {

            // Not editable (view only)
            buttonSave.setVisibility(View.INVISIBLE);
            buttonCancel.setText("Back");

            buttonCamera.setVisibility(View.GONE);
            buttonFile.setVisibility(View.GONE);
        }

        //editTextTitle.setEnabled(isEditMode);
        //editTextMessage.setEnabled(isEditMode);

        // set values
        editTextTitle.setText(mMessage.getTitle());
        editTextMessage.setText(mMessage.getMessage());
        textViewLocation.setText(String.format("Lat/Lon %f/%f", //String.format("Lat/Lon %.2f/%.2f",
                mMessage.getLatitude(),
                mMessage.getLongitude()));

        if (!Utility.checkCameraHardware(this)) {
            // No device camera - wtf?!
            buttonCamera.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(GlobalConstants.APP_NAME, "onSaveInstanceState!");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_TITLE, mMessage.getTitle());
        savedInstanceState.putString(KEY_MESSAGE, mMessage.getMessage());
        savedInstanceState.putDouble(KEY_LAT, mMessage.getLatitude());
        savedInstanceState.putDouble(KEY_LON, mMessage.getLongitude());
    }

    private void saveNewMessage() {
        String title = editTextTitle.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, getString(R.string.label_add_name_desc), Toast.LENGTH_LONG).show();
            return;
        }
        mMessage.setTitle(title);
        mMessage.setMessage(message);

        Log.i(GlobalConstants.APP_NAME, "Location: " + mMessage.getLatLng().toString());
        Log.i(GlobalConstants.APP_NAME, "Title: " + title);
        Log.i(GlobalConstants.APP_NAME, "Message: " + message);

        mMessage.save();
        finish();
    }

    public Message getMessage() {
        return mMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(GlobalConstants.APP_NAME, "receiveCameraResult!");
        /*
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
         */
    }


    /**
     * 
     */
    private void startCameraActivity() {
        File imgFile;
        final String filename = "img_" + UUID.randomUUID().toString().replace("-", "") + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        if (Utility.hasImageCaptureBug()) {
            Log.d(GlobalConstants.APP_NAME, "HAS THE CAMERA BUG");
            imgFile = new File(Environment.getExternalStorageDirectory().getPath(), filename);
        } else {
            imgFile = new File(Environment.getDownloadCacheDirectory(), filename);
            //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }

        Log.d(GlobalConstants.APP_NAME, "File uri: " + Uri.fromFile(imgFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));

        // start the image capture Intent
        startActivityForResult(intent, GlobalConstants.IMAGE_CAPTURE);
    }

}
