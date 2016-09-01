package com.uscc.ncku.androiditri;

import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity {
    /* time period(ms) while loading logo */
    public static final int LOADING_PERIOD = 70;
    /* loading level every time period from 0 to 10000 */
    public static final int LOADING_LEVEL = 400;

    private ClipDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        logoLoading();

        dataLoading();

    }

    /**
     * Logo load animation.
     */
    private void logoLoading() {
        ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);

        drawable = (ClipDrawable) imgLoading.getDrawable();



        final Handler handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    drawable.setLevel(drawable.getLevel() + LOADING_LEVEL);
                }
            }
        };

        final Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0x123;
                if (drawable.getLevel() <= 10000 - LOADING_LEVEL) {
                    handler.sendMessage(msg);
                } else if (drawable.getLevel() > 14000) {
                    timer.cancel();
                }
            }
        }, 0, LOADING_PERIOD);
    }

    private void dataLoading() {
        ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);
        imgLoading.setBackgroundResource(R.drawable.living_logo);
    }

}
