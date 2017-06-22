package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.TaskAlertsAdapter;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.model.TaskAlert;

/**
 * Created by CanTran on 6/21/17.
 */

public class TaskAlertsActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<TaskAlert> taskAlerts;
    private TaskAlertsAdapter taskAlertsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_task_alerts;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_alert);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAlerts = new ArrayList<>();
        taskAlertsAdapter = new TaskAlertsAdapter(taskAlerts);
        mRecyclerView.setAdapter(taskAlertsAdapter);
    }

    @Override
    protected void initData() {
        if (!(CategoryManager.getAllCategories() == null || CategoryManager.getAllCategories().size() == 0))
            for (CategoryEntity categoryEntity : CategoryManager.getAllCategories()
                    ) {
                TaskAlert taskAlert = new TaskAlert();
                taskAlert.setName(categoryEntity.getName());
                taskAlert.setSelected(true);
                taskAlerts.add(taskAlert);
            }


    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
        }
    }
}
