package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.setViewBackground;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyTaskFragment.class.getSimpleName();
    private TextViewHozo tvWorker, tvPoster;
    private String role = Constants.ROLE_POSTER;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        tvWorker = (TextViewHozo) findViewById(R.id.tv_worker);
        tvPoster = (TextViewHozo) findViewById(R.id.tv_poster);
    }

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle.containsKey(Constants.ROLE_EXTRA)) role = bundle.getString(Constants.ROLE_EXTRA);

        if (role != null && role.equals(Constants.ROLE_POSTER)) {

            Bundle bundleRefresh = new Bundle();
            bundleRefresh.putBoolean(Constants.REFRESH_EXTRA, true);

            showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, bundleRefresh, TransitionScreen.FADE_IN);
            selectedTab(1);
        } else {
            showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
            selectedTab(1);
        }
        tvWorker.setOnClickListener(this);
        tvPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
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

    private void selectedTab(int position) {
        if (position == 1) {
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_active));
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_default));
        } else {
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_default));
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_active));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_worker:
                if (role.equals(Constants.ROLE_TASKER)) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER);
                    getActivity().sendBroadcast(intentAnswer);
                    break;
                }
                role = Constants.ROLE_TASKER;
                showChildFragment(R.id.layout_container_my_task, MyTaskWorkerFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                selectedTab(2);
                break;

            case R.id.tv_poster:
                if (role.equals(Constants.ROLE_POSTER)) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER);
                    getActivity().sendBroadcast(intentAnswer);
                    break;
                }
                role = Constants.ROLE_POSTER;
                showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
                selectedTab(1);
                break;

        }
    }
}

