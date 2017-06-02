package vn.tonish.hozo.fragment;

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
    public static final int LIMIT = 15;
    private String role = Constants.ROLE_TASKER;

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

        showChildFragment(R.id.layout_container_my_task, MyTaskWorkerFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);

        tvWorker.setOnClickListener(this);
        tvPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    private void selectedTab(int position) {
        if (position == 1) {
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_active));
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_default));
        } else {
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_default));
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_active));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_worker:
                if (role.equals(Constants.ROLE_TASKER)) break;
                role = Constants.ROLE_TASKER;
                showChildFragment(R.id.layout_container_my_task, MyTaskWorkerFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
                selectedTab(1);
                break;

            case R.id.tv_poster:
                if (role.equals(Constants.ROLE_POSTER)) break;
                role = Constants.ROLE_POSTER;
                showChildFragment(R.id.layout_container_my_task, MyTaskPosterFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                selectedTab(2);
                break;

        }
    }
}

