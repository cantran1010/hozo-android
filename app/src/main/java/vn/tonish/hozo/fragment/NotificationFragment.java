package vn.tonish.hozo.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 9/15/17.
 */

public class NotificationFragment extends BaseFragment {

    private static final String TAG = NotificationFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.detail_tab_1)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.detail_tab_2)));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

}
