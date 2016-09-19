package com.uscc.ncku.androiditri.fragment;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.uscc.ncku.androiditri.R;

/**
 * Created by yiwei on 2016/9/17.
 */
public class ChooseTemp extends PagerAdapter {
    private static final int[] Template_Image = {
            R.drawable.card_1,
            R.drawable.card_2,
    };
    private LayoutInflater mLayoutInflater;
    public ChooseTemp(){




    }
    @Override
    public int getCount() {
        return Template_Image.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fragment_choose_template, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_vpager_item);
        imageView.setImageResource(Template_Image[position]);
        container.addView(itemView);
        Button btnNextStep = (Button)itemView.findViewById(R.id.btn_next_step);
        btnNextStep.setBackgroundResource(R.drawable.selector_btn_nextpage);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
