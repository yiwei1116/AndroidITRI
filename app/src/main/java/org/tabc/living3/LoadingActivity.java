package org.tabc.living3;

import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.tabc.living3.util.ITRIObject;
import org.tabc.living3.util.SQLiteDbManager;

import java.util.List;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity {
    /* time period(ms) while loading logo */
    public static final int LOADING_PERIOD = 40;
    /* loading level every time period from 0 to 10000 */
    public static final int LOADING_LEVEL = 200;

    private static CommunicationWithServer communicationWithServer;

    public SQLiteDbManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        communicationWithServer = new CommunicationWithServer(this);

        ITRIObject.setIsDownloadDone();

        logoLoading();
        manager = new SQLiteDbManager(this);
    }

    /**
     * Logo loading animation.
     */
    private void logoLoading() {
        communicationWithServer.downloadAllTables();
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

        ImageView imgBar = (ImageView) findViewById(R.id.img_bar_loading);
        imgBar.setVisibility(View.VISIBLE);

        final Animation animation = new AlphaAnimation(1, (float) 0.2);
        animation.setDuration(800);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imgText.startAnimation(animation);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    ImageView imgBar = (ImageView) findViewById(R.id.img_bar_loading);
                    ClipDrawable drawable = (ClipDrawable) imgBar.getDrawable();

                    drawable.setLevel(msg.arg1);
                }
                super.handleMessage(msg);
            }
        };

        // download all tables from server and save into SQLite
        communicationWithServer.downloadAllTables();
        // get all paths that require downloading files from server
        List<String> pathList = manager.getAllDownloadPaths();

        // download files
        communicationWithServer.DownloadFiles(pathList, this, handler);
    }

    public void startNextActivity() {
        Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static CommunicationWithServer getCommunicationWithServer() {
        return communicationWithServer;
    }

}
