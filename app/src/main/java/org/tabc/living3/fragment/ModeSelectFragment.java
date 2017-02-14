package org.tabc.living3.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tabc.living3.MainActivity;
import org.tabc.living3.R;
import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.HelperFunctions;
import org.tabc.living3.util.SQLiteDbManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeSelectFragment extends Fragment {
    private static final String TAG = "MODE_SELECT_DEBUG";
    private static final String MODE_NUMBER = "MODE_NUMBER";
    private static final String CURRENT_ZONE = "CURRENT_ZONE";
    private static final String CURRENT_NAME = "CURRENT_NAME";

    private int modeNumber;
    private int currentZone;
    private String zoneName;
    private JSONArray modesArray;

    private View view;
    private ArrayList<Item> modeItem;

    private SQLiteDbManager dbManager;
    private HelperFunctions helperFunctions;

    public ModeSelectFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ModeSelectFragment.
     */
    public static ModeSelectFragment newInstance(int param1, int param2, String param3) {
        ModeSelectFragment fragment = new ModeSelectFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_NUMBER, param1);
        args.putInt(CURRENT_ZONE, param2);
        args.putString(CURRENT_NAME, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modeNumber = getArguments().getInt(MODE_NUMBER);
            currentZone = getArguments().getInt(CURRENT_ZONE);
            zoneName = getArguments().getString(CURRENT_NAME);
        }

        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
        helperFunctions = new HelperFunctions(getActivity().getApplicationContext());
        try {
            modesArray = dbManager.queryModeDataWithZoneId(currentZone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        modeItem = new ArrayList<Item>();

        try {
            addModeItem();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_select, container, false);
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        setHasOptionsMenu(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getActivity());

                getActivity().onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbManager.addZoneLikeCount(currentZone);
                return true;
            }
        });

        ((MainActivity) getActivity()).setToolbarTitle(zoneName);
        ((MainActivity) getActivity()).showModeCoachSwapUp();

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
        TextView modeTitle = (TextView) view.findViewById(R.id.txt_mode_select);
        String title = String.valueOf(modeNumber) + getResources().getString(R.string.mode_select_title);
        if (((MainActivity) getActivity()).isEnglish()) {
            title = String.valueOf(modeNumber) + " " + getResources().getString(R.string.mode_select_title);
            if (modeNumber > 1)
                title += "s";
        }
        modeTitle.setText(title);

        GridView modeGrid = (GridView) view.findViewById(R.id.gridview_mode_select);
        Adapter adapter = new Adapter(this.getActivity());

        modeGrid.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // upload zone like count

        // 2/13/2017
//        try {
//            helperFunctions.uploadZoneLikeAndReadCount(currentZone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void addModeItem() throws JSONException {
        boolean isEnglish = ((MainActivity) getActivity()).isEnglish();
        JSONObject mode;
        for (int i = 0; i < modesArray.length(); i++) {
            mode = modesArray.getJSONObject(i);
            Item item = new Item();
            item.modeId = mode.getInt("mode_id");
            item.splash_bg_vertical = mode.getString("splash_bg_vertical");
            item.title = isEnglish ? mode.getString("name_en") : mode.getString("name");
            modeItem.add(item);
        }
    }

    class Item {
        int modeId;
        String splash_bg_vertical;
        String title;
    }

    class Adapter extends BaseAdapter {
        Context context;

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return modeNumber;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            int selectModeId = modeItem.get(position).modeId;

            // update is read to mode
            dbManager.updateModeDidRead(selectModeId);
            // add mode read count
            dbManager.addModeReadCount(selectModeId);

            // renew grid view
            this.notifyDataSetInvalidated();

            ButtonSound.play(getActivity());
            ModeHighlightFragment modeHighlight = ModeHighlightFragment.newInstance(selectModeId, zoneName);
            ((MainActivity) getActivity()).replaceFragment(modeHighlight);

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflate = LayoutInflater.from(context);
                convertView = inflate.inflate(R.layout.fragment_mode_select_grid_item, null);
            }

            Bitmap bg = null;
            try {
                Matrix matrix = new Matrix();
                Bitmap bitmap = helperFunctions.getBitmapFromFile(getActivity(), modeItem.get(position).splash_bg_vertical);
                matrix.postScale((float) 0.25, (float) 0.25);
                bg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
//                bg = bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView gridBg = (ImageView) convertView.findViewById(R.id.grid_item_bg);
            gridBg.setImageBitmap(bg);

            TextView gridTitle = (TextView) convertView.findViewById(R.id.grid_item_title);
            gridTitle.setText(modeItem.get(position).title);

            TextView read = (TextView) convertView.findViewById(R.id.grid_item_read);

            int isRead = dbManager.getModeDidRead(modeItem.get(position).modeId);
            read.setVisibility(isRead == 1 ? View.VISIBLE : View.INVISIBLE);

            return convertView;
        }
    }

}
