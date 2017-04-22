package vn.tonish.hozo.fragment;

import android.view.View;

import vn.tonish.hozo.R;

/**
 * Created by Admin on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private View layoutWorker, layoutPoster;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.my_fragment;
    }

    @Override
    protected void initView() {
        layoutWorker = findViewById(R.id.layout_worker);
        layoutPoster = findViewById(R.id.layout_poster);

    }

    @Override
    protected void initData() {
        openFragment(R.id.my_task_container, SelectTaskFragment.class, false);
        layoutWorker.setOnClickListener(this);
        layoutPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_worker:
                openFragment(R.id.my_task_container, SelectTaskFragment.class, false);
                break;

            case R.id.layout_poster:
                openFragment(R.id.my_task_container, BrowseTaskFragment.class, false);
                break;

        }

    }
}
