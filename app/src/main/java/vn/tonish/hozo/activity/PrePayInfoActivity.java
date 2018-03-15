package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;

/**
 * Created by CanTran on 3/12/18.
 */

public class PrePayInfoActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayout() {
        if (getIntent().getIntExtra(Constants.PREPAY_TYPE_EXTRA, 1) == 1) {
            return R.layout.activity_prepay_poster_info;
        } else {
            return R.layout.activity_prepay_tasker_info;
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
