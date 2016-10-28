package com.uscc.ncku.androiditri.fragment;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.EquipmentTabInformation;
import com.uscc.ncku.androiditri.util.IFontSize;
import com.uscc.ncku.androiditri.util.ISoundInterface;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EquipmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/*
    設備欄位 的 layout
 */


public class EquipmentTabFragment extends Fragment implements ISoundInterface, IFontSize {
    private static final String EQUIP_NUMBER = "EQUIPMENT_NUMBER";
    private static final String TXTTAG = "txtContentTag";

    public int equipNumber, currentIndex ;

    private View view;
    private static final String API_KEY = "AIzaSyAK8nxWNAqa9y1iCQIWpEyKl9F_1WzdUTU";

    private static String VIDEO_ID = "tYA6TSTBjQ0";
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private ArrayList<EquipmentTabInformation> equipTabs;
    private SeekBar seekBar;
    public MediaPlayer mediaPlayer;
    private static final int[] rm_images = {
            R.drawable.rm_grid1_a1m4,
            R.drawable.rm_grid1_a1m3
    };
    private int image_index = 0;
    //private ArrayList<Integer> audioList = new ArrayList<Integer>();

    private   int[] audioList = {
            R.raw.test,
            R.raw.test1,
            R.raw.test2,
            R.raw.test3,
    };

    public EquipmentTabFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EquipmentTabFragment.
     */
    public static EquipmentTabFragment newInstance(int param1) {
        EquipmentTabFragment fragment = new EquipmentTabFragment();
        Bundle args = new Bundle();
        args.putInt(EQUIP_NUMBER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipNumber = getArguments().getInt(EQUIP_NUMBER);
        }
        equipTabs = new ArrayList<EquipmentTabInformation>();

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addTabs();
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
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity) getActivity()).setSoundNormalIfActive();
                ((MainActivity) getActivity()).setFontNormalIfActive();
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

        // release sound
        for (EquipmentTabInformation tab : equipTabs) {
            tab.getMediaPlayer().release();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {


                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    if (!wasRestored) {
                        player.cueVideo(VIDEO_ID);

                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                    // YouTube error
                    String errorMessage = error.toString();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.d("errorMessage:", errorMessage);
                }
            });
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentByTag(YouTubePlayerFragment.class.getSimpleName());
    }

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

            // set equipment title
            TextView title = (TextView) v.findViewById(R.id.equipment_title);
            title.setText(equipTabs.get(position).getTitle());

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
            String txtContentTag = TXTTAG + String.valueOf(position);
            TextView txtContent = (TextView) v.findViewById(R.id.txt_equip_intro_content);
            // set text content tag for font size later
            txtContent.setTag(txtContentTag);
            txtContent.setText(equipTabs.get(position).getTextContent());
            txtContent.setTextSize(equipTabs.get(position).getFontSize());
            txtContent.setMovementMethod(new ScrollingMovementMethod());

            // set youtube player
            YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
            youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {


                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    if (!wasRestored) {
                        player.cueVideo(VIDEO_ID);

                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                    // YouTube error
                    String errorMessage = error.toString();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.d("errorMessage:", errorMessage);
                }
            });
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.equip_item_youtube, youTubePlayerFragment).commit();

            container.addView(v);

            // set current view to equipment tab
            equipTabs.get(position).setView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//
//        public void renew() {
//            this.notifyDataSetChanged();
//        }

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
            final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme_NoActionBar);
            dialog.setContentView(R.layout.item_equipment_zoom_photo);

            final ImageView imageView = (ImageView) dialog.findViewById(R.id.zoom_photo_image);

            ImageButton close = (ImageButton) dialog.findViewById(R.id.zoom_photo_close);
            ImageButton previous = (ImageButton) dialog.findViewById(R.id.zoom_photo_previous);
            ImageButton next = (ImageButton) dialog.findViewById(R.id.zoom_photo_next);

            if (rm_images.length > 1) {
                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_index--;
                        image_index = (image_index < 0) ? image_index + rm_images.length : image_index;
                        imageView.setImageResource(rm_images[image_index]);
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_index++;
                        image_index %= rm_images.length;
                        imageView.setImageResource(rm_images[image_index]);
                    }
                });

                imageView.setImageResource(rm_images[image_index]);
            } else if (rm_images.length == 1) {
                imageView.setImageResource(rm_images[image_index]);
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
    private void addTabs() {
        for (int i = 0; i < equipNumber; i++) {
            EquipmentTabInformation tab = new EquipmentTabInformation();
            tab.setTitle("互動資訊牆");
            tab.setVideo(true);
            tab.setPhoto(true);
            tab.setTextContent(getResources().getString(R.string.rm_test));
            // add sound to each tab
            tab.setPlayList(audioList[i]);
            tab.setMediaPlayer(MediaPlayer.create(getActivity(), tab.getPlayList()));

            equipTabs.add(tab);
        }
    }

    public View getCurrentTabView() {
        return equipTabs.get(mViewPager.getCurrentItem()).getView();
    }

    /*
        font size button
     */
    @Override
    public void setFontSize(int size) {
        String tag = TXTTAG + String.valueOf(mViewPager.getCurrentItem());
        TextView tvRecord = (TextView) mViewPager.findViewWithTag(tag);
        tvRecord.setTextSize(size);
        equipTabs.get(mViewPager.getCurrentItem()).setFontSize(size);
    }

    @Override
    public MediaPlayer getCurrentmedia(){
        currentIndex = mViewPager.getCurrentItem();
        Log.e("index", String.valueOf(currentIndex));
        mediaPlayer = equipTabs.get(currentIndex).getMediaPlayer();

        return mediaPlayer;

    }


}
