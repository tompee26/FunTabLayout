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
package com.tompee.funtablayout;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.widget.LinearLayout;

class FlipTabView extends LinearLayout {
    private final IconView mIconView;
    private final TitleView mTitleView;
    private int mDefaultColor;

    public FlipTabView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        mIconView = new IconView(getContext());
        mTitleView = new TitleView(getContext());
        addView(mIconView);
        addView(mTitleView);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTitleView.setSelected(selected);
    }

    public void setIconDimension(int dimension) {
        mIconView.setIconDimension(dimension);
    }

    public void setTextAppearance(int textAppearance) {
        TextViewCompat.setTextAppearance(mTitleView, textAppearance);
        mDefaultColor = mTitleView.getTextColors().getDefaultColor();
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

    public void setSelectedTextColor(int color) {
        mTitleView.setTextColor(mTitleView.createColorStateList(mDefaultColor, color));
    }
}
