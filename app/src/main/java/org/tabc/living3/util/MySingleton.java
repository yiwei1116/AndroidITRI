package org.tabc.living3.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Oslo on 12/1/16.
 */
public class MySingleton {

    private static MySingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingleton(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(this.context.getApplicationContext());
        }
        return this.requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new MySingleton(context);
        }
        return singleton;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
