package org.tabc.living3.util;

/**
 * Created by Oslo on 2/13/17.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Foreground implements Application.ActivityLifecycleCallbacks{

    private static Foreground instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private Runnable check;
    public static final String TAG = Foreground.class.getName();

    public interface Listener {
        void onBecameForeground();

        void onBecameBackground();
    }

    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    public static Foreground init(Application app) {
        if (instance == null) {
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static Foreground get(Application app) {
        if (instance == null) {
            init(app);
        }
        return instance;
    }

    public static Foreground get(Context context) {
        if (instance == null) {
            Context appContext = context.getApplicationContext();
            if (appContext instanceof  Application) {
                init((Application)appContext);
            }
            throw new IllegalStateException(
                    "Foreground is not initialized and " + "cannot obtain the Application object"
            );
        }
        return instance;
    }

    public static Foreground get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialized and " + "cannot obtain the Application object"
            );
        }
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null) {
            handler.removeCallbacks(check);
        }

        if (wasBackground) {
            Log.i(TAG, "went to foreground");
            for (Listener l:listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exception) {
                    Log.e(TAG, "Listener threw exception", exception);
                }
            }
        } else {
            Log.i(TAG, "still foreground");
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        foreground = false;

        if (check != null) {
            handler.removeCallbacks(check);
        }

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.i(TAG, "went background");
                    for (Listener l: listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exception) {
                            Log.e(TAG, "Listener threw exception!", exception);
                        }
                    }
                } else {
                    Log.i(TAG, "still foreground");
                }
            }
        }, 500);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
