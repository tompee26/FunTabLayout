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

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<T> {

    protected ViewPager mViewPager;
    protected int mIndicatorPosition;

    public BaseAdapter(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public int getCurrentIndicatorPosition() {
        return mIndicatorPosition;
    }

    public void setCurrentIndicatorPosition(int indicatorPosition) {
        mIndicatorPosition = indicatorPosition;
    }

    protected abstract void setTabVisibleCount(int count);

    protected abstract int getTabIndicatorColor();

    protected abstract int getTabIndicatorHeight();
}
