package vn.tonish.hozo.fragment;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import vn.tonish.hozo.R;

import static vn.tonish.hozo.utils.Utils.setViewBackground;

/**
 * Created by Admin on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private TextView layoutWorker, layoutPoster;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        layoutWorker = (TextView) findViewById(R.id.layout_worker);
        layoutPoster = (TextView) findViewById(R.id.layout_poster);
        selectedTab(1);

    }

    @Override
    protected void initData() {
        openFragment(R.id.my_task_container, WorkerFragment.class, false);
        layoutWorker.setOnClickListener(this);
        layoutPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    private void selectedTab(int position) {
        if (position == 1) {
            layoutWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
            setViewBackground(layoutWorker,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_green));
            layoutPoster.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray));
            setViewBackground(layoutPoster,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_gray));
        }else {
            layoutWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            setViewBackground(layoutWorker,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_gray));
            layoutPoster.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
            setViewBackground(layoutPoster,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_green));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_worker:
                selectedTab(1);
                openFragment(R.id.my_task_container, WorkerFragment.class, false);
                break;

            case R.id.layout_poster:
                selectedTab(2);
                openFragment(R.id.my_task_container, PosterFragment.class, false);
                break;

        }

    }
}
