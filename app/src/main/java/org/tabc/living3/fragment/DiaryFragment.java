package org.tabc.living3.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener{
    public static final String DIARY_FRAGMENT_TAG = "DIARY_FRAGMENT_TAG";
    private DisplayMetrics mPhone;
    private final static int CAMERA_RESULT = 0;
    private final static int PHOTO_RESULT = 1;
    private final static int CAMERA_STROAGE = 2;
    private final static int STROAGE_READ = 3 ;
    private String mImageFileLocation = "";
    private ImageView mImg;
    private Button cameraCall,photoCall,nextStep;
    private Boolean flagSelect;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiaryFragment.
     */
    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        mPhone = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        mImg = (ImageView)view.findViewById(R.id.img);
        cameraCall = (Button)view.findViewById(R.id.camera);
        photoCall = (Button)view.findViewById(R.id.photo);
        cameraCall.setOnClickListener(this);
        photoCall.setOnClickListener(this);

        if (((MainActivity) getActivity()).isEnglish()) {
            ImageView title = (ImageView) view.findViewById(R.id.diary_title);
            title.setBackgroundResource(R.drawable.title_english);
        }

        ((MainActivity) getActivity()).hideToolbar();
        ((MainActivity) getActivity()).setDiaryActive();

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showMainBtn();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
        ((MainActivity) getActivity()).setDiaryNormal();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.camera:
                flagSelect = true;
                Log.e("flagSelect",String.valueOf(flagSelect));
                takePhoto();
                break;
            case R.id.photo:
                flagSelect = false;
                Log.e("flagSelect",String.valueOf(flagSelect));
                choosePhoto();
                break;


        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    public void takePhoto(){
        String[] permissionNeed = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

        };
        if( hasPermission(permissionNeed)){
            callCamera();
        }else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity(), "External storage permission require to save images", Toast.LENGTH_SHORT).show();

            }
            requestPermissions(permissionNeed, CAMERA_STROAGE);


        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    public void choosePhoto(){
        String[] permissionNeed = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        if( hasPermission(permissionNeed)){
            callPhoto();
        }else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity(), "External storage permission require to read images", Toast.LENGTH_SHORT).show();

            }
            requestPermissions(permissionNeed, STROAGE_READ);


        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults ){
        switch (requestCode){
            case CAMERA_STROAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callCamera();
                } else {

                    Toast.makeText(getActivity(),"External write permission has not meen granted, cannot save images",Toast.LENGTH_SHORT).show();
                }
            break;
            case STROAGE_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callPhoto();
                } else {

                    Toast.makeText(getActivity(),"External write permission has not meen granted, cannot read images",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }}
    private boolean hasPermission(String[] permission) {
        if (canMakeSmores()) {
            for (String permissions : permission) {

                return (ContextCompat.checkSelfPermission(getContext(), permissions) == PackageManager.PERMISSION_GRANTED);

            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void callCamera( ){

        CustomCameras customCameras = new CustomCameras();
        Bundle bundle = new Bundle();
        bundle.putString("flagSelect", String.valueOf(flagSelect));
        customCameras.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragment(customCameras);
      /*
       Intent cameraIntent = new Intent();
        // sent to have the camera application capture an image and return it.
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.e(Uri.fromFile(photoFile).toString(), "test");
        // Ar1 The name of the Intent-extra used to indicate a content resolver Uri to be used to store the requested image or video.
        Log.e("uri", Uri.fromFile(photoFile).toString());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(cameraIntent, CAMERA_RESULT);*/

    }

    /*private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photoFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/
    private void dialogAlert(){
        final Dialog dialog = new Dialog(getActivity(), R.style.selectorDialog);
        dialog.setContentView(R.layout.dialogpermission);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);

//        Dialog dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialogpermission);
        final Button btn_confirm = (Button)dialog.findViewById(R.id.dialog_button_ok);
        final Button btn_cancel = (Button)dialog.findViewById(R.id.dialog_button_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancel.setBackgroundResource(R.drawable.btn_graysquare_normal);
                btn_cancel.setTextColor(Color.parseColor("#ff000000"));
                btn_confirm.setBackgroundResource(R.drawable.btn_bluesquare_normal);
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setBackgroundResource(R.drawable.btn_graysquare_normal);
                btn_confirm.setTextColor(Color.parseColor("#ff000000"));
                btn_cancel.setBackgroundResource(R.drawable.btn_bluesquare_normal);
                Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();

    }
    public void callPhoto(){

        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_RESULT);*/
        CustomPhoto customPhoto = new CustomPhoto();
        Bundle bundle = new Bundle();
        bundle.putString("flagSelect", String.valueOf(flagSelect));
        customPhoto.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragment(customPhoto);
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT && resultCode == -1) {

            rotateImage(setReducedImageSize());
            nextPage();
        }

        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        else if ( requestCode == PHOTO_RESULT  && resultCode == -1 && data != null)
        {

            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = getActivity().getContentResolver();

            try
            {
                //讀取照片，型態為Bitmap
                BitmapFactory.Options mOptions = new BitmapFactory.Options();
                //Size=2為將原始圖片縮小1/2，Size=4為1/4，以此類推
                mOptions.inSampleSize = 2;
                // raw data to bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,mOptions);


                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                if(bitmap.getWidth()>bitmap.getHeight())
                    ScalePic(bitmap, mPhone.heightPixels);
                else ScalePic(bitmap,mPhone.widthPixels);
            }
            catch (FileNotFoundException e)
            {
            }
            nextPage();
        }



    }
    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(new Date());
        String imageFileName = "IMAGE" + timeStamp + "_";
        //  Get a top-level shared/external storage directory for placing files of a particular type.

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        // Create image  ** path => where to create image
        //File image = File.createTempFile(imageFileName, ".jpg", path);
        File image = new File(path,imageFileName+".jpg");

        mImageFileLocation = image.getAbsolutePath();
        Log.e("123", mImageFileLocation);
        return image;

    }
    private void ScalePic(Bitmap bitmap,int phone)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例

            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            mImg.setImageBitmap(mScaleBitmap);
        }
        else mImg.setImageBitmap(bitmap);
    }
    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mImg.setImageBitmap(rotatedBitmap);
    }
    public Bitmap setReducedImageSize() {

        int targetImageViewWidth = mImg.getWidth();
        int targetImageViewHeight = mImg.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true; // no bitmap

        BitmapFactory.decodeFile(mImageFileLocation, bmOptions); // Decode a file path into a bitmap
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
    }
    public void nextPage(){
        if(mImg.getDrawable()!=null){
            photoCall.setVisibility(View.INVISIBLE);
            cameraCall.setVisibility(View.INVISIBLE);
            nextStep.setVisibility(View.VISIBLE);



        }




    }
*/
}