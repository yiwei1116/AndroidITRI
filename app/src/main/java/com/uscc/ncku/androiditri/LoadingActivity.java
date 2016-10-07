package com.uscc.ncku.androiditri;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.uscc.ncku.androiditri.util.SQLiteDbManager;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity {
    /* time period(ms) while loading logo */
    public static final int LOADING_PERIOD = 40;
    /* loading level every time period from 0 to 10000 */
    public static final int LOADING_LEVEL = 200;

    public SQLiteDbManager manager;

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

        final ClipDrawable drawable = (ClipDrawable) imgLoading.getDrawable();

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
                    logoAnimation();
                }
            }
        }, LOADING_PERIOD);
    }

    private void logoAnimation() {
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
                dataLoading();
            }
        });
    }

    private void dataLoading() {
        ImageView imgText = (ImageView) findViewById(R.id.img_text_loading);
        imgText.setImageResource(R.drawable.loading_text);

        final Animation animation = new AlphaAnimation(1, (float) 0.2);
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imgText.startAnimation(animation);

        new DataLoadingAsyncTask().execute();
    }

    class DataLoadingAsyncTask extends AsyncTask<Void, Integer, Void> {
        private ClipDrawable drawable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ImageView imgBar = (ImageView) findViewById(R.id.img_bar_loading);

            imgBar.setBackgroundResource(R.drawable.loading_line_base);
            imgBar.setImageResource(R.drawable.clip_loading_bar);

            drawable = (ClipDrawable) imgBar.getDrawable();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 10000; i+=LOADING_LEVEL) {
                publishProgress(i);
                try {
                    Thread.sleep(LOADING_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            drawable.setLevel(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
