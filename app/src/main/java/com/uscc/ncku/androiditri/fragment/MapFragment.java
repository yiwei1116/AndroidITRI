package com.uscc.ncku.androiditri.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.JavaScriptInterface;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        WebView mapWVive = (WebView) v.findViewById(R.id.webview_map);

        mapWVive.setWebChromeClient(new WebChromeClient());
        mapWVive.setWebViewClient(new WebViewClient());
        JavaScriptInterface jsInterface = new JavaScriptInterface();
        mapWVive.addJavascriptInterface(jsInterface, "Android");
        mapWVive.setHorizontalScrollBarEnabled(false);
        mapWVive.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mapWVive.setVerticalScrollBarEnabled(false);
        mapWVive.setHorizontalScrollBarEnabled(false);

        WebSettings settings = mapWVive.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);  // do not remove this
        settings.setAllowFileAccessFromFileURLs(true); // do not remove this
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

//        mapWVive.setInitialScale(100);

//        mapWVive.loadUrl("file:///android_asset/living_1f.svg");
        String aURL = "file:///android_asset/index.html";
        String scriptHtml = "<script>document.location =\"" + aURL + "\";</script>";
        mapWVive.loadDataWithBaseURL(aURL, scriptHtml, "text/html", "utf-8", null);

        String call = "javascript:sayHello()";
        mapWVive.loadUrl(call);

        return v;
    }
}
