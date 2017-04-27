package com.tompee.funtablayoutsample.bubbletablayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tompee.funtablayout.BubbleTabAdapter;
import com.tompee.funtablayout.FunTabLayout;
import com.tompee.funtablayoutsample.R;
import com.tompee.funtablayoutsample.fragment.SampleFragment;

public class BubbleTabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        getSupportActionBar().setElevation(0);

        FunTabLayout tablayout = (FunTabLayout) findViewById(R.id.tablayout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
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

        BubbleTabAdapter.IconFetcher fetcher = new BubbleTabAdapter.IconFetcher() {
            @Override
            public int getIcon(int position) {
                return getIconInternal(position);
            }

            @Override
            public int getSelectedIcon(int position) {
                return getIconInternal(position);
            }

            private int getIconInternal(int position) {
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
        };

        BubbleTabAdapter.Builder builder = new BubbleTabAdapter.Builder(this).
                setViewPager(viewPager).
                setTabPadding(24, 24, 24, 24).
                setTabIndicatorColor(Color.parseColor("#ffa022")).
                setTabBackgroundResId(R.drawable.ripple).
                setIconFetcher(fetcher).
                setTabTextAppearance(R.style.BubbleTabText);
        tablayout.setUpWithAdapter(builder.build());
    }
}
