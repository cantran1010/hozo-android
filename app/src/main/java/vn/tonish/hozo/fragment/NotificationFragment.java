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
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.NotifyFragmentAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 9/15/17.
 */

public class NotificationFragment extends BaseFragment {

    private static final String TAG = NotificationFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NotifyFragmentAdapter notifyFragmentAdapter;
    private ImageView imgTab1, imgTab2;
    private TextViewHozo tvTab1, tvTab2, tvCountTab1, tvCountTab2;

    @Override
    protected int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notification_tab_1)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.notification_tab_2)));

        final ViewGroup test = (ViewGroup) (tabLayout.getChildAt(0));//tabs is your Tablayout
        int tabLen = test.getChildCount();
        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(0, 0, 0, 0);
        }

        @SuppressLint("InflateParams") View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab_notification, null, false);

        tabLayout.getTabAt(0).setCustomView(headerView.findViewById(R.id.ll1));
        tabLayout.getTabAt(1).setCustomView(headerView.findViewById(R.id.ll2));

        imgTab1 = (ImageView) findViewById(R.id.img_tab1);
        imgTab2 = (ImageView) findViewById(R.id.img_tab2);

        tvTab1 = (TextViewHozo) findViewById(R.id.tv_tab1);
        tvTab2 = (TextViewHozo) findViewById(R.id.tv_tab2);

        tvCountTab1 = (TextViewHozo) findViewById(R.id.tv_count_tab1);
        tvCountTab2 = (TextViewHozo) findViewById(R.id.tv_count_tab2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager != null)
                    viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_tab_notification_off));

                    imgTab1.setImageResource(R.drawable.icon_notificaion1_on);
                    imgTab2.setImageResource(R.drawable.icon_notificaion2_off);
                } else if (tab.getPosition() == 1) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_tab_notification_off));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                    imgTab1.setImageResource(R.drawable.icon_notificaion1_off);
                    imgTab2.setImageResource(R.drawable.icon_notificaion2_on);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.pager);
        notifyFragmentAdapter = new NotifyFragmentAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(notifyFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastCountNewMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver broadcastCountNewMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra(Constants.COUNT_NEW_MSG_EXTRA)) {
                int countTab2 = intent.getIntExtra(Constants.COUNT_NEW_MSG_EXTRA, 0);

                if (countTab2 > 0) {
                    tvCountTab2.setVisibility(View.VISIBLE);
                    tvCountTab2.setText(String.valueOf(countTab2));
                } else
                    tvCountTab2.setVisibility(View.GONE);
            } else updateCountMsg();

        }
    };

    public void updateCountMsg() {
        int pushCount = PreferUtils.getNewPushCount(getActivity());

        if (pushCount == 0) {
            tvCountTab1.setVisibility(View.GONE);
        } else {
            tvCountTab1.setVisibility(View.VISIBLE);
            tvCountTab1.setText(String.valueOf(pushCount));
        }
    }

}
