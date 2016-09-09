package com.uscc.ncku.androiditri;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.uscc.ncku.androiditri.fragment.DiaryFragment;
import com.uscc.ncku.androiditri.fragment.MapFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LOG_TAG";

    public static final String GET_TOUR_INDEX = "GET_TOUR_INDEX";
    public static final int[] TOUR_IMG = {
            R.drawable.tour_select_designer,
            R.drawable.tour_select_robot,
            R.drawable.tour_select_housekeeper
    };

    private int tourSelect;

    private Button infoBtn;
    private Button diaryBtn;
    private Button mapBtn;
    private Button soundBtn;
    private Button fontBtn;

    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = this.getIntent().getExtras();
        tourSelect = bundle.getInt(GET_TOUR_INDEX);

        infoBtn = (Button) findViewById(R.id.btn_info_main);
        diaryBtn = (Button) findViewById(R.id.btn_dairy_main);
        mapBtn = (Button) findViewById(R.id.btn_map_main);
        soundBtn = (Button) findViewById(R.id.btn_sound_main);
        fontBtn = (Button) findViewById(R.id.btn_font_main);
        infoBtn.setOnClickListener(new ButtonListener(this));
        diaryBtn.setOnClickListener(new ButtonListener(this));
        mapBtn.setOnClickListener(new ButtonListener(this));
        soundBtn.setOnClickListener(new ButtonListener(this));
        fontBtn.setOnClickListener(new ButtonListener(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mapBtn.setBackgroundResource(R.drawable.btn_main_map_active);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance("a", "b");
        }

        if (diaryFragment == null) {
            diaryFragment = DiaryFragment.newInstance("a", "b");
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.flayout_fragment_continer_main, mapFragment);
        transaction.commit();
    }

    class ButtonListener implements View.OnClickListener {
        private MainActivity activity;

        public ButtonListener(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (v.getId()) {
                case R.id.btn_info_main:
                    break;
                case R.id.btn_dairy_main:
                    transaction.replace(R.id.flayout_fragment_continer_main, diaryFragment);
                    if (v.getBackground() == getDrawable(R.drawable.btn_main_diary_normal)) {
                        transaction.replace(R.id.flayout_fragment_continer_main, diaryFragment);
                    }
                    break;
                case R.id.btn_map_main:
                    Log.d(TAG, String.valueOf(mapBtn.getBackground()));
                    Log.d(TAG, String.valueOf(getResources().getIdentifier("btn_main_map_active" , "drawable", getPackageName())));
                    transaction.replace(R.id.flayout_fragment_continer_main, mapFragment);
                    if (v.getBackground() == getDrawable(R.drawable.btn_main_map_normal)) {
                        transaction.replace(R.id.flayout_fragment_continer_main, mapFragment);
                    }
                    break;
                case R.id.btn_sound_main:
                    break;
                case R.id.btn_font_main:
                    break;
            }

            transaction.commit();
        }
    }

}
