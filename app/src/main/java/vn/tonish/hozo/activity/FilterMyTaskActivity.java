package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.FilterMyTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.StatusEntity;
import vn.tonish.hozo.database.manager.StatusManager;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 11/7/17.
 */

public class FilterMyTaskActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = FilterMyTaskActivity.class.getSimpleName();
    private List<StatusEntity> statuses = new ArrayList<>();
    private String role = Constants.ROLE_POSTER;

    @Override
    protected int getLayout() {
        return R.layout.activity_mytask_filter;
    }

    @Override
    protected void initView() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_MY_TASK)) {
            role = intent.getStringExtra(Constants.EXTRA_MY_TASK);
            LogUtils.d(TAG, "role" + role);
        }
        if (StatusManager.getStatuswithRole(role) == null || !(StatusManager.getStatuswithRole(role).size() > 0)) {
            LogUtils.d(TAG, "check role " + statuses.toString());
            setData(role);
        }
        RecyclerView mRecyclerView = findViewById(R.id.rcv_task);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        statuses = StatusManager.getStatuswithRole(role);
        FilterMyTaskAdapter filterMyTaskAdapter = new FilterMyTaskAdapter(this, statuses);
        mRecyclerView.setAdapter(filterMyTaskAdapter);

    }

    @Override
    protected void resumeData() {

    }

    private void setData(final String mRole) {
        if (mRole.equalsIgnoreCase(Constants.ROLE_POSTER)) {
            StatusManager.insertStatusEntity(new StatusEntity("P0", Constants.ROLE_POSTER, getString(R.string.hozo_all), "", true));
            StatusManager.insertStatusEntity(new StatusEntity("P1", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_open), getString(R.string.status_poster_open), false));
            StatusManager.insertStatusEntity(new StatusEntity("P2", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_assigned), getString(R.string.tatus_poster_assigned), false));
            StatusManager.insertStatusEntity(new StatusEntity("P3", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_completed), getString(R.string.status_poster_completed), false));
            StatusManager.insertStatusEntity(new StatusEntity("P4", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_overdue), getString(R.string.status_poster_overdue), false));
            StatusManager.insertStatusEntity(new StatusEntity("P5", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_canceled), getString(R.string.status_poster_canceled), false));
            StatusManager.insertStatusEntity(new StatusEntity("P6", Constants.ROLE_POSTER, getString(R.string.my_task_status_poster_draft), getString(R.string.status_poster_draft), false));
        } else {
            StatusManager.insertStatusEntity(new StatusEntity("T0", Constants.ROLE_TASKER, getString(R.string.hozo_all), "", true));
            StatusManager.insertStatusEntity(new StatusEntity("T1", Constants.ROLE_TASKER, getString(R.string.my_task_status_worker_open), getString(R.string.status_worker_open), false));
            StatusManager.insertStatusEntity(new StatusEntity("T2", Constants.ROLE_TASKER, getString(R.string.my_task_status_worker_assigned), getString(R.string.status_worker_assigned), false));
            StatusManager.insertStatusEntity(new StatusEntity("T3", Constants.ROLE_TASKER, getString(R.string.my_task_status_worker_completed), getString(R.string.status_worker_completed), false));
            StatusManager.insertStatusEntity(new StatusEntity("T4", Constants.ROLE_TASKER, getString(R.string.my_task_status_worker_missed), getString(R.string.status_worker_missed), false));
            StatusManager.insertStatusEntity(new StatusEntity("T5", Constants.ROLE_TASKER, getString(R.string.my_task_status_worker_canceled), getString(R.string.status_worker_canceled), false));
            StatusManager.insertStatusEntity(new StatusEntity("T6", Constants.ROLE_TASKER, getString(R.string.my_task_status_poster_overdue), getString(R.string.status_poster_overdue), false));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                setResult(Constants.FILTER_MY_TASK_RESPONSE_CODE);
                finish();
                break;
        }

    }
}
