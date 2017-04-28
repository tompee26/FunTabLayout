package com.tompee.funtablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tompee.funtablayout.custom.PopTabView;

public class PopTabAdapter extends BaseAdapter<PopTabAdapter.ViewHolder> {
    private static final int MAX_TAB_TEXT_LINES = 1;

    private int mDefaultIconColor = Color.GRAY;
    private IconFetcher mIconFetcher;
    private int mIconDimension;
    private int mPopDuration = 500;

    public PopTabAdapter(Builder builder) {
        super(builder);

        if (builder.mIconDimension != null) {
            mIconDimension = builder.mIconDimension;
        }
        if (builder.mDefaultIconColor != null) {
            mDefaultIconColor = builder.mDefaultIconColor;
        }
        if (builder.mPopDuration != null) {
            mPopDuration = builder.mPopDuration;
        }
        mIconFetcher = builder.mIconFetcher;
    }

    @Override
    protected int getTabIndicatorColor() {
        return mTabIndicatorColor;
    }

    @Override
    protected int getTabIndicatorHeight() {
        return 1;
    }

    @Override
    public PopTabAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopTabView itemView = new PopTabView(parent.getContext());
        itemView.setLayoutParams(createLayoutParamsForTabs(parent));
        ViewCompat.setPaddingRelative(itemView, mTabPaddingStart, mTabPaddingTop,
                mTabPaddingEnd, mTabPaddingBottom);
        itemView.setBackgroundResource(mTabBackgroundResId);
        itemView.setIconDimension(mIconDimension);
        itemView.setTextAppearance(mTabTextAppearance);
        itemView.setMaxLines(MAX_TAB_TEXT_LINES);
        itemView.setTextColor(mTabIndicatorColor);
        itemView.setPopDuration(mPopDuration);
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
    public void onBindViewHolder(PopTabAdapter.ViewHolder holder, int position) {
        CharSequence title = getViewPager().getAdapter().getPageTitle(position);
        PopTabView view = (PopTabView) holder.mTitle.getParent();
        view.setSelected(getCurrentIndicatorPosition() == position);
        view.setTextVisible(getCurrentIndicatorPosition() == position);
        holder.mTitle.setText(title);
        if (mIconFetcher != null) {
            holder.mIcon.setImageDrawable(loadIconWithTint(holder.mIcon.getContext(),
                    mIconFetcher.getIcon(position)));
        } else {
            holder.mIcon.setVisibility(View.GONE);
        }
    }

    private Drawable loadIconWithTint(Context context, @DrawableRes int resourceId) {
        Drawable icon = ContextCompat.getDrawable(context, resourceId);
        ColorStateList colorStateList;
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{-android.R.attr.state_empty}
        };
        int[] colors = new int[]{
                mTabIndicatorColor,
                mDefaultIconColor
        };
        colorStateList = new ColorStateList(states, colors);
        icon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(icon, colorStateList);
        return icon;
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
        private Integer mDefaultIconColor;
        private Integer mPopDuration;

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
         * Sets icon color when a tab is not selected. Default color is gray
         *
         * @param color New color to set
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setDefaultIconColor(int color) {
            mDefaultIconColor = color;
            return this;
        }

        /**
         * Sets the pop animation duration
         *
         * @param duration New duration in milliseconds. Default is 500
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPopDuration(int duration) {
            mPopDuration = duration;
            return this;
        }

        /**
         * Creates a SimpleTabAdapter with the arguments supplied to this builder.
         *
         * @return A SimpleTabAdapter instance
         */
        public PopTabAdapter build() {
            if (getViewPager() == null) {
                throw new IllegalArgumentException("ViewPager cannot be null");
            }
            return new PopTabAdapter(this);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = ((PopTabView) itemView).getIconView();
            mTitle = ((PopTabView) itemView).getTitleView();
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
