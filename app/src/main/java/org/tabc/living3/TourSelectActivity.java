package org.tabc.living3;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import org.tabc.living3.util.ButtonSound;
import org.tabc.living3.util.ICoachProtocol;
import org.tabc.living3.util.TourViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TourSelectActivity extends AppCompatActivity implements ICoachProtocol {
    public static TourSelectActivity instance = null;

    private boolean isEnglish;

    private static final int[] TOUR_IMG = {
            R.drawable.tour_select_designer,
            R.drawable.tour_select_robot,
            R.drawable.tour_select_housekeeper
    };
    private static final int[] TOUR_TXT = {
            R.string.tour_select_designer,
            R.string.tour_select_robot,
            R.string.tour_select_housekeeper
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_select);
        instance = this;

        Bundle bundle = this.getIntent().getExtras();
        isEnglish = bundle.getBoolean(MainActivity.GET_IS_ENGLISH);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tour_select);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.btn_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSound.play(getApplication());

                onBackPressed();
            }
        });

        SharedPreferences settings = getSharedPreferences(ICoachProtocol.PREFS_NAME, 0);
        boolean isNotFirst = settings.getBoolean(ICoachProtocol.TOUR_SELECT_COACH, false);

        if (!isNotFirst) {
            Button confirmBtn = (Button) findViewById(R.id.btn_confirm_tour);
            confirmBtn.setVisibility(View.INVISIBLE);

            final Dialog dialog = new Dialog(TourSelectActivity.this, R.style.dialog_coach_normal);
            dialog.setContentView(R.layout.alertdialog_coach_tour_select);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            Button understand = (Button) dialog.findViewById(R.id.btn_understand_tour_select);
            understand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonSound.play(getApplication());

                    Button confirmBtn = (Button) findViewById(R.id.btn_confirm_tour);
                    confirmBtn.setVisibility(View.VISIBLE);

                    SharedPreferences settings = getSharedPreferences(ICoachProtocol.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(ICoachProtocol.TOUR_SELECT_COACH, true);
                    editor.apply();

                    dialog.dismiss();
                }
            });
        }

        tourSelect();
    }

    private void tourSelect() {
        TourViewPager tourViewPager = (TourViewPager) findViewById(R.id.viewpager_tour_select);

        TourPagerAdapter tourPagerAdapter = new TourPagerAdapter(this, tourViewPager);

        tourViewPager.setAdapter(tourPagerAdapter);
        tourViewPager.setCurrentItem(1);
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
            return TOUR_IMG.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_viewpager, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_vpager_item);
            imageView.setImageResource(TOUR_IMG[position]);

            TextView textView = (TextView) itemView.findViewById(R.id.txt_vpager_item);
            textView.setText(TOUR_TXT[position]);

            container.addView(itemView);

            Button confirmBtn = (Button) findViewById(R.id.btn_confirm_tour);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonSound.play(getApplication());

                    Intent intent = new Intent(TourSelectActivity.this, SurveyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(MainActivity.GET_IS_ENGLISH, isEnglish);
                    bundle.putInt(MainActivity.GET_TOUR_INDEX, tourViewPager.getCurrentItem());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

}
