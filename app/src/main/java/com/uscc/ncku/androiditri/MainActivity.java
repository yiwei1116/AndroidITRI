package com.uscc.ncku.androiditri;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uscc.ncku.androiditri.fragment.ChooseTemplate;
import com.uscc.ncku.androiditri.fragment.DiaryFragment;
import com.uscc.ncku.androiditri.fragment.EquipmentTabFragment;
import com.uscc.ncku.androiditri.fragment.MapFragment;
import com.uscc.ncku.androiditri.util.ITRIObject;
import com.uscc.ncku.androiditri.util.MainButton;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LOG_TAG";
    public static final String GET_TOUR_INDEX = "GET_TOUR_INDEX";

    private int tourIndex;

    private static MainButton infoBtn;
    private static MainButton diaryBtn;
    private static MainButton mapBtn;
    private static MainButton soundBtn;
    private static MainButton fontBtn;

    private static Toolbar toolbar;
    private static TextView toolbarTitle;
    private static ImageView mainBtnNavBg;
    private static LinearLayout mainBtnLayout;
    private static FrameLayout mainContainer;
    private static int container_margin_top;

    private static RelativeLayout containerSL;

    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;
    private ChooseTemplate chooseTemplate;

    public ITRIObject myObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbarTitle = (TextView) findViewById(R.id.txt_toolbar_main);
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

        // call function to get current projectId
        myObject = new ITRIObject();

        mainBtnNavBg = (ImageView) findViewById(R.id.img_btnnavagitor_main);
        mainBtnLayout = (LinearLayout) findViewById(R.id.llayout_button_main);

        mainContainer = (FrameLayout) findViewById(R.id.flayout_fragment_continer);
        container_margin_top = (int) getResources().getDimension(R.dimen.toolbar_content_paddingTop);

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

        containerSL = (RelativeLayout) findViewById(R.id.rlayout_font_size_zoom);

        finishOtherActivity();

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
                    disableSoundFont();
                    break;
                case R.id.btn_diary_main:
                    disableSoundFont();
                    if (diaryBtn.isBackgroundEqual(R.drawable.btn_main_diary_normal)) {
                        setBtnActive(diaryBtn, R.drawable.btn_main_diary_active);
                        transaction.replace(R.id.flayout_fragment_continer, diaryFragment, DiaryFragment.DIARY_FRAGMENT_TAG);
                        transaction.addToBackStack(DiaryFragment.DIARY_FRAGMENT_TAG);
                    }
                    break;
                case R.id.btn_map_main:
                    disableSoundFont();
                    if (mapBtn.isBackgroundEqual(R.drawable.btn_main_map_normal)) {
                        setBtnActive(mapBtn, R.drawable.btn_main_map_active);
                        transaction.replace(R.id.flayout_fragment_continer, mapFragment, MapFragment.MAP_FRAGMENT_TAG);
                        transaction.addToBackStack(MapFragment.MAP_FRAGMENT_TAG);
                    }
                    break;
                case R.id.btn_sound_main:
                    break;
                case R.id.btn_font_main:
                    if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_normal)) {
                        setFontActive();

                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.sound_font_in);
                        containerSL.startAnimation(animation);

                        /////////// ****** method to get which fragment is it
                        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
                        EquipmentTabFragment ef = null;
                        if (currentFragment instanceof EquipmentTabFragment) {
                            ef = (EquipmentTabFragment) currentFragment;
                        }

                        SeekBar seekBar = (SeekBar) findViewById(R.id.textBar);
                        final EquipmentTabFragment finalEf = ef;
                        assert seekBar != null;
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    int level = progress / 5;
                                    finalEf.setFontSize(level + 15);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    } else if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_active)) {
                        setFontNormal();
                    }
                    break;
            }

            transaction.commit();
        }
    }

    private void disableSoundFont() {
        if (soundBtn.isBackgroundEqual(R.drawable.btn_main_sound_active)) {
            setSoundNormal();
        }

        if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_active)) {
            setFontNormal();
        }
    }

    private void initFragment() {
        setMapActive();
        setDiaryNormal();
        setInfoDisabled();
        setSoundDisabled();
        setFontDisabled();

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(tourIndex);
        }

        if (diaryFragment == null) {
            diaryFragment = DiaryFragment.newInstance();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.flayout_fragment_continer, mapFragment);
        transaction.commit();
    }

    private void setBtnActive(MainButton activeBtn, int bgId) {
        if (diaryBtn.isBackgroundEqual(R.drawable.btn_main_diary_active)) {
            setDiaryNormal();
        }

        if (mapBtn.isBackgroundEqual(R.drawable.btn_main_map_active)) {
            setMapNormal();
        }

        activeBtn.setActive(bgId);
    }

    @Override
    public void onBackPressed() {
        int previousFragmentIndex = getFragmentManager().getBackStackEntryCount() - 1;
        Fragment f = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        if (f instanceof MapFragment) {
            setBtnActive(mapBtn, R.drawable.btn_main_map_active);
            return;
        } else if (f instanceof DiaryFragment) {
            FragmentManager fm = getFragmentManager();
            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(previousFragmentIndex);
            String name = backEntry.getName();
            if (!name.equals(MapFragment.MAP_FRAGMENT_TAG)) {
                setDiaryNormal();
                fm.popBackStack();
                return;
            }

            FragmentTransaction transaction = fm.beginTransaction();
            setBtnActive(mapBtn, R.drawable.btn_main_map_active);
            transaction.replace(R.id.flayout_fragment_continer, mapFragment, MapFragment.MAP_FRAGMENT_TAG);
            transaction.commit();
            return;
        }

        super.onBackPressed();
    }

    public static void hideToolbar() {
        toolbar.setVisibility(View.GONE);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainContainer.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, -13);
        mainContainer.setLayoutParams(layoutParams);
    }

    public static void showDefaultToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackgroundResource(R.drawable.header_blank);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainContainer.getLayoutParams();
        layoutParams.setMargins(0, container_margin_top, 0, -13);
        mainContainer.setLayoutParams(layoutParams);
    }

    public static void showFeedbackToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackgroundResource(R.color.colorWhite);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainContainer.getLayoutParams();
        layoutParams.setMargins(0, container_margin_top, 0, -13);
        mainContainer.setLayoutParams(layoutParams);
    }

    public static void transparateToolbar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setBackgroundResource(R.color.trans);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainContainer.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, -13);
        mainContainer.setLayoutParams(layoutParams);
    }

    public static Toolbar getToolbar() {
        return toolbar;
    }

    public static void setToolbarTitle(int stringID) {
        toolbarTitle.setText(stringID);
    }

    public static void hideMainBtn() {
        mainBtnNavBg.getLayoutParams().height = 0;
        mainBtnLayout.setVisibility(View.GONE);
    }

    public static void showMainBtn() {
        mainBtnNavBg.getLayoutParams().height = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
        mainBtnLayout.setVisibility(View.VISIBLE);
    }

    public static void setInfoActive() {
        infoBtn.setActive(R.drawable.btn_main_info_active);
    }

    public static void setInfoNormal() {
        infoBtn.setNormal(R.drawable.btn_main_info_normal);
    }

    public static void setInfoDisabled() {
        infoBtn.setDisable(R.drawable.btn_main_info_disabled);
    }

    public static void setDiaryActive() {
        diaryBtn.setActive(R.drawable.btn_main_diary_active);
    }

    public static void setDiaryNormal() {
        diaryBtn.setNormal(R.drawable.btn_main_diary_normal);
    }

    public static void setDiaryDisabled() {
        diaryBtn.setDisable(R.drawable.btn_main_diary_disabled);
    }

    public static void setMapActive() {
        mapBtn.setActive(R.drawable.btn_main_map_active);
    }

    public static void setMapNormal() {
        mapBtn.setNormal(R.drawable.btn_main_map_normal);
    }

    public static void setMapDisabled() {
        mapBtn.setDisable(R.drawable.btn_main_map_disabled);
    }

    public static void setSoundActive() {
        soundBtn.setActive(R.drawable.btn_main_sound_active);
    }

    public static void setSoundNormal() {
        soundBtn.setNormal(R.drawable.btn_main_sound_normal);
    }

    public static void setSoundDisabled() {
        soundBtn.setDisable(R.drawable.btn_main_sound_disabled);
    }

    public static void setFontActive() {
        fontBtn.setActive(R.drawable.btn_main_font_active);
        containerSL.setVisibility(View.VISIBLE);
    }

    public static void setFontNormal() {
        fontBtn.setNormal(R.drawable.btn_main_font_normal);
        containerSL.setVisibility(View.GONE);
    }

    public static void setFontDisabled() {
        fontBtn.setDisable(R.drawable.btn_main_font_disabled);
        containerSL.setVisibility(View.GONE);
    }

    private void finishOtherActivity() {
        if(AboutActivity.instance != null) {
            try {
                AboutActivity.instance.finish();
            } catch (Exception e) {}
        }
        if(HomeActivity.instance != null) {
            try {
                HomeActivity.instance.finish();
            } catch (Exception e) {}
        }
        if(SurveyActivity.instance != null) {
            try {
                SurveyActivity.instance.finish();
            } catch (Exception e) {}
        }
        if(TourSelectActivity.instance != null) {
            try {
                TourSelectActivity.instance.finish();
            } catch (Exception e) {}
        }
    }

}
