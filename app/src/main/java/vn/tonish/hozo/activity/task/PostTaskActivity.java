package vn.tonish.hozo.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.fragment.postTask.CreateTaskFragment;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by CanTran on 12/6/17.
 */

public class PostTaskActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PostTaskActivity.class.getSimpleName();
    private Category category;
    private TaskResponse taskResponse = new TaskResponse();
    private String taskType = "";
    private boolean isCopy = false;
    public boolean isExtraTask = false;
    public boolean isEdit = false;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TaskResponse getTaskResponse() {
        return taskResponse;
    }

    public void setTaskResponse(TaskResponse taskResponse) {
        this.taskResponse = taskResponse;
    }


    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean copy) {
        isCopy = copy;
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_post_task;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, CreateTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_TASK)) {
            isExtraTask = true;
            taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
            LogUtils.d(TAG, "PostATaskActivity , taskResponse : " + taskResponse.toString());
            if (intent.hasExtra(Constants.TASK_EDIT_EXTRA)) {
                isEdit = true;
                taskType = intent.getStringExtra(Constants.TASK_EDIT_EXTRA);
            }
            category = DataParse.convertCatogoryEntityToCategory(CategoryManager.getCategoryById(taskResponse.getCategoryId()));

        } else {
            category = (Category) intent.getSerializableExtra(Constants.EXTRA_CATEGORY);
        }
        taskResponse.setCategoryId(category.getId());
    }


    @Override
    protected void resumeData() {
    }


    @Override
    public void onClick(View view) {

    }
}
