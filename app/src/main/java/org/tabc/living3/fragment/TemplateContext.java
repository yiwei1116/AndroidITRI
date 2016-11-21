package org.tabc.living3.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.tabc.living3.CommunicationWithServer;
import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.SQLiteDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TemplateContext#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateContext extends Fragment {
    private ToggleButton writeContext,buildContext;
    private EditText editText;
    private RadioGroup radiogroup1;
    private FrameLayout write,build;
    private String templateIndex , photoUri,picPath;
    private String textBulid ;
    MergeTemplatePic MTP;
    private String StringContext;
    private  Bundle bundle1;
    private  Spinner spinner;
    private RadioButton radioSexButton;
    private int selectedId;
    public String db_name = "android_itri_1.db";
    public String table_name_zone = "zone";
    public String table_name_hipster_text = "hipster_text";
    private SQLiteDbManager dbManager;
    private SQLiteDatabase db;
    private Cursor cursor_zone;
    private Cursor cursor_hipster_text;
    private ArrayList<String> arrayList_zone_name = new ArrayList<String>();
    private ArrayList<String> arrayList_hipster_text = new ArrayList<String>();
    private int minX,minY,maxX,maxY;
    private int width,length;
    private Button btnNextStep;
    private List<String> imageList = new ArrayList<>();
    private CommunicationWithServer communicationWithServer = new CommunicationWithServer();
    private HelperFunctions helperFunctions = new HelperFunctions();
    private Activity activity;
    private ArrayList<Bitmap> bitmapArray;
    private SharedPreferences settings;
    private static final String sIndex = "sIndex";
    private static final String sMinX = "sMinX";
    private static final String sMinY = "sMinY";
    private static final String sLength = "sLength";
    private static final String sWidth = "sWidth";
    private static final String data = "DATA";
    private boolean flag;
    private DisplayMetrics metrics;
    private Bitmap sourceBitmap;
    public TemplateContext() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TemplateContext.
     */
    public static TemplateContext newInstance() {
        TemplateContext fragment = new TemplateContext();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new SQLiteDbManager(getActivity(), db_name);
        db = dbManager.getReadableDatabase();
        cursor_zone = db.query(table_name_zone, null, null, null, null, null, null);
        cursor_hipster_text = db.query(table_name_hipster_text,null,null,null,null,null,null);


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_context, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner_item);

        spinnerArea();

        ((MainActivity) getActivity()).setToolbarTitle(R.string.text_master);

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        imageList = dbManager.getHipsterTemplateDownloadFiles();
        bitmapArray = new ArrayList<Bitmap>();
        for(int i=0;i<imageList.size();i++){
            try {
                bitmapArray.add(helperFunctions.getBitmapFromFile(getActivity(),imageList.get(i)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.e("imageList",imageList.get(i));

        }

        writeContext = (ToggleButton)view.findViewById(R.id.writeContext) ;
        buildContext = (ToggleButton)view.findViewById(R.id.buildContext) ;

        writeContext.setBackgroundResource(R.drawable.btn_left_active);
        buildContext.setBackgroundResource(R.drawable.btn_right_normal);
        editText = (EditText)view.findViewById(R.id.edit);

        radiogroup1 = (RadioGroup)view.findViewById(R.id.rGroup);
        writeContext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (writeContext.isChecked()) {
                    buildContext.setChecked(false);
                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.setHint(null);
                        }
                    });
                    write.setVisibility(View.VISIBLE);
                    build.setVisibility(View.GONE);
                    writeContext.setBackgroundResource(R.drawable.btn_left_active);
                    buildContext.setBackgroundResource(R.drawable.btn_right_normal);
                    flag = false;

                    Log.e("textBulid", String.valueOf(isChecked));


                }
            }
        });
        buildContext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buildContext.isChecked()) {
                    writeContext.setChecked(false);
                    write.setVisibility(View.GONE);
                    build.setVisibility(View.VISIBLE);
                    writeContext.setBackgroundResource(R.drawable.btn_left_normal);
                    buildContext.setBackgroundResource(R.drawable.btn_right_active);
                    flag = true;

                    Log.e("buildtext", String.valueOf(isChecked));


                }
            }
        });
        getTemplateContext();
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                    if (btn.getId() == checkedId) {
                        textBulid = (String) btn.getText();
                        Log.e("String", textBulid);
                        return;
                    }
                }
            }
        });
        write = (FrameLayout)view.findViewById(R.id.write);
        build = (FrameLayout)view.findViewById(R.id.build);
        final Bundle bundle = getArguments();
        if (bundle != null) {
            templateIndex = (String)getArguments().get("Template");
            //photoUri = (String)getArguments().get("photoUri");
            picPath = (String)getArguments().get("picPath");


        }
        settings = getActivity().getSharedPreferences(templateIndex,Context.MODE_APPEND);
        btnNextStep = (Button)view.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.camera_btn_select);
         activity = getActivity();
        if(activity != null && isAdded()) {
                new CalcPosition().execute();
        }




        return view;
    }

    public void spinnerArea(){


        arrayList_zone_name.add(getResources().getString(R.string.spinner_please_select));
        getZone();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arrayList_zone_name);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }

    public void getZone() {
        for (cursor_zone.moveToFirst(); !cursor_zone.isAfterLast(); cursor_zone.moveToNext()) {

            arrayList_zone_name.add(cursor_zone.getString(cursor_zone.getColumnIndex("name")));


        }

        cursor_zone.close();

    }
    public void getTemplateContext(){


        for (cursor_hipster_text.moveToFirst(); !cursor_hipster_text.isAfterLast(); cursor_hipster_text.moveToNext()) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setText(cursor_hipster_text.getString(cursor_hipster_text.getColumnIndex("content")));
                radioButton.setTextSize(20);
                radiogroup1.addView(radioButton);
            //arrayList_hipster_text.add(cursor_hipster_text.getString(cursor_hipster_text.getColumnIndex("content")));

               // Log.e("hipster_text",arrayList_hipster_text.get(cursor_hipster_text.getPosition()));
        }

        cursor_hipster_text.close();



    }

    class CalcPosition extends AsyncTask<Void, Void, HashMap> {

        @Override
        protected void onPostExecute(HashMap hashMap) {
            super.onPostExecute(hashMap);
            btnNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MTP = new MergeTemplatePic();
                    bundle1 = new Bundle();
                    if(!flag){
                    StringContext = editText.getText().toString().trim();
                    }
                    else{
                        StringContext = textBulid;
                    }
                    if(StringContext==null){
                        StringContext = "";
                    }
                    bundle1.putString("TemplateNum", templateIndex);
                    bundle1.putString("StringContext", StringContext);
                    bundle1.putString("minX", String.valueOf(minX));
                    bundle1.putString("minY", String.valueOf(minY));
                    bundle1.putString("weight", String.valueOf(width));
                    bundle1.putString("height", String.valueOf(length));
                    bundle1.putString("picPath", picPath);
                    MTP.setArguments(bundle1);
                    ((MainActivity) getActivity()).replaceFragment(MTP);
                }
            });
        }

        @Override
        protected HashMap doInBackground(Void... params) {
            /*
                Important!!!
                this drawable must put in drawable-nodpi, or there might be a wrong calculation.
            */
            Log.e("templateIndex",String.valueOf(templateIndex));
            Log.e("sIndex",settings.getString(sIndex, ""));
            if(String.valueOf(templateIndex).equals(settings.getString(sIndex, ""))){
                readData();
            }
            else {
                if (activity != null && isAdded()) {
                    metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    sourceBitmap = bitmapArray.get((Integer.valueOf(templateIndex)));
                    float scaleX = pxComputing(metrics.widthPixels, sourceBitmap.getWidth());
                    float scaleY = pxComputing(metrics.heightPixels, sourceBitmap.getHeight());


                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleX, scaleY);
                    // 得到新的圖片
                    Bitmap scaleBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
                    minX = scaleBitmap.getWidth();
                    minY = scaleBitmap.getHeight();
                    maxX = -1;
                    maxY = -1;
                    int areaY = 2 * (scaleBitmap.getHeight()) / 3;
                    int startX = (scaleBitmap.getWidth()) / 8;
                    int startY = (scaleBitmap.getHeight()) / 8;

                    Log.e("widthPixels", String.valueOf(metrics.widthPixels));
                    Log.e("heightPixels", String.valueOf(metrics.heightPixels));


            /*    Log.e("scaleX",df.format(c));
               Log.e("scaleY",Float.toString(scaleY));*/
                    Log.e("Tem_getWidth()", String.valueOf(scaleBitmap.getWidth()));
                    Log.e("Tem_getHeight()", String.valueOf(scaleBitmap.getHeight()));
                    Log.e("areaY", String.valueOf(areaY));
                    for (int y = startY; y < areaY; y++) {
                        for (int x = startX; x < scaleBitmap.getWidth(); x++) {

                            int alpha = scaleBitmap.getPixel(x, y) >> 24;

                            if (alpha == 0) {
                                if (x < minX)
                                    minX = x;
                                if (x > maxX)
                                    maxX = x;
                                if (y < minY)
                                    minY = y;
                                if (y > maxY)
                                    maxY = y;
                            }

                        }
                    }

                    width = Math.round(maxX - minX);
                    length = Math.round(maxY - minY);
                    minY = minY - 20;
                    //.minY = minY - getStatusBarHeight();
                    Log.e("px_new", String.format("minX: %d, minY: %d, maxX: %d, maxY %d,width %d,length %d", minX, minY, maxX, maxY, width, length));
                    saveData();
                    Log.e("iii","123");
                }
            }
            return null;
        }

    }

   /* public static float convertPixelToDp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }
    *//**
     * 取得螢幕密度
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     *//*
    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }*/
    public int getStatusBarHeight() {
        int result = 0;
         activity = getActivity();
        if(activity != null && isAdded()){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
        }}
        Log.e("status_bar_height", String.valueOf(result));
        return result;
    }



    public void saveData(){

        settings.edit()
                .putString(sIndex,templateIndex)
                .putString(sMinX,String.valueOf(minX))
                .putString(sMinY, String.valueOf(minY))
                .putString(sWidth, String.valueOf(width))
                .putString(sLength,String.valueOf(length))
                .apply();
    }
    public void readData(){


        {
            minX = Integer.parseInt(settings.getString(sMinX, ""));
            minY = Integer.parseInt(settings.getString(sMinY, ""));
            width = Integer.parseInt(settings.getString(sWidth, ""));
            length = Integer.parseInt(settings.getString(sLength, ""));
        }
    }
    //tempX=手機螢幕尺寸 tempY=圖片尺寸
    public float pxComputing(float tempX,float tempY ){
        DecimalFormat df=new DecimalFormat("#.###");
        float scale;
        scale = (tempX/tempY);
        Log.e("test",String.format("tempX %s , tempY %s , scale %s",df.format(tempX),df.format(tempY),df.format(scale)));
        return scale;
    }



}
