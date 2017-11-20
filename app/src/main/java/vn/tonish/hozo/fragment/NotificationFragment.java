package vn.tonish.hozo.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.NotifyFragmentAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TypefaceContainer;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 9/15/17.
 */

public class NotificationFragment extends BaseFragment {

    private static final String TAG = NotificationFragment.class.getSimpleName();
    private static final int TIME_DELAY = 500;
    private ViewPager viewPager;
    private TextViewHozo tvTab1, tvTab2, tvTab3, tvCountTab1, tvCountTab2, tvCountTab3;
    private int position = 0;

    @Override
    protected int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notification_tab_1)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notification_tab_2)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notification_tab_3)));
        final ViewGroup test = (ViewGroup) (tabLayout.getChildAt(0));//tabs is your Tablayout
        int tabLen = test.getChildCount();
        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(0, 0, 0, 0);
        }
        @SuppressLint("InflateParams") View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) != null ? ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab_notification, null, false) : null;

        tabLayout.getTabAt(0).setCustomView(headerView.findViewById(R.id.ll1));
        tabLayout.getTabAt(1).setCustomView(headerView.findViewById(R.id.ll2));
        tabLayout.getTabAt(2).setCustomView(headerView.findViewById(R.id.ll3));

        tvTab1 = (TextViewHozo) findViewById(R.id.tv_tab1);
        tvTab2 = (TextViewHozo) findViewById(R.id.tv_tab2);
        tvTab3 = (TextViewHozo) findViewById(R.id.tv_tab3);

        tvCountTab1 = (TextViewHozo) findViewById(R.id.tv_count_tab1);
        tvCountTab2 = (TextViewHozo) findViewById(R.id.tv_count_tab2);
        tvCountTab3 = (TextViewHozo) findViewById(R.id.tv_count_tab3);
        viewPager = (ViewPager) findViewById(R.id.pagerView);
        NotifyFragmentAdapter notifyFragmentAdapter = new NotifyFragmentAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(notifyFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (viewPager != null)
                    viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab3.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab1.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
                    tvTab2.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTab3.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                } else if (tab.getPosition() == 1) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTab3.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab1.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTab2.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
                    tvTab3.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);

                } else if (tab.getPosition() == 2) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab3.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTab1.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTab2.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTab3.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initData() {
        updateCountMsg();
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastCountNewMsg, new IntentFilter(Constants.BROAD_CAST_PUSH_COUNT));
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(broadcastCountNewMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver broadcastCountNewMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG,"push: ");

            if (intent.hasExtra(Constants.COUNT_NEW_MSG_EXTRA)) {
                int countTab2 = intent.getIntExtra(Constants.COUNT_NEW_MSG_EXTRA, 0);

                if (countTab2 > 0) {
                    tvCountTab2.setVisibility(View.VISIBLE);
                    tvCountTab2.setText(String.valueOf(countTab2));
                } else
                    tvCountTab2.setVisibility(View.GONE);
            } else updateCountMsg();
            if (intent.hasExtra(Constants.BROAD_CAST_SMOOTH_TOP_NOTIFICATION)) {
                if (intent.getStringExtra(Constants.BROAD_CAST_SMOOTH_TOP_NOTIFICATION).equalsIgnoreCase(getActivity().getString(R.string.smooth_top))) {
                    if (position == 0) {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_SYS_TEM);
                        getActivity().sendBroadcast(intentAnswer);
                    } else if (position == 1) {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_CHAT);
                        getActivity().sendBroadcast(intentAnswer);
                    } else {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_NEW_TASK);
                        getActivity().sendBroadcast(intentAnswer);
                    }
                }
            }

        }
    };

    private void updateCountMsg() {

        int pushCount = PreferUtils.getNewPushCount(getActivity());
        int pushNewTaskCount = PreferUtils.getPushNewTaskCount(getActivity());
        if (pushCount == 0) {
            tvCountTab1.setVisibility(View.GONE);
        } else {
            tvCountTab1.setVisibility(View.VISIBLE);
            tvCountTab1.setText(String.valueOf(pushCount));
        }
        if (pushNewTaskCount == 0) {
            tvCountTab3.setVisibility(View.GONE);
        } else {
            tvCountTab3.setVisibility(View.VISIBLE);
            tvCountTab3.setText(String.valueOf(pushNewTaskCount));
        }
    }

}
