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
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tompee.funtablayout.custom.FlipTabView;

public class FlipTabAdapter extends BaseAdapter<FlipTabAdapter.ViewHolder> {
    private static final int MAX_TAB_TEXT_LINES = 1;
    private IconFetcher mIconFetcher;
    private int mIconDimension;

    public FlipTabAdapter(Builder builder) {
        super(builder);

        if (builder.mIconDimension != null) {
            mIconDimension = builder.mIconDimension;
        }
        mIconFetcher = builder.mIconFetcher;
    }

    @Override
    protected int getTabIndicatorHeight() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlipTabView itemView = new FlipTabView(parent.getContext());
        itemView.setLayoutParams(createLayoutParamsForTabs(parent));
        ViewCompat.setPaddingRelative(itemView, mTabPaddingStart, mTabPaddingTop,
                mTabPaddingEnd, mTabPaddingBottom);
        itemView.setBackgroundResource(mTabBackgroundResId);
        itemView.setIconDimension(mIconDimension);
        itemView.setTextAppearance(mTabTextAppearance);
        itemView.setMaxLines(MAX_TAB_TEXT_LINES);
        itemView.setTextColor(mTabIndicatorColor);
        return new ViewHolder(itemView);
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs(ViewGroup parent) {
        LinearLayout.LayoutParams params;
        int width;
        if (getItemCount() > getTabVisibleCount()) {
            width = parent.getWidth() / getTabVisibleCount();
        } else {
            width = parent.getWidth() / getItemCount();
        }
        params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CharSequence title = getViewPager().getAdapter().getPageTitle(position);
        FlipTabView view = (FlipTabView) holder.mTitle.getParent();
        view.setSelected(getCurrentIndicatorPosition() == position);
        Log.d("hello", "position: " + position + " selectedstate: " + view.getSelectedState());
        holder.mTitle.setText(title);
        if (mIconFetcher != null) {
            holder.mIcon.setBackgroundResource(mIconFetcher.getIcon(position));
        } else {
            holder.mIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getViewPager().getAdapter().getCount();
    }

    public interface IconFetcher {
        int getIcon(int position);
    }

    public static class Builder extends BaseAdapter.BaseBuilder {
        private IconFetcher mIconFetcher;
        private Integer mIconDimension;

        /**
         * Creates a builder for a simple tab adapter
         *
         * @param context the parent context
         */
        public Builder(Context context) {
            super(context);
        }

        /**
         * Set a list of items, which are supplied by the given ViewPager
         *
         * @param viewPager the ViewPager to link to, or null to clear any previous link
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setViewPager(ViewPager viewPager) {
            super.setViewPager(viewPager);
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
            super.setTabPadding(tabPaddingStart, tabPaddingTop, tabPaddingEnd, tabPaddingBottom);
            return this;
        }

        /**
         * Sets the text appearance from the specified style resource.
         *
         * @param tabTextAppearance The resource identifier of the style to apply.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabTextAppearance(int tabTextAppearance) {
            super.setTabTextAppearance(tabTextAppearance);
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
            super.setTabBackgroundResId(tabBackgroundResId);
            return this;
        }

        /**
         * Sets the tab indicator bar color.
         *
         * @param color The new indicator color
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTabIndicatorColor(int color) {
            super.setTabIndicatorColor(color);
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
         * Sets the dimension of the icon to be displayed. If not set, it will wrap parent height or
         * default to smallest dimension
         *
         * @param dimension Dimension in pixels
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIconDimension(int dimension) {
            mIconDimension = dimension;
            return this;
        }

        /**
         * Creates a SimpleTabAdapter with the arguments supplied to this builder.
         *
         * @return A SimpleTabAdapter instance
         */
        public FlipTabAdapter build() {
            if (getViewPager() == null) {
                throw new IllegalArgumentException("ViewPager cannot be null");
            }
            return new FlipTabAdapter(this);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = ((FlipTabView) itemView).getIconView();
            mTitle = ((FlipTabView) itemView).getTitleView();
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
