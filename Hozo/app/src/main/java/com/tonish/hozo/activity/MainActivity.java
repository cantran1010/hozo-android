package com.tonish.hozo.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.tonish.hozo.R;
import com.tonish.hozo.fragment.SelectTaskFragment;
import com.tonish.hozo.fragment.MyTaskFragment;
import com.tonish.hozo.fragment.InboxFragment;
import com.tonish.hozo.fragment.BrowseTaskFragment;
import com.tonish.hozo.fragment.HelpFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout homeLayout, searchLayout, myLayout, notifiLayout, settingLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        homeLayout = (LinearLayout) findViewById(R.id.home_layout);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        myLayout = (LinearLayout) findViewById(R.id.my_layout);
        notifiLayout = (LinearLayout) findViewById(R.id.notifi_layout);
        settingLayout = (LinearLayout) findViewById(R.id.setting_layout);

        homeLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
        notifiLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, SelectTaskFragment.class, false);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout:
                openFragment(R.id.layout_container, SelectTaskFragment.class, true);
                break;

            case R.id.search_layout:
                openFragment(R.id.layout_container, BrowseTaskFragment.class, true);
                break;

            case R.id.my_layout:
                openFragment(R.id.layout_container, MyTaskFragment.class, true);
                break;

            case R.id.notifi_layout:
                openFragment(R.id.layout_container, InboxFragment.class, true);
                break;

            case R.id.setting_layout:
                openFragment(R.id.layout_container, HelpFragment.class, true);
                break;
        }
    }
}
