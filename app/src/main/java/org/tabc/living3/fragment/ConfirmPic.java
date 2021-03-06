package org.tabc.living3.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import android.util.DisplayMetrics;

import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;


import java.io.IOException;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link ConfirmPic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPic extends Fragment implements View.OnClickListener {
    private ImageView imageView;
    private  String picPath;
    private DisplayMetrics mPhone;
    private int targetImageViewWidth;
    private int targetImageViewHeight;
    private Button nextStep,reTake,backTo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfirmPic.
     */
    public static ConfirmPic newInstance() {
        ConfirmPic fragment = new ConfirmPic();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_confirm_pic, container, false);
        picPath = (String)getArguments().get("picPath");
        imageView = (ImageView)view.findViewById(R.id.iv_camera_result);
        nextStep = (Button)view.findViewById(R.id.next_step);
        reTake = (Button)view.findViewById(R.id.retake);
        backTo = (Button)view.findViewById(R.id.back_Btn);
        nextStep.setOnClickListener(this);
        reTake.setOnClickListener(this);
        backTo.setOnClickListener(this);
        nextStep.setBackgroundResource(R.drawable.camera_btn_nextstep);
        reTake.setBackgroundResource(R.drawable.camera_btn_retake);
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                targetImageViewWidth = imageView.getMeasuredWidth();
                targetImageViewHeight = imageView.getMeasuredHeight();
                rotateImage(setReducedImageSize());
                return true;
            }
        });


        return view;
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
    public void nextStep(){
        ChooseTemplate CT = new ChooseTemplate();
        ((MainActivity) getActivity()).replaceFragment(CT);

    }
    public void ReTake(){
        CustomCameras CC = new CustomCameras();
        ((MainActivity) getActivity()).replaceFragment(CC);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.next_step:
                nextStep();
                break;
            case R.id.retake:
                ReTake();
            case R.id.back_Btn:
                getActivity().onBackPressed();
                break;
        }
    }
}
