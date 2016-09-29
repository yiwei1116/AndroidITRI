package com.uscc.ncku.androiditri.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.uscc.ncku.androiditri.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmPic.OnFragmentInteractionListener} interface
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button nextStep,reTake;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmPic.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmPic newInstance(String param1, String param2) {
        ConfirmPic fragment = new ConfirmPic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        nextStep.setOnClickListener(this);
        nextStep.setBackgroundResource(R.drawable.camera_btn_nextstep);
        reTake = (Button)view.findViewById(R.id.retake);
        reTake.setOnClickListener(this);
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
        FragmentManager fm = getFragmentManager();
        ChooseTemplate CT = new ChooseTemplate();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flayout_fragment_continer, CT );
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public void ReTake(){
        FragmentManager fm = getFragmentManager();
        CustomCameras CC = new CustomCameras();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flayout_fragment_continer, CC );
        transaction.addToBackStack(null);
        transaction.commit();




    }


    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.next_step:
                nextStep();

                break;
            case R.id.retake:
                ReTake();

                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
