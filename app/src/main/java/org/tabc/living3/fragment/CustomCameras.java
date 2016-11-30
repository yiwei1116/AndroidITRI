package org.tabc.living3.fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomCameras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomCameras extends Fragment implements SurfaceHolder.Callback,View.OnClickListener {

    private  String picPath,flagSelect;
    private File pictureFile;
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private File image;
    private Uri contentUri;
    private static final String PREF_CAMERA = "PREF_CAMERA";
    private static final String CAMERA_PREFERENCES = "CAMERA_PREFERENCES";
    private int mOrientation =  -1;
    private static final int ORIENTATION_PORTRAIT_NORMAL =  1;
    private static final int ORIENTATION_PORTRAIT_INVERTED =  2;
    private static final int ORIENTATION_LANDSCAPE_NORMAL =  3;
    private static final int ORIENTATION_LANDSCAPE_INVERTED =  4;
    private int onClickOrientation =  1;
    OrientationEventListener mOrientationEventListener;
    public static int degrees = 0;
    public View view;
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
         view = inflater.inflate(R.layout.camera_preview, container, false);



        mCameraPreview = (SurfaceView) view.findViewById(R.id.sv_camera);
        initViews();
        Button switchCamera = (Button)view.findViewById(R.id.btn_switch_camera);
        Button capture = (Button)view.findViewById(R.id.btn_capture);
        switchCamera.setOnClickListener(this);
        capture.setOnClickListener(this);

        int numCameras = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            numCameras = Camera.getNumberOfCameras();
        }

        if(numCameras > 1){
            switchCamera.setVisibility(View.VISIBLE);
        }else {
            switchCamera.setVisibility(View.GONE);
        }
        ((MainActivity) getActivity()).hideMainBtn();
        ((MainActivity) getActivity()).hideToolbar();
        Button back = (Button) view.findViewById(R.id.btn_camera_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Bundle bundle1 = getArguments();
        if(bundle1 != null) {
            flagSelect  = (String) getArguments().get("flagSelect");

        }


        return view;
    }
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            pictureFile = getOutputMediaFile();
            if (pictureFile == null) {

                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);


                // galleryAddPic();
                picPath = pictureFile.getAbsolutePath();
                Log.e("picPath",picPath);
                try {

                //fos.write(data);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inDither = true;

                Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length,opts);
                ExifInterface exif = new ExifInterface(pictureFile.toString());
                if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                    realImage = rotate(realImage, 270);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                    realImage = rotate(realImage, 180);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
                    if(getCameraPreferences(getActivity()) == Camera.CameraInfo.CAMERA_FACING_BACK){
                        switch (onClickOrientation){
                            case ORIENTATION_PORTRAIT_NORMAL:
                                realImage = rotate(realImage, 90);
                                break;
                            case ORIENTATION_PORTRAIT_INVERTED:
                                realImage = rotate(realImage, -90);
                                break;
                            case ORIENTATION_LANDSCAPE_NORMAL:
                                break;
                            case ORIENTATION_LANDSCAPE_INVERTED:
                                realImage = rotate(realImage, 180);
                                break;
                        }
                    }else{
                        switch (onClickOrientation){
                            case ORIENTATION_PORTRAIT_NORMAL:
                                realImage = rotate(realImage, -90);
                                break;
                            case ORIENTATION_PORTRAIT_INVERTED:
                                realImage = rotate(realImage, 90);
                                break;
                            case ORIENTATION_LANDSCAPE_NORMAL:
                                break;
                            case ORIENTATION_LANDSCAPE_INVERTED:
                                realImage = rotate(realImage, -180);
                                break;
                        }
                    }
                }
                boolean bo = realImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                //rotateImage(setReducedImageSize());
                    CameraDisplay CD = new CameraDisplay();
                    Bundle bundle = new Bundle();
                    bundle.putString("picPath", picPath);
                    bundle.putString("flagSelect", String.valueOf(flagSelect));
                    CD.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragment(CD);


            } catch (IOException e) {
                e.printStackTrace();
            }
            fos.close();

            }
         catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void setCameraPreferences(int cameraPreferencesId){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_CAMERA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CAMERA_PREFERENCES,cameraPreferencesId);
        editor.commit();
    }
    public static int getCameraPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_CAMERA, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CAMERA_PREFERENCES,Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    Bitmap rotate(Bitmap bitmap, int degrees){
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return oriented;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }


   /* public Bitmap setReducedImageSize() {

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SurfaceViewWidth  = layout.getMeasuredWidth();
                SurfaceViewHeight = layout.getMeasuredHeight();

            }
        });
       *//* SurfaceViewWidth = imageView.getMeasuredWidth();
        SurfaceViewHeight = imageView.getMeasuredHeight();*//*



        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true; // no bitmap

        BitmapFactory.decodeFile(picPath, bmOptions); // Decode a file path into a bitmap
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / SurfaceViewWidth, cameraImageHeight / SurfaceViewHeight);
        //inSampleSize == 4 returns an image that is 1/4 the width/height of the original
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(picPath, bmOptions);
    }*/
    private void initViews() {

        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
                try {
                    onClickOrientation = mOrientation;
                    capture();
                }catch (Exception e){
                    Log.e("Tag","Error taking picture : " + e.getMessage());
                }


    }}

    public View switchCamera() {

        if(getCameraPreferences(getActivity()) == Camera.CameraInfo.CAMERA_FACING_BACK){
            setCameraPreferences(Camera.CameraInfo.CAMERA_FACING_FRONT);

        }else if(getCameraPreferences(getActivity()) == Camera.CameraInfo.CAMERA_FACING_FRONT){
            setCameraPreferences(Camera.CameraInfo.CAMERA_FACING_BACK);

        }

        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mCamera = getCameraInstance(getActivity());
        refreshCamera();
        return view;
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(Activity context){
        Camera c = null;
        try {
            c = Camera.open(getCameraPreferences(context)); // attempt to get a Camera instance
            //c.setDisplayOrientation(90);

            setCameraDisplayOrientation(context, getCameraPreferences(context),c);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)

            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    public void refreshCamera(){
        if (mSurfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
            e.printStackTrace();
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e){
            e.printStackTrace();
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


        mCamera.takePicture(null, null, mPictureCallback);


    }

    private void galleryAddPic() {
         Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
         contentUri = Uri.fromFile(image);
         mediaScanIntent.setData(contentUri);
         getActivity().sendBroadcast(mediaScanIntent);
     }
    private File getOutputMediaFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(new Date());
        String imageFileName = "IMAGE" + timeStamp + "_";
        //  Get a top-level shared/external storage directory for placing files of a particular type.

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        // Create image  ** path => where to create image
        //File image = File.createTempFile(imageFileName, ".jpg", path);
         image = new File(path,imageFileName+".JPEG");

        return image;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = getCameraInstance(getActivity());
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
       refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }



    @Override
    public void onResume() {
        super.onResume();

        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(getActivity()) {
                @Override
                public void onOrientationChanged(int orientation) {
                    // determine our orientation based on sensor response
                    int lastOrientation = mOrientation;

                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
                        }
                    }
                    else if (orientation < 315 && orientation >= 225) {
                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
                        }
                    }
                    else if (orientation < 225 && orientation >= 135) {
                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
                        }
                    }
                    else { // orientation <135 && orientation > 45
                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
                        }
                    }
                }
            };
        }

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
    }




    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }




    @Override
    public void onPause() {
        super.onPause();
        mOrientationEventListener.disable();
    }




}
