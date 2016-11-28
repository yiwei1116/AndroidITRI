package org.tabc.living3.fragment;


import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
    private ImageView imageView;
    private  String picPath,flagSelect;
    private int   SurfaceViewWidth, orientation;
    private int   SurfaceViewHeight;
    private File pictureFile;
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private boolean isBackCameraOn=true;
    private String mImageFileLocation = "";
    private Button switchCamera,capture,nextStep,reTake;
    private FrameLayout layout;
    private File image;
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

        layout = (FrameLayout)view.findViewById(R.id.custom_camera_container);
        imageView = (ImageView)view.findViewById(R.id.test);
        nextStep = (Button)view.findViewById(R.id.next_step);
        reTake = (Button)view.findViewById(R.id.retake);
        mCameraPreview = (SurfaceView) view.findViewById(R.id.sv_camera);
        switchCamera = (Button)view.findViewById(R.id.btn_switch_camera);
        capture = (Button)view.findViewById(R.id.btn_capture);
        switchCamera.setOnClickListener(this);
        capture.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        reTake.setOnClickListener(this);

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

        initViews();
        return view;
    }
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            pictureFile = getOutputMediaFile();

            galleryAddPic();
            if (pictureFile == null) {

                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                picPath = pictureFile.getAbsolutePath();
                switchCamera.setVisibility(View.GONE);
                capture.setVisibility(View.GONE);
                reTake.setVisibility(View.VISIBLE);
                nextStep.setVisibility(View.VISIBLE);
                //rotateImage(setReducedImageSize());
                fos.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };




 private Bitmap rotateImage(Bitmap bitmap,String picPath) {
     ExifInterface exifInterface ;
     try {
         exifInterface = new ExifInterface(picPath);
         orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
         Log.e("orientation",String.valueOf(orientation));
     } catch (IOException e) {
         e.printStackTrace();
     }


     Matrix matrix = new Matrix();
     switch (orientation) {
         case ExifInterface.ORIENTATION_NORMAL:
             return bitmap;
         case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
             matrix.setScale(-1, 1);
             break;
         case ExifInterface.ORIENTATION_ROTATE_180:
             matrix.setRotate(180);
             break;
         case ExifInterface.ORIENTATION_FLIP_VERTICAL:
             matrix.setRotate(180);
             matrix.postScale(-1, 1);
             break;
         case ExifInterface.ORIENTATION_TRANSPOSE:
             matrix.setRotate(90);
             matrix.postScale(-1, 1);
             break;
         case ExifInterface.ORIENTATION_ROTATE_90:
             matrix.setRotate(90);
             break;
         case ExifInterface.ORIENTATION_TRANSVERSE:
             matrix.setRotate(-90);
             matrix.postScale(-1, 1);
             break;
         case ExifInterface.ORIENTATION_ROTATE_270:
             matrix.setRotate(-90);
             break;
         default:
             return bitmap;
     }
     Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
     bitmap.recycle();

  return rotatedBitmap;
 }
    public Bitmap setReducedImageSize() {

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SurfaceViewWidth  = layout.getMeasuredWidth();
                SurfaceViewHeight = layout.getMeasuredHeight();

            }
        });
       /* SurfaceViewWidth = imageView.getMeasuredWidth();
        SurfaceViewHeight = imageView.getMeasuredHeight();*/



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
    }
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
            case R.id.next_step:
                nextStep();

                break;
            case R.id.retake:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
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
        else {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        mCamera.takePicture(null, null, mPictureCallback);

                    }
                }
            });
        }
    }

    private void galleryAddPic() {
         Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
         Uri contentUri = Uri.fromFile(image);
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
        SurfaceViewWidth = width;
        SurfaceViewHeight = height;
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
        ((MainActivity) getActivity()).showDefaultToolbar();
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
    public void nextStep(){
        //setReducedImageSize();

        imageView.setImageBitmap(rotateImage(setReducedImageSize(),pictureFile.getAbsolutePath()));
        imageView.setVisibility(View.VISIBLE);
      /*  ChooseTemplate CT = new ChooseTemplate();
        Bundle bundle = new Bundle();
        bundle.putString("picPath", picPath);
        bundle.putString("flagSelect", String.valueOf(flagSelect));
        CT.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragment(CT);
*/
    }

}
