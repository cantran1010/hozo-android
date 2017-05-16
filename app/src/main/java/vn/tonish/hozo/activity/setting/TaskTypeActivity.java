package vn.tonish.hozo.activity.setting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.TaskTypeAdapter;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback;
    private RecyclerView mRecyclerView;
    private TaskTypeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_tast_type;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_task_type);

    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);

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
        }
    }
}