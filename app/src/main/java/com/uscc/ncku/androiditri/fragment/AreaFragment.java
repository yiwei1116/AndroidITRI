package com.uscc.ncku.androiditri.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
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

import com.uscc.ncku.androiditri.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaFragment extends Fragment {
    private static final String TOUR_INDEX = "TOUR_INDEX";
    private static final int[] TOUR_GUIDE = {
            R.drawable.designer_talking,
            R.drawable.robot_talking,
            R.drawable.housekeeper_talking
    };

    private int tourIndex;
    private View view;


    public AreaFragment() {
        // Required empty public constructor
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
        Log.e("GG", "onCreat");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("GG", "onCreatView");

        view = inflater.inflate(R.layout.fragment_area, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("GG", "onStart");

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

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("GG", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("GG", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("GG", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("GG", "onDestoryView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("GG", "onDestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("GG", "onDetach");
    }
}
