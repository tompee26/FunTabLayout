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

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FlipTabView extends ViewFlipper {
    private int mIconDimension;
    private IconView mIconView;
    private TitleView mTitleView;
    private AnimationSet mLeftIn;

    public FlipTabView(Context context) {
        super(context);
        mIconView = new IconView(getContext());
        mTitleView = new TitleView(getContext());
        addView(mIconView, 0);
        addView(mTitleView, 1);

        mLeftIn = new AnimationSet(true);
        Animation translate = new TranslateAnimation(1, 0, 0, 0, Animation.RELATIVE_TO_PARENT,
                Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT);
        translate.setDuration(500);
        Animation alpha = new AlphaAnimation(0.1f, 1.0f);
        alpha.setDuration(500);
        mLeftIn.addAnimation(translate);
        mLeftIn.addAnimation(alpha);
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

    public void setFlip(boolean isSelected) {
        if ((isSelected && getDisplayedChild() == 0) ||
                (!isSelected && getDisplayedChild() == 1)) {
            setInAnimation(mLeftIn);
            setOutAnimation(mLeftIn);
            showNext();
        }
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
                    setMeasuredDimension(width, width);
                } else {
                    setMeasuredDimension(height, height);
                }
            }
        }
    }
}
