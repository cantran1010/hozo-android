package vn.tonish.hozo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.HomeTabsAdapter;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HomeTabsAdapter homeTabsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        viewPager = (ViewPager) findViewById(R.id.pagers);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        homeTabsAdapter = new HomeTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeTabsAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        initTabs();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {

    }

    public void initTabs() {

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.getTabAt(0).setCustomView(R.layout.tab_1);
        tabLayout.getTabAt(1).setCustomView(R.layout.tab_2);
        tabLayout.getTabAt(2).setCustomView(R.layout.tab_3);
        tabLayout.getTabAt(3).setCustomView(R.layout.tab_4);
        tabLayout.getTabAt(3).setCustomView(R.layout.tab_5);

    }
}
