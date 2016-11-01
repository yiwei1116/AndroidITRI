package com.uscc.ncku.androiditri.fragment;


import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uscc.ncku.androiditri.CommunicationWithServer;
import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;
import com.uscc.ncku.androiditri.util.SQLiteDbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static final int[] RM_GRID_BG = {
        R.drawable.rm_grid1_a1m1,
        R.drawable.rm_grid1_a1m2,
        R.drawable.rm_grid1_a1m3,
        R.drawable.rm_grid1_a1m4
    };

    private int modeNumber;
    private int currentZone;
    private JSONArray modesArray;

    private View view;
    private ArrayList<Item> modeItem;

    private SQLiteDbManager dbManager;
    private CommunicationWithServer comm;

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
        }

        comm = ((MainActivity) getActivity()).getCommunicationWithServer();
        dbManager = new SQLiteDbManager(getActivity(), SQLiteDbManager.DATABASE_NAME);
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ((MainActivity) getActivity()).setToolbarTitle(getArguments().getString(CURRENT_NAME));
        ((MainActivity) getActivity()).showModeCoachSwapUp();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onstart");
        TextView modeTitle = (TextView) view.findViewById(R.id.txt_mode_select);
        String title = String.valueOf(modeNumber) + getResources().getString(R.string.mode_select_title);
        modeTitle.setText(title);

        GridView modeGrid = (GridView) view.findViewById(R.id.gridview_mode_select);
        Adapter adapter = new Adapter(this.getActivity());

        modeGrid.setAdapter(adapter);
    }

    private void addModeItem() throws JSONException {
        boolean isEnglish = ((MainActivity) getActivity()).isEnglish();
        JSONObject mode;
        for (int i = 0; i < modesArray.length(); i++) {
            mode = modesArray.getJSONObject(i);
            Item item = new Item();
            item.modeId = mode.getInt("mode_id");
            item.imgID = RM_GRID_BG[0];
            item.title = isEnglish ? mode.getString("name_en") : mode.getString("name");
            modeItem.add(item);
        }
    }

    class Item {
        int modeId;
        int imgID;
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

            // renew grid view
            this.notifyDataSetInvalidated();

            ModeHighlightFragment modeHighlight = ModeHighlightFragment.newInstance(selectModeId);
            ((MainActivity) getActivity()).replaceFragment(modeHighlight);

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflate = LayoutInflater.from(context);
                convertView = inflate.inflate(R.layout.mode_select_grid_item, null);
            }

            ImageView gridBg = (ImageView) convertView.findViewById(R.id.grid_item_bg);
            gridBg.setBackgroundResource(modeItem.get(position).imgID);

            TextView gridTitle = (TextView) convertView.findViewById(R.id.grid_item_title);
            gridTitle.setText(modeItem.get(position).title);

            TextView read = (TextView) convertView.findViewById(R.id.grid_item_read);

            int isRead = dbManager.getModeDidRead(modeItem.get(position).modeId);
            if (isRead == 1) {
                read.setVisibility(View.VISIBLE);
            } else {
                read.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

}
