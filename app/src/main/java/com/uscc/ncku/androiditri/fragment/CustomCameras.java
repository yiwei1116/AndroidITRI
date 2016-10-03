package com.uscc.ncku.androiditri.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.media.ExifInterface;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomCameras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomCameras extends Fragment implements SurfaceHolder.Callback,View.OnClickListener  {
    private ImageView imageView;
    private  String picPath;
    private int targetImageViewWidth;
    private int targetImageViewHeight;
    private File pictureFile;
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean isBackCameraOn;
    private String mImageFileLocation = "";
    private Button switchCamera,capture;
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomCameras.
     */
    public static CustomCameras newInstance() {
        CustomCameras fragment = new CustomCameras();
        return fragment;
    }

    public CustomCameras() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_preview, container, false);
        mCameraPreview = (SurfaceView) view.findViewById(R.id.sv_camera);
        switchCamera = (Button)view.findViewById(R.id.btn_switch_camera);
        capture = (Button)view.findViewById(R.id.btn_capture);
        switchCamera.setOnClickListener(this);
        capture.setOnClickListener(this);

        MainActivity.hideMainBtn();
        MainActivity.hideToolbar();
        Button back = (Button) view.findViewById(R.id.btn_camera_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        initViews();
        return view;
    }
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureFile = getOutputMediaFile();
            // picPath =pictureFile.getAbsolutePath();
            if (pictureFile == null) {

                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);



                fos.close();

                ConfirmPic CP = new ConfirmPic();
                Bundle bundle = new Bundle();
                bundle.putString("picPath",pictureFile.getAbsolutePath());
                CP.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.custom_camera_container, CP );
                transaction.addToBackStack(null);
                transaction.commit();
                //CustomCameras.this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
 /*   @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.camera_menu, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.ic_swith_camera:
                switchCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void initViews() {

        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btn_switch_camera:
                switchCamera();

                break;
            case R.id.btn_capture:
                capture();

                break;
        }

    }

    public void switchCamera() {
        int cameraCount;


        cameraCount = Camera.getNumberOfCameras();

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (isBackCameraOn) {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    releaseCamera();
                    mCamera = Camera.open(i);
                    setStartPreview(mCamera, mSurfaceHolder);
                    isBackCameraOn = false;
                    break;
                }
            } else {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    releaseCamera();
                    mCamera = Camera.open(i);
                    setStartPreview(mCamera, mSurfaceHolder);
                    isBackCameraOn = true;
                    break;
                }
            }
        }
    }


    public void capture() {

        Camera.Parameters params = mCamera.getParameters();

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        List<Camera.Size> list = params.getSupportedPictureSizes();
        if(list.size() >2){

            Camera.Size size = list.get(list.size()/2);
            params.setPictureSize(size.width, size.height);
        }else{
            Camera.Size size = list.get(0);
            params.setPictureSize(size.width, size.height);
        }
        Camera.Size ps = previewSizes.get(0);
        params.setPictureFormat(ImageFormat.JPEG);
        params.setPreviewSize(ps.width, ps.height);

        params.set("jpeg-quality", 100);

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(params);

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCamera.takePicture(null, null, mPictureCallback);

        }
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success ) {
                    mCamera.takePicture(null, null, mPictureCallback);

                }
            }
        });
    }


    private File getOutputMediaFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(new Date());
        String imageFileName = "IMAGE" + timeStamp + "_";
        //  Get a top-level shared/external storage directory for placing files of a particular type.

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        // Create image  ** path => where to create image
        //File image = File.createTempFile(imageFileName, ".jpg", path);
        File image = new File(path,imageFileName+".JPEG");

        return image;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.checkCameraHardware(getActivity()) && (mCamera == null)) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                setStartPreview(mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.showDefaultToolbar();
    }

    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }


    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }


    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

}
