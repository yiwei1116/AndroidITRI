package com.uscc.ncku.androiditri.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link MergeTemplatePic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MergeTemplatePic extends Fragment implements View.OnClickListener {
    private String mPath;
    private String templateIndex;
    private String WriteContext,BuildContext;
    private String photoUri,picPath;
    private ImageView mergeImage,qrcodeImage,pic;
    private TextView textView;
    private Button icDownload,savePhone,sendMail,backTour;
    private Toolbar toolbar;
    private Bitmap mBitmap;
    private LinearLayout mask,function;
    private File imageFile;
    private  int minX,minY,picWidth,picHeight;
    private RelativeLayout layout;
    int width,length;
    Bundle bundle1;
    private static final int[] Template_Image = {
            R.drawable.template_1,
            R.drawable.template_2,
    };
    public MergeTemplatePic() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MergeTemplatePic.
     */
    public static MergeTemplatePic newInstance() {
        MergeTemplatePic fragment = new MergeTemplatePic();
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
        ((MainActivity) getActivity()).transparateToolbar();
        ((MainActivity) getActivity()).setToolbarTitle(R.string.nothing);

        toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        /**
         * 下面OOM Failed to allocate a 8294412 byte allocation with 7047616 free bytes and 6MB until OOM
         */
        View view =  inflater.inflate(R.layout.fragment_merge_template_pic, container, false);
        //FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.mergeFramelayout);
        layout = (RelativeLayout)view.findViewById(R.id.pic_location);
        mask = (LinearLayout)view.findViewById(R.id.mask);
        function = (LinearLayout)view.findViewById(R.id.function);
        textView = (TextView)view.findViewById(R.id.context);
        mergeImage = (ImageView)view.findViewById(R.id.mergeImage);
        qrcodeImage = (ImageView)view.findViewById(R.id.QRcode);
        icDownload = (Button)view.findViewById(R.id.ic_download);
        savePhone = (Button)view.findViewById(R.id.savetoPhone);
        sendMail = (Button)view.findViewById(R.id.sendMail);
        backTour = (Button)view.findViewById(R.id.backtoMain);

        savePhone.setOnClickListener(this);
        sendMail.setOnClickListener(this);
        backTour.setOnClickListener(this);
        icDownload.setOnClickListener(this);

         bundle1 = getArguments();
       if(bundle1 != null) {
           templateIndex = (String) getArguments().get("TemplateNum");
           mergeImage.setImageResource(Template_Image[Integer.valueOf(templateIndex).intValue()]);
           WriteContext = (String) getArguments().get("WriteContext");
           BuildContext = (String) getArguments().get("BuildContext");
           picPath = (String) getArguments().get("picPath");
           minX = Integer.valueOf((String) getArguments().get("minX"));
           minY = Integer.valueOf((String) getArguments().get("minY"));
           width =Integer.valueOf((String) getArguments().get("weight"));
           length = Integer.valueOf((String) getArguments().get("height"));

           if (WriteContext != null) {
               textView.setText(WriteContext);
           }
           Log.e("textview1",textView.getText().toString());
           if (BuildContext != null) {
               textView.setText(BuildContext);
           }
           Log.e("textview2",textView.getText().toString());
       }
        textView.setVisibility(View.VISIBLE);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        icDownload.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);

        try {
            mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + now + ".jpeg";
            Log.e("mPath",mPath);
            //藉由View來Cache全螢幕畫面後放入Bitmap
            View mView = getActivity().getWindow().getDecorView();
            mView.setDrawingCacheEnabled(true);
            mView.buildDrawingCache();
            Bitmap mFullBitmap = mView.getDrawingCache();

            //取得系統狀態列高度
            Rect mRect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);
            int mStatusBarHeight = mRect.top;

            //取得手機螢幕長寬尺寸
            int mPhoneWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            int mPhoneHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

            //將狀態列的部分移除並建立新的Bitmap
             mBitmap = Bitmap.createBitmap(mFullBitmap, 0, mStatusBarHeight, mPhoneWidth, mPhoneHeight - mStatusBarHeight);
            //將Cache的畫面清除
            mView.destroyDrawingCache();
            //openScreenshot(imageFile);
            icDownload.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    private void generateQRcode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(mPath, BarcodeFormat.QR_CODE,70,70);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
    private void savetoPhone(){
         imageFile = new File(mPath);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            generateQRcode();

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
    private void sendEmail() {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm") ||
                        info.activityInfo.name.toLowerCase().contains("com.google.android.gm")
                        )
                {
                    share.putExtra(Intent.EXTRA_SUBJECT,  "");
                    share.putExtra(Intent.EXTRA_TEXT,    "");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(mPath)) ); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }

            }
            if (!found)
                return;
            startActivity(Intent.createChooser(share, "Select"));
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.ic_download:
                takeScreenshot();
                showStoreLayout();

                break;
            case R.id.savetoPhone:
                savetoPhone();
                //openScreenshot(imageFile);
                showStoreSuccess();
                break;
            case R.id.sendMail:
                savetoPhone();
                sendEmail();
        }

    }
    private void showStoreSuccess(){

        final Dialog dialog = new Dialog(getActivity(), R.style.dialog_store_success);
        dialog.setContentView(R.layout.store_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }
    private void showStoreLayout(){


        icDownload.setVisibility(View.GONE);
        //textView.setVisibility(View.GONE);
        mask.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.info_fade_in);
        function.setVisibility(View.VISIBLE);
        function.setAnimation(fadeIn);
    }
       private void init() {

           pic = new ImageView(getActivity());
           pic.setImageURI(Uri.parse(picPath));

            /*pic.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    pic.setImageURI(Uri.parse(photoUri));
                    pic.buildDrawingCache();
                    Bitmap bmp = pic.getDrawingCache();

                    picWidth = bmp.getWidth();
                    picHeight = bmp.getHeight();
                    float scaleWidth = ((float) width) / picWidth;
                    float scaleHeight = ((float) length) / picHeight;
                    Matrix matrix = new Matrix();

                    Log.e("width",String.valueOf(width));
                    Log.e("length", String.valueOf(length));
                    Log.e("picWidth",String.valueOf(picWidth));
                    Log.e("picHeight", String.valueOf(picHeight));
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的圖片
                    Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, picWidth, picHeight, matrix, true);
                    pic.setImageBitmap(newbm);
                    return true;
                }
            });*/
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, length);
            params.leftMargin = minX;
            params.topMargin = minY;
            layout.addView(pic, params);

        }
}
