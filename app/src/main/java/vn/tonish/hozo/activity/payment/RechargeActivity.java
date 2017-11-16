package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.DepositResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;
    private LinearLayout promotionLayout, atmLayout;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        promotionLayout = (LinearLayout) findViewById(R.id.promotion_layout);
        promotionLayout.setOnClickListener(this);

        atmLayout = (LinearLayout) findViewById(R.id.atm_layout);
        atmLayout.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    private void doDeposit() {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("amount", 40000);
            jsonRequest.put("provider", "1pay");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doDeposit data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().deposit(UserManager.getUserToken(), body).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {

                LogUtils.d(TAG, "doDeposit onResponse : " + response.body());
                LogUtils.d(TAG, "doDeposit code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Intent intent = new Intent(RechargeActivity.this, DepositActivity.class);
                    intent.putExtra(Constants.URL_EXTRA, response.body().getPaymentUrl());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                } else {
                    DialogUtils.showRetryDialog(RechargeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doDeposit();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(RechargeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doDeposit();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.promotion_layout:
                startActivity(PromotionCodeActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.atm_layout:
                doDeposit();
                break;


        }
    }

}
