package com.uscc.ncku.androiditri;

import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity {
    /* time period(ms) while loading logo */
    public static final int LOADING_PERIOD = 30;
    /* loading level every time period from 0 to 10000 */
    public static final int LOADING_LEVEL = 200;

    private ClipDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        logoLoading();

    }

    /**
     * Logo load animation.
     */
    private void logoLoading() {
        ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);

        drawable = (ClipDrawable) imgLoading.getDrawable();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (drawable.getLevel() <= 10000 - LOADING_LEVEL) {
                    drawable.setLevel(drawable.getLevel() + LOADING_LEVEL);
                    handler.postDelayed(this, LOADING_PERIOD);
                } else {
                    dataLoading();
                }
            }
        }, LOADING_PERIOD);
    }

    private void dataLoading() {
        ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);
        imgLoading.setImageResource(R.drawable.living_logo);
    }

}
