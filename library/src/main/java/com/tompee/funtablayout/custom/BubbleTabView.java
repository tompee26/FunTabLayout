package com.tompee.funtablayout.custom;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BubbleTabView extends LinearLayout {

    public BubbleTabView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        addView(new IconView(getContext()));
        addView(new TitleView(getContext()));
    }

    @Override
    public void setAlpha(float alpha) {
        for (int index = 0; index < getChildCount(); index++) {
            ViewCompat.setAlpha(getChildAt(index), alpha);
        }
    }

    public void resetAlpha() {
        for (int index = 0; index < getChildCount(); index++) {
            ViewCompat.setAlpha(getChildAt(index), 1);
        }
    }

    private class TitleView extends TextView {
        public TitleView(Context context) {
            super(context);
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            setEllipsize(TextUtils.TruncateAt.END);
            setGravity(Gravity.CENTER);
        }
    }

    private class IconView extends ImageView {

        public IconView(Context context) {
            super(context);
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.
                    LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 4, 0);
            setLayoutParams(params);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();
            if (width < height) {
                setMeasuredDimension(width, width);
            } else {
                setMeasuredDimension(height, height);
            }
        }
    }
}
