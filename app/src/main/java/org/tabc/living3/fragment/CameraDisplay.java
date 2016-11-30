package org.tabc.living3.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.tabc.living3.MainActivity;
import org.tabc.living3.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class CameraDisplay extends Fragment implements View.OnClickListener{


    public CameraDisplay() {
        // Required empty public constructor
    }
    private String picPath,flagSelect;
    private FrameLayout frameLayout;
    private Button nextStep,backStep,reTake;
    private ImageView diplayPic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_display, container, false);

        frameLayout = (FrameLayout)view.findViewById(R.id.frame_layout);
        diplayPic = (ImageView) view.findViewById(R.id.display_pic);
        nextStep = (Button)view.findViewById(R.id.next_step);
        nextStep.setBackgroundResource(R.drawable.camera_btn_select_1);
        reTake = (Button)view.findViewById(R.id.retake);
        reTake.setBackgroundResource(R.drawable.camera_btn_retake);
        ((MainActivity) getActivity()).hideMainBtn();
        ((MainActivity) getActivity()).hideToolbar();
        backStep = (Button) view.findViewById(R.id.btn_camera_back);
        backStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        nextStep.setOnClickListener(this);
        reTake.setOnClickListener(this);

        Bundle bundle1 = getArguments();
        if (bundle1 != null) {

            picPath = (String)getArguments().get("picPath");
            flagSelect = (String)getArguments().get("flagSelect");

        }

        Bitmap myBitmap = BitmapFactory.decodeFile(picPath);
       /* frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache();
        frameLayout.setBackground(new BitmapDrawable(myBitmap));*/
        diplayPic.setImageBitmap(myBitmap);




        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.next_step:
                ChooseTemplate CT = new ChooseTemplate();
                Bundle bundle = new Bundle();
                bundle.putString("picPath", picPath);
                bundle.putString("flagSelect", String.valueOf(flagSelect));
                CT.setArguments(bundle);
                ((MainActivity) getActivity()).replaceFragment(CT);

                break;
            case R.id.retake:
                reTake.setTextColor(Color.WHITE);
                getActivity().onBackPressed();




        }
    }
}
