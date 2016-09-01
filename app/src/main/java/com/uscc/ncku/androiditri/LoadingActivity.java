package com.uscc.ncku.androiditri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Lin on 2016/9/1.
 */
public class LoadingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        LinearLayout llayout = (LinearLayout) findViewById(R.id.llayout_loading);
        ImageView imgLoading = (ImageView) findViewById(R.id.img_loading);
    }
}
