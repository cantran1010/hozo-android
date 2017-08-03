package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.AssignerCallAdapter;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;

public class AssignersActivity extends BaseActivity {
    private final static String TAG = AssignersActivity.class.getSimpleName();
    private RecyclerView rcvAssign;
    private ImageView imgBack;
    private TaskResponse taskResponse;
    private ArrayList<Assigner> assigners;
    private String assignType;
    private AssignerCallAdapter assignerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_assigners;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        assignerAdapter = new AssignerCallAdapter(assigners, assignType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskResponse.getId());
        rcvAssign.setAdapter(assignerAdapter);
    }

    @Override
    protected void resumeData() {

    }

}
