package com.uscc.ncku.androiditri.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.uscc.ncku.androiditri.JavaScriptInterface;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.ble.BLEModule;
import com.uscc.ncku.androiditri.ble.BLEScannerWrapper;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONObject;


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

    JavaScriptInterface mJavaScriptInterface;
    private WebView mWebViewMap;
    private RelativeLayout notice;
    private Button cancel;
    private Button enter;
    private TextView txtMapArea;
    private String mSvgFile = "";
    private BLEScannerWrapper mBLEScannerWrapper;
    private JSONObject mLastSacnBeacon = null;
    private View view;
    private SQLiteDbManager dbManager;
    private String fileDirPath;
    private Boolean isSVGLOADED = false;

    //test
    TextView address0;

    private int currentZone;
    private int currentZoneOrder = 0;
    private int pathOrder[] = {1,2,3};
    private int zoneOrder[] = {1,2,3,4 };

    ///

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
        fileDirPath = String.valueOf(getActivity().getFilesDir()) + "/itri/";
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);

        ((MainActivity) getActivity()).setMapActive();
        ((MainActivity) getActivity()).setToolbarTitle(R.string.nothing);

        mWebViewMap = (WebView) view.findViewById(R.id.webview_map);
        address0 = (TextView)view.findViewById(R.id.device_address0);

        //test
        mSvgFile = "Living3_Map_1F_no_bg_20161020.svg";

        //

        buildBLEScannerWrapper();
        initWebView();

        String aURL = "file:///android_asset/index.html";
        String scriptHtml = "<script>document.location =\"" + aURL + "\";</script>";
        mWebViewMap.loadDataWithBaseURL(aURL, scriptHtml, "text/html", "utf-8", null);


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
                Dialog dialog = new Dialog(getActivity(), R.style.selectorDialog);
                dialog.setContentView(R.layout.alertdialog_map_info);

                dialog.show();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FeedbackFragment feedback = FeedbackFragment.newInstance(currentZone);
                feedback.feedbackAlertDialog(getActivity(), feedback);
                return true;
            }
        });

        notice = (RelativeLayout) view.findViewById(R.id.rlayout_map_area);


        //notice.setVisibility(View.GONE);

        notice.setVisibility(View.VISIBLE);


        notice.setVisibility(View.VISIBLE);
        cancel = (Button) view.findViewById(R.id.btn_cancel_map_area);
        enter = (Button) view.findViewById(R.id.btn_enter_map_area);
        txtMapArea = (TextView) view.findViewById(R.id.txt_map_area);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);

                ((MainActivity) getActivity()).setMapNormal();

                AreaFragment areaFragment = AreaFragment.newInstance(tourIndex,currentZone);
                ((MainActivity) getActivity()).replaceFragment(areaFragment);
            }
        });

        return view;
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
        mWebViewMap.setWebViewClient(new WebViewClient());
        mJavaScriptInterface = new JavaScriptInterface(this);
        mWebViewMap.addJavascriptInterface(mJavaScriptInterface, "Android");
        mWebViewMap.setHorizontalScrollBarEnabled(false);
        mWebViewMap.setVerticalScrollBarEnabled(false);
        mWebViewMap.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebViewMap.setBackgroundColor(Color.TRANSPARENT);
        //mWebViewMap.setInitialScale(180);
        WebSettings websettings = mWebViewMap.getSettings();
        websettings.setJavaScriptEnabled(true);
        //websettings.setSupportZoom(false);  // do not remove this
        websettings.setBuiltInZoomControls(true); //顯示放大縮小 controller
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
                        ///////////////////////////////

                        //address0.setText("\n\n\n當前偵測到的address: "+(mac.equals("")?"無":mac));
                        //DEBUG用
                        ///

                        ///
                        ////////////////////////////////
                        // query region id from database
                        if(mac.equals(""))
                            return;
                        JSONObject beacon = dbManager.queryBeaconFileWithMacAddr(mac);
                        /*
                        Log.e("test mac  ", beacon.optString("mac_addr"));
                        Log.e("test bid  ", beacon.optString("device_id"));
                        Log.e("test name  ", beacon.optString("name"));
                        Log.e("test zone  ", beacon.optString("zone"));
                        */
                        if (beacon == null) {
                            return;
                        }
                        currentZone = beacon.optInt("zone");        //紀錄當前Zone
                        if (currentZone != zoneOrder[currentZoneOrder])
                        {
                            //不是下一步該去的地方
                            return;
                        }
                        if(currentZoneOrder != zoneOrder.length-1)
                            currentZoneOrder++;

                        notice.setVisibility(View.VISIBLE);
                        txtMapArea.setText("   "+beacon.optString("name"));     //進入導覽顯示名稱

                        if (mLastSacnBeacon != null && mLastSacnBeacon.optInt("field_id") != beacon.optInt("field_id")) {
                            //Change field

                            String loadfile =  "file:///android_asset/" +  beacon.optString("field_name") +".svg";
                            //loadfile =  "file:///android_asset/" + mSvgFile;
                            mSvgFile = beacon.getString("map_svg");
                            loadfile = fileDirPath + mSvgFile;
                            //Log.e(TAG, "javascript: setSVGLoad('" + loadfile + "'," + currentZone + "," + zoneOrder[currentZoneOrder] + ")");
                            mWebViewMap.loadUrl("javascript: setSVGLoad('" + loadfile + "'," + currentZone + "," + zoneOrder[currentZoneOrder] + ")");
                            isSVGLOADED = false;


                        } else {
                            if (mJavaScriptInterface.getOnRegionChanged() != null && !mJavaScriptInterface.getOnRegionChanged().equals("")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + currentZone + "," + zoneOrder[currentZoneOrder] + ")");
                                Log.e(TAG,currentZone+"    "+zoneOrder[currentZoneOrder]);
                            }
                        }

                        // change map button status
                        /*
                        if (mCurrentViewMode != ViewMode.MAP) {
                            if (mSelectZone.zone_num != b.zone_num) {
                                ivMap.setBackgroundResource(R.mipmap.map_btn_flash);
                                ivMap.startAnimation(mapAnimation);
                            } else {
                                ivMap.clearAnimation();
                                ivMap.setBackgroundResource(R.mipmap.map_btn_inactive);
                            }
                        }
                        */
                        mLastSacnBeacon = beacon;
                        break;
                    case BLEModule.BLE_LOW_POWER_WARNING:

                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
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

                                //String loadfile =  "file:///android_asset/" + mSvgFile ;
                                String loadfile =  fileDirPath + mSvgFile ;
                                js = "javascript: setSVGLoad('" + loadfile + "',-1,1)";
                                isSVGLOADED = false;
                                mWebViewMap.loadUrl(js);
                            }

                            break;
                        case JavaScriptInterface.SVGLOAD:
                            isSVGLOADED = true;
                            for(int i = 0; i<currentZoneOrder;i++)
                            {
                                if (mJavaScriptInterface.getOnRegionChanged() != null && !mJavaScriptInterface.getOnRegionChanged().equals("")) {

                                    mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + zoneOrder[i] + "," + zoneOrder[i+1] + ")");
                                }
                            }
                            break;
                        case JavaScriptInterface.REGION_CLICKED:
                            //buildZoneView(msg.arg1);
                            //switchViewMode(ViewMode.ZONE);

                            //int reg =  msg.arg1+1;
                            //mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + reg + ")");
                            break;
                    }
                }
            };
}