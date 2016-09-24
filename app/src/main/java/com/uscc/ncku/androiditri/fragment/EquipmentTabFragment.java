package com.uscc.ncku.androiditri.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uscc.ncku.androiditri.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EquipmentTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EquipmentTabFragment extends Fragment {
    private static final String EQUIP_NUMBER = "EQUIPMENT_NUMBER";

    private int equipNumber;

    private View view;

    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;


    public EquipmentTabFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EquipmentTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EquipmentTabFragment newInstance(int param1) {
        EquipmentTabFragment fragment = new EquipmentTabFragment();
        Bundle args = new Bundle();
        args.putInt(EQUIP_NUMBER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipNumber = getArguments().getInt(EQUIP_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equipment_tab, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mTabs = (android.support.design.widget.TabLayout) view.findViewById(R.id.tabs_equipments);
        for (int i = 0; i < equipNumber; i++) {
            String equipTitle = getResources().getString(R.string.equip) + " " + String.valueOf(i + 1);
            mTabs.addTab(mTabs.newTab().setText(equipTitle));
        }

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_equipment_content);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return equipNumber;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = LayoutInflater.from(view.getContext()).inflate(R.layout.pager_item,
                    container, false);
            container.addView(v);
            TextView title = (TextView) v.findViewById(R.id.item_title);
            title.setText(String.valueOf(position + 1));
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
