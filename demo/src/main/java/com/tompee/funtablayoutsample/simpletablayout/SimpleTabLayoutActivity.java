package com.tompee.funtablayoutsample.simpletablayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tompee.funtablayout.FunTabLayout;
import com.tompee.funtablayout.SimpleTabAdapter;
import com.tompee.funtablayoutsample.R;
import com.tompee.funtablayoutsample.fragment.SampleFragment;

public class SimpleTabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        getSupportActionBar().setElevation(0);

        FunTabLayout tablayout = (FunTabLayout) findViewById(R.id.tablayout);
        tablayout.setTabVisibleCount(5);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SampleFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Fragment" + position;
            }
        });

        SimpleTabAdapter.Builder builder = new SimpleTabAdapter.Builder(this).
                setViewPager(viewPager).
                setTabPadding(16, 16, 16, 16).
                setTabSelectedTextColor(Color.WHITE).
                setTabIndicatorHeight(5).
                setTabIndicatorColor(Color.WHITE).
                setTabBackgroundResId(R.drawable.ripple).
                setTabTextAppearance(R.style.SimpleTabText);
        tablayout.setUpWithAdapter(builder.build());
    }
}
