package vn.tonish.hozo.activity;

import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 7/29/17.
 */

public class BlockTaskActivity extends BaseActivity implements View.OnClickListener {

    private TextViewHozo tvTitle, tvContent, tvYes;

    @Override
    protected int getLayout() {
        return R.layout.block_task_dialog;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvYes = findViewById(R.id.tv_yes);
        tvYes.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText(getIntent().getStringExtra(Constants.TITLE_INFO_EXTRA));
        tvContent.setText(getIntent().getStringExtra(Constants.CONTENT_INFO_EXTRA));
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_yes:
                finish();
                break;

        }
    }
}
