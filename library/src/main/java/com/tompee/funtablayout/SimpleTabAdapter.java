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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleTabAdapter extends BaseAdapter<SimpleTabAdapter.ViewHolder> {
    private static final int MAX_TAB_TEXT_LINES = 2;

    private int mTabVisibleCount;
    private int mTabPaddingStart;
    private int mTabPaddingTop;
    private int mTabPaddingEnd;
    private int mTabPaddingBottom;
    private int mTabTextAppearance;
    private int mTabBackgroundResId;
    private int mTabSelectedTextColor;
    private int mTabIndicatorColor;
    private int mTabIndicatorHeight;

    private SimpleTabAdapter(Builder builder) {
        super(builder.mViewPager);
        getDefaultValues(builder.mContext);

        if (builder.mTabPaddingStart != null) {
            mTabPaddingStart = builder.mTabPaddingStart;
        }
        if (builder.mTabPaddingTop != null) {
            mTabPaddingTop = builder.mTabPaddingTop;
        }
        if (builder.mTabPaddingEnd != null) {
            mTabPaddingEnd = builder.mTabPaddingEnd;
        }
        if (builder.mTabPaddingBottom != null) {
            mTabPaddingBottom = builder.mTabPaddingBottom;
        }
        if (builder.mTabTextAppearance != null) {
            mTabTextAppearance = builder.mTabTextAppearance;
        }
        if (builder.mTabBackgroundResId != null) {
            mTabBackgroundResId = builder.mTabBackgroundResId;
        }
        if (builder.mTabSelectedTextColor != null) {
            mTabSelectedTextColor = builder.mTabSelectedTextColor;
        }
        if (builder.mTabIndicatorColor != null) {
            mTabIndicatorColor = builder.mTabIndicatorColor;
        }
        if (builder.mTabIndicatorHeight != null) {
            mTabIndicatorHeight = builder.mTabIndicatorHeight;
        }
    }

    private void getDefaultValues(Context context) {
        /** Padding */
        mTabPaddingStart = mTabPaddingTop = mTabPaddingEnd = mTabPaddingBottom =
                context.getResources().getDimensionPixelSize(R.dimen.tabPadding);
        mTabPaddingStart = context.getResources().getDimensionPixelSize(R.dimen.tabPaddingStart);
        mTabPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.tabPaddingTop);
        mTabPaddingEnd = context.getResources().getDimensionPixelSize(R.dimen.tabPaddingEnd);
        mTabPaddingBottom = context.getResources().getDimensionPixelSize(R.dimen.tabPaddingBottom);

        /** Text Appearance */
        mTabTextAppearance = R.style.SimpleText;

        /** View Appearance */
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        mTabBackgroundResId = typedValue.resourceId;

        /** Selected Appearance */
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr = context.obtainStyledAttributes(typedValue.data, new int[]{
                android.R.attr.textColorPrimary});
        mTabSelectedTextColor = arr.getColor(0, -1);
        arr.recycle();

        /** Tab Indicator */
        mTabIndicatorHeight = context.getResources().getDimensionPixelSize(R.dimen.tabIndicatorHeight);
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        mTabIndicatorColor = typedValue.data;
    }

    @Override
    public void setTabVisibleCount(int count) {
        mTabVisibleCount = count;
    }

    @Override
    public int getTabIndicatorColor() {
        return mTabIndicatorColor;
    }

    @Override
    public int getTabIndicatorHeight() {
        return mTabIndicatorHeight;
    }

    @Override
    public SimpleTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TabTextView tabTextView = new TabTextView(parent.getContext());
        tabTextView.setLayoutParams(createLayoutParamsForTabs(parent));
        ViewCompat.setPaddingRelative(tabTextView, mTabPaddingStart, mTabPaddingTop,
                mTabPaddingEnd, mTabPaddingBottom);
        TextViewCompat.setTextAppearance(tabTextView, mTabTextAppearance);
        tabTextView.setGravity(Gravity.CENTER);
        tabTextView.setMaxLines(MAX_TAB_TEXT_LINES);
        tabTextView.setEllipsize(TextUtils.TruncateAt.END);

        /** Set background */
        tabTextView.setBackgroundResource(mTabBackgroundResId);

        /** Set tab selected color */
        tabTextView.setTextColor(tabTextView.createColorStateList(
                tabTextView.getCurrentTextColor(), mTabSelectedTextColor));
        return new ViewHolder(tabTextView);
    }

    @Override
    public void onBindViewHolder(SimpleTabAdapter.ViewHolder holder, int position) {
        CharSequence title = getViewPager().getAdapter().getPageTitle(position);
        holder.mTitle.setText(title);
        holder.mTitle.setSelected(getCurrentIndicatorPosition() == position);
    }

    @Override
    public int getItemCount() {
        return getViewPager().getAdapter().getCount();
    }

    private RecyclerView.LayoutParams createLayoutParamsForTabs(ViewGroup parent) {
        RecyclerView.LayoutParams params;
        int width;
        if (getItemCount() > mTabVisibleCount) {
            width = parent.getWidth() / mTabVisibleCount;
        } else {
            width = parent.getWidth() / getItemCount();
        }
        params = new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }

    private static class TabTextView extends TextView {
        public TabTextView(Context context) {
            super(context);
        }

        public ColorStateList createColorStateList(int defaultColor, int selectedColor) {
            final int[][] states = new int[2][];
            final int[] colors = new int[2];
            states[0] = SELECTED_STATE_SET;
            colors[0] = selectedColor;
            // Default enabled state
            states[1] = EMPTY_STATE_SET;
            colors[1] = defaultColor;
            return new ColorStateList(states, colors);
        }
    }

    public static class Builder {
        private final Context mContext;
        private ViewPager mViewPager;
        private Integer mTabPaddingStart;
        private Integer mTabPaddingTop;
        private Integer mTabPaddingEnd;
        private Integer mTabPaddingBottom;
        private Integer mTabTextAppearance;
        private Integer mTabBackgroundResId;
        private Integer mTabSelectedTextColor;
        private Integer mTabIndicatorColor;
        private Integer mTabIndicatorHeight;

        /**
         * Creates a builder for a simple tab adapter
         *
         * @param context the parent context
         */
        public Builder(Context context) {
            mContext = context;
        }

        /**
         * Set a list of items, which are supplied by the given ViewPager
         *
         * @param viewPager the ViewPager to link to, or null to clear any previous link
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setViewPager(ViewPager viewPager) {
            mViewPager = viewPager;
            return this;
        }

        /**
         * Sets the padding.
         *
         * @param tabPaddingStart  the start padding in pixels
         * @param tabPaddingTop    the top padding in pixels
         * @param tabPaddingEnd    the end padding in pixels
         * @param tabPaddingBottom the bottom padding in pixels
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabPadding(int tabPaddingStart, int tabPaddingTop, int tabPaddingEnd,
                                     int tabPaddingBottom) {
            mTabPaddingStart = tabPaddingStart;
            mTabPaddingTop = tabPaddingTop;
            mTabPaddingEnd = tabPaddingEnd;
            mTabPaddingBottom = tabPaddingBottom;
            return this;
        }

        /**
         * Sets the text appearance from the specified style resource.
         *
         * @param tabTextAppearance The resource identifier of the style to apply.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabTextAppearance(int tabTextAppearance) {
            mTabTextAppearance = tabTextAppearance;
            return this;
        }

        /**
         * Set the background to a given resource. The resource should refer to
         * a Drawable object or 0 to remove the background.
         *
         * @param tabBackgroundResId The identifier of the resource.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabBackgroundResId(int tabBackgroundResId) {
            mTabBackgroundResId = tabBackgroundResId;
            return this;
        }

        /**
         * Sets the tab text selected color.
         *
         * @param tabSelectedTextColor The new text selected color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabSelectedTextColor(int tabSelectedTextColor) {
            mTabSelectedTextColor = tabSelectedTextColor;
            return this;
        }

        /**
         * Sets the tab indicator bar color.
         *
         * @param color The new indicator color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabIndicatorColor(int color) {
            mTabIndicatorColor = color;
            return this;
        }

        /**
         * Sets the tab indicator height.
         *
         * @param height The new indicator height in pixels
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabIndicatorHeight(int height) {
            mTabIndicatorHeight = height;
            return this;
        }

        /**
         * Creates a SimpleTabAdapter with the arguments supplied to this builder.
         *
         * @return A SimpleTabAdapter instance
         */
        public SimpleTabAdapter build() {
            if (mViewPager == null) {
                throw new IllegalArgumentException("ViewPager cannot be null");
            }
            return new SimpleTabAdapter(this);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        getViewPager().setCurrentItem(pos, true);
                    }
                }
            });
        }
    }
}
