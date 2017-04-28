package com.tompee.funtablayout.custom;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BubbleTabView extends LinearLayout {
    private int mIconDimension;
    private IconView mIconView;
    private TitleView mTitleView;

    public BubbleTabView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        mIconView = new IconView(getContext());
        mTitleView = new TitleView(getContext());
        addView(mIconView);
        addView(mTitleView);
    }

    public void setViewAlpha(float alpha) {
        for (int index = 0; index < getChildCount(); index++) {
            ViewCompat.setAlpha(getChildAt(index), alpha);
        }
    }

    public void resetAlpha() {
        for (int index = 0; index < getChildCount(); index++) {
            ViewCompat.setAlpha(getChildAt(index), 1);
        }
    }

    public void setIconDimension(int dimension) {
        mIconDimension = dimension;
    }

    public void setTextAppearance(int textAppearance) {
        TextViewCompat.setTextAppearance(mTitleView, textAppearance);
    }

    public void setMaxLines(int maxLines) {
        mTitleView.setMaxLines(maxLines);
    }

    public IconView getIconView() {
        return mIconView;
    }

    public TitleView getTitleView() {
        return mTitleView;
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
            if (mIconDimension != 0) {
                setMeasuredDimension(mIconDimension, mIconDimension);
            } else {
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
}
