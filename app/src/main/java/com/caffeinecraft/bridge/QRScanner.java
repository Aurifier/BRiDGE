package com.caffeinecraft.bridge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Created by ronitkumar on 11/14/14.
 */
public class QRScanner extends Activity implements Camera.PreviewCallback{
    private SurfaceView preview=null;
    private SurfaceHolder previewHolder=null;
    private Camera camera=null;
    private boolean inPreview=false;
    private boolean cameraConfigured=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qrscanner);

        preview=(SurfaceView)findViewById(R.id.qrscanner);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onResume() {
        super.onResume();

        camera=Camera.open();
        camera.setPreviewCallback(this);
        startPreview();
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.setPreviewCallback(null);
        camera.release();
        camera=null;
        inPreview=false;

        super.onPause();
    }

    private Camera.Size getBestPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void initPreview(int width, int height) {
        if (camera!=null && previewHolder.getSurface()!=null) {
            try {
                camera.setPreviewDisplay(previewHolder);
            }
            catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback",
                        "Exception in setPreviewDisplay()", t);
                Toast
                        .makeText(QRScanner.this, t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }

            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
                Camera.Size size = getBestPreviewSize(sizes, width,
                        height);

                if (size!=null) {
                    parameters.setPreviewSize(size.width, size.height);
                    Log.e("Preview Size", "Width: " + size.width + "\nHeight: " + size.height);
                    camera.setParameters(parameters);
                    cameraConfigured=true;
                }
            }
        }
    }

    private void startPreview() {
        if (cameraConfigured && camera!=null) {
            camera.startPreview();
            inPreview=true;
        }
    }

    SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,
                                   int height) {
            if (inPreview) {
                camera.stopPreview();
            }

            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                initPreview(height, width);
                camera.setDisplayOrientation(90);
            }

            if(display.getRotation() == Surface.ROTATION_90)
            {
                initPreview(width, height);
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                initPreview(height, width);
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                initPreview(width, height);
                camera.setDisplayOrientation(180);
            }

            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Size pSize = camera.getParameters().getPreviewSize();
        Map<DecodeHintType,Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));

        try {
            String result = readQRCode(data, tmpHintsMap, pSize);
            Log.e("On Preview Frame", "Read Successful! Data: " + result);
            Bundle b = new Bundle();
            b.putString("result", result);
            Intent i = getIntent(); //gets the intent that called this intent
            i.putExtras(b);
            setResult(Activity.RESULT_OK, i);
            camera.setPreviewCallback(null);
            finish();
        } catch (NotFoundException e) {
            e.printStackTrace();
            Log.e("On Preview Frame", "Read Unsuccessful. (stack trace): " + e.toString());
        }
    }

    public static String readQRCode(byte[] data, Map hintMap, Camera.Size pSize) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new PlanarYUVLuminanceSource(data, pSize.width, pSize.height, 0, 0, pSize.width, pSize.height, false)));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }
}