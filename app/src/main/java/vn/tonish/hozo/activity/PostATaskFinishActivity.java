package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import vn.tonish.hozo.R;

/**
 * Created by ADMIN on 4/18/2017.
 */

public class PostATaskFinishActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layoutBack;
    private Button btnDone;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_finish_activity;
    }

    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);
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

            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_done:

                break;

        }
    }
}
