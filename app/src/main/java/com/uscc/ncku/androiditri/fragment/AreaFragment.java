package com.uscc.ncku.androiditri.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
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

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaFragment extends Fragment {
    public static final String AREA_FRAGMENT_TAG = "AREA_FRAGMENT_TAG";
    private static final String TOUR_INDEX = "TOUR_INDEX";
    private static final int[] TOUR_GUIDE = {
            R.drawable.designer_talking,
            R.drawable.robot_talking,
            R.drawable.housekeeper_talking
    };

    private int tourIndex;
    private View view;


    public AreaFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AreaFragment.
     */
    public static AreaFragment newInstance(int param1) {
        AreaFragment fragment = new AreaFragment();
        Bundle args = new Bundle();
        args.putInt(TOUR_INDEX, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourIndex = getArguments().getInt(TOUR_INDEX);
        }
        Log.i("GG", "onCreat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("GG", "onCreatView");

        MainActivity.hideToolbar();
        Toolbar toolbar = MainActivity.getToolbar();
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

        TextView areaTitle = (TextView) view.findViewById(R.id.title_area_fragment);

        TextView areaContent = (TextView) view.findViewById(R.id.content_area_fragment);

        ImageView tourGuide = (ImageView) view.findViewById(R.id.tour_guide_area);
        tourGuide.setBackgroundResource(TOUR_GUIDE[tourIndex]);

        TextView tourSpeech = (TextView) view.findViewById(R.id.tour_speech_area);

        RelativeLayout tour = (RelativeLayout) view.findViewById(R.id.rlayout_tour_speech);

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.tour_guide_translate);
        tour.startAnimation(animation);

        Button next = (Button) view.findViewById(R.id.btn_next_area_fragment);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeSelectFragment modeSelectFragment = ModeSelectFragment.newInstance(4);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.flayout_fragment_continer, modeSelectFragment).addToBackStack(null);
                transaction.commit();
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
        MainActivity.showDefaultToolbar();
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
