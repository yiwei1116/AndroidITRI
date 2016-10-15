package com.uscc.ncku.androiditri.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.uscc.ncku.androiditri.util.DownloadProject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    public static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";
    private static final String TOUR_INDEX = "TOUR_INDEX";

    private int tourIndex;
    private int currentZone;

    private static final String TAG = "MapFragment";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    JavaScriptInterface mJavaScriptInterface;
    private WebView mWebViewMap;
    private RelativeLayout notice;
    private Button cancel;
    private Button enter;
    private String mSvgFile = "";
    private int mScanedField;
    private BLEScannerWrapper mBLEScannerWrapper;
    private Beacon mLastSacnBeacon = null;
    private View view;
    private DownloadProject mTourProject;

    //test
    private String lastbeacon_mac;
    TextView address0;

    private int currentPathOrder = 0;
    private int currentZoneOrder = 0;
    private int pathOrder[] = {1,2,3};
    private int zoneOrder[] = {2,3,4,5 };

    public ArrayList<Beacon> mBeaconList = new ArrayList<>();
    public ArrayList<Field> mFieldList = new ArrayList<>();
    public class Beacon
    {
        public String mac_addr;
        public int zone;
        public int field;
        public Beacon(String mac_addr,int zone,int field)
        {
            this.mac_addr = mac_addr;
            this.zone = zone;
            this.field = field;
        }
    }
    public class Field
    {
        public String map_name;
        public int id;
        public Field(int id,String map_name)
        {
            this.id = id;
            this.map_name = map_name;
        }
    }

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
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        mWebViewMap = (WebView) view.findViewById(R.id.webview_map);
        address0 = (TextView)view.findViewById(R.id.device_address0);

        //test
        lastbeacon_mac = "";
        mSvgFile = "living_1f.svg";
        ///
        mTourProject = new DownloadProject();
        mTourProject.mIsDownloadCompleted =true;

        buildBLEScannerWrapper();
        initWebView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        Toolbar toolbar = MainActivity.getToolbar();
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
                FeedbackFragment feedback = new FeedbackFragment();
                feedback.feedbackAlertDialog(getActivity(), feedback);
                return true;
            }
        });

        notice = (RelativeLayout) view.findViewById(R.id.rlayout_map_area);
        cancel = (Button) view.findViewById(R.id.btn_cancel_map_area);
        enter = (Button) view.findViewById(R.id.btn_enter_map_area);
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

                MainActivity.setMapNormal();

                AreaFragment areaFragment = AreaFragment.newInstance(tourIndex,currentZone);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.flayout_fragment_continer, areaFragment, AreaFragment.AREA_FRAGMENT_TAG);
                transaction.addToBackStack(AreaFragment.AREA_FRAGMENT_TAG);
                transaction.commit();
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
        Toolbar toolbar = MainActivity.getToolbar();
        toolbar.setNavigationIcon(R.drawable.btn_back);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBLEScannerWrapper.startBLEScan();
        mWebViewMap.loadUrl("javascript: onResumeMap();");
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

        String aURL = "file:///android_asset/index.html";
        String scriptHtml = "<script>document.location =\"" + aURL + "\";</script>";
        mWebViewMap.loadDataWithBaseURL(aURL, scriptHtml, "text/html", "utf-8", null);
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
                //test
                mBeaconList.add(new Beacon("7C:EC:79:E5:C1:30",2,1));
                mBeaconList.add(new Beacon("98:7B:F3:5B:27:64",3,1));
                mBeaconList.add(new Beacon("98:7B:F3:5B:28:8D",15,2));
                mBeaconList.add(new Beacon("7C:EC:79:E5:C3:77",16,2));
                mFieldList.add(new Field(1,"living_1f"));
                mFieldList.add(new Field(2,"living_2f"));
                //test
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
                        if(!mTourProject.mIsDownloadCompleted) {
                            // Project not download yet.
                            return;
                        }
                        if(mLastSacnBeacon != null){
                            if(mac.equals(mLastSacnBeacon.mac_addr)){
                                // 跟上次的Beacon 一樣
                                return;
                            }
                        }
                        ///////////////////////////////

                        address0.setText("\n\n\n當前偵測到的address: "+(mac.equals("")?"無":mac)+"\n上一個偵測到的address: "+(lastbeacon_mac.equals("")?"無":lastbeacon_mac));
                        /*
                        if(!mac.equals("") && !mac.equals(lastbeacon_mac))
                        {
                            lastbeacon_mac = mac;
                            if(mac.equals("7C:EC:79:E5:C1:30")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + "2" + ")");
                            }
                            else if(mac.equals("98:7B:F3:5B:27:64")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + "3" + ")");
                            }
                            else if(mac.equals("98:7B:F3:5B:28:8D")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + "4" + ")");
                            }
                            else if(mac.equals("7C:EC:79:E5:C3:77")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + "5" + ")");
                            }
                        }
                        */
                        ////////////////////////////////
                        // query region id from database
                        Beacon b = GetZoneNum(mac);
                        if (b == null) {
                            return;
                        }
                        Log.d("ZontActivity", "b.zone.num = " + b.zone);
                        notice.setVisibility(View.VISIBLE);
                        currentZone = b.zone;
                        lastbeacon_mac = mac;
                        if (mLastSacnBeacon == null || mLastSacnBeacon.field != b.field) {
                            // Change field
                            for (Field f : mFieldList){
                                if(f.id == b.field){
                                    mScanedField = f.id;
                                    String loadfile =  "file:///android_asset/" +  f.map_name +".svg";
                                    Log.d(TAG, "javascript: setSVGLoad('" + loadfile + "'," + b.zone + ")");
                                    mWebViewMap.loadUrl("javascript: setSVGLoad('" + loadfile + "'," + b.zone + ")");

                                    break;
                                }
                            }
                        } else {
                            if (mJavaScriptInterface.getOnRegionChanged() != null && !mJavaScriptInterface.getOnRegionChanged().equals("")) {
                                mWebViewMap.loadUrl("javascript: " + mJavaScriptInterface.getOnRegionChanged() + "(" + b.zone + ")");
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
                        mLastSacnBeacon = b;
                        break;
                    case BLEModule.BLE_LOW_POWER_WARNING:

                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        private Beacon GetZoneNum(String mac){
            if(mac.length() == 0)
                return null;
            for(Beacon b: mBeaconList){
                if(mac.equals(b.mac_addr)){
                    return b;
                }
            }
            return null;
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
                            }else{
                                //String loadfile =  "file:///" + mTourProject.getStoragePath() + mSvgFile ;
                                String loadfile =  "file:///android_asset/" + mSvgFile ;
                                js = "javascript: setSVGLoad('" + loadfile + "',-1)";
                            }
                            mWebViewMap.loadUrl(js);
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