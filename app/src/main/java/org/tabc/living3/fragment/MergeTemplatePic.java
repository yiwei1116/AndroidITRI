package org.tabc.living3.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.DatabaseUtilizer;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.SQLiteDbManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String templateIndex,templateID,templateTextID,zoneID;
    private String StringContext;
    private String photoUri,picPath;
    private ImageView mergeImage,qrcodeImage,pic,TT;
    private TextView textView;
    private Button icDownload,savePhone,sendMail,backTour;
    private ImageButton btnShowSucess;
    private Toolbar toolbar;
    private Bitmap mBitmap, qrCode;
    private LinearLayout mask,function,showSuccess;
    private File imageFile;
    private  int minX,minY,picWidth,picHeight;
    private RelativeLayout layout;
    public String db_name = "android_itri_1.db";
    private SQLiteDbManager dbManager;
    private SQLiteDatabase db;
    public String table_name_hipster_template = "hipster_template";
    private Cursor cursor_template;
    private HelperFunctions helperFunctions;
    private DatabaseUtilizer databaseUtilizer;
    int width,length;
    Bundle bundle1;

    private List<String> imageList = new ArrayList<>();

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
        dbManager = new SQLiteDbManager(getActivity(), db_name);
        db = dbManager.getReadableDatabase();
        cursor_template = db.query(table_name_hipster_template, null, null, null, null, null, null);
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
                ButtonSound.play(getActivity());
                getActivity().onBackPressed();
            }
        });

        View view =  inflater.inflate(R.layout.fragment_merge_template_pic, container, false);
        layout = (RelativeLayout)view.findViewById(R.id.pic_location);
        mask = (LinearLayout)view.findViewById(R.id.mask);
        showSuccess = (LinearLayout)view.findViewById(R.id.show_success);
        function = (LinearLayout)view.findViewById(R.id.function);
        textView = (TextView)view.findViewById(R.id.context);
        mergeImage = (ImageView)view.findViewById(R.id.mergeImage);
        qrcodeImage = (ImageView)view.findViewById(R.id.QRcode);
        icDownload = (Button)view.findViewById(R.id.ic_download);
        savePhone = (Button)view.findViewById(R.id.savetoPhone);
        sendMail = (Button)view.findViewById(R.id.sendMail);
        backTour = (Button)view.findViewById(R.id.backtoMain);
        btnShowSucess = (ImageButton)view.findViewById(R.id.check);
        TT =(ImageView)view.findViewById(R.id.test);
        savePhone.setOnClickListener(this);
        sendMail.setOnClickListener(this);
        backTour.setOnClickListener(this);
        icDownload.setOnClickListener(this);
        btnShowSucess.setOnClickListener(this);
        dbManager = new SQLiteDbManager(getActivity(), db_name);
        helperFunctions = new HelperFunctions(getActivity().getApplicationContext());
        databaseUtilizer = new DatabaseUtilizer();
        Log.e("IP",databaseUtilizer.getIP());
        imageList = dbManager.getHipsterTemplateDownloadFiles();
        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
        for(int i=0;i<imageList.size();i++){
            try {
                bitmapArray.add(helperFunctions.getBitmapFromFile(getActivity(),imageList.get(i)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
         bundle1 = getArguments();
       if(bundle1 != null) {
           zoneID = (String) getArguments().get("zoneID");
           templateTextID = (String) getArguments().get("templateTextID");
           templateIndex = (String) getArguments().get("TemplateNum");
           mergeImage.setImageBitmap(bitmapArray.get(Integer.valueOf(templateIndex)));
           StringContext = (String) getArguments().get("StringContext");
           picPath = (String) getArguments().get("picPath");
           minX = Integer.valueOf((String) getArguments().get("minX"));
           minY = Integer.valueOf((String) getArguments().get("minY"));
           width =Integer.valueOf((String) getArguments().get("weight"));
           length = Integer.valueOf((String) getArguments().get("height"));


               textView.setText(StringContext);




           Log.e("textview",textView.getText().toString());
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
     /*   Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        String now = formatter.format(new Date());
        icDownload.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);

        try {
            mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + now + ".jpeg";
            Log.e("mPath",mPath);
            //藉由View來Cache全螢幕畫面後放入Bitmap

            addImageToGallery(mPath,getActivity());
            //displayPic();
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
        String combinePicToServer = "http://" + databaseUtilizer.getIP() + "/web/media/combine_picture/";
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(combinePicToServer + getPicName(mPath), BarcodeFormat.QR_CODE,80,80);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrCode = barcodeEncoder.createBitmap(bitMatrix);
            qrcodeImage.setImageBitmap(qrCode);
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
        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share.setType("image/jpeg");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm") ||
                        info.activityInfo.name.toLowerCase().contains("com.google.android.gm")
                        )
                {
                    ArrayList<Uri> uris = new ArrayList<Uri>();
                    //convert from paths to Android friendly Parcelable Uri's

               /*     for (String file : filePaths)
                    {
                        File fileIn = new File(file);
                        Uri u = Uri.fromFile(fileIn);
                        uris.add(u);
                    }*/
                    uris.add(Uri.fromFile(new File(mPath)));
                    uris.add(getImageUri(getActivity(),qrCode));
                    share.putExtra(Intent.EXTRA_SUBJECT,  "");
                    share.putExtra(Intent.EXTRA_TEXT,    "");
                    //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(mPath)) );
                    share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);// Optional, just if you wanna share an image.
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
        ButtonSound.play(getActivity());
        switch(v.getId()) {
            case R.id.ic_download:
                takeScreenshot();
                showStoreLayout();
                Log.e("picPath",picPath);
                Log.e("mPath",mPath);
                String templateID = getTemplateID(templateIndex);
                savetoPhone();
                try {
                    helperFunctions.uploadHipster(toUtf8(StringContext),getPicName(picPath),getPicName(mPath),getPicDirPath(picPath),getPicDirPath(mPath),Integer.parseInt(templateID),Integer.parseInt(templateTextID),Integer.parseInt(zoneID));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.savetoPhone:

                showSuccess.setVisibility(View.VISIBLE);
                //openScreenshot(imageFile);
                    //showStoreSuccess();
                break;
            case R.id.sendMail:
                savetoPhone();
                sendEmail();
                break;
            case R.id.backtoMain:
                hideStoreLayout();
                break;
            case R.id.check:
                getActivity().onBackPressed();
                break;
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
        textView.setVisibility(View.GONE);
        mask.setVisibility(View.VISIBLE);
       // Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.info_fade_in);
        function.setVisibility(View.VISIBLE);
        //function.setAnimation(fadeIn);
    }
    private void hideStoreLayout(){


        icDownload.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        mask.setVisibility(View.GONE);
        function.setVisibility(View.GONE);

    }
       private void init() {

           pic = new ImageView(getActivity());
           pic.setImageURI(Uri.parse(picPath));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, length);

            params.leftMargin = minX;
            params.topMargin = minY;

            layout.addView(pic, params);
        }
    private static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    private String getTemplateID(String viewPagerIndex){

        cursor_template.moveToPosition(Integer.parseInt(viewPagerIndex));
        templateID = cursor_template.getString(cursor_template.getColumnIndex("hipster_template_id"));
        Log.e("templateID",templateID);
        cursor_template.close();
        return templateID;



    }
    //檔名
    private String getPicName(String picPath){

        String[] paths = picPath.split("/");

        String finalFile =  paths[paths.length-1];
        Log.e("finalFile",finalFile);
        return finalFile;
    }
    //到目錄
    private String getPicDirPath(String picDir){

        String dirPath=new String();



        String[] paths = picDir.split("/");
        for(int i = 0 ; i < paths.length-1 ; i++){

        dirPath += paths[i]+"/";

        }
        Log.e("dirPath",dirPath);
        return  dirPath;

    }
    private void displayPic(){
        File pic = new File(getPicDirPath(mPath),getPicName(mPath));
        Bitmap bitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(pic), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TT.setImageBitmap(bitmap);

    }
    private static String toUtf8(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes("UTF-8"),"UTF-8");
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
