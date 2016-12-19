package org.tabc.living3.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.BuildConfig;
import org.tabc.living3.JavaScriptInterface;
import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.ble.BLEModule;
import org.tabc.living3.ble.BLEScannerWrapper;
import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.DatabaseUtilizer;
import org.tabc.living3.util.SQLiteDbManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private static final String TOUR_INDEX = "TOUR_INDEX";

    private int tourIndex;


    private static final String TAG = "MapFragment";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    DisplayMetrics metrics = new DisplayMetrics();
    JavaScriptInterface mJavaScriptInterface;
    private WebView mWebViewMap;
    private RelativeLayout notice;      //進入導覽
    private TextView txtMapArea;        //顯示當前區域名稱
    private BLEScannerWrapper mBLEScannerWrapper;
    private SQLiteDbManager dbManager;
    private JSONObject mLastSacnBeacon = null;      //紀錄上一個偵測到的beacon
    private JSONObject mCurrentFieldMap = null;     //紀錄SVG檔
    private String mSvgFile = "";
    private String mBGFile = "";
    private String mFileDirPath;
    private Boolean isSVGLOADED = false;

    private int mCurrentField = 1;
    private int mCurrentZone = 1;
    private int currentZoneOrder = 0;
    //private int zoneOrder[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
    private ArrayList<Integer> zoneOrder2 = new ArrayList<>();
    private ArrayList<String> pathOrder;
    private boolean isEnglish;

    private float scaleLevel;

    public MapFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(int param1) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(TOUR_INDEX, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourIndex = getArguments().getInt(TOUR_INDEX);
        }

        ((MainActivity) getActivity()).showMapCoachInfo();

        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        mFileDirPath = String.valueOf(getActivity().getFilesDir()) + "/itri/";
        pathOrder = dbManager.querySvgId();
        isEnglish = ((MainActivity) getActivity()).isEnglish();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ((MainActivity) getActivity()).setMapActive();
        ((MainActivity) getActivity()).setToolbarTitle(R.string.nothing);

        mWebViewMap = (WebView) view.findViewById(R.id.webview_map);


        //initial img file path
        try {
            mCurrentFieldMap = dbManager.queryFieldMapWithFieldMapId(mCurrentField);
        }catch (JSONException e){
            e.printStackTrace();
        }
        mSvgFile = mCurrentFieldMap.optString("map_svg");
        if (isEnglish && mCurrentFieldMap.optString("map_svg_en") != null)
            mSvgFile = mCurrentFieldMap.optString("map_svg_en");

        mBGFile = mCurrentFieldMap.optString("map_bg");
        //initial zone order
        try{
            zoneOrder2.add(dbManager.queryBeaconFileWithZoneId(1).optInt("start"));
            for(int i=1 ;  ;i++) {
                zoneOrder2.add(dbManager.queryBeaconFileWithZoneId(i).optInt("end"));
                if (zoneOrder2.get(i) == 19)           //討論終點end條件
                    break;
            }
        }catch(JSONException e){}

        // set toolbar title
        String field_name = getFieldName(mCurrentField);
        ((MainActivity) getActivity()).setToolbarTitle(field_name);

        buildBLEScannerWrapper();
        initWebView();

        String aURL = "file:///android_asset/index.html";
        String scriptHtml = "<script>document.location =\"" + aURL + "\";</script>";
        mWebViewMap.loadDataWithBaseURL(aURL, scriptHtml, "text/html", "utf-8", null);

        //ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        setHasOptionsMenu(true);
        toolbar.setNavigationIcon(R.drawable.icon_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());
                Dialog dialog = new Dialog(getActivity(), R.style.selectorDialog);
                dialog.setContentView(R.layout.alertdialog_map_info);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
                dialog.getWindow().getAttributes().x = 30;
                dialog.getWindow().getAttributes().y = 140;
                dialog.getWindow().setDimAmount(0.6f);

                dialog.show();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FeedbackFragment feedback = FeedbackFragment.newInstance(mCurrentField);
                feedback.feedbackAlertDialog(getActivity(), feedback);
                return true;
            }
        });

        notice = (RelativeLayout) view.findViewById(R.id.rlayout_map_area);

        Button cancel = (Button) view.findViewById(R.id.btn_cancel_map_area);
        Button enter = (Button) view.findViewById(R.id.btn_enter_map_area);
        txtMapArea = (TextView) view.findViewById(R.id.txt_map_area);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());
                notice.setVisibility(View.GONE);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());
                notice.setVisibility(View.GONE);
                enterZone();
            }
        });

        return view;
    }
    public void enterZone()
    {
        ((MainActivity) getActivity()).setMapNormal();

        AreaFragment areaFragment = AreaFragment.newInstance(tourIndex,mCurrentZone);
        ((MainActivity) getActivity()).replaceFragment(areaFragment);
    }

    private String getFieldName(int field_id) {
        Cursor cursor = dbManager.getFieldMap(field_id);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(DatabaseUtilizer.NAME_EN));
    }

    private String getZoneName(int zone_id) {
        Cursor cursor = dbManager.getReadableDatabase().rawQuery("SELECT name, name_en from zone where zone_id=" + zone_id + "", null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(isEnglish ?
                DatabaseUtilizer.NAME_EN : DatabaseUtilizer.NAME));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_questionnaire, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
        ((MainActivity) getActivity()).setMapNormal();
    }


    @Override
    public void onResume() {
        super.onResume();
        mBLEScannerWrapper.startBLEScan();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBLEScannerWrapper.stopBLEScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }

    private void initWebView() {
        mWebViewMap.setWebChromeClient(new WebChromeClient());
        mWebViewMap.setWebViewClient(new WebViewClient(){
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                scaleLevel = newScale;
            }
        });
        mJavaScriptInterface = new JavaScriptInterface(this);
        mWebViewMap.addJavascriptInterface(mJavaScriptInterface, "Android");
        mWebViewMap.setHorizontalScrollBarEnabled(false);
        mWebViewMap.setVerticalScrollBarEnabled(false);
        mWebViewMap.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebViewMap.setBackgroundColor(Color.TRANSPARENT);
        mWebViewMap.setInitialScale(180);

        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            mWebViewMap.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebViewMap.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        WebSettings websettings = mWebViewMap.getSettings();
        websettings.setJavaScriptEnabled(true);
        //websettings.setSupportZoom(true);  // do not remove this
        websettings.setBuiltInZoomControls(true);
        websettings.setDisplayZoomControls(false);  //顯示放大縮小 controller
        websettings.setAllowFileAccessFromFileURLs(true); // do not remove this
        websettings.setSupportMultipleWindows(false);
        websettings.setJavaScriptCanOpenWindowsAutomatically(false);
        websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        websettings.setLoadWithOverviewMode(true);
        websettings.setUseWideViewPort(true);


    }

    private void buildBLEScannerWrapper(){
        try {
            mBLEScannerWrapper = new BLEScannerWrapper(this.getActivity(), bleHandler);
            mBLEScannerWrapper.setScanDeviceName("CC2541BLEBroadcaster");
            // from 0 to 100
            mBLEScannerWrapper.setPowerLevelWarningThreshold(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Handler bleHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                String mac = "";

                switch (msg.what) {
                    case BLEModule.BLE_SCAN_DONE:
                        // get region number from mac address (device.getAddress())
                        if (mBLEScannerWrapper.getTheHighestRssiDevice() != null) {
                            mac = mBLEScannerWrapper.getTheHighestRssiDevice().getAddress();
                            if(mac == null)
                                return;
                            Log.d("Highest RSSI Mac = ", mac);
                        }
                        if (mBLEScannerWrapper.getTheLowestRssiDevice() != null) {
                            Log.d("Lowest RSSI Mac = ", mBLEScannerWrapper.getTheLowestRssiDevice().getAddress());
                        }

                        if(!true) {        ////////// isDownload need to modify and
                            // Project not download yet.
                            return;
                        }
                        if(!isSVGLOADED){   //svg 尚未讀取完畢
                            return;
                        }
                        if(mLastSacnBeacon != null){
                            if(mac.equals(mLastSacnBeacon.optString("mac_addr"))){
                                // 跟上次的Beacon 一樣
                                return;
                            }
                        }
                        // query region id from database
                        if(mac.equals(""))
                            return;
                        JSONObject beacon = dbManager.queryBeaconFileWithMacAddr(mac);

                        if (beacon == null) {
                            return;
                        }

                        if (beacon.optInt("zone") != zoneOrder2.get(currentZoneOrder))
                        {
                            //不是該到的地方
                            return;
                        }
                        if(currentZoneOrder == zoneOrder2.size()-1)      //已到最後一個點
                            return;

                        enterNextZone(beacon);


                        break;
                    case BLEModule.BLE_LOW_POWER_WARNING:

                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void enterNextZone(JSONObject beacon) throws JSONException
    {
        mCurrentZone = beacon.optInt("zone");
        currentZoneOrder++;     //更新下一個該到的順序
        //Log.e(TAG,"scaleLevel: "+scaleLevel);

        mWebViewMap.loadUrl("javascript: setScreenFocus("+mCurrentZone+","+metrics.widthPixels+","+metrics.widthPixels+","+scaleLevel+")");

        String currentPath = (currentZoneOrder<=1)?"":pathOrder.get(currentZoneOrder-2);
        String nextPath = pathOrder.get(currentZoneOrder-1);
        notice.setVisibility(View.VISIBLE);
        txtMapArea.setText(getZoneName(mCurrentZone));     //"進入導覽"顯示名稱

        if (mLastSacnBeacon != null && mLastSacnBeacon.optInt("field_id") != beacon.optInt("field_id")) {
            //Change field
            mCurrentField = beacon.optInt("field_id");
            mSvgFile = beacon.getString("map_svg");
            String loadfile = mFileDirPath + mSvgFile;

            // set toolbar title
            String field_name = getFieldName(mCurrentField);
            ((MainActivity) getActivity()).setToolbarTitle(field_name);

            //Log.e(TAG, "javascript: setSVGLoad('" + loadfile + "'," + currentZone + "," + zoneOrder[currentZoneOrder] + ")");
            //svg,currentZone,nextZone,currentPath(to hide),nextPath(to appear)
            String url = "javascript: setSVGLoad('" + loadfile + "'," + mCurrentZone + "," + zoneOrder2.get(currentZoneOrder) + ",'"+currentPath+"','"+nextPath+"')";
            mWebViewMap.loadUrl(url);
            isSVGLOADED = false;


        } else {
            if (mJavaScriptInterface.getOnRegionChanged() != null && !mJavaScriptInterface.getOnRegionChanged().equals("")) {
                String url = "javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + mCurrentZone + "," + zoneOrder2.get(currentZoneOrder) + ",'"+currentPath+"','"+nextPath+ "')";
                mWebViewMap.loadUrl(url);
            }
        }
        mLastSacnBeacon = beacon;
        //Log.e("start,end","start: "+mLastSacnBeacon.optInt("start")+"   end:"+mLastSacnBeacon.optInt("end"));
    }
    public Handler getJsHandler() {
        return jsHandler;
    }

    private final Handler jsHandler =
            new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case JavaScriptInterface.DOCUMENTLOAD:
                            String js;

                            if(mSvgFile.length() == 0){
                                js = "javascript: loadSVGLater()";
                                mWebViewMap.loadUrl(js);
                            }else{

                                //String loadfile =  "file:///android_asset/" + mBGFile ;
                                String loadfile =  mFileDirPath + mBGFile ;
                                js = "javascript: setBG('url(" + loadfile + ")')";
                                mWebViewMap.loadUrl(js);

                                loadfile =  mFileDirPath + mSvgFile ;
                                js = "javascript: setSVGLoad('" + loadfile + "',-1,1,'','')";
                                isSVGLOADED = false;
                                mWebViewMap.loadUrl(js);
                            }

                            break;
                        case JavaScriptInterface.SVGLOAD:
                            isSVGLOADED = true;
                            mWebViewMap.loadUrl("javascript: setTestClick()");
                            mWebViewMap.loadUrl("javascript: setScreenFocus("+ (mCurrentZone==0?1:mCurrentZone) + ","+metrics.widthPixels+","+metrics.widthPixels+","+mWebViewMap.getScale()+")");
                            String currentPath = (currentZoneOrder<=1)?"":pathOrder.get(currentZoneOrder-2);
                            String nextPath = (currentZoneOrder<=0)?"":pathOrder.get(currentZoneOrder-1);
                            for(int i = 0; i<currentZoneOrder;i++)
                            {
                                if (mJavaScriptInterface.getOnRegionChanged() != null && !mJavaScriptInterface.getOnRegionChanged().equals("")) {

                                    mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + zoneOrder2.get(i) + "," + zoneOrder2.get(i+1) + ",'"+currentPath+"','"+nextPath+ "')");
                                }
                            }
                            //mWebViewMap.loadUrl("javascript: onRegionChanged(1,2)");
                            break;
                        case JavaScriptInterface.MNREGION_CLICKED:
                            try {
                                //Log.e("aaaaa",msg.arg1+"");
                                JSONObject beacon;
                                if(msg.arg1 == 11)      //for click demo
                                {
                                    currentZoneOrder++;
                                    beacon = dbManager.queryBeaconFileWithZoneId(12);
                                }else if(msg.arg1 == 19)      //for click demo
                                {
                                    currentZoneOrder = 0;
                                    beacon = dbManager.queryBeaconFileWithZoneId(1);
                                }else
                                    beacon = dbManager.queryBeaconFileWithZoneId(msg.arg1);
                                enterNextZone(beacon);
                            }catch (JSONException e){}
                            //int reg =  msg.arg1+1;
                            //mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + reg + ")");
                            break;
                        case JavaScriptInterface.MAREGION_CLICKED:
                            mCurrentZone = msg.arg1;
                            enterZone();
                            break;
                    }
                }
            };
}