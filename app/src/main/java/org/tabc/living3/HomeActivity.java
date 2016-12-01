package org.tabc.living3;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.ITRIObject;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public static HomeActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;

        ImageButton btnCht = (ImageButton) findViewById(R.id.btn_cht_home);
        ImageButton btnEng = (ImageButton) findViewById(R.id.btn_eng_home);

        ITRIObject.setIsDownloadDone();

        btnCht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getApplication());

                Locale locale = new Locale("default");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);

                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MainActivity.GET_IS_ENGLISH, false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getApplication());

                Locale locale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);

                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(MainActivity.GET_IS_ENGLISH, true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // This button can be remove after release app
        Button rm = (Button) findViewById(R.id.rm_skip);
        rm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.GET_TOUR_INDEX, 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (BuildConfig.DEBUG) {
            rm.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }
}
