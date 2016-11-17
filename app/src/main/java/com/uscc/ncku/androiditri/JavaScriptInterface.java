package com.uscc.ncku.androiditri;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.uscc.ncku.androiditri.fragment.MapFragment;

public class JavaScriptInterface {
	// Debugging
	private static final String TAG = "JavaScriptInterface";
	private static final boolean D = true;

	public static final int DOCUMENTLOAD = 0;
	public static final int SVGLOAD = 1;
	public static final int MNREGION_CLICKED = 2;
	public static final int MAREGION_CLICKED = 3;

	//    private Context mContext;
	private String onRegionChanged;
	private MapFragment mapFragment;

    /** Instantiate the interface and set the context */
    public JavaScriptInterface(MapFragment Fragment) {
		mapFragment = Fragment;
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
	public void onMNRegionClick(int regionNumber) {
		if (D) Log.d(TAG, "onMNRegionClick");
		mapFragment.getJsHandler().obtainMessage(MNREGION_CLICKED, regionNumber, -1).sendToTarget();
	}
	@JavascriptInterface
	public void onMARegionClick(int regionNumber) {
		if (D) Log.d(TAG, "onMARegionClick");
		mapFragment.getJsHandler().obtainMessage(MAREGION_CLICKED, regionNumber, -1).sendToTarget();
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
		mapFragment.getJsHandler().obtainMessage(DOCUMENTLOAD).sendToTarget();
	}
	@JavascriptInterface
	public void onSVGLoadDone(){
		mapFragment.getJsHandler().obtainMessage(SVGLOAD).sendToTarget();
	}
}
