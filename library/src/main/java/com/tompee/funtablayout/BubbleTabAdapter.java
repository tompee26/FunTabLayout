/**
 * Copyright (C) 2017 tompee
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tompee.funtablayout.custom.BubbleTabView;

import java.util.ArrayList;
import java.util.List;

public class BubbleTabAdapter extends BaseAdapter<BubbleTabAdapter.ViewHolder> {
    private static final int MAX_TAB_TEXT_LINES = 2;

    private int mTabVisibleCount;
    private int mTabPaddingStart;
    private int mTabPaddingTop;
    private int mTabPaddingEnd;
    private int mTabPaddingBottom;
    private int mTabTextAppearance;
    private int mTabBackgroundResId;
    private int mTabIndicatorColor;
    private IconFetcher mIconFetcher;
    private List<Bitmap> mPreloadedBitmaps;

    private BubbleTabAdapter(Builder builder) {
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
        if (builder.mTabIndicatorColor != null) {
            mTabIndicatorColor = builder.mTabIndicatorColor;
        }
        mIconFetcher = builder.mIconFetcher;
        getPreloadedBitmaps(builder.mContext);
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
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        mTabBackgroundResId = typedValue.resourceId;

        /** Tab Indicator */
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        mTabIndicatorColor = typedValue.data;
    }

    private void getPreloadedBitmaps(Context context) {
        mPreloadedBitmaps = new ArrayList<>();
        if (mIconFetcher != null) {
            for (int index = 0; index < getItemCount(); index++) {
                int resourceId = mIconFetcher.getSelectedIcon(index);
                mPreloadedBitmaps.add(BitmapFactory.decodeResource(context.getResources(),
                        resourceId, null));
            }
        }
    }

    public Bitmap getBitmapIcon(int position) {
        if (mPreloadedBitmaps.isEmpty()) {
            return null;
        }
        return mPreloadedBitmaps.get(position);
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
    protected int getTabIndicatorHeight() {
        /* No need to return since indicator is circular */
        return 1;
    }

    @Override
    public BubbleTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BubbleTabView itemView = new BubbleTabView(parent.getContext());
        itemView.setLayoutParams(createLayoutParamsForTabs(parent));
        ViewCompat.setPaddingRelative(itemView, mTabPaddingStart, mTabPaddingTop,
                mTabPaddingEnd, mTabPaddingBottom);
        itemView.setBackgroundResource(mTabBackgroundResId);

        TextView tabTextView = (TextView) itemView.getChildAt(1);
        TextViewCompat.setTextAppearance(tabTextView, mTabTextAppearance);
        tabTextView.setMaxLines(MAX_TAB_TEXT_LINES);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BubbleTabAdapter.ViewHolder holder, int position) {
        CharSequence title = getViewPager().getAdapter().getPageTitle(position);
        holder.mTitle.setText(title);
        if (mIconFetcher != null) {
            holder.mIcon.setBackgroundResource(mIconFetcher.getIcon(position));
        } else {
            holder.mIcon.setVisibility(View.GONE);
        }
        ((BubbleTabView) holder.mTitle.getParent()).resetAlpha();
    }

    @Override
    public int getItemCount() {
        return getViewPager().getAdapter().getCount();
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs(ViewGroup parent) {
        LinearLayout.LayoutParams params;
        int width;
        if (getItemCount() > mTabVisibleCount) {
            width = parent.getWidth() / mTabVisibleCount;
        } else {
            width = parent.getWidth() / getItemCount();
        }
        params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }

    public interface IconFetcher {
        int getIcon(int position);
        int getSelectedIcon(int position);
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
        private Integer mTabIndicatorColor;
        private IconFetcher mIconFetcher;

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
         * Sets the callback method to get the icon displayed in tabs
         *
         * @param fetcher The fetcher to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIconFetcher(IconFetcher fetcher) {
            mIconFetcher = fetcher;
            return this;
        }

        /**
         * Creates a SimpleTabAdapter with the arguments supplied to this builder.
         *
         * @return A SimpleTabAdapter instance
         */
        public BubbleTabAdapter build() {
            if (mViewPager == null) {
                throw new IllegalArgumentException("ViewPager cannot be null");
            }
            return new BubbleTabAdapter(this);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) ((ViewGroup) itemView).getChildAt(0);
            mTitle = (TextView) ((ViewGroup) itemView).getChildAt(1);
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
