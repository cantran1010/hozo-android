package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
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
    private Spinner spType;
    private int posterFilterPosition = 0;
    private int workerFilterPosition = 0;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        tvWorker = (TextViewHozo) findViewById(R.id.tv_worker);
        tvPoster = (TextViewHozo) findViewById(R.id.tv_poster);
        spType = (Spinner) findViewById(R.id.sp_type);
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
        updateSpinner(1);
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

    private void updateSpinner(final int position) {

        List<String> list = new ArrayList<>();
        if (position == 1) {
            list.add(getString(R.string.my_task_status_all));
            list.add(getString(R.string.my_task_status_poster_open));
            list.add(getString(R.string.my_task_status_poster_assigned));
            list.add(getString(R.string.my_task_status_poster_completed));
            list.add(getString(R.string.my_task_status_poster_overdue));
            list.add(getString(R.string.my_task_status_poster_canceled));
        } else {
            list.add(getString(R.string.my_task_status_all));
            list.add(getString(R.string.my_task_status_worker_open));
            list.add(getString(R.string.my_task_status_worker_assigned));
            list.add(getString(R.string.my_task_status_worker_completed));
            list.add(getString(R.string.my_task_status_worker_missed));
            list.add(getString(R.string.my_task_status_worker_canceled));
            list.add(getString(R.string.my_task_status_poster_overdue));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(dataAdapter);

        if (role.equals(Constants.ROLE_TASKER)) {
            spType.setSelection(workerFilterPosition);
        } else if (role.equals(Constants.ROLE_POSTER)) {
            spType.setSelection(posterFilterPosition);
        }

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LogUtils.d(TAG, "onItemSelected , pos : " + pos + " , workerFilterPosition : " + workerFilterPosition + " , posterFilterPosition : " + posterFilterPosition);
//                if (pos == 0) return;
                if (role.equals(Constants.ROLE_TASKER)) {

                    if (pos == workerFilterPosition) return;

                    workerFilterPosition = pos;
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER);
                    intentAnswer.putExtra(Constants.MYTASK_FILTER_EXTRA, getStatus(Constants.ROLE_TASKER, pos));
                    getActivity().sendBroadcast(intentAnswer);
                } else if (role.equals(Constants.ROLE_POSTER)) {

                    if (pos == posterFilterPosition) return;

                    posterFilterPosition = pos;
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_POSTER);
                    intentAnswer.putExtra(Constants.MYTASK_FILTER_EXTRA, getStatus(Constants.ROLE_POSTER, pos));
                    getActivity().sendBroadcast(intentAnswer);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                updateSpinner(2);
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
                updateSpinner(1);
                break;

        }
    }

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
                    result = "assigned";
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

            }
        }
        return result;
    }
}

