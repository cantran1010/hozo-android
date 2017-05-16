package vn.tonish.hozo.activity.setting;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.TaskTypeAdapter;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.model.TaskType;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback;
    private RecyclerView mRecyclerView;
    private TaskTypeAdapter mAdapter;
    private ButtonHozo btnReset;
    private List<TaskType> taskTypes;

    @Override
    protected int getLayout() {
        return R.layout.activity_tast_type;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_type);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskTypes = new ArrayList<>();
        getTaskTypes();
        mAdapter = new TaskTypeAdapter(taskTypes);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);
        btnReset.setOnClickListener(this);

    }

    @Override
    protected void resumeData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_reset:
                clearSelected();
                break;
        }
    }

    private void clearSelected() {
        for (int i = 0; i < taskTypes.size(); i++) {
            taskTypes.get(i).setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    public List<TaskType> getTaskTypes() {
        for (int i = 0; i < CategoryManager.getAllCategories(TaskTypeActivity.this).size(); i++) {
            TaskType taskType = new TaskType();
            taskType.setTaskType(CategoryManager.getAllCategories(TaskTypeActivity.this).get(i).getName());
            taskType.setSelected(false);
            taskTypes.add(taskType);
        }
        return taskTypes;
    }
}