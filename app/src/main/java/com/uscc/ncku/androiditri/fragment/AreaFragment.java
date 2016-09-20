package com.uscc.ncku.androiditri.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uscc.ncku.androiditri.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;


    public AreaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AreaFragment newInstance(String param1, String param2) {
        AreaFragment fragment = new AreaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        RelativeLayout tour = (RelativeLayout) view.findViewById(R.id.rlayout_tour_speech);

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.tour_guide_translate);
        tour.startAnimation(animation);

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
