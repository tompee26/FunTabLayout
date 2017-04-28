package com.tompee.funtablayout.custom;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopTabView extends LinearLayout implements Animation.AnimationListener {
    private int mIconDimension;
    private IconView mIconView;
    private TitleView mTitleView;
    private int mAnimationDuration;
    private Animation mAnimation;

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

    public void setPopDuration(int duration) {
        mAnimationDuration = duration;
    }

    @Override
    public void setSelected(boolean isSelected) {
        super.setSelected(isSelected);
        mIconView.setSelected(isSelected);
        mTitleView.setSelected(isSelected);
    }

    public void setTextVisible(boolean isVisible) {
        if (isVisible) {
            mTitleView.setVisibility(INVISIBLE);
            ViewCompat.postOnAnimation(mTitleView, new Runnable() {
                @Override
                public void run() {
                    scaleView(mTitleView, 0f, 1f);
                }
            });
        } else {
            if (mAnimation != null) {
                mAnimation.cancel();
                mAnimation.reset();
            }
            mTitleView.setVisibility(GONE);
        }
    }

    public void scaleView(View v, float startScale, float endScale) {
        mAnimation = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setFillAfter(true);
        mAnimation.setDuration(mAnimationDuration);
        mAnimation.setAnimationListener(this);
        v.startAnimation(mAnimation);
    }

    public void setTextColor(int color) {
        mTitleView.setTextColor(color);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        mTitleView.setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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
