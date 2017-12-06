package vn.tonish.hozo.activity.task;

import android.os.Bundle;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.fragment.postTask.CreateTaskFragment;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by CanTran on 12/6/17.
 */

public class PostTaskActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_post_task;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        showFragment(R.id.layout_container, CreateTaskFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
    }

    @Override
    protected void resumeData() {

    }
}
