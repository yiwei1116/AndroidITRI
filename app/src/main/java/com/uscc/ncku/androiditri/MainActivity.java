package com.uscc.ncku.androiditri;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.widget.Toast;

import com.uscc.ncku.androiditri.fragment.ChooseTemplate;
import com.uscc.ncku.androiditri.fragment.DiaryFragment;
import com.uscc.ncku.androiditri.fragment.EquipmentTabFragment;
import com.uscc.ncku.androiditri.fragment.MapFragment;
import com.uscc.ncku.androiditri.util.ITRIObject;
import com.uscc.ncku.androiditri.util.MainButton;

import java.util.LinkedList;
import java.util.Locale;

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
    /* custom fragment back stack */
    private static LinkedList<Fragment> fragmentBackStack;

    private static RelativeLayout containerSL;

    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;
    private ChooseTemplate chooseTemplate;
    private TextToSpeech textToSpeech;
    public ITRIObject myObject;
    private Locale l;

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
        infoBtn.setOnClickListener(new ButtonListener());
        diaryBtn.setOnClickListener(new ButtonListener());
        mapBtn.setOnClickListener(new ButtonListener());
        soundBtn.setOnClickListener(new ButtonListener());
        fontBtn.setOnClickListener(new ButtonListener());

        containerSL = (RelativeLayout) findViewById(R.id.rlayout_font_size_zoom);
        createLanguageTTS();
        finishOtherActivity();

        fragmentBackStack = new LinkedList<Fragment>();

        initFragment();

    }

    @Override
    public void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {

        if( textToSpeech != null )
            textToSpeech.stop();
            textToSpeech.shutdown();

        super.onDestroy();
    }

    class ButtonListener implements View.OnClickListener {

        public ButtonListener() {}

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_info_main:
                    disableSoundFont();
                    break;
                case R.id.btn_diary_main:

                    disableSoundFont();
                    setSoundDisabled();
                    if (diaryBtn.isBackgroundEqual(R.drawable.btn_main_diary_normal)) {
                        replaceFragment(diaryFragment);
                    }
                    break;
                case R.id.btn_map_main:
                    disableSoundFont();
                    if (mapBtn.isBackgroundEqual(R.drawable.btn_main_map_normal)) {
                        replaceFragment(mapFragment);
                    }
                    break;
                case R.id.btn_sound_main:

                    if (soundBtn.isBackgroundEqual(R.drawable.btn_main_sound_normal)){
                        setSoundActive();
                        textToSpeech.speak(EquipmentTabFragment.getIntroduction(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if (soundBtn.isBackgroundEqual(R.drawable.btn_main_sound_active)) {
                        setSoundNormal();
                        textToSpeech.stop();

                    }
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

        // add mapFragment to back stack
        fragmentBackStack.addFirst(mapFragment);

        if (diaryFragment == null) {
            diaryFragment = DiaryFragment.newInstance();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.flayout_fragment_continer, mapFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentBackStack.isEmpty()) {
            replaceFragment(mapFragment);
        } else {
            // get last fragment
            Fragment f = fragmentBackStack.pop();
            String fragmentTag = f.getClass().getSimpleName();

            // if last fragment is map fragment, add it back to back stack
            if (fragmentTag.equals(mapFragment.getClass().getSimpleName()))
                fragmentBackStack.addLast(f);

            // replace fragment
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flayout_fragment_continer, f, fragmentTag);
            ft.commit();
        }
    }

    private void replaceFragment (Fragment fragment){
        String fragmentTag = fragment.getClass().getSimpleName();

        // find fragment in back stack
        int i = 0;
        while (i < fragmentBackStack.size()) {
            Fragment f = fragmentBackStack.get(i);
            if (f.getClass().getSimpleName().equals(fragmentTag)) {
                fragmentBackStack.remove(i);
                break;
            }
            i++;
        }

        // add current fragment to back stack
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        fragmentBackStack.addFirst(currentFragment);

        // replace fragment with input fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.flayout_fragment_continer, fragment, fragmentTag);
        ft.commit();
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

    public static LinkedList<Fragment> getFragmentBackStack() {
        return fragmentBackStack;
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
    private void createLanguageTTS()
    {

        if( textToSpeech == null )
        {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
                @Override
                public void onInit(int status)
                {

                    if( status == TextToSpeech.SUCCESS ) {

                        l = Locale.CHINESE;
                        // 目前指定的【語系+國家】TTS, 已下載離線語音檔, 可以離線發音
                        if( textToSpeech.isLanguageAvailable( l ) == TextToSpeech.LANG_COUNTRY_AVAILABLE )
                        {
                            textToSpeech.setLanguage( l );
                        }
                    }
                    else{

                        Toast.makeText(MainActivity.this,"語音導覽不支援此機型",Toast.LENGTH_SHORT).show();

                    }



                }}
            );
        }
    }

}
