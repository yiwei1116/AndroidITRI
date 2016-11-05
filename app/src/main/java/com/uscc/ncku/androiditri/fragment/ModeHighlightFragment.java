package com.uscc.ncku.androiditri.fragment;


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

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeHighlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeHighlightFragment extends Fragment {
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

                modeHighlight();
            }
        });

    }

    private void modeHighlight() {
        // hide toolbar menu
        ((MainActivity) getActivity()).getToolbar().getMenu().clear();

        ((MainActivity) getActivity()).setToolbarTitle(modeName);

        ImageView equipHighlight = (ImageView) view.findViewById(R.id.img_equipment_highlight);
        equipHighlight.setImageResource(R.drawable.rm_a1m1_highlight);

        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(HIGHLIGHT_FLIP_DURATION);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(HIGHLIGHT_FLIP_TIMES);
        animation.setRepeatMode(Animation.REVERSE);
        equipHighlight.startAnimation(animation);

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

//    private void setTitlePosition(HashMap cor) {
//        Resources resources = view.getContext().getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//
//        // convert pixel position to dp positon
////        float dpX = (float) cor.get("leftMargin") / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
////        float dpY = (float) cor.get("topMargin") / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//
//        TextView equip = (TextView) view.findViewById(R.id.txt_equipment_highlight);
//        equip.setVisibility(View.VISIBLE);
//        equip.setText("互動資訊牆");
//
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = (int) cor.get("leftMargin");
//        params.topMargin = (int) cor.get("topMargin");
//        equip.setLayoutParams(params);
//    }
//
//    class CalcPosition extends AsyncTask<Void, Void, HashMap> {
//
//        @Override
//        protected HashMap doInBackground(Void... params) {
//            /*
//                Important!!!
//                this drawable must put in drawable-nodpi, or there might be a wrong calculation.
//            */
//            Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rm_a1m1_highlight);
//
//            int minX = sourceBitmap.getWidth();
//            int minY = sourceBitmap.getHeight();
//            int maxX = -1;
//            int maxY = -1;
//            for(int y = 0; y < sourceBitmap.getHeight(); y++) {
//                for(int x = 0; x < sourceBitmap.getWidth(); x++) {
//                    int alpha = sourceBitmap.getPixel(x, y) >> 24;
//                    if(alpha > 0) {
//                        if(x < minX)
//                            minX = x;
//                        if(x > maxX)
//                            maxX = x;
//                        if(y < minY)
//                            minY = y;
//                        if(y > maxY)
//                            maxY = y;
//                    }
//                }
//            }
//
//            Log.i(TAG, String.format("minX: %d, minY: %d, maxX: %d, maxY %d", minX, minY, maxX, maxY));
//
//            int leftMargin;
//            int topMargin;
//
//            if((maxX < minX) || (maxY < minY)) {
//                leftMargin = sourceBitmap.getWidth() >> 1;
//                topMargin = sourceBitmap.getHeight() >> 1;
//            } else {
//                leftMargin = (maxX + minX) >> 1;
//                topMargin = ((maxY + minY) >> 1) - ((maxY - minY));
//            }
//
//            HashMap coordinate = new HashMap();
//            coordinate.put("leftMargin", leftMargin);
//            coordinate.put("topMargin", topMargin);
//
//            return coordinate;
//        }
//
//        @Override
//        protected void onPostExecute(HashMap hashMap) {
//            super.onPostExecute(hashMap);
//            setTitlePosition(hashMap);
//        }
//    }
}
