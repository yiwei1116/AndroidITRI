package org.tabc.living3.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lin on 2017/1/2.
 */

public class AutoResizeTextView extends TextView {

    static final String TAG = "TextFitTextView";
    boolean fit = false;

    public AutoResizeTextView(Context context) {
        super(context);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFitTextToBox(Boolean fit) {
        this.fit = fit;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fit) _shrinkToFit();
    }

    protected void _shrinkToFit() {

        int height = this.getHeight();
        int lines = this.getLineCount();
        Rect r = new Rect();
        int y1 = this.getLineBounds(0, r);
        int y2 = this.getLineBounds(lines - 1, r);

        float size = this.getTextSize();
        if (y2 > height && size >= 8.0f) {
            this.setTextSize(size - 2.0f);
            _shrinkToFit();
        }

    }
}