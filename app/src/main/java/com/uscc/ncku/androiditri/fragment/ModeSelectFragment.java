package com.uscc.ncku.androiditri.fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeSelectFragment extends Fragment {
    private static final String TAG = "MODE_SELECT_DEBUG";
    private static final String MODE_NUMBER = "modeNumber";
    private static final int[] RM_GRID_BG = {
        R.drawable.rm_grid1_a1m1,
        R.drawable.rm_grid1_a1m2,
        R.drawable.rm_grid1_a1m3,
        R.drawable.rm_grid1_a1m4
    };
    private static final String[] RM_GRID_TITLE = {
            "迎賓模式",
            "浮空投影模式",
            "大氣環境監測模式",
            "植生牆自動澆灌系統"
    };

    private int modeNumber;

    private View view;
    private ArrayList<Item> modeItem;


    public ModeSelectFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ModeSelectFragment.
     */
    public static ModeSelectFragment newInstance(int param1) {
        ModeSelectFragment fragment = new ModeSelectFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_NUMBER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modeNumber = getArguments().getInt(MODE_NUMBER);
        }

        ((MainActivity) getActivity()).showModeCoachSwapUp();

        modeItem = new ArrayList<Item>();
        addModeItem();
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

    private void addModeItem() {
        for (int i = 0; i < modeNumber; i++) {
            Item item = new Item();
            item.imgID = RM_GRID_BG[i];
            item.title = RM_GRID_TITLE[i];
            item.isRead = false;
            modeItem.add(item);
        }
    }

    class Item {
        int imgID;
        String title;
        boolean isRead;
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
            modeItem.get(position).isRead = true;
            this.notifyDataSetInvalidated();

            ModeHighlightFragment modeHighlight = ModeHighlightFragment.newInstance("a", "b");
            replaceFragment(modeHighlight);
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
            if (modeItem.get(position).isRead) {
                read.setVisibility(View.VISIBLE);
            } else {
                read.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    private void replaceFragment (Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        LinkedList<Fragment> fragmentBackStack = ((MainActivity) getActivity()).getFragmentBackStack();

        // find fragment in back stack
        int i = 0;
        while (i < fragmentBackStack.size()) {
            Fragment f = fragmentBackStack.get(i);
            if (f.getClass().getSimpleName().equals(fragmentTag)) {
                fragmentBackStack.remove(i);
                break;
            }
            i++;
        }

        // add current fragment to back stack
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.flayout_fragment_continer);
        fragmentBackStack.addFirst(currentFragment);

        // replace fragment with input fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.flayout_fragment_continer, fragment, fragmentTag);
        ft.commit();
    }

}
