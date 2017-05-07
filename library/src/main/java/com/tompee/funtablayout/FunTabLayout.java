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

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class FunTabLayout extends RecyclerView {
    protected static final long DEFAULT_SCROLL_DURATION = 200;
    protected static final float DEFAULT_POSITION_THRESHOLD = 0.6f;
    protected static final float POSITION_THRESHOLD_ALLOWABLE = 0.001f;
    private static final String TAG = "FunTabLayout";
    protected final Paint mIndicatorPaint;
    protected final LinearLayoutManager mLinearLayoutManager;
    protected int mTabVisibleCount;
    protected RecyclerOnScrollListener mRecyclerOnScrollListener;
    protected ViewPager mViewPager;
    protected BaseAdapter<?> mAdapter;

    protected int mIndicatorPosition;
    protected int mIndicatorOffset;
    protected int mScrollOffset;
    protected float mOldPositionOffset;
    protected float mPositionThreshold;
    protected boolean mRequestScrollToTab;
    protected boolean mScrollEanbled;
    protected float mTabPositionOffset;

    public FunTabLayout(Context context) {
        this(context, null);
    }

    public FunTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mIndicatorPaint = new Paint();
        getAttributes(context, attrs, defStyle);
        mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return mScrollEanbled;
            }
        };
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(mLinearLayoutManager);
        addItemDecoration(new IndicatorDecoration());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private void getAttributes(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FunTabLayout, defStyle, 0);
        mScrollEanbled = a.getBoolean(R.styleable.FunTabLayout_scrollEnabled, true);
        mTabVisibleCount = a.getInteger(R.styleable.FunTabLayout_tabVisibleCount, 0);
        mPositionThreshold = a.getFloat(R.styleable.FunTabLayout_positionThreshold,
                DEFAULT_POSITION_THRESHOLD);
        a.recycle();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRecyclerOnScrollListener != null) {
            removeOnScrollListener(mRecyclerOnScrollListener);
            mRecyclerOnScrollListener = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * Sets the maximum number of visible tab
     *
     * @param count The new visible tab count
     */
    public void setTabVisibleCount(int count) {
        mTabVisibleCount = count;
    }

    /**
     * Sets the position threshold on when to switch tabs
     *
     * @param threshold New position threshold
     */
    public void setPositionThreshold(float threshold) {
        mPositionThreshold = threshold;
    }

    public void setUpWithAdapter(BaseAdapter<?> adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter cannot be null");
        }
        mAdapter = adapter;
        mViewPager = adapter.getViewPager();
        if (mViewPager.getAdapter() == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        if (mTabVisibleCount == 0) {
            throw new IllegalArgumentException("Tab visible count cannot be 0");
        }
        if (mAdapter instanceof SimpleTabAdapter) {
            mViewPager.addOnPageChangeListener(new SimpleTabOnPageChangeListener(this));
        } else if (mAdapter instanceof BubbleTabAdapter) {
            mPositionThreshold = 1;
            mViewPager.addOnPageChangeListener(new BubbleTabOnPageChangeListener(this));
        } else if (mAdapter instanceof PopTabAdapter) {
            mViewPager.addOnPageChangeListener(new SimpleTabOnPageChangeListener(this));
        } else if (mAdapter instanceof FlipTabAdapter) {
            mPositionThreshold = 0.5f;
            mViewPager.addOnPageChangeListener(new FlipTabOnPageChangeListener(this));
        }
        mAdapter.setTabVisibleCount(mTabVisibleCount);
        setAdapter(adapter);
        scrollToTab(mViewPager.getCurrentItem());
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, smoothScroll);
            scrollToTab(mViewPager.getCurrentItem());
            return;
        }

        if (smoothScroll && position != mIndicatorPosition) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                startAnimation(position);
            } else {
                scrollToTab(position); //FIXME add animation
            }

        } else {
            scrollToTab(position);
        }
    }

    @Override
    public void childDrawableStateChanged(View child) {
        super.childDrawableStateChanged(child);

        // force ItemDecorations to be drawn
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void startAnimation(final int position) {

        float distance = 1;

        View view = mLinearLayoutManager.findViewByPosition(position);
        if (view != null) {
            float currentX = view.getX() + view.getMeasuredWidth() / 2.f;
            float centerX = getMeasuredWidth() / 2.f;
            distance = Math.abs(centerX - currentX) / view.getMeasuredWidth();
        }

        ValueAnimator animator;
        if (position < mIndicatorPosition) {
            animator = ValueAnimator.ofFloat(distance, 0);
        } else {
            animator = ValueAnimator.ofFloat(-distance, 0);
        }
        animator.setDuration(DEFAULT_SCROLL_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollToTab(position, (float) animation.getAnimatedValue(), true);
            }
        });
        animator.start();
    }

    protected void scrollToTab(int position) {
        scrollToTab(position, 0, false);
        mAdapter.setCurrentIndicatorPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    protected void scrollToTab(int position, float positionOffset, boolean fitIndicator) {
        int scrollOffset = 0;

        View selectedView = mLinearLayoutManager.findViewByPosition(position);
        View nextView = mLinearLayoutManager.findViewByPosition(position + 1);

        if (selectedView != null) {
            int width = getMeasuredWidth();
            float scroll1 = width / 2.f - selectedView.getMeasuredWidth() / 2.f;

            if (nextView != null) {
                float scroll2 = width / 2.f - nextView.getMeasuredWidth() / 2.f;

                float scroll = scroll1 + (selectedView.getMeasuredWidth() - scroll2);
                float dx = scroll * positionOffset;
                scrollOffset = (int) (scroll1 - dx);

                mScrollOffset = (int) dx;
                mIndicatorOffset = (int) ((scroll1 - scroll2) * positionOffset);

            } else {
                scrollOffset = (int) scroll1;
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }
            if (fitIndicator) {
                mScrollOffset = 0;
                mIndicatorOffset = 0;
            }

            if (mAdapter != null && mIndicatorPosition == position) {
                updateCurrentIndicatorPosition(position, positionOffset - mOldPositionOffset,
                        positionOffset);
            }

            mIndicatorPosition = position;

        } else {
            mRequestScrollToTab = true;
        }

        stopScroll();
        mLinearLayoutManager.scrollToPositionWithOffset(position, scrollOffset);

        if (mAdapter.getTabIndicatorHeight() > 0) {
            invalidate();
        }

        mOldPositionOffset = positionOffset;
    }

    protected void updateCurrentIndicatorPosition(int position, float dx, float positionOffset) {
        int indicatorPosition = -1;
        if (dx > 0 && positionOffset >= mPositionThreshold - POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position + 1;

        } else if (dx < 0 && positionOffset <= 1 - mPositionThreshold + POSITION_THRESHOLD_ALLOWABLE) {
            indicatorPosition = position;
        }
        if (indicatorPosition >= 0 && indicatorPosition != mAdapter.getCurrentIndicatorPosition()) {
            mAdapter.setCurrentIndicatorPosition(indicatorPosition);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    protected static class RecyclerOnScrollListener extends OnScrollListener {

        protected final FunTabLayout mFunTabLayout;
        protected final LinearLayoutManager mLinearLayoutManager;
        public int mDx;

        public RecyclerOnScrollListener(FunTabLayout funTabLayout,
                                        LinearLayoutManager linearLayoutManager) {
            mFunTabLayout = funTabLayout;
            mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mDx += dx;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    if (mDx > 0) {
                        selectCenterTabForRightScroll();
                    } else {
                        selectCenterTabForLeftScroll();
                    }
                    mDx = 0;
                    break;
                case SCROLL_STATE_DRAGGING:
                case SCROLL_STATE_SETTLING:
            }
        }

        protected void selectCenterTabForRightScroll() {
            int first = mLinearLayoutManager.findFirstVisibleItemPosition();
            int last = mLinearLayoutManager.findLastVisibleItemPosition();
            int center = mFunTabLayout.getMeasuredWidth() / 2;
            for (int position = first; position <= last; position++) {
                View view = mLinearLayoutManager.findViewByPosition(position);
                if (view.getLeft() + view.getMeasuredWidth() >= center) {
                    mFunTabLayout.setCurrentItem(position, false);
                    break;
                }
            }
        }

        protected void selectCenterTabForLeftScroll() {
            int first = mLinearLayoutManager.findFirstVisibleItemPosition();
            int last = mLinearLayoutManager.findLastVisibleItemPosition();
            int center = mFunTabLayout.getWidth() / 2;
            for (int position = last; position >= first; position--) {
                View view = mLinearLayoutManager.findViewByPosition(position);
                if (view.getLeft() <= center) {
                    mFunTabLayout.setCurrentItem(position, false);
                    break;
                }
            }
        }
    }

    private class SimpleTabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final FunTabLayout mFunTabLayout;
        private int mScrollState;

        public SimpleTabOnPageChangeListener(FunTabLayout funTabLayout) {
            mFunTabLayout = funTabLayout;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mFunTabLayout.scrollToTab(position, positionOffset, false);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mFunTabLayout.mIndicatorPosition != position) {
                    mFunTabLayout.scrollToTab(position);
                }
            }
        }
    }

    private class BubbleTabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final FunTabLayout mFunTabLayout;
        private int mScrollState;

        public BubbleTabOnPageChangeListener(FunTabLayout funTabLayout) {
            mFunTabLayout = funTabLayout;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mFunTabLayout.scrollToTab(position, positionOffset, false);
            BubbleTabView view = (BubbleTabView) mLinearLayoutManager.findViewByPosition(position);
            if (view != null) {
                view.setViewAlpha(positionOffset);
            }
            BubbleTabView nextView = (BubbleTabView) mLinearLayoutManager.findViewByPosition(position + 1);
            if (nextView != null) {
                nextView.setViewAlpha(1 - positionOffset);
            }
            mTabPositionOffset = positionOffset;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mFunTabLayout.mIndicatorPosition != position) {
                    mFunTabLayout.scrollToTab(position);
                }
            }
        }
    }

    private class FlipTabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final FunTabLayout mFunTabLayout;
        private int mScrollState;

        public FlipTabOnPageChangeListener(FunTabLayout funTabLayout) {
            mFunTabLayout = funTabLayout;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mFunTabLayout.scrollToTab(position, positionOffset, false);
            FlipTabView view = (FlipTabView) mLinearLayoutManager.findViewByPosition(position);
            if (view != null && positionOffset != 0) {
                view.setRotationY(positionOffset * 360);
            }
            FlipTabView nextView = (FlipTabView) mLinearLayoutManager.findViewByPosition(position + 1);
            if (nextView != null && positionOffset != 0) {
                nextView.setRotationY(positionOffset * 360);
            }
            mTabPositionOffset = positionOffset;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mFunTabLayout.mIndicatorPosition != position) {
                    mFunTabLayout.scrollToTab(position);
                }
            }
        }
    }

    public final class IndicatorDecoration extends RecyclerView.ItemDecoration {
        private static final float BUBBLE_ICON_SCALE_FACTOR = 0.80f;

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            View view = mLinearLayoutManager.findViewByPosition(mIndicatorPosition);
            if (view == null) {
                if (mRequestScrollToTab) {
                    mRequestScrollToTab = false;
                    scrollToTab(mViewPager.getCurrentItem());
                }
                return;
            }
            mRequestScrollToTab = false;

            int left, right, top, bottom;
            if (mAdapter instanceof SimpleTabAdapter) {
                if (isLayoutRtl()) {
                    left = view.getLeft() - mScrollOffset - mIndicatorOffset;
                    right = view.getRight() - mScrollOffset + mIndicatorOffset;
                } else {
                    left = view.getLeft() + mScrollOffset - mIndicatorOffset;
                    right = view.getRight() + mScrollOffset + mIndicatorOffset;
                }
                mIndicatorPaint.setColor(mAdapter.getTabIndicatorColor());
                top = getHeight() - mAdapter.getTabIndicatorHeight();
                bottom = getHeight();
                canvas.drawRect(left, top, right, bottom, mIndicatorPaint);
            } else if (mAdapter instanceof BubbleTabAdapter) {
                if (isLayoutRtl()) {
                    left = view.getLeft() - mScrollOffset - mIndicatorOffset;
                    right = view.getRight() - mScrollOffset + mIndicatorOffset;
                } else {
                    left = view.getLeft() + mScrollOffset - mIndicatorOffset;
                    right = view.getRight() + mScrollOffset + mIndicatorOffset;
                }
                top = getTop();
                bottom = getBottom();
                int centerX = (right + left) / 2;
                int centerY = (top + bottom) / 2;
                mIndicatorPaint.setColor(mAdapter.getTabIndicatorColor());
                BubbleTabAdapter adapter = (BubbleTabAdapter) mAdapter;
                int maxRadius = (getHeight() / 2) - 12;
                if (mTabPositionOffset > 0.1 && mTabPositionOffset < 0.50) {
                    canvas.drawCircle(centerX, centerY, maxRadius * (1 - mTabPositionOffset),
                            mIndicatorPaint);
                } else if (mTabPositionOffset >= 0.50 && mTabPositionOffset < 0.99) {
                    canvas.drawCircle(centerX, centerY, maxRadius * (mTabPositionOffset), mIndicatorPaint);
                } else {
                    canvas.drawCircle(centerX, centerY, maxRadius, mIndicatorPaint);
                    Bitmap bitmap = adapter.getBitmapIcon(mViewPager.getCurrentItem());
                    if (bitmap != null) {
                        drawBubbleImageInCanvas(canvas, bitmap, maxRadius, centerX, centerY);
                    }
                }
            }
        }

        private void drawBubbleImageInCanvas(Canvas canvas, Bitmap bitmap, int radius, int centerX,
                                             int centerY) {
            int location = (int) (Math.sin(45) * radius * BUBBLE_ICON_SCALE_FACTOR);
            int dimen = location * 2;
            canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, dimen, dimen, true),
                    centerX - location, centerY - location, mIndicatorPaint);
        }
    }
}