package vn.tonish.hozo.activity.setting;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.TaskTypeAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = TaskTypeActivity.class.getSimpleName();
    private ImageView imgback;
    private RecyclerView mRecyclerView;
    private TaskTypeAdapter mAdapter;
    private ButtonHozo btnSave;
    private TextViewHozo btnReset;
    private ArrayList<Category> taskTypes;
    private Category mCategory;


    @Override
    protected int getLayout() {
        return R.layout.activity_tast_type;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_type);
        btnReset = (TextViewHozo) findViewById(R.id.tv_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskTypes = new ArrayList<>();


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCategory = (Category) intent.getExtras().get(Constants.EXTRA_CATEGORY);
        if (mCategory != null)
            taskTypes.addAll(mCategory.getCategories());
        else
            getTaskTypes();
        imgback.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        mAdapter = new TaskTypeAdapter(taskTypes);
        mRecyclerView.setAdapter(mAdapter);

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
            case R.id.tv_reset:
                clearSelected();
                break;
            case R.id.btn_save:
                saveData();
                break;
        }
    }


    private void clearSelected() {
        taskTypes.clear();
        if (mCategory != null)
            taskTypes.addAll(mCategory.getCategories());
        else
            getTaskTypes();

        mAdapter.notifyDataSetChanged();
    }

    public List<Category> getTaskTypes() {
        for (CategoryEntity categoryEntity : CategoryManager.getAllCategories()
                ) {
            Category taskType = new Category();
            taskType.setId(categoryEntity.getId());
            taskType.setName(categoryEntity.getName());
            taskType.setSelected(categoryEntity.isSelected());
            taskTypes.add(taskType);

        }
        return taskTypes;
    }


    private void saveData() {
        Intent intent = new Intent();
        Category category = new Category();
        category.setCategories((ArrayList<Category>) taskTypes);
        intent.putExtra(Constants.EXTRA_CATEGORY_ID, category);
        setResult(Constants.REQUEST_CODE_TASK_TYPE, intent);
        finish();//finishing
    }

}