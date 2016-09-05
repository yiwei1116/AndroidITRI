package com.uscc.ncku.androiditri;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import com.uscc.ncku.androiditri.util.TourViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TourActivity extends AppCompatActivity {

    private final static int[] IMG_SOURCE = {
            R.drawable.tour_designer,
            R.drawable.tour_robot,
            R.drawable.tour_housekeeper
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tour);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_tour);
        toolBarTxt.setText(R.string.tour_title);

        tourSelect();
    }

    private void tourSelect() {
        TourPagerAdapter tourPagerAdapter = new TourPagerAdapter(this);

        TourViewPager tourViewPager = (TourViewPager) findViewById(R.id.viewpager_tour);
        tourViewPager.setAdapter(tourPagerAdapter);
    }

    class TourPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public TourPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return IMG_SOURCE.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_vpager_item);
            imageView.setImageResource(IMG_SOURCE[position]);

            container.addView(itemView);

            Button nextBtn = (Button) findViewById(R.id.btn_confirm_tour);
            nextBtn.setBackgroundResource(R.drawable.selecter_btn_confirm);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    tourPage();
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
    }

}
