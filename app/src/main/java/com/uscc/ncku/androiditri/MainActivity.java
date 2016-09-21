package com.uscc.ncku.androiditri;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uscc.ncku.androiditri.fragment.ChooseTemplate;
import com.uscc.ncku.androiditri.fragment.DiaryFragment;
import com.uscc.ncku.androiditri.fragment.MapFragment;
import com.uscc.ncku.androiditri.fragment.TextFragment;
import com.uscc.ncku.androiditri.util.MainButton;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LOG_TAG";

    public static final String GET_TOUR_INDEX = "GET_TOUR_INDEX";

    private int tourIndex;

    private MainButton infoBtn;
    private MainButton diaryBtn;
    private MainButton mapBtn;
    private MainButton soundBtn;
    private MainButton fontBtn;

    private static ImageView mainBtnNavBg;
    private static LinearLayout mainBtnLayout;

    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;
    private TextFragment textFragment;
    private ChooseTemplate chooseTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        tourIndex = bundle.getInt(GET_TOUR_INDEX);

        mainBtnNavBg = (ImageView) findViewById(R.id.img_btnnavagitor_main);
        mainBtnLayout = (LinearLayout) findViewById(R.id.llayout_button_main);

        infoBtn = (MainButton) findViewById(R.id.btn_info_main);
        diaryBtn = (MainButton) findViewById(R.id.btn_diary_main);
        mapBtn = (MainButton) findViewById(R.id.btn_map_main);
        soundBtn = (MainButton) findViewById(R.id.btn_sound_main);
        fontBtn = (MainButton) findViewById(R.id.btn_font_main);
        infoBtn.setOnClickListener(new ButtonListener(this));
        diaryBtn.setOnClickListener(new ButtonListener(this));
        mapBtn.setOnClickListener(new ButtonListener(this));
        soundBtn.setOnClickListener(new ButtonListener(this));
        fontBtn.setOnClickListener(new ButtonListener(this));
        initFragment();

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
                case R.id.btn_diary_main:
                    if (diaryBtn.isBackgroundEqual(R.drawable.btn_main_diary_normal)) {
                        setBtnActive(diaryBtn, R.drawable.btn_main_diary_active);
                        transaction.replace(R.id.flayout_fragment_continer, diaryFragment).addToBackStack(null);
                    }
                    break;
                case R.id.btn_map_main:
                    if (mapBtn.isBackgroundEqual(R.drawable.btn_main_map_normal)) {
                        setBtnActive(mapBtn, R.drawable.btn_main_map_active);
                        transaction.replace(R.id.flayout_fragment_continer, mapFragment).addToBackStack(null);
                    }
                    break;
                case R.id.btn_sound_main:
                    break;
                case R.id.btn_font_main:
                    if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_normal)) {
                        // present new view
                        setBtnActive(fontBtn, R.drawable.btn_main_font_active);
                        transaction.replace(R.id.flayout_fragment_continer, textFragment).addToBackStack(null);

                    }
                    break;
            }

            transaction.commit();
        }
    }

    private void initFragment() {
        mapBtn.setActive(R.drawable.btn_main_map_active);
        diaryBtn.setNormal(R.drawable.btn_main_diary_normal);
        infoBtn.setDisable(R.drawable.btn_main_info_disabled);
        soundBtn.setDisable(R.drawable.btn_main_sound_disabled);
        fontBtn.setNormal(R.drawable.btn_main_font_normal);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(tourIndex);
        }

        if (diaryFragment == null) {
            diaryFragment = DiaryFragment.newInstance();
        }

        if (textFragment == null) {
            textFragment = TextFragment.newInstance();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.flayout_fragment_continer, mapFragment);
        transaction.commit();
    }

    private void setBtnActive(MainButton activeBtn, int bgId) {
        if (diaryBtn.isBackgroundEqual(R.drawable.btn_main_diary_active)) {
            diaryBtn.setNormal(R.drawable.btn_main_diary_normal);
        }

        if (mapBtn.isBackgroundEqual(R.drawable.btn_main_map_active)) {
            mapBtn.setNormal(R.drawable.btn_main_map_normal);
        }

        if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_active)) {
            fontBtn.setNormal(R.drawable.btn_main_font_normal);
        }

        activeBtn.setActive(bgId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment f = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        if (f instanceof MapFragment) {
            setBtnActive(mapBtn, R.drawable.btn_main_map_active);
        } else if (f instanceof DiaryFragment) {
            setBtnActive(diaryBtn, R.drawable.btn_main_diary_active);
        } else if (f instanceof TextFragment){
            setBtnActive(fontBtn, R.drawable.btn_main_font_active);
        }
    }

    public static void hideMainBtn() {
        mainBtnNavBg.getLayoutParams().height = 0;

        mainBtnLayout.setVisibility(View.GONE);
    }

    public static void showMainBtn() {
        mainBtnNavBg.getLayoutParams().height = ViewGroup.MarginLayoutParams.WRAP_CONTENT;

        mainBtnLayout.setVisibility(View.VISIBLE);
    }
}
