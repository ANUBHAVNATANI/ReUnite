package com.hacks.reunite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.hacks.reunite.ViewModel.MainViewModel;

import java.util.Collections;

public class CheckActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    private final String TAG = getClass().getSimpleName();
    private CameraManager cameraManager;
    private String cameraId;
    private Size previewSize;
    private CameraDevice.StateCallback stateCallback;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CameraDevice cameraDevice;
    private TextureView textureView;
    private CameraCaptureSession cameraCaptureSession;
    private MainViewModel mainViewModel;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        initDataBinding();

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        textureView = findViewById(R.id.texture_view);

        setUpStateCallback();

        AppCompatButton takePicButton = findViewById(R.id.take_pic);
        takePicButton.setOnClickListener(view->{
            Bitmap bitmap = textureView.getBitmap();
            mainViewModel.upload(bitmap);
        });
    }

    private void initDataBinding(){
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init();

        mainViewModel.getShowLoading().observe(this, showLoading->{
            if (showLoading != null){
                if (showLoading){
                    showLoadingDialog("Checking...");
                }else{
                    hideLoadingDialog();
                }
            }
        });

        mainViewModel.getPrediction().observe(this, prediction->{
            if (prediction != null) {
                showResults(prediction);
                Log.d(TAG, "Prediction = " + prediction);
            }else{
                showNetworkError();
            }
        });

        mainViewModel.getDetectedId().observe(this, detectedId->{
            if (detectedId != null) {
                retrieveDataFromFirebase(detectedId);
                Toast.makeText(this, "Id = " + detectedId, Toast.LENGTH_SHORT);
            }
        });
    }

    private void showNetworkError(){
        new AlertDialog.Builder(this)
                .setTitle("Network error")
                .setMessage("Please make sure you are connected to the internet and retry")
                .setPositiveButton("Retry", (dialog, which) -> {
                    mainViewModel.retryCheck();
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();
    }

    private void showResults(int prediction){
        String result;
        if (prediction == 1)
            result = "Match found! This child might be missing";
        else
            result = "No match found";
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(result)
                .setPositiveButton("Ok", null)
                .show();
    }

    private void showLoadingDialog(String loadingMessage) {
        dialog.setMessage(loadingMessage);
        dialog.show();
    }

    private void hideLoadingDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void retrieveDataFromFirebase(int id){
        //Here we use the id retrieved from the model to get the probable missing child's data
    }

    @Override
    protected void onResume() {
        super.onResume();

        openBackgroundThread();
        if (textureView.isAvailable()) {
            setUpCamera();
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    private void setUpStateCallback(){
        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice cameraDevice) {
                CheckActivity.this.cameraDevice = cameraDevice;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                CheckActivity.this.cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CheckActivity.this.cameraDevice = null;
            }
        };
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                CaptureRequest captureRequest = captureRequestBuilder.build();
                                CheckActivity.this.cameraCaptureSession = cameraCaptureSession;
                                CheckActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCamera() {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);

                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.cameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
            return;
        }

        try {
            cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void requestPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Camera permission")
                .setMessage("We need camera permission to work. Please grant it!")
                .setPositiveButton("Grant", (dialog, which) -> {
                    openCamera();
                })
                .setNegativeButton("Close app", (dialog, which) -> {
                    finish();
                    System.exit(0);
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Unable to get camera permission", Toast.LENGTH_SHORT).show();
                requestPermissionDialog();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            setUpCamera();
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
}
