/**
 * Copyright (C) 2017 tompee
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tompee.funtablayout.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FlipTabView extends FrameLayout {
    private static final int DEFAULT_DELAY = 1000;
    private int mIconDimension;
    private IconView mIconView;
    private TitleView mTitleView;
    private AnimatorSet mAnimatorSet;
    private boolean mIsBackVisible;
    private boolean mPreviousSelectedState;

    public FlipTabView(Context context) {
        super(context);
        mIconView = new IconView(getContext());
        mTitleView = new TitleView(getContext());
        mTitleView.setAlpha(0f);
        addView(mIconView, 0);
        addView(mTitleView, 1);
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

    public boolean getSelectedState() {
        return mIsBackVisible;
    }

    @Override
    public void setSelected(boolean isSelected) {
        super.setSelected(isSelected);
        if (isSelected) {
            Log.d("hello", "isbackvisible: " + mIsBackVisible);
            flip();
        } else {
            if (mAnimatorSet != null) {
                mAnimatorSet.cancel();
            }
            if (mPreviousSelectedState) {
                flip();
            } else {
                removeAllViews();
                mTitleView.setAlpha(0f);
                addView(mIconView, 0);
                addView(mTitleView, 1);
                mIsBackVisible = false;
            }
        }
        mPreviousSelectedState = isSelected;
    }

    private void flip() {
        View frontView;
        View backView;
        if (mIsBackVisible) {
            frontView = getChildAt(1);
            backView = getChildAt(0);
        } else {
            frontView = getChildAt(0);
            backView = getChildAt(1);
        }

        /* Out animation */
        mAnimatorSet = new AnimatorSet();
        ObjectAnimator rotateOut = ObjectAnimator.ofFloat(frontView, "rotationY", 0.0f, 180f);
        rotateOut.setDuration(DEFAULT_DELAY);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(frontView, "alpha", 1.0f, 0f);
        alphaOut.setDuration(0);
        alphaOut.setStartDelay(DEFAULT_DELAY / 2);

        /* In animation */
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(backView, "alpha", 1.0f, 0f);
        alphaIn.setDuration(0);
        ObjectAnimator rotateIn = ObjectAnimator.ofFloat(backView, "rotationY", -180f, 0f);
        rotateIn.setRepeatMode(ValueAnimator.REVERSE);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(DEFAULT_DELAY);
        ObjectAnimator alphaReveal = ObjectAnimator.ofFloat(backView, "alpha", 0f, 1f);
        alphaReveal.setDuration(0);
        alphaReveal.setStartDelay(DEFAULT_DELAY / 2);

        mAnimatorSet.playTogether(rotateOut, alphaOut, alphaIn, rotateIn, alphaReveal);
        mAnimatorSet.start();
        mIsBackVisible = !mIsBackVisible;
    }

    public void setTextColor(int color) {
        mTitleView.setTextColor(color);
    }

    private class TitleView extends TextView {
        public TitleView(Context context) {
            super(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            setLayoutParams(params);
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
            params.gravity = Gravity.CENTER;
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
                    //noinspection SuspiciousNameCombination
                    setMeasuredDimension(width, width);
                } else {
                    //noinspection SuspiciousNameCombination
                    setMeasuredDimension(height, height);
                }
            }
        }
    }
}
