package com.uscc.ncku.androiditri;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import com.uscc.ncku.androiditri.util.TourViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TourSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tour_select);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView toolBarTxt = (TextView) findViewById(R.id.txt_toolbar_tour_select);
        toolBarTxt.setText(R.string.tour_title);

        Button btnUnderstand = (Button) findViewById(R.id.btn_understand_tour_select);
        btnUnderstand.setBackgroundResource(R.drawable.selecter_btn_understand);
        btnUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button confirmBtn = (Button) findViewById(R.id.btn_confirm_tour);
                confirmBtn.setVisibility(View.VISIBLE);

                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.flayout_tour_select);
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        tourSelect();
    }

    private void tourSelect() {
        TourViewPager tourViewPager = (TourViewPager) findViewById(R.id.viewpager_tour_select);

        TourPagerAdapter tourPagerAdapter = new TourPagerAdapter(this, tourViewPager);

        tourViewPager.setAdapter(tourPagerAdapter);
    }

    class TourPagerAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private TourViewPager tourViewPager;

        public TourPagerAdapter(Context context, TourViewPager tourViewPager) {
            this.mContext = context;
            this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.tourViewPager = tourViewPager;
        }

        @Override
        public int getCount() {
            return MainActivity.TOUR_IMG.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_vpager_item);
            imageView.setImageResource(MainActivity.TOUR_IMG[position]);

            container.addView(itemView);

            Button confirmBtn = (Button) findViewById(R.id.btn_confirm_tour);
            confirmBtn.setBackgroundResource(R.drawable.selecter_btn_confirm);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TourSelectActivity.this, SurveyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(MainActivity.GET_TOUR_INDEX, tourViewPager.getCurrentItem());
                    intent.putExtras(bundle);
                    startActivity(intent);
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
