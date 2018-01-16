package vn.tonish.hozo.activity.task_detail;

import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 7/29/17.
 */

public class BlockTaskActivity extends BaseActivity implements View.OnClickListener {

    private TextViewHozo tvTitle;
    private TextViewHozo tvContent;

    @Override
    protected int getLayout() {
        return R.layout.block_task_dialog;
    }

    @Override
    protected void initView() {
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvContent = (TextViewHozo) findViewById(R.id.tv_content);
        TextViewHozo tvYes = (TextViewHozo) findViewById(R.id.tv_yes);
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
