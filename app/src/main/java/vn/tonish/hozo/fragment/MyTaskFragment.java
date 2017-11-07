package vn.tonish.hozo.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.MyTaskFragmentAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.TypefaceContainer;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment {
    private static final String TAG = MyTaskFragment.class.getSimpleName();
    private TextViewHozo tvWorker, tvPoster;
    private String role = Constants.ROLE_POSTER;
    private Spinner spType;
    private int posterFilterPosition = 0;
    private int workerFilterPosition = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyTaskFragmentAdapter myTaskFragmentAdapter;
    private TextViewHozo tvTab1, tvTab2;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_mytask;
    }

    @Override
    protected void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getString(R.string.my_task_poster)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getString(R.string.my_task_worker)));
        @SuppressLint("InflateParams") View headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab_mytask, null, false);
        tabLayout.getTabAt(0).setCustomView(headerView.findViewById(R.id.ll1));
        tabLayout.getTabAt(1).setCustomView(headerView.findViewById(R.id.ll2));
        tvTab1 = (TextViewHozo) findViewById(R.id.tv_tab1);
        tvTab2 = (TextViewHozo) findViewById(R.id.tv_tab2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        myTaskFragmentAdapter = new MyTaskFragmentAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myTaskFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager != null)
                    viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab1.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
                    tvTab2.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    if (role.equals(Constants.ROLE_TASKER)) {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER);
                        getActivity().sendBroadcast(intentAnswer);
                    }
                    role = Constants.ROLE_TASKER;

                } else if (tab.getPosition() == 1) {
                    if (role.equals(Constants.ROLE_POSTER)) {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER);
                        getActivity().sendBroadcast(intentAnswer);
                    }
                    role = Constants.ROLE_POSTER;
                    tvTab1.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTab2.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTab1.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTab2.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
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
        Bundle bundle = getArguments();
        if (bundle.containsKey(Constants.ROLE_EXTRA)) role = bundle.getString(Constants.ROLE_EXTRA);
        tabLayout.getTabAt(1).select();
//
//        if (role != null && role.equals(Constants.ROLE_POSTER)) {
//            new Handler().postDelayed(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            tabLayout.getTabAt(0).select();
//                        }
//                    }, 100);
//
////            Bundle bundleRefresh = new Bundle();
////            bundleRefresh.putBoolean(Constants.REFRESH_EXTRA, true);
////            showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, bundleRefresh, TransitionScreen.FADE_IN);
////            selectedTab(1);
//        } else {
//            new Handler().postDelayed(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            tabLayout.getTabAt(1).select();
//                        }
//                    }, 100);
////            showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
////            selectedTab(1);
//        }
//        updateSpinner(1);
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.REFRESH_EXTRA)) {
                openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.FADE_IN);
                updateMenuUi(3);
            } else {
                if (role.equals(Constants.ROLE_TASKER)) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER);
                    getActivity().sendBroadcast(intentAnswer);
                } else if (role.equals(Constants.ROLE_POSTER)) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER);
                    getActivity().sendBroadcast(intentAnswer);
                }
            }
        }
    };


//    private void updateSpinner(final int position) {
//
//        List<String> list = new ArrayList<>();
//        if (position == 1) {
//            list.add(getString(R.string.hozo_all));
//            list.add(getString(R.string.my_task_status_poster_open));
//            list.add(getString(R.string.my_task_status_poster_assigned));
//            list.add(getString(R.string.my_task_status_poster_completed));
//            list.add(getString(R.string.my_task_status_poster_overdue));
//            list.add(getString(R.string.my_task_status_poster_canceled));
//            list.add(getString(R.string.my_task_status_poster_draft));
//        } else {
//            list.add(getString(R.string.hozo_all));
//            list.add(getString(R.string.my_task_status_worker_open));
//            list.add(getString(R.string.my_task_status_worker_assigned));
//            list.add(getString(R.string.my_task_status_worker_completed));
//            list.add(getString(R.string.my_task_status_worker_missed));
//            list.add(getString(R.string.my_task_status_worker_canceled));
//            list.add(getString(R.string.my_task_status_poster_overdue));
//        }
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spType.setAdapter(dataAdapter);
//
//        if (role.equals(Constants.ROLE_TASKER)) {
//            spType.setSelection(workerFilterPosition);
//        } else if (role.equals(Constants.ROLE_POSTER)) {
//            spType.setSelection(posterFilterPosition);
//        }
//
//        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                LogUtils.d(TAG, "onItemSelected , pos : " + pos + " , workerFilterPosition : " + workerFilterPosition + " , posterFilterPosition : " + posterFilterPosition);
////                if (pos == 0) return;
//                if (role.equals(Constants.ROLE_TASKER)) {
//
//                    if (pos == workerFilterPosition) return;
//
//                    workerFilterPosition = pos;
//                    Intent intentAnswer = new Intent();
//                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER);
//                    intentAnswer.putExtra(Constants.MYTASK_FILTER_EXTRA, getStatus(Constants.ROLE_TASKER, pos));
//                    getActivity().sendBroadcast(intentAnswer);
//                } else if (role.equals(Constants.ROLE_POSTER)) {
//
//                    if (pos == posterFilterPosition) return;
//
//                    posterFilterPosition = pos;
//                    Intent intentAnswer = new Intent();
//                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER);
//                    intentAnswer.putExtra(Constants.MYTASK_FILTER_EXTRA, getStatus(Constants.ROLE_POSTER, pos));
//                    getActivity().sendBroadcast(intentAnswer);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }


    private String getStatus(String role, int pos) {

        String result = "";
        if (role.equals(Constants.ROLE_TASKER)) {

            switch (pos) {
                case 0:
                    result = "";
                    break;

                case 1:
                    result = "pending";
                    break;

                case 2:
                    result = "accepted";
                    break;

                case 3:
                    result = "completed";
                    break;
                case 4:
                    result = "missed";
                    break;
                case 5:
                    result = "canceled";
                    break;

                case 6:
                    result = "overdue";
                    break;
            }

        } else {

            switch (pos) {

                case 0:
                    result = "";
                    break;

                case 1:
                    result = "open";
                    break;

                case 2:
                    result = "assigned";
                    break;

                case 3:
                    result = "completed";
                    break;

                case 4:
                    result = "overdue";
                    break;

                case 5:
                    result = "canceled";
                    break;

                case 6:
                    result = "draft";
                    break;

            }
        }
        return result;
    }


}

