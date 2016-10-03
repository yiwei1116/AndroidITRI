package com.uscc.ncku.androiditri.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uscc.ncku.androiditri.MainActivity;
import com.uscc.ncku.androiditri.R;

import java.util.ArrayList;


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
    private ArrayList<EquipmentTab> equipTabs;


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
        equipTabs = new ArrayList<EquipmentTab>();
        addTabs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_equipment_tab, container, false);
        MainActivity.setFontNormal();
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

        mTabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.setFontDisabled();
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
            String equipTitle = getResources().getString(R.string.equip) + " " + String.valueOf(position + 1);
            return equipTitle;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_equipment,
                    container, false);
            container.addView(v);

            TextView title = (TextView) v.findViewById(R.id.equipment_title);
            title.setText(equipTabs.get(position).title);

            ToggleButton video = (ToggleButton) v.findViewById(R.id.btn_equip_video);
            ToggleButton photo = (ToggleButton) v.findViewById(R.id.btn_equip_photo);
            video.setChecked(true);
            video.setOnCheckedChangeListener(new ChangeChecker(v));
            photo.setOnCheckedChangeListener(new ChangeChecker(v));

            if (!equipTabs.get(position).isVideo) {
                video.setVisibility(View.GONE);
            } else if (!equipTabs.get(position).isPhoto) {
                photo.setVisibility(View.GONE);
            }

            TextView txtContent = (TextView) v.findViewById(R.id.txt_equip_intro_content);
            txtContent.setText(equipTabs.get(position).textContent);
            txtContent.setTextSize(equipTabs.get(position).fontSize);
            txtContent.setMovementMethod(new ScrollingMovementMethod());

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void renew() {
            this.notifyDataSetChanged();
        }

    }

    class ChangeChecker implements CompoundButton.OnCheckedChangeListener {
        private View v;

        public ChangeChecker(View v) {
            this.v = v;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (buttonView.getId() == R.id.btn_equip_video) {
                    ToggleButton photo = (ToggleButton) v.findViewById(R.id.btn_equip_photo);
                    photo.setChecked(false);
                } else if (buttonView.getId() == R.id.btn_equip_photo) {
                    ToggleButton video = (ToggleButton) v.findViewById(R.id.btn_equip_video);
                    video.setChecked(false);
                }
            }
        }
    }

    class EquipmentTab {
        String title;
        boolean isVideo;
        boolean isPhoto;
        String textContent;
        int fontSize;
    }

    private void addTabs() {
        for (int i = 0; i < equipNumber; i++) {
            EquipmentTab tab = new EquipmentTab();
            tab.title = "互動資訊牆";
            tab.isVideo = true;
            tab.isPhoto = true;
            tab.textContent = getResources().getString(R.string.rm_test);
            tab.fontSize = 18;
            equipTabs.add(tab);
        }
    }

    public void setFontSize(int size) {
        equipTabs.get(mViewPager.getCurrentItem()).fontSize = size;
        SamplePagerAdapter samplePagerAdapter = (SamplePagerAdapter) mViewPager.getAdapter();
        samplePagerAdapter.renew();
    }
}
