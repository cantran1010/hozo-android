package vn.tonish.hozo.activity.payment;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;

/**
 * Created by LongBui on 1/3/18.
 */

public class WalletInfoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayout() {

        if (getIntent().getIntExtra(Constants.WALLET_TYPE_EXTRA, 1) == 1) {
            return R.layout.wallet_info_activity;
        } else {
            return R.layout.wallet_accout_info_layout;
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
