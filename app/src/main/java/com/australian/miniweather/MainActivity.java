package com.australian.miniweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.australian.miniweather.fragment.MessageFragment;
import com.australian.miniweather.fragment.MainFragment;
import com.australian.miniweather.fragment.MeFragment;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class MainActivity extends AppCompatActivity implements TabHost.TabContentFactory{

    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        HeConfig.init("HE1906231629371478", "bba6db50910f456ba32432d1fb763e29");
        HeConfig.switchToFreeServerNode();
        initTabhost();
        initViewPager();
    }

    private void initViewPager() {

        //三个fragment组成的viewpager

        final Fragment[] fragments = new Fragment[]{
                new MainFragment(),
                new MessageFragment(),
                new MeFragment()
        };

        final ViewPager viewPager = findViewById(R.id.ac_main_view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments[i];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(mTabHost != null){
                    mTabHost.setCurrentTab(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mTabHost.setOnTabChangedListener( new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(mTabHost != null || viewPager != null){
                    int position = mTabHost.getCurrentTab();
                    viewPager.setCurrentItem(position);
                }
            }
        });
    }

    private void initTabhost() {
        mTabHost = findViewById(R.id.tab_host);
        mTabHost.setup();
        int[] titleIDs = {
                R.string.weather,
                R.string.message,
                R.string.me};
        int[] drawableIDs = {
                R.drawable.main_tab_icon_home,
                R.drawable.main_tab_icon_message,
                R.drawable.main_tab_icon_me};

        for (int i = 0; i < titleIDs.length; i++) {

            View view = getLayoutInflater().inflate(R.layout.main_tab_layout,null,false);

            ImageView icon = view.findViewById(R.id.main_tab_icon);
            TextView title = view.findViewById(R.id.main_tab_txt);
            View tab = view.findViewById(R.id.tab_bg);

            icon.setImageResource(drawableIDs[i]);
            title.setText(titleIDs[i]);

            tab.setBackgroundColor(getResources().getColor(R.color.activity_main_bg));

            mTabHost.addTab(
                    mTabHost.newTabSpec(getString(titleIDs[i]))
                            .setIndicator(view)
                            .setContent(this)
            );
        }
    }

    @Override
    public View createTabContent(String tag) {
        View view = new View(this);
        view.setMinimumHeight(0);
        view.setMinimumWidth(0);
        return view;
    }
}
