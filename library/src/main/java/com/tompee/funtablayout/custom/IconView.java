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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IconView extends ImageView {
    private int mIconDimension;

    public IconView(Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.
                LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 4, 0);
        setLayoutParams(params);
    }

    public void setIconDimension(int dimension) {
        mIconDimension = dimension;
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