package vn.tonish.hozo.activity.payment;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CustomWebView;

/**
 * Created by LongBui on 11/16/17.
 */

public class DepositActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DepositActivity.class.getSimpleName();
    private CustomWebView customWebView;
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.deposit_activity;
    }

    @Override
    protected void initView() {
        customWebView = (CustomWebView) findViewById(R.id.custom_web_view);

        customWebView.setWebviewListener(new CustomWebView.WebviewListener() {
            @Override
            public void onSuccess() {
                Utils.showLongToast(DepositActivity.this, getString(R.string.recharge_success), false, false);
                finish();
            }

            @Override
            public void onFail() {

            }
        });

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        customWebView.loadUrl(getIntent().getStringExtra(Constants.URL_EXTRA));
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
