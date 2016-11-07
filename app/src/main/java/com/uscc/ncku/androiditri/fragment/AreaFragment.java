package com.uscc.ncku.androiditri.fragment;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.uscc.ncku.androiditri.CommunicationWithServer;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.HelperFunctions;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaFragment extends Fragment {
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

            // FIXME: if there are no beacon nearby, than currentZone set to 2
            if (currentZone == 0)
                currentZone = 2;
        }
        Log.i("GG", "onCreat");
        Log.i("GG", currentZone+"");

        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        try {
            JSONObject area = dbManager.queryZone(currentZone);

            title = area.getString("name");
            title_en = area.getString("name_en");
            photoBg = area.getString("photo");
            photoBg_vertical = area.getString("photo_vertical");
            introduction = area.getString("introduction");
            hint = area.getString("hint");
            guide_voice = area.getString("guide_voice");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isEnglish = ((MainActivity) getActivity()).isEnglish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("GG", "onCreatView");

        ((MainActivity) getActivity()).hideToolbar();
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        view = inflater.inflate(R.layout.fragment_area, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("GG", "onStart");

        RelativeLayout background = (RelativeLayout) view.findViewById(R.id.flayout_area_fragment);
        Bitmap bitmap = HelperFunctions.getBitmapFromFile(getActivity(), photoBg);
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
                int modeNumber = dbManager.getNumbersOfModeFromZone(currentZone);
                String currentZonename = isEnglish ? title_en : title;
                ModeSelectFragment modeSelectFragment = ModeSelectFragment.newInstance(modeNumber, currentZone, currentZonename);
                ((MainActivity) getActivity()).replaceFragment(modeSelectFragment);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("GG", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("GG", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("GG", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).showDefaultToolbar();
        Log.i("GG", "onDestoryView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("GG", "onDestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("GG", "onDetach");
    }

}
