package com.tompee.funtablayout.custom;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopTabView extends LinearLayout {
    private int mIconDimension;
    private IconView mIconView;
    private TitleView mTitleView;

    public PopTabView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        mIconView = new IconView(getContext());
        mTitleView = new TitleView(getContext());
        mTitleView.setVisibility(GONE);
        addView(mIconView);
        addView(mTitleView);
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

    @Override
    public void setSelected(boolean isSelected) {
        super.setSelected(isSelected);
        mIconView.setSelected(isSelected);
        mTitleView.setSelected(isSelected);
    }

    public void setTextVisible(boolean isVisible) {
        mTitleView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setTextColor(int color) {
        mTitleView.setTextColor(color);
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
            LayoutParams params = new LayoutParams(ViewGroup.
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
