package vn.tonish.hozo.fragment;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 8/22/17.
 */

public class TaskDetailTab2Fragment extends BaseFragment {

    private static final String TAG = TaskDetailTab2Fragment.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.task_detail_tab2_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {
        LogUtils.d(TAG, "TaskDetailTab2Fragment resumeData");
    }

    public void updateData(){
        LogUtils.d(TAG, "TaskDetailTab2Fragment updateData");
    }

}
