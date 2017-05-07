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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class PopTabView extends LinearLayout {

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
        mIconView.setIconDimension(dimension);
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
        v.startAnimation(mAnimation);
    }

    public void setTextColor(int color) {
        mTitleView.setTextColor(color);
    }
}
