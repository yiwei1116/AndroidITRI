package com.uscc.ncku.androiditri.util;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Lin on 2016/9/19.
 */
public class JavaScriptInterface {
    // Debugging
    private static final String TAG = "JavaScriptInterface";
    private static final boolean D = true;

    public static final int DOCUMENTLOAD = 0;
    public static final int REGION_CLICKED = 1;


    //    private Context mContext;
    private String onRegionChanged;
//    private ZoneActivity mainActivity;

    /** Instantiate the interface and set the context */
    public JavaScriptInterface() {
//        mainActivity = activity;
    }

    /** Show a toast from the web page */
    public void showToast(String toast) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        //Log.d("toast", toast);
        sendMsg(toast);
    }

    private void sendMsg(String msg){
		/*
		Message status = mainActivity.getHandler().obtainMessage();
		Bundle data = new Bundle();
	  	data.putString(KentecSystem.VIDEO_URL, msg);
	  	status.setData(data);
	  	mainActivity.getHandler().sendMessage(status);
	  	*/
    }

    @JavascriptInterface
    public void onRegionClick(int regionNumber) {
        if (D) Log.d(TAG, "onRegionClick");
//        mainActivity.getJsHandler().obtainMessage(REGION_CLICKED, regionNumber, -1).sendToTarget();
    }

    public String getOnRegionChanged() {
        return onRegionChanged;
    }

    // called by javascript
    @JavascriptInterface
    public void setOnBleChanged(String onRegionChanged) {
        this.onRegionChanged = onRegionChanged;
    }

    @JavascriptInterface
    public void onDocumentLoadDone(){
//        mainActivity.getJsHandler().obtainMessage(DOCUMENTLOAD).sendToTarget();
    }
}
