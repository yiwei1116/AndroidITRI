package com.uscc.ncku.androiditri;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uscc.ncku.androiditri.fragment.ChooseTemplate;
import com.uscc.ncku.androiditri.fragment.DiaryFragment;
import com.uscc.ncku.androiditri.fragment.EquipmentTabFragment;
import com.uscc.ncku.androiditri.fragment.MapFragment;
import com.uscc.ncku.androiditri.util.ISoundInterface;
import com.uscc.ncku.androiditri.util.ITRIObject;
import com.uscc.ncku.androiditri.util.MainButton;
import com.uscc.ncku.androiditri.util.TimeUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Timer;

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

    private static FrameLayout containerSoundFontSize;
    private static RelativeLayout fontSizeRL;
    private static RelativeLayout soundRL;

    private CommunicationWithServer communicationWithServer;

    private MapFragment mapFragment;
    private DiaryFragment diaryFragment;

    public ITRIObject myObject;
    private Locale l;

    private Button startButton,pauseButton;
    private MediaPlayer soundPlayer;
    private SeekBar soundSeekBar;
    private Thread  soundThread;
    private Handler mHandler = new Handler();
    private TextView currentTime,completeTime;
    private TimeUtilities utils;
    private EquipmentTabFragment equipmentTabFragment = new EquipmentTabFragment();

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

        // AudioTime
        startButton = (Button)findViewById(R.id.play_audio);
        pauseButton = (Button)findViewById(R.id.pause_audio);
        soundSeekBar = (SeekBar) findViewById(R.id.audioBar);
        currentTime = (TextView) findViewById(R.id.current_time);
        completeTime = (TextView)findViewById(R.id.complete_time);
        //soundPlayer = MediaPlayer.create(this.getBaseContext(),R.raw.test);
        utils = new TimeUtilities();
        setupListeners();

     /*soundThread = new Thread(this);
        soundThread.start();*/



        equipmentTabFragment = new EquipmentTabFragment();

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



        containerSoundFontSize = (FrameLayout) findViewById(R.id.flayout_sound_font_container);
        fontSizeRL = (RelativeLayout) findViewById(R.id.rlayout_font_size_zoom);
        soundRL = (RelativeLayout) findViewById(R.id.rlayout_sound);
        //createLanguageTTS();
        finishOtherActivity();

        fragmentBackStack = new LinkedList<Fragment>();

        communicationWithServer = LoadingActivity.getCommunicationWithServer();

        initFragment();

    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {



        super.onDestroy();
    }

    class ButtonListener implements View.OnClickListener {

        public ButtonListener() {}

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_info_main:
                    disableSoundFont();

                    if (infoBtn.isBackgroundEqual(R.drawable.btn_main_info_normal)) {
                        setInfoActive();

                        EquipmentTabFragment currentInfo = (EquipmentTabFragment) getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
                        View infoView = currentInfo.getCurrentTabView();
                        ScrollView infoLayout = (ScrollView) infoView.findViewById(R.id.scrollview_equipment_info);

                        Animation fadeIn = AnimationUtils.loadAnimation(
                                currentInfo.getActivity(), R.anim.info_fade_in);

                        infoLayout.setVisibility(View.VISIBLE);
                        infoLayout.startAnimation(fadeIn);

                    } else if (infoBtn.isBackgroundEqual(R.drawable.btn_main_info_active)) {
                        setInfoNormal();

                        EquipmentTabFragment currentInfo = (EquipmentTabFragment) getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
                        View infoView = currentInfo.getCurrentTabView();
                        final ScrollView infoLayout = (ScrollView) infoView.findViewById(R.id.scrollview_equipment_info);

                        Animation fadeOut = AnimationUtils.loadAnimation(
                                currentInfo.getActivity(), R.anim.info_fade_out);

                        infoLayout.startAnimation(fadeOut);

                        fadeOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                infoLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
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
                        soundPlayer = equipmentTabFragment.getCurrentmedia();
                        //soundPlayer = MediaPlayer.create(getBaseContext(),R.raw.test);
                        soundThread = new Thread(mUpdateTimeTask);
                        soundThread.start();

                        //soundPlayer = equipmentTabFragment.getCurrentmedia();.


                        audioPlay();
                       /* ISoundInterface iSoundInterface =
                                (ISoundInterface) getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);

                        iSoundInterface.doPlay();*/

                    }
                    else if (soundBtn.isBackgroundEqual(R.drawable.btn_main_sound_active)) {
                        setSoundNormal();
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        soundThread.interrupt();
                        soundPlayer.release();
                       /* ISoundInterface iSoundInterface =
                                (ISoundInterface) getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);

                        iSoundInterface.pausePlay();*/

                    }
                    break;
                case R.id.btn_font_main:
                    if (fontBtn.isBackgroundEqual(R.drawable.btn_main_font_normal)) {
                        setFontActive();

                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.sound_font_in);
                        fontSizeRL.startAnimation(animation);

                        /////////// ****** method to get which fragment is it
                        Fragment currentFont = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
                        EquipmentTabFragment ef = null;
                        if (currentFont instanceof EquipmentTabFragment) {
                            ef = (EquipmentTabFragment) currentFont;
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
    private void setupListeners(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlay();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPause();
            }
        });
        soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    soundPlayer.seekTo(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            getAudioTime();

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
    private void getAudioTime(){


        long totalDuration = soundPlayer.getDuration();
        long currentDuration = soundPlayer.getCurrentPosition();
        soundSeekBar.setMax((int)totalDuration);
        soundSeekBar.setProgress((int)currentDuration);
        // Displaying Total Duration time
        completeTime.setText("/ "+utils.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        currentTime.setText(""+utils.milliSecondsToTimer(currentDuration));
    }
    private void audioPlay(){

        soundPlayer.start();
        updateProgressBar();
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

    }
    private void audioPause(){


        soundPlayer.pause();
        pauseButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
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
        /** if current fragment is equipment fragment and the info btn is actived
         *  than hide info page while pressing back button
         */
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        if (currentFragment instanceof EquipmentTabFragment) {
            if (infoBtn.isBackgroundEqual(R.drawable.btn_main_info_active)) {
                setInfoNormal();

                View infoView = ((EquipmentTabFragment) currentFragment).getCurrentTabView();
                final ScrollView infoLayout = (ScrollView) infoView.findViewById(R.id.scrollview_equipment_info);

                Animation fadeOut = AnimationUtils.loadAnimation(
                        currentFragment.getActivity(), R.anim.info_fade_out);

                infoLayout.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        infoLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                return;
            }
        }

        /**
         *  normal situation of pressing back button
         *  popping up latest fragment from fragment back stack
         */
        if (fragmentBackStack.isEmpty()) {
            replaceFragment(mapFragment);
        } else {
            // get last fragment
            Fragment f = fragmentBackStack.pop();
            String fragmentTag = f.getClass().getSimpleName();

            Log.d(TAG, String.valueOf(fragmentBackStack.size()));

            // if current fragment is mapFragment and previous is diaryFragment
            // then pop next fragment in back stack.
            if (currentFragment instanceof MapFragment
                    && f instanceof DiaryFragment) {

                if (fragmentBackStack.size() > 0) {
                    f = fragmentBackStack.pop();
                    fragmentTag = f.getClass().getSimpleName();
                }
                
            }

            // if last fragment is map fragment, add it back to back stack
            // if the last fragment in back stack is mapFragment than skip
            if (fragmentTag.equals(mapFragment.getClass().getSimpleName()) &&
                    !(fragmentBackStack.peekLast() instanceof MapFragment))
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
        soundRL.setVisibility(View.VISIBLE);


    }

    public static void setSoundNormal() {
        soundBtn.setNormal(R.drawable.btn_main_sound_normal);
        soundRL.setVisibility(View.GONE);
    }

    public static void setSoundDisabled() {
        soundBtn.setDisable(R.drawable.btn_main_sound_disabled);
        soundRL.setVisibility(View.GONE);
    }

    public static void setFontActive() {
        fontBtn.setActive(R.drawable.btn_main_font_active);
        fontSizeRL.setVisibility(View.VISIBLE);
    }

    public static void setFontNormal() {
        fontBtn.setNormal(R.drawable.btn_main_font_normal);
        fontSizeRL.setVisibility(View.GONE);
    }

    public static void setFontDisabled() {
        fontBtn.setDisable(R.drawable.btn_main_font_disabled);
        fontSizeRL.setVisibility(View.GONE);
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







   /* private void createLanguageTTS()
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
*/
}
