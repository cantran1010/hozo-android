package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.TaskTypeAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = TaskTypeActivity.class.getSimpleName();
    private ImageView imgback;
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
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_type);
        btnReset = (TextViewHozo) findViewById(R.id.tv_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskTypes = new ArrayList<>();
        mAdapter = new TaskTypeAdapter(taskTypes);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCategory = (Category) intent.getExtras().get(Constants.EXTRA_CATEGORY);
        if (mCategory != null)
            taskTypes.addAll(mCategory.getCategories());

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
            case R.id.tv_reset:
                clearSelected();
                break;
            case R.id.btn_save:
                saveData();
                break;
        }
    }


    private void clearSelected() {
        for (Category category : taskTypes
                ) {
            category.setSelected(true);

        }
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "checkbox :" + mCategory.getCategories());
    }

    private void saveData() {
        if (checkSelected(taskTypes)) {
            Intent intent = new Intent();
            Category category = new Category();
            category.setCategories(taskTypes);
            intent.putExtra(Constants.EXTRA_CATEGORY_ID, category);
            setResult(Constants.RESULT_CODE_TASK_TYPE, intent);
            finish();//finishing
        } else {
            Utils.showLongToast(this, getString(R.string.taks_type_empty), true, false);
        }
    }

    private boolean checkSelected(ArrayList<Category> cs) {
        for (Category c : cs
                ) {
            if (c.isSelected()) return true;

        }

        return false;
    }

}