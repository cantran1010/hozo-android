package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;

/**
 * Created by CanTran on 6/21/17.
 */

public class TaskAlertsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayout() {
        return R.layout.activity_task_alerts;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {


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
