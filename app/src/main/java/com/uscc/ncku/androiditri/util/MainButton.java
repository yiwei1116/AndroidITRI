package com.uscc.ncku.androiditri.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

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

    public int getBackgroundId() {
        return bgId;
    }
}
