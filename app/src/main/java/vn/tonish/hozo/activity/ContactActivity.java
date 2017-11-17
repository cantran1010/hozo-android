package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.AssignerCallAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;

/**
 * Created by LongBui on 11/17/17.
 */

public class ContactActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ContactActivity.class.getSimpleName();
    private RecyclerView rcvAssign;
    private AssignerCallAdapter assignerAdapter;
    private ArrayList<Assigner> assigners;
    private TaskResponse taskResponse;

    @Override
    protected int getLayout() {
        return R.layout.contact_activity;
    }

    @Override
    protected void initView() {
        rcvAssign = (RecyclerView) findViewById(R.id.lvList);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        setupList();
    }

    @Override
    protected void resumeData() {

    }

    private void setupList() {
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        assignerAdapter = new AssignerCallAdapter(assigners, "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskResponse.getId());
        rcvAssign.setAdapter(assignerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

        }
    }
}