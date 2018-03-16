package vn.tonish.hozo.fragment.mytask;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.FilterMyTaskActivity;
import vn.tonish.hozo.adapter.MyTaskFragmentAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.TypefaceContainer;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.showSearch;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyTaskFragment.class.getSimpleName();
    private String role = Constants.ROLE_POSTER;
    private TabLayout myTaskTabLayout;
    private ViewPager myTaskViewPager;
    private MyTaskFragmentAdapter myTaskFragmentAdapter;
    private TextViewHozo tvTabPoster, tvTabTasker;
    private ImageView imgClear;
    private String mQuery = "";
    private int position = 0;
    private EdittextHozo edtSearch;
    private RelativeLayout layoutHeader, layoutSearch;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_mytask;
    }

    @Override
    protected void initView() {
        layoutHeader = (RelativeLayout) findViewById(R.id.layout_header_my_task);
        layoutSearch = (RelativeLayout) findViewById(R.id.layout_hedaer_search);
        myTaskTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ImageView imgFilter = (ImageView) findViewById(R.id.img_filter);
        ImageView imgSearch = (ImageView) findViewById(R.id.img_search);
        edtSearch = (EdittextHozo) findViewById(R.id.edt_search);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgFilter.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        myTaskViewPager = (ViewPager) findViewById(R.id.pager);
        myTaskTabLayout.addTab(myTaskTabLayout.newTab().setText(getContext().getString(R.string.my_task_poster)));
        myTaskTabLayout.addTab(myTaskTabLayout.newTab().setText(getContext().getString(R.string.my_task_worker)));
        @SuppressLint("InflateParams") View headerView = getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) != null ? ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_tab_mytask, null, false) : null : null : null;
        final ViewGroup test = (ViewGroup) (myTaskTabLayout.getChildAt(0));
        int tabLen = test.getChildCount();
        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(0, 0, 0, 0);
        }
        myTaskTabLayout.getTabAt(0).setCustomView(headerView.findViewById(R.id.ll1));
        myTaskTabLayout.getTabAt(1).setCustomView(headerView.findViewById(R.id.ll2));
        tvTabPoster = (TextViewHozo) findViewById(R.id.tv_tab1);
        tvTabTasker = (TextViewHozo) findViewById(R.id.tv_tab2);
        myTaskViewPager = (ViewPager) findViewById(R.id.pager);
        myTaskFragmentAdapter = new MyTaskFragmentAdapter
                (getActivity().getSupportFragmentManager(), myTaskTabLayout.getTabCount());
        myTaskViewPager.setAdapter(myTaskFragmentAdapter);
        myTaskViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTaskTabLayout));
        myTaskTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (myTaskViewPager != null)
                    myTaskViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tvTabPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTabTasker.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTabPoster.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
                    tvTabTasker.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    role = Constants.ROLE_POSTER;
                } else if (tab.getPosition() == 1) {
                    role = Constants.ROLE_TASKER;
                    tvTabPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.setting_text));
                    tvTabTasker.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
                    tvTabPoster.setTypeface(TypefaceContainer.TYPEFACE_LIGHT);
                    tvTabTasker.setTypeface(TypefaceContainer.TYPEFACE_REGULAR);
                }
                LogUtils.d(TAG, "my role: " + role + "position" + position);
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
        if (role != null && role.equalsIgnoreCase(Constants.ROLE_POSTER)) {
            myTaskTabLayout.getTabAt(0).select();
        } else {
            myTaskTabLayout.getTabAt(1).select();
        }
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                hideKeyBoard(getActivity());
                return actionId == EditorInfo.IME_ACTION_SEARCH;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }
                search();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void search() {
        mQuery = edtSearch.getText().toString().trim();
        if (position == 0)
            myTaskFragmentAdapter.search(mQuery, 0);
        else myTaskFragmentAdapter.search(mQuery, 1);
    }

    @Override
    protected void resumeData() {
        LogUtils.d(TAG, "my role 1: " + role + "position" + position);
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
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_filter:
                actionFilter();
                break;
            case R.id.img_search:
                edtSearch.requestFocus();
                Utils.showSoftKeyboard(getActivity(), edtSearch);
                showSearch(getContext(), layoutSearch, true);
                showSearch(getContext(), layoutHeader, false);
                break;
            case R.id.img_back:
                mQuery = null;
                showSearch(getContext(), layoutHeader, true);
                showSearch(getContext(), layoutSearch, false);
                myTaskFragmentAdapter.resetState(position);
                if (!edtSearch.getText().toString().trim().isEmpty())
                    myTaskFragmentAdapter.onRefreshTab(position);
                edtSearch.setText("");

                break;
            case R.id.img_clear:
                edtSearch.setText("");
                break;
        }

    }

    private void actionFilter() {
        Intent intent = new Intent(getContext(), FilterMyTaskActivity.class);
        intent.putExtra(Constants.EXTRA_MY_TASK, role);
        startActivityForResult(intent, Constants.REQUEST_CODE_FILTER_MY_TASK, TransitionScreen.RIGHT_TO_LEFT);
    }

    public void changeTab(int tab) {
        myTaskTabLayout.getTabAt(tab).select();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Constants.REQUEST_CODE_FILTER_MY_TASK && resultCode == Constants.FILTER_MY_TASK_RESPONSE_CODE)) {
            if (position == 0)
                myTaskFragmentAdapter.onRefreshTab(0);
            else
                myTaskFragmentAdapter.onRefreshTab(1);
        }

    }
}

