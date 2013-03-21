
package com.jaam.footprint;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaam.footprint.domain.Message;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
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
    public static final String KEY_FILE_URI = "file_uri";

    private Message mMessage;
    private boolean isEditMode = true;

    private EditText editTextTitle;
    private EditText editTextMessage;
    private TextView textViewLocation;
    private ImageView imageView;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Parse.initialize(this, GlobalConstants.PARSE_APP_ID, GlobalConstants.PARSE_CLIENT_KEY);

        if (bundle == null) {
            // like from Intent
            Log.d(GlobalConstants.APP_NAME, "MessageActivity.onCreate no savedBundle");
            bundle = getIntent().getExtras();
        } else {
            // like after image captured
            Log.d(GlobalConstants.APP_NAME, "MessageActivity.onCreate with savedBundle");
        }

        mMessage = new Message(bundle.getString(KEY_TITLE), bundle.getString(KEY_MESSAGE),
                bundle.getDouble(KEY_LAT), bundle.getDouble(KEY_LON));
        isEditMode = bundle.getBoolean(KEY_EDITABLE);
        mMessage.setFileUri(bundle.getString(KEY_FILE_URI));
        mMessage.log();

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
        imageView = (ImageView) findViewById(R.id.imagePreview);

        if (isEditMode) {

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

        imageView.setVisibility(View.GONE);
        //UrlImageViewHelper.setUrlDrawable(imageView, "https://www.google.com/images/srpr/logo4w.png");

        if (mMessage.getFileUri() != null) {
            drawImagePreview();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(GlobalConstants.APP_NAME, "onSaveInstanceState!");

        mMessage.setTitle(editTextTitle.getText().toString());
        mMessage.setMessage(editTextMessage.getText().toString());

        mMessage.log();

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_TITLE, mMessage.getTitle());
        savedInstanceState.putString(KEY_MESSAGE, mMessage.getMessage());
        savedInstanceState.putDouble(KEY_LAT, mMessage.getLatitude());
        savedInstanceState.putDouble(KEY_LON, mMessage.getLongitude());
        savedInstanceState.putString(KEY_FILE_URI, mMessage.getFileUri());
        savedInstanceState.putBoolean(KEY_EDITABLE, isEditMode);
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

        mMessage.save();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(GlobalConstants.APP_NAME, "receiveCameraResult: " + mMessage.getFileUri());

        switch (requestCode) {
        case GlobalConstants.IMAGE_CAPTURE:
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" + mMessage.getFileUri(), Toast.LENGTH_LONG).show();
                drawImagePreview();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Why cancel? Don't you want a pic", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Error capturing picture", Toast.LENGTH_SHORT).show();
            }
            break;

        default: break;
        }
    }

    public void onClickCancelButton(View view) {
        finish();
    }

    public void onClickSaveButton(View view) {
        saveNewMessage();
    }

    public void onClickCameraButton(View view) {
        startCameraActivity();
    }

    public void onClickFileButton(View view) {
        Toast.makeText(MessageActivity.this, "Pick file button clicked", Toast.LENGTH_SHORT).show();
    }


    private void drawImagePreview() {
        if (mMessage.getFileUri() == null) {
            Log.d(GlobalConstants.APP_NAME, "imagePreview: No fileuri to draw");
            return;
        }

        imageView = (ImageView) findViewById(R.id.imagePreview);
        imageView.setVisibility(View.VISIBLE);

        if (mMessage.isFileLocal()) {
            final String fn = mMessage.getFilePathLocal();

            // TODO calculate adequate sample for imageView drawable regio
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fn, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            Log.d(GlobalConstants.APP_NAME, "Image attr: " + imageType + ": " + imageWidth + "x" + imageHeight + "px");

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 6;
            Bitmap bitmap = BitmapFactory.decodeFile(fn, bitmapOptions);

            Log.d(GlobalConstants.APP_NAME, "Bitmap path " + fn);
            if (bitmap == null) {
                Log.d(GlobalConstants.APP_NAME, "Bitmap decode failed");
            } else {
                Log.d(GlobalConstants.APP_NAME, "Bitmap decode success");
                imageView = (ImageView) findViewById(R.id.imagePreview);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            UrlImageViewHelper.setUrlDrawable(imageView, mMessage.getFileUri());
        }
    }

    /**
     * 
     */
    private void startCameraActivity() {
        File imgFile;
        final String filename = "img_" + UUID.randomUUID().toString().replace("-", "") + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // TODO get to the bottom of this issue... why can't I read bytes from the bitmap
        // on the /cache partition? via getDownloadCacheDirectory()
        //if (Utility.hasImageCaptureBug()) {
        //Log.d(GlobalConstants.APP_NAME, "HAS THE CAMERA BUG");
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + GlobalConstants.APP_NAME;
        new File(path).mkdirs();
        imgFile = new File(path, filename);
        //} else {
        //    imgFile = new File(Environment.getDownloadCacheDirectory(), filename);
        //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //}

        Log.d(GlobalConstants.APP_NAME, "File uri: " + Uri.fromFile(imgFile).toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        mMessage.setFileUri(Uri.fromFile(imgFile).toString());

        // start the image capture Intent
        startActivityForResult(intent, GlobalConstants.IMAGE_CAPTURE);
    }

}
