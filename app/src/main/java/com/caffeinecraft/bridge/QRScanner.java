package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by ronitkumar on 11/14/14.
 */
public class QRScanner extends Activity {

    public Camera mCamera;
    public CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanner);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        if (mCamera == null) {
            Log.i("Camera Status", "Camera is null YO");
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        if (mPreview == null) {
            Log.i("mPreview Status", "mPreview is null YO");
        }
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        if (preview == null) {
            Log.i("Preview Status", "preview is null YO");
        }
        preview.addView(mPreview);
    }

    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
//        releaseCamera();              // release the camera immediately on pause event
    }

    public void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}