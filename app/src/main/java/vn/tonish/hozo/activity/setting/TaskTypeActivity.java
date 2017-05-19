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

import static vn.tonish.hozo.common.Constants.EXTRA_CATEGORY;
import static vn.tonish.hozo.network.DataParse.convertListCategoryToListCategoryEntity;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = TaskTypeActivity.class.getSimpleName();
    private ImageView imgback;
    private RecyclerView mRecyclerView;
    private TaskTypeAdapter mAdapter;
    private ButtonHozo btnReset, btnSave;
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
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskTypes = new ArrayList<>();
        mAdapter = new TaskTypeAdapter(taskTypes);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        mCategory = new Category();
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_CATEGORY)) {
            mCategory = (Category) intent.getExtras().get(EXTRA_CATEGORY);
        }
        getTaskTypes();
        imgback.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);

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
            case R.id.btn_save:
                saveData();
                break;
        }
    }


    private void clearSelected() {
        for (int i = 0; i < taskTypes.size(); i++) {
            taskTypes.get(i).setSelected(false);
        }
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.addAll(convertListCategoryToListCategoryEntity(taskTypes));
        CategoryManager.insertCategories(this, categoryEntities);
        mAdapter.notifyDataSetChanged();
    }

    public List<Category> getTaskTypes() {
        if (mCategory == null) {
            for (int i = 0; i < CategoryManager.getAllCategories(TaskTypeActivity.this).size(); i++) {
                Category taskType = new Category();
                taskType.setId(CategoryManager.getAllCategories(TaskTypeActivity.this).get(i).getId());
                taskType.setName(CategoryManager.getAllCategories(TaskTypeActivity.this).get(i).getName());
                taskType.setSelected(CategoryManager.getAllCategories(TaskTypeActivity.this).get(i).isSelected());
                taskTypes.add(taskType);
            }
        } else {
            taskTypes.addAll(mCategory.getCategories());
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