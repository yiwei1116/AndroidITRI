package org.tabc.living3.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.IFontSize;
import org.tabc.living3.util.ISoundInterface;
import org.tabc.living3.util.SQLiteDbManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeHighlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeHighlightFragment extends Fragment implements ISoundInterface, IFontSize {
    private static final String TAG = "DEGUB";
    private static final int HIGHLIGHT_FLIP_TIMES = 4;
    private static final int HIGHLIGHT_FLIP_DURATION = 300;
    private static final String MODE_ID = "MODE_ID";

    private boolean isEnglish;
    private int modeId;
    private SQLiteDbManager dbManager;
    private JSONObject mode;
    private String modeName;
    private String modeIntroduction;
    private String splash_bg_vertical;
    private String splash_fg_vertical;
    private String splash_blur_vertical;

    private View view;

    public ModeHighlightFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModeHighlightFragment.
     */
    public static ModeHighlightFragment newInstance(int param2) {
        ModeHighlightFragment fragment = new ModeHighlightFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modeId = getArguments().getInt(MODE_ID);
        }
        isEnglish = ((MainActivity) getActivity()).isEnglish();
        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        try {
            mode = dbManager.queryModeFiles(modeId);

            modeName = isEnglish ? mode.getString("name_en") : mode.getString("name");
            modeIntroduction = mode.getString("introduction");
            splash_bg_vertical = mode.getString("splash_bg_vertical");
            splash_fg_vertical = mode.getString("splash_fg_vertical");
            splash_blur_vertical = mode.getString("splash_blur_vertical");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_highlight, container, false);

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
       // ((MainActivity) getActivity()).stopTexttoSpeech();
        ((MainActivity) getActivity()).setSoundNormal();
        ((MainActivity) getActivity()).setFontNormal();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_thumbup, menu);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bitmap bg = null;
        try {
            bg = HelperFunctions.getBitmapFromFile(getActivity(), splash_bg_vertical);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable back = new BitmapDrawable(bg);
        ImageView modeIntroBg = (ImageView) view.findViewById(R.id.img_mode_intro_bg);
        modeIntroBg.setBackgroundDrawable(back);

        TextView modeIntroTitle = (TextView) view.findViewById(R.id.txt_title_mode_intro);
        modeIntroTitle.setText(modeName);

        TextView modeIntroContent = (TextView) view.findViewById(R.id.txt_content_mode_intro);
        modeIntroContent.setText(modeIntroduction);
        modeIntroContent.setMovementMethod(new ScrollingMovementMethod());

        Button nextIntro = (Button) view.findViewById(R.id.btn_next_mode_intro);
        nextIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout intro = (RelativeLayout) view.findViewById(R.id.rlayout_mode_intro);
                intro.setVisibility(View.INVISIBLE);

                Button highlightBtn = (Button) view.findViewById(R.id.btn_next_equipment_highlight);
                highlightBtn.setVisibility(View.VISIBLE);
                try {
                    modeHighlight();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void modeHighlight() throws FileNotFoundException {
        // hide toolbar menu
        ((MainActivity) getActivity()).getToolbar().getMenu().clear();
        ((MainActivity) getActivity()).setFontDisabled();
        ((MainActivity) getActivity()).setSoundDisabled();
        ((MainActivity) getActivity()).stopTexttoSpeech();

        ((MainActivity) getActivity()).setToolbarTitle(modeName);

        Bitmap bg = HelperFunctions.getBitmapFromFile(getActivity(), splash_bg_vertical);
        final ImageView modeBg = (ImageView) view.findViewById(R.id.img_mode_highlight_bg);
        modeBg.setImageBitmap(bg);

        Bitmap highlight = HelperFunctions.getBitmapFromFile(getActivity(), splash_fg_vertical);
        ImageView modeHighlight = (ImageView) view.findViewById(R.id.img_mode_highlight_fg);
        modeHighlight.setImageBitmap(highlight);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(HIGHLIGHT_FLIP_DURATION);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(HIGHLIGHT_FLIP_TIMES);
        animation.setRepeatMode(Animation.REVERSE);
        modeHighlight.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Bitmap blur = null;
                try {
                    blur = HelperFunctions.getBitmapFromFile(getActivity(), splash_blur_vertical);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ImageView modeBlur = (ImageView) view.findViewById(R.id.img_mode_highlight_blur);
                modeBlur.setImageBitmap(blur);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Button next = (Button) view.findViewById(R.id.btn_next_equipment_highlight);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfDevices = dbManager.getNumbersOfDevicesFromMode(modeId);

                EquipmentTabFragment equipTabFragment = EquipmentTabFragment.newInstance(numberOfDevices, modeId);
                ((MainActivity) getActivity()).replaceFragment(equipTabFragment);

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setFontDisabled();
        ((MainActivity)getActivity()).setSoundDisabled();

        ((MainActivity) getActivity()).stopTexttoSpeech();
    }

    @Override
    public void setFontSize(int size) {
        TextView modeIntroContent = (TextView) view.findViewById(R.id.txt_content_mode_intro);
        modeIntroContent.setTextSize(size);
    }

    @Override
    public int getFontSize() {
        TextView modeIntroContent = (TextView) view.findViewById(R.id.txt_content_mode_intro);
        return (int) modeIntroContent.getTextSize() / 3;
    }

    @Override
    public MediaPlayer getCurrentmedia() {
        return null;
    }

    @Override
    public String getIntroduction() {
        String getIntrod =  modeIntroduction;
        return getIntrod;
    }
}
