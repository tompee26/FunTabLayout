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
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {
    protected int mTabPaddingStart;
    protected int mTabPaddingTop;
    protected int mTabPaddingEnd;
    protected int mTabPaddingBottom;
    protected int mTabTextAppearance;
    protected int mTabBackgroundResId;
    protected int mTabIndicatorColor;
    private ViewPager mViewPager;
    private int mIndicatorPosition;
    private int mTabVisibleCount;

    public BaseAdapter(BaseBuilder baseBuilder) {
        mViewPager = baseBuilder.mViewPager;
        mTabPaddingStart = mTabPaddingTop = mTabPaddingEnd = mTabPaddingBottom =
                baseBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.tabPadding);
        mTabPaddingStart = baseBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.tabPaddingStart);
        mTabPaddingTop = baseBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.tabPaddingTop);
        mTabPaddingEnd = baseBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.tabPaddingEnd);
        mTabPaddingBottom = baseBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.tabPaddingBottom);

        mTabTextAppearance = R.style.SimpleText;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = baseBuilder.mContext.getTheme();
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        mTabBackgroundResId = typedValue.resourceId;

        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        mTabIndicatorColor = typedValue.data;

        if (baseBuilder.mTabPaddingStart != null) {
            mTabPaddingStart = baseBuilder.mTabPaddingStart;
        }
        if (baseBuilder.mTabPaddingTop != null) {
            mTabPaddingTop = baseBuilder.mTabPaddingTop;
        }
        if (baseBuilder.mTabPaddingEnd != null) {
            mTabPaddingEnd = baseBuilder.mTabPaddingEnd;
        }
        if (baseBuilder.mTabPaddingBottom != null) {
            mTabPaddingBottom = baseBuilder.mTabPaddingBottom;
        }
        if (baseBuilder.mTabTextAppearance != null) {
            mTabTextAppearance = baseBuilder.mTabTextAppearance;
        }
        if (baseBuilder.mTabBackgroundResId != null) {
            mTabBackgroundResId = baseBuilder.mTabBackgroundResId;
        }
        if (baseBuilder.mTabIndicatorColor != null) {
            mTabIndicatorColor = baseBuilder.mTabIndicatorColor;
        }
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

    protected int getTabVisibleCount() {
        return mTabVisibleCount;
    }

    protected void setTabVisibleCount(int count) {
        mTabVisibleCount = count;
    }

    protected int getTabIndicatorColor() {
        return mTabIndicatorColor;
    }

    protected abstract int getTabIndicatorHeight();

    public static class BaseBuilder {
        private final Context mContext;
        private ViewPager mViewPager;
        private Integer mTabPaddingStart;
        private Integer mTabPaddingTop;
        private Integer mTabPaddingEnd;
        private Integer mTabPaddingBottom;
        private Integer mTabTextAppearance;
        private Integer mTabBackgroundResId;
        private Integer mTabIndicatorColor;

        public BaseBuilder(Context context) {
            mContext = context;
        }

        protected Context getContext() {
            return mContext;
        }

        protected ViewPager getViewPager() {
            return mViewPager;
        }

        public BaseBuilder setViewPager(ViewPager viewPager) {
            mViewPager = viewPager;
            return this;
        }

        public BaseBuilder setTabPadding(int tabPaddingStart, int tabPaddingTop, int tabPaddingEnd,
                                         int tabPaddingBottom) {
            mTabPaddingStart = tabPaddingStart;
            mTabPaddingTop = tabPaddingTop;
            mTabPaddingEnd = tabPaddingEnd;
            mTabPaddingBottom = tabPaddingBottom;
            return this;
        }

        public BaseBuilder setTabTextAppearance(int tabTextAppearance) {
            mTabTextAppearance = tabTextAppearance;
            return this;
        }

        public BaseBuilder setTabBackgroundResId(int tabBackgroundResId) {
            mTabBackgroundResId = tabBackgroundResId;
            return this;
        }

        public BaseBuilder setTabIndicatorColor(int color) {
            mTabIndicatorColor = color;
            return this;
        }
    }
}
