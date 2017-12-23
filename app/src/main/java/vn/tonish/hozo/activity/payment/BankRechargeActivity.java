package vn.tonish.hozo.activity.payment;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.view.CustomWebView;

/**
 * Created by LongBui on 11/16/17.
 */

public class BankRechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BankRechargeActivity.class.getSimpleName();
    private CustomWebView customWebView;

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
                DialogUtils.showOkDialog(BankRechargeActivity.this, getString(R.string.app_name), getString(R.string.recharge_success), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        setResult(Constants.PROMOTION_RESULT_CODE);
                        finish();
                    }
                });
            }

            @Override
            public void onFail() {
//                Utils.showLongToast(BankRechargeActivity.this, getString(R.string.error_recharge), true, false);
//                finish();
            }
        });

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
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
