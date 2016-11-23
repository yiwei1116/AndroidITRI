package org.tabc.living3.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.EquipmentTabInformation;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.IFontSize;
import org.tabc.living3.util.ISoundInterface;
import org.tabc.living3.util.SQLiteDbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *  equipment number can not be over 10,
 *  if equipment number over 10, than must add more id in res/values/ids.xml
 */

/*
    設備欄位 的 layout
 */

public class EquipmentTabFragment extends Fragment implements ISoundInterface, IFontSize {
    private static final String EQUIP_NUMBER = "EQUIPMENT_NUMBER";
    private static final String MODE_ID = "MODE_ID";
    private static final String TXT_TAG = "txtContentTag";
    private static final String YOUTUBE_LAYOUT_ID_ = "equipment_youtube_";

    public int equipNumber, currentIndex ;
    private int modeId;
    private boolean isEnglish;

    private View view;

    private static final String API_KEY = "AIzaSyAK8nxWNAqa9y1iCQIWpEyKl9F_1WzdUTU";

    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private int mLastViewPage = 0;
    private ArrayList<EquipmentTabInformation> equipTabs;
    private SeekBar seekBar;
    public MediaPlayer mediaPlayer;
    //private ArrayList<Integer> audioList = new ArrayList<Integer>();

    private int[] audioList = {
            R.raw.test,
            R.raw.test1,
            R.raw.test2,
            R.raw.test3,
            R.raw.test,
            R.raw.test1,
            R.raw.test2,
            R.raw.test3,
    };

    private SQLiteDbManager dbManager;

    public EquipmentTabFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EquipmentTabFragment.
     */
    public static EquipmentTabFragment newInstance(int param1, int param2) {
        EquipmentTabFragment fragment = new EquipmentTabFragment();
        Bundle args = new Bundle();
        args.putInt(EQUIP_NUMBER, param1);
        args.putInt(MODE_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipNumber = getArguments().getInt(EQUIP_NUMBER);
            if (equipNumber > 10)
                equipNumber = 10;
            modeId = getArguments().getInt(MODE_ID);
        }
        equipTabs = new ArrayList<EquipmentTabInformation>();

        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        setHasOptionsMenu(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        ((MainActivity) getActivity()).showEquipCoachSlide();
        isEnglish = ((MainActivity) getActivity()).isEnglish();

        try {
            addTabs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_thumbup, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equipment_tab, container, false);
        ((MainActivity) getActivity()).setFontNormal();
        ((MainActivity) getActivity()).setSoundNormal();

        ((MainActivity) getActivity()).setInfoNormal();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mTabs = (android.support.design.widget.TabLayout) view.findViewById(R.id.tabs_equipments);
        for (int i = 0; i < equipNumber; i++) {
            String equipTitle = getResources().getString(R.string.equip) + " " + String.valueOf(i + 1);
            mTabs.addTab(mTabs.newTab().setText(equipTitle));
        }

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_equipment_content);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setOffscreenPageLimit(equipNumber);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // normal sound and font size button
//                ((MainActivity) getActivity()).setSoundNormalIfActive();
//                ((MainActivity) getActivity()).audioPause();
                ((MainActivity) getActivity()).setFontNormalIfActive();
                ((MainActivity) getActivity()).stopTexttoSpeech();
                ((MainActivity) getActivity()).setSoundNormal();

                // normal information button and hide previous company information
                ((MainActivity) getActivity()).setInfoNormalIfActive();

                ////////////////////////////// set youtube fragment while changing page
                String preYoutubeId = YOUTUBE_LAYOUT_ID_ + String.valueOf(mLastViewPage);
                Fragment preFragment = getFragmentManager().findFragmentById(getYoutubeLayoutId(preYoutubeId));
                if (preFragment != null)
                    getFragmentManager().beginTransaction().remove(preFragment).commit();

                String idName = YOUTUBE_LAYOUT_ID_ + String.valueOf(position);
                FrameLayout frameLayout = (FrameLayout) mViewPager.findViewById(getYoutubeLayoutId(idName));
                YouTubePlayerFragment youTubePlayerFragment = equipTabs.get(position).getYouTubePlayerFragment();
                youTubePlayerFragment = initYoutubeFragment(youTubePlayerFragment, equipTabs.get(position).getVideoID());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(frameLayout.getId(), youTubePlayerFragment);
                transaction.commit();
                //////////////////////////////

                // close company info if change page
                View preView = equipTabs.get(mLastViewPage).getView();
                ScrollView infoLayout = (ScrollView) preView.findViewById(R.id.scrollview_equipment_info);
                infoLayout.setVisibility(View.GONE);

                // add current equipment read count
                dbManager.addReadCount(equipTabs.get(position).getDeviceId());

                // set current position to last position
                mLastViewPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabs.setupWithViewPager(mViewPager);
       /* view.findViewById(R.id.pause_audio).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    view.findViewById(R.id.pause_audio).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.play_audio).setVisibility(View.VISIBLE);
                }
                mediaPlayer.pause();
            }
        });
        view.findViewById(R.id.play_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.play_audio).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.pause_audio).setVisibility(View.VISIBLE);
                if (mediaPlayer != null) {
                    try {

                        mediaPlayer.seekTo(equipTabs.get(currentIndex).getPlayLength());
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }


                }
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setFontDisabled();
        ((MainActivity) getActivity()).setSoundDisabled();
        ((MainActivity) getActivity()).setInfoDisabled();
        ((MainActivity) getActivity()).stopTexttoSpeech();
        // release sound
        for (EquipmentTabInformation tab : equipTabs) {
            tab.getMediaPlayer().release();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
//
//                @Override
//                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//                    if (!wasRestored) {
//                        player.cueVideo(VIDEO_ID);
//                    }
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
//                    // YouTube error
//                    String errorMessage = error.toString();
//                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
//                    Log.d("errorMessage:", errorMessage);
//                }
//            });
//        }
//    }
//
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerFragment) getFragmentManager().findFragmentByTag(YouTubePlayerFragment.class.getSimpleName());
//    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return equipNumber;
        }

        // 判斷是否由對象生成界面
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String equipTitle = getResources().getString(R.string.equip) + " " + String.valueOf(position + 1);
            return equipTitle;
        }

        // 初始化position位置的界面 類似於baseAdapter的 getView方法
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_equipment,
                    container, false);

            try {
                setEquipmentTab(v, position);
                setCompanyInfo(v, position);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            container.addView(v);

            // set current view to equipment tab
            equipTabs.get(position).setView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    class RadioButtonListener implements RadioGroup.OnCheckedChangeListener {
        private View v;

        public RadioButtonListener(View v) {
            this.v = v;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            ImageButton zoom = (ImageButton) v.findViewById(R.id.btn_equip_photo_zoom);
            FrameLayout youtubeLayout = (FrameLayout) v.findViewById(R.id.equip_item_youtube);
            ImageView imageView = (ImageView) v.findViewById(R.id.equip_item_image_view);

            switch (checkedId) {
                case R.id.btn_equip_video:
                    youtubeLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    zoom.setVisibility(View.GONE);
                    break;
                case R.id.btn_equip_photo:
                    youtubeLayout.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    zoom.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }

    class ZoomButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final int position = mViewPager.getCurrentItem();

            final Dialog dialog = new Dialog(getActivity(), R.style.dialog_coach_normal);
            dialog.setContentView(R.layout.item_equipment_zoom_photo);

            final ImageView imageView = (ImageView) dialog.findViewById(R.id.zoom_photo_image);
            final ArrayList<String> photoList = equipTabs.get(position).getEquipPhoto();

            ImageButton close = (ImageButton) dialog.findViewById(R.id.zoom_photo_close);
            ImageButton previous = (ImageButton) dialog.findViewById(R.id.zoom_photo_previous);
            ImageButton next = (ImageButton) dialog.findViewById(R.id.zoom_photo_next);

            // if there are more than 1 image, than set the next and previous button
            if (photoList.size() > 1) {
                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int image_index = equipTabs.get(position).getEquipPhotoIndex();
                        image_index--;
                        image_index = (image_index < 0) ? image_index + photoList.size() : image_index;
                        equipTabs.get(position).setEquipPhotoIndex(image_index);

                        String name = photoList.get(image_index);
                        Bitmap bitmap = null;
                        try {
                            bitmap = HelperFunctions.getBitmapFromFile(getActivity(), name);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(bitmap);
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int image_index = equipTabs.get(position).getEquipPhotoIndex();
                        image_index++;
                        image_index %= photoList.size();
                        equipTabs.get(position).setEquipPhotoIndex(image_index);

                        String name = photoList.get(image_index);
                        Bitmap bitmap = null;
                        try {
                            bitmap = HelperFunctions.getBitmapFromFile(getActivity(), name);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }

            // set first image to show
            if (photoList.size() >= 0) {
                int image_index = equipTabs.get(position).getEquipPhotoIndex();
                String name = photoList.get(image_index);
                Bitmap bitmap = null;
                try {
                    bitmap = HelperFunctions.getBitmapFromFile(getActivity(), name);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
            }

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    //cursor
    private void addTabs() throws JSONException {
        JSONArray devicesArray = dbManager.queryDeviceFilesByMode(modeId);
        int[] deviceIds = new int[equipNumber];
        for (int i = 0; i < equipNumber; i++) {
            JSONObject d = devicesArray.getJSONObject(i);
            deviceIds[i] = d.getInt("device_id");
        }

        for (int i = 0; i < equipNumber; i++) {
            JSONObject equip = dbManager.queryDeviceAndCompanyData(deviceIds[i]);

            EquipmentTabInformation tab = new EquipmentTabInformation();

            tab.setDeviceId(deviceIds[i]);

            String name = isEnglish ? equip.getString("name_en") : equip.getString("name");
            tab.setTitle(name);

            // insert photo to array list
            tab.insertEquipPhoto(equip.getString("photo_vertical"));
            // insert second photo to photo array list, temporary useless
//            tab.insertEquipPhoto(equip.getString("photo"));

            tab.setVideo(true);
            tab.setPhoto(true);
            tab.setTextContent(equip.getString("introduction"));
            // add sound to each tab
            tab.setPlayList(audioList[i]);
            tab.setMediaPlayer(MediaPlayer.create(getActivity(), tab.getPlayList()));

            // youtube
            String VIDEO_ID = "tYA6TSTBjQ0";
            tab.setVideoID(VIDEO_ID);
            YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
            tab.setYouTubePlayerFragment(youTubePlayerFragment);

            JSONObject company = equip.getJSONObject("company_data");
            // add company information
            tab.setCompanyTitleText(name);
            tab.setCompanyTitleImage(equip.getString("photo"));
            tab.setCompanyName(company.getString("name"));
            tab.setCompanyWebsite(company.getString("web"));
            tab.setCompanyPhone(company.getString("tel"));
            tab.setCompanyLocation(company.getString("addr"));
            tab.setCompanyQRcode(company.getString("qrcode"));

            equipTabs.add(tab);
        }
    }

    private void setEquipmentTab(View v, int position) throws FileNotFoundException {
        // set equipment title
        TextView title = (TextView) v.findViewById(R.id.equipment_title);
        title.setText(equipTabs.get(position).getTitle());

        // set equipment image
        String imageName = equipTabs.get(position).getEquipPhotoFirst();
        Bitmap bitmap = HelperFunctions.getBitmapFromFile(getActivity(), imageName);
        ImageView imageView = (ImageView) v.findViewById(R.id.equip_item_image_view);
        imageView.setImageBitmap(bitmap);

        // set vidoe and photo button
        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.equip_item_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioButtonListener(v));
        RadioButton video = (RadioButton) v.findViewById(R.id.btn_equip_video);
        RadioButton photo = (RadioButton) v.findViewById(R.id.btn_equip_photo);
        video.setChecked(true);

        // set zoom button in photo button
        ImageButton zoom = (ImageButton) v.findViewById(R.id.btn_equip_photo_zoom);
        zoom.setOnClickListener(new ZoomButtonListener());

        // if there is no photo button or no video button
        if (!equipTabs.get(position).isVideo()) {
            video.setVisibility(View.GONE);
        } else if (!equipTabs.get(position).isPhoto()) {
            photo.setVisibility(View.GONE);
        }

        // set equipment content text
        String txtContentTag = TXT_TAG + String.valueOf(position);
        TextView txtContent = (TextView) v.findViewById(R.id.txt_equip_intro_content);
        // set text content tag for font size later
        txtContent.setTag(txtContentTag);
        txtContent.setText(equipTabs.get(position).getTextContent());
        txtContent.setTextSize(equipTabs.get(position).getFontSize());
        txtContent.setMovementMethod(new ScrollingMovementMethod());

        // set each youtube frame layout a specific ID in tabs.
        String idName = YOUTUBE_LAYOUT_ID_ + String.valueOf(position);
        FrameLayout youtubeLayout = (FrameLayout) v.findViewById(R.id.equip_item_youtube);
        youtubeLayout.setId(getYoutubeLayoutId(idName));

        // replace first youtube layout in prevent Youtube API overlapping
        if (position == mViewPager.getCurrentItem()) {
            YouTubePlayerFragment youTubePlayerFragment = equipTabs.get(position).getYouTubePlayerFragment();
            youTubePlayerFragment = initYoutubeFragment(youTubePlayerFragment, equipTabs.get(position).getVideoID());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(youtubeLayout.getId(), youTubePlayerFragment);
            transaction.commit();
        }
    }

    private void setCompanyInfo(View v, int position) throws FileNotFoundException {
        EquipmentTabInformation currTab = equipTabs.get(position);

        String name = currTab.getCompanyTitleImage();
        Bitmap bitmap = HelperFunctions.getBitmapFromFile(getActivity(), name);
        Drawable back = new BitmapDrawable(bitmap);
        ImageView bg = (ImageView) v.findViewById(R.id.equipment_info_title_image);
        bg.setBackgroundDrawable(back);

        TextView title = (TextView) v.findViewById(R.id.equipment_info_title);
        title.setText(currTab.getCompanyTitleText());

        TextView compName = (TextView) v.findViewById(R.id.equipment_info_company_name);
        compName.setText(currTab.getCompanyName());

        TextView compWeb = (TextView) v.findViewById(R.id.equipment_info_company_site);
        compWeb.setText(currTab.getCompanyWebsite());

        TextView compPhone = (TextView) v.findViewById(R.id.equipment_info_company_phone);
        compPhone.setText(currTab.getCompanyPhone());

        TextView compLocation = (TextView) v.findViewById(R.id.equipment_info_company_location);
        compLocation.setText(currTab.getCompanyLocation());

        Bitmap qrcode = HelperFunctions.getBitmapFromFile(getActivity(), currTab.getCompanyQRcode());
        ImageView compQRcode = (ImageView) v.findViewById(R.id.equipment_info_company_qrcode);
        compQRcode.setImageBitmap(qrcode);
    }

    private int getYoutubeLayoutId(String idName) {
        Resources r = getResources();
        return r.getIdentifier(idName, "id", getActivity().getPackageName());
    }

    private YouTubePlayerFragment initYoutubeFragment(YouTubePlayerFragment f, final String VIDEO_ID) {
        f.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer.cueVideo(VIDEO_ID);
                }
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // YouTube error
                String errorMessage = youTubeInitializationResult.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });
        return f;
    }

    public View getCurrentTabView() {
        return equipTabs.get(mViewPager.getCurrentItem()).getView();
    }

    /*
        font size button
     */
    @Override
    public void setFontSize(int size) {
        String tag = TXT_TAG + String.valueOf(mViewPager.getCurrentItem());
        TextView tvRecord = (TextView) mViewPager.findViewWithTag(tag);
        tvRecord.setTextSize(size);
        equipTabs.get(mViewPager.getCurrentItem()).setFontSize(size);
    }

    @Override
    public int getFontSize() {
        return equipTabs.get(mViewPager.getCurrentItem()).getFontSize();
    }

    @Override
    public MediaPlayer getCurrentmedia(){
        currentIndex = mViewPager.getCurrentItem();
        Log.e("index", String.valueOf(currentIndex));
        mediaPlayer = equipTabs.get(currentIndex).getMediaPlayer();

        return mediaPlayer;

    }

    @Override
    public  String getIntroduction(){
        String getIntrod = equipTabs.get(mViewPager.getCurrentItem()).getTextContent();
        Log.e("getIntrod",getIntrod);

        return getIntrod;

    }
}
