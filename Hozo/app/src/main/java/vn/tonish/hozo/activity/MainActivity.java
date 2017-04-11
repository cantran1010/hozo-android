package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout homeLayout, searchLayout, myLayout, notifiLayout, settingLayout;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.activity_main;
    }

    @Override
    protected void initView() {
        homeLayout = (LinearLayout) findViewById(vn.tonish.hozo.R.id.home_layout);
        searchLayout = (LinearLayout) findViewById(vn.tonish.hozo.R.id.search_layout);
        myLayout = (LinearLayout) findViewById(vn.tonish.hozo.R.id.my_layout);
        notifiLayout = (LinearLayout) findViewById(vn.tonish.hozo.R.id.notifi_layout);
        settingLayout = (LinearLayout) findViewById(vn.tonish.hozo.R.id.setting_layout);

        homeLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
        notifiLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        openFragment(vn.tonish.hozo.R.id.layout_container, SelectTaskFragment.class, false);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case vn.tonish.hozo.R.id.home_layout:
                openFragment(R.id.layout_container, SelectTaskFragment.class, true);
                break;

            case vn.tonish.hozo.R.id.search_layout:
                openFragment(R.id.layout_container, BrowseTaskFragment.class, true);
                break;

            case vn.tonish.hozo.R.id.my_layout:
                openFragment(R.id.layout_container, MyTaskFragment.class, true);
                break;

            case vn.tonish.hozo.R.id.notifi_layout:
                openFragment(R.id.layout_container, InboxFragment.class, true);
                break;

            case vn.tonish.hozo.R.id.setting_layout:
                openFragment(R.id.layout_container, HelpFragment.class, true);
                break;
        }
    }
}
