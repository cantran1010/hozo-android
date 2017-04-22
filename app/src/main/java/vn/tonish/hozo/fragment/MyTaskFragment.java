package vn.tonish.hozo.fragment;

import android.view.View;
import android.widget.TextView;

import vn.tonish.hozo.R;

/**
 * Created by Admin on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private TextView layoutWorker, layoutPoster;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.my_fragment;
    }

    @Override
    protected void initView() {
        layoutWorker = (TextView) findViewById(R.id.layout_worker);
        layoutPoster = (TextView) findViewById(R.id.layout_poster);

    }

    @Override
    protected void initData() {
        openFragment(R.id.my_task_container, PosterFragment.class, false);
        layoutWorker.setOnClickListener(this);
        layoutPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    private void selectedTab(int position) {
        if (position == 1) {
            layoutWorker.setTextColor(getResources().getColor(R.color.green));
//            layoutPoster.setBackground(getResources().getDrawable(R.drawable.bg_border_green));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_worker:
                openFragment(R.id.my_task_container, WorkerFragment.class, false);
                break;

            case R.id.layout_poster:
                openFragment(R.id.my_task_container, PosterFragment.class, false);
                break;

        }

    }
}
