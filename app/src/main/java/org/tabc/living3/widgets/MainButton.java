package org.tabc.living3.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import org.tabc.living3.R;

/**
 * Created by 振凱 on 9月09日.
 */
public class MainButton extends Button{
    private int bgId;

    public MainButton(Context context) {
        super(context);
    }

    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        bgId = resId;
    }

    public boolean isBackgroundEqual(int targetID) {
        return bgId == targetID;
    }

    public void setActive(int bgId) {
        setBackgroundResource(bgId);
        int color = (bgId == R.drawable.btn_main_map_active) ?
                getResources().getColor(R.color.colorWhite) :
                getResources().getColor(R.color.btn_font_color_blue);
        this.setTextColor(color);
    }

    public void setNormal(int bgId) {
        setBackgroundResource(bgId);
        this.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    public void setDisable(int bgId) {
        setBackgroundResource(bgId);
        this.setTextColor(getResources().getColor(R.color.btn_font_color_grey));
    }
}
