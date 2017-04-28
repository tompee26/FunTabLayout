package com.tompee.funtablayoutsample.fliptablayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tompee.funtablayout.FlipTabAdapter;
import com.tompee.funtablayout.FunTabLayout;
import com.tompee.funtablayoutsample.R;
import com.tompee.funtablayoutsample.fragment.SampleFragment;

public class FlipTabLayoutActivity extends AppCompatActivity implements FlipTabAdapter.IconFetcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        //noinspection ConstantConditions
        getSupportActionBar().setElevation(0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        FunTabLayout tabLayout = (FunTabLayout) findViewById(R.id.tablayout);
        FlipTabAdapter.Builder builder = new FlipTabAdapter.Builder(this).
                setViewPager(viewPager).
                setTabPadding(24, 24, 24, 24).
                setTabTextAppearance(R.style.FlipTabText).
                setTabBackgroundResId(R.drawable.ripple).
                setTabIndicatorColor(Color.GREEN).
                setIconFetcher(this).
                setIconDimension(80).
                setDefaultIconColor(Color.WHITE);
        tabLayout.setUpWithAdapter(builder.build());
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SampleFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "Call";
                        break;
                    case 1:
                        title = "Play";
                        break;
                    case 2:
                        title = "Chat";
                        break;
                    case 3:
                        title = "Write";
                        break;
                    case 4:
                        title = "Mail";
                        break;
                }
                return title;
            }
        });
    }

    @Override
    public int getIcon(int position) {
        int resource = R.mipmap.ic_launcher;
        switch (position) {
            case 0:
                resource = R.drawable.ic_call_white_48dp;
                break;
            case 1:
                resource = R.drawable.ic_games_white_48dp;
                break;
            case 2:
                resource = R.drawable.ic_chat_white_48dp;
                break;
            case 3:
                resource = R.drawable.ic_create_white_48dp;
                break;
            case 4:
                resource = R.drawable.ic_mail_white_48dp;
                break;
        }
        return resource;
    }
}
