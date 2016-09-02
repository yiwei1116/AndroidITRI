package com.uscc.ncku.androiditri;

import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity {
    /* time period(ms) while loading logo */
    public static final int LOADING_PERIOD = 40;
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
     * Logo loading animation.
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
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dataLoading();
                }
            }
        }, LOADING_PERIOD);
    }

    private void dataLoading() {
        final ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);

        final Animation fadeIn = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.logo_fade_in);
        final Animation fadeOut = AnimationUtils.loadAnimation(LoadingActivity.this, R.anim.logo_fade_out);

        imgLoading.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {

                imgLoading.setImageResource(R.drawable.living_logo);
                imgLoading.startAnimation(fadeIn);
            }
        });
    }

}
