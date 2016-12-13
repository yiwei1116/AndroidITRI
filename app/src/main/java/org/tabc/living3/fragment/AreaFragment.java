package org.tabc.living3.fragment;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.DatabaseUtilizer;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.IFontSize;
import org.tabc.living3.util.ISoundInterface;
import org.tabc.living3.util.SQLiteDbManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaFragment extends Fragment implements ISoundInterface, IFontSize {
    private static final String TOUR_INDEX = "TOUR_INDEX";
    private static final String ZONE = "ZONE";
    private static final int[] TOUR_GUIDE = {
            R.drawable.designer_talking,
            R.drawable.robot_talking,
            R.drawable.housekeeper_talking
    };

    private int tourIndex;
    private int currentZone;
    private View view;

    private SQLiteDbManager dbManager;

    private String title;
    private String title_en;
    private String photoBg;
    private String photoBg_vertical;
    private String introduction;
    private String hint;
    private String guide_voice;

    private boolean isEnglish;


    public AreaFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreaFragment.
     */
    public static AreaFragment newInstance(int param1,int param2) {
        AreaFragment fragment = new AreaFragment();
        Bundle args = new Bundle();
        args.putInt(TOUR_INDEX, param1);
        args.putInt(ZONE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourIndex = getArguments().getInt(TOUR_INDEX);
            currentZone = getArguments().getInt(ZONE);
        }

        isEnglish = ((MainActivity) getActivity()).isEnglish();
        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        try {
            JSONObject area = dbManager.queryZone(currentZone);

            title = area.getString(DatabaseUtilizer.NAME);
            title_en = area.getString(DatabaseUtilizer.NAME_EN);
            photoBg = area.getString("photo");
            photoBg_vertical = area.getString("photo_vertical");
            introduction = area.getString(DatabaseUtilizer.INTRODUCTION);
            if (isEnglish) {
                String introduction_en = area.getString(DatabaseUtilizer.INTRODUCTION_EN);
                if (introduction_en == null || introduction_en.equals("null"))
                    introduction = "";
                else
                    introduction = introduction_en;
            }
            hint = area.getString("hint");
            guide_voice = area.getString(DatabaseUtilizer.GUIDE_VOICE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).hideToolbar();
        ((MainActivity) getActivity()).setFontNormal();
        ((MainActivity) getActivity()).setSoundNormal();

        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());

                getActivity().onBackPressed();
            }
        });
        view = inflater.inflate(R.layout.fragment_area, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView page = (TextView) view.findViewById(R.id.txt_page_area);
        page.setText(String.valueOf(currentZone));
        if (currentZone < 10) {
            page.setTextSize(29);
            page.setPadding(0, 0, 0, 0);
        }

//        RelativeLayout background = (RelativeLayout) view.findViewById(R.id.flayout_area_fragment);
        ImageView background = (ImageView) view.findViewById(R.id.img_area_bg);
        Bitmap bitmap = null;
        try {
            bitmap = HelperFunctions.getBitmapFromFile(getActivity(), photoBg_vertical);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable back = new BitmapDrawable(bitmap);
        background.setBackgroundDrawable(back);

        TextView areaTitle = (TextView) view.findViewById(R.id.title_area_fragment);
        areaTitle.setText(isEnglish ? title_en : title);

        TextView areaContent = (TextView) view.findViewById(R.id.content_area_fragment);
        areaContent.setText(introduction);
        areaContent.setMovementMethod(new ScrollingMovementMethod());

        ImageView tourGuide = (ImageView) view.findViewById(R.id.tour_guide_area);
        tourGuide.setBackgroundResource(TOUR_GUIDE[tourIndex]);

        TextView tourSpeech = (TextView) view.findViewById(R.id.tour_speech_area);
        tourSpeech.setText(hint);

        RelativeLayout tour = (RelativeLayout) view.findViewById(R.id.rlayout_tour_speech);

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.tour_guide_translate);
        tour.startAnimation(animation);

        Button next = (Button) view.findViewById(R.id.btn_next_area_fragment);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());

                int modeNumber = dbManager.getNumbersOfModeFromZone(currentZone);
                String currentZonename = isEnglish ? title_en : title;
                ModeSelectFragment modeSelectFragment = ModeSelectFragment.newInstance(modeNumber, currentZone, currentZonename);
                ((MainActivity) getActivity()).replaceFragment(modeSelectFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // disable main ui
        ((MainActivity) getActivity()).showDefaultToolbar();
        ((MainActivity) getActivity()).setFontDisabled();
        ((MainActivity)getActivity()).setSoundDisabled();

        // stop text to speech
        ((MainActivity)getActivity()).stopTexttoSpeech();
    }

    @Override
    public void setFontSize(int size) {
        TextView areaContent = (TextView) view.findViewById(R.id.content_area_fragment);
        areaContent.setTextSize(size);
    }

    @Override
    public int getFontSize() {
        TextView areaContent = (TextView) view.findViewById(R.id.content_area_fragment);
        return (int) areaContent.getTextSize() / 3;
    }

    @Override
    public MediaPlayer getCurrentmedia() {
        return null;
    }

    @Override
    public String getIntroduction() {
        return introduction;
    }
}
