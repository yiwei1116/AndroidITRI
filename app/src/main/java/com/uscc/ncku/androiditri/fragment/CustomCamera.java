package com.uscc.ncku.androiditri.fragment;

import android.app.Activity;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.uscc.ncku.androiditri.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCamera extends Activity implements SurfaceHolder.Callback {
    private ImageView imageView;
    private  String picPath;
    private int targetImageViewWidth;
    private int targetImageViewHeight;
    private File pictureFile;
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean isBackCameraOn = true;
    private String mImageFileLocation = "";
    /**
     * Camera回调，通过data[]保持图片数据信息
     */
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureFile = getOutputMediaFile();
            picPath =pictureFile.getAbsolutePath();
            if (pictureFile == null) {

                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);

                imageView = (ImageView) findViewById(R.id.Imageview);
                mCameraPreview.setVisibility(View.INVISIBLE);
                rotateImage(setReducedImageSize());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);
        initViews();
    }

    private void initViews() {
        mCameraPreview = (SurfaceView) findViewById(R.id.sv_camera);
        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }
    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(picPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        imageView.setImageBitmap(rotatedBitmap);
    }
    public Bitmap setReducedImageSize() {

        targetImageViewWidth = imageView.getMeasuredWidth();
        targetImageViewHeight = imageView.getMeasuredHeight();


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true; // no bitmap

        BitmapFactory.decodeFile(picPath, bmOptions); // Decode a file path into a bitmap
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth /targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(picPath, bmOptions);
    }

    /**
     * 切换前后摄像头
     *
     * @param view view
     */
    public void switchCamera(View view) {
        int cameraCount;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        // 遍历可用摄像头
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

    /**
     * 拍照
     *
     * @param view view
     */
    public void capture(View view) {

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
        // 使用自动对焦功能

        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mCamera.takePicture(null, null, mPictureCallback);

                }
            }
        });
    }

    /**
     * 获取图片保持路径
     *
     * @return pic Path
     */
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

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                setStartPreview(mCamera, mSurfaceHolder);
            }
        }
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 检查是否具有相机功能
     *
     * @param context context
     * @return 是否具有相机功能
     */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * 在SurfaceView中预览相机内容
     *
     * @param camera camera
     * @param holder SurfaceHolder
     */
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
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
}
