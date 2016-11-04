package com.uscc.ncku.androiditri.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uscc.ncku.androiditri.CommunicationWithServer;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


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
    private String templateIndex , photoUri;
    private String textBulid ;
    MergeTemplatePic MTP;
    private String StringContext;
    private  Bundle bundle1;
    private  Spinner spinner;
    private RadioButton radioSexButton;
    private int selectedId;
    public String db_name = "android_itri_1.db";
    public String table_name_zone = "zone";
    private SQLiteDbManager dbManager;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private int minX,minY,maxX,maxY;
    private int width,length;
    private Button btnNextStep;

    private CommunicationWithServer communicationWithServer = new CommunicationWithServer();
    private Activity activity;
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
        cursor = db.query(table_name_zone,null,null,null,null,null,null);
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
                    textBulid = null;
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
                    StringContext = null;
                    Log.e("buildtext", String.valueOf(isChecked));


                }
            }
        });

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
            photoUri = (String)getArguments().get("photoUri");

            Log.e("photoUri", photoUri);

        }
        btnNextStep = (Button)view.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.camera_btn_select);
         activity = getActivity();
        if(activity != null && isAdded()) {
            new CalcPosition().execute();
        }

        /*btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MTP = new MergeTemplatePic();
                bundle1 = new Bundle();
                StringContext = editText.getText().toString().trim();

                Log.e("StringContext", StringContext);
                bundle1.putString("TemplateNum", templateIndex);
                bundle1.putString("WriteContext", StringContext);
                bundle1.putString("BuildContext", textBulid);
                bundle1.putString("minX", String.valueOf(minX));
                bundle1.putString("minY", String.valueOf(minY));
                bundle1.putString("weight", String.valueOf(width));
                bundle1.putString("height", String.valueOf(length));
                MTP.setArguments(bundle1);
                ((MainActivity) getActivity()).replaceFragment(MTP);
            }
        });*/


        return view;
    }
 /*   public void writeText() {
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


    }
    public void buildText(){
        write.setVisibility(View.GONE);
        build.setVisibility(View.VISIBLE);
        writeContext.setBackgroundResource(R.drawable.btn_left_normal);
        buildContext.setBackgroundResource(R.drawable.btn_right_active);

    }*/
    public void spinnerArea(){


        arrayList.add("請選擇");
        getZone();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {

                /*String city = "The Area is " + parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }

    public void getZone() {
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            arrayList.add(cursor.getString(cursor.getColumnIndex("name_en")));


        }

        cursor.close();

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
                    StringContext = editText.getText().toString().trim();

                    Log.e("StringContext", StringContext);
                    bundle1.putString("TemplateNum", templateIndex);
                    bundle1.putString("WriteContext", StringContext);
                    bundle1.putString("BuildContext", textBulid);
                    bundle1.putString("photoUri", photoUri);
                    bundle1.putString("minX", String.valueOf(minX));
                    bundle1.putString("minY", String.valueOf(minY));
                    bundle1.putString("weight", String.valueOf(width));
                    bundle1.putString("height", String.valueOf(length));

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
            final int[] Template_merge = {
                    R.drawable.template_1,
                    R.drawable.template_2,
            };


            if(activity != null && isAdded()) {
                Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(), Template_merge[Integer.valueOf(templateIndex)]);
                minX = sourceBitmap.getWidth();
                minY = sourceBitmap.getHeight();
                maxX = -1;
                maxY = -1;
                int areaY = 2 * (sourceBitmap.getHeight()) / 3 ;
                int startX = (sourceBitmap.getWidth()) /8 ;
                int startY = (sourceBitmap.getHeight()) /8 ;
                Log.e("Tem_getWidth()", String.valueOf(sourceBitmap.getWidth()));
                Log.e("Tem_getHeight()", String.valueOf(sourceBitmap.getHeight()));
                Log.e("areaY", String.valueOf(areaY));
                for (int y = startY; y < areaY; y++) {
                    for (int x = startX; x < sourceBitmap.getWidth(); x++) {

                        int alpha = sourceBitmap.getPixel(x, y) >> 24;

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


                minY = minY - getStatusBarHeight();
          /*  dp_minX = Math.round(convertPixelToDp(minX, getActivity()));
            dp_minY = Math.round(convertPixelToDp(minY-getStatusBarHeight(), getActivity()));
            dp_maxX = Math.round(convertPixelToDp(maxX, getActivity()));
            dp_maxY = Math.round(convertPixelToDp(maxY, getActivity()));*/
                width = Math.round(maxX - minX);
                length = Math.round(maxY - minY);
                Log.e("px", String.format("minX: %d, minY: %d, maxX: %d, maxY %d,width %d,length %d", minX, minY, maxX, maxY, width, length));
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
        Log.e("bar", String.valueOf(result));
        return result;
    }

}
