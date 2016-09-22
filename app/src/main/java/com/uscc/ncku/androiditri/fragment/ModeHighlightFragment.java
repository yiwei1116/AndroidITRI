package com.uscc.ncku.androiditri.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uscc.ncku.androiditri.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeHighlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeHighlightFragment extends Fragment {
    private static final String TAG = "DEGUB";
    private static final int HIGHLIGHT_FLIP_TIMES = 4;
    private static final int HIGHLIGHT_FLIP_DURATION = 300;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;


    public ModeHighlightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModeHighlightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModeHighlightFragment newInstance(String param1, String param2) {
        ModeHighlightFragment fragment = new ModeHighlightFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mode_highlight, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView modeIntroTitle = (TextView) view.findViewById(R.id.txt_title_mode_intro);
        TextView modeIntroContent = (TextView) view.findViewById(R.id.txt_content_mode_intro);

        Button nextIntro = (Button) view.findViewById(R.id.btn_next_mode_intro);
        nextIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout intro = (RelativeLayout) view.findViewById(R.id.rlayout_mode_intro);
                intro.setVisibility(View.INVISIBLE);

                FrameLayout highlight = (FrameLayout) view.findViewById(R.id.flayout_mode_highlight);
                highlight.setVisibility(View.VISIBLE);
                modeHighlight();
            }
        });

    }

    private void modeHighlight() {
        ImageView equipHighlight = (ImageView) view.findViewById(R.id.img_equipment_highlight);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(HIGHLIGHT_FLIP_DURATION);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(HIGHLIGHT_FLIP_TIMES);
        animation.setRepeatMode(Animation.REVERSE);
        equipHighlight.startAnimation(animation);

//        equipHighlight.getDrawingCache();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rm_a1m1_highlight);
//        Bitmap bitmap = equipHighlight.getDrawingCache();
        HashMap cor = cropBitmapTransparency(bitmap);

        TextView equip = (TextView) view.findViewById(R.id.txt_equipment_highlight);
        equip.setX(540);
        equip.setY(960);
    }

    private HashMap cropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++) {
            for(int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = sourceBitmap.getPixel(x, y);
                if(alpha > 0) {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }

        Log.i(TAG, String.valueOf(sourceBitmap.getPixel(99, 1039)));
        Log.i(TAG, String.format("minX: %d, minY: %d, maxX: %d, maxY %d", minX, minY, maxX, maxY));

        float leftMargin;
        float topMargin;

//        if((maxX < minX) || (maxY < minY)) {
            leftMargin = sourceBitmap.getWidth() / 2;
            topMargin = sourceBitmap.getHeight() / 2;
//        } else {
//            leftMargin = (maxX + minX) / 4;
//            topMargin = ((maxY + minY) / 4);
//        }

        Log.i(TAG, String.format("left: %f, top: %f", leftMargin, topMargin));

        HashMap coordinate = new HashMap();
        coordinate.put("leftMargin", leftMargin);
        coordinate.put("topMargin", topMargin);

        return coordinate;
    }
}
