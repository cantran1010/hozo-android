package vn.tonish.hozo.activity.payment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TransactionResponse;
import vn.tonish.hozo.rest.responseRes.WalletResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 1/11/18.
 */

public class TransferMoneyActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = TransferMoneyActivity.class.getSimpleName();
    private EdittextHozo edtMoney;
    private TextViewHozo tvBalanceAccount, tvBalanceMoney;
    private WalletResponse walletResponse;

    @Override
    protected int getLayout() {
        return R.layout.transfer_money_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        edtMoney = (EdittextHozo) findViewById(R.id.edt_money);

        tvBalanceAccount = (TextViewHozo) findViewById(R.id.tv_balance_accout);
        tvBalanceMoney = (TextViewHozo) findViewById(R.id.tv_balance_money);

        ButtonHozo btnAccept = (ButtonHozo) findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        edtMoney.addTextChangedListener(new NumberTextWatcher(edtMoney));
    }

    @Override
    protected void resumeData() {
        getWalletInfo();
    }

    private void doTransfer() {

        if (TextUtils.isEmpty(edtMoney.getText().toString())) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.money_transfer_null_error));
            return;
        }

        if (Utils.getLongEdittext(edtMoney) > walletResponse.getBalanceCash()) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.money_transfer_not_enough));
            return;
        }

        if (Utils.getLongEdittext(edtMoney) < 1000) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.money_transfer_min_error));
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("amount", Utils.getLongEdittext(edtMoney));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doTransfer data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().transfer(UserManager.getUserToken(), body).enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                LogUtils.d(TAG, "doTransfer onResponse : " + response.body());
                LogUtils.d(TAG, "doTransfer code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(TransferMoneyActivity.this, getString(R.string.transfer_success_msg), false, false);
                    setResult(Constants.PROMOTION_RESULT_CODE);
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.status().equals("invalid_data")) {
                        DialogUtils.showOkDialog(TransferMoneyActivity.this, getString(R.string.error), getString(R.string.withdraw_error_invalid_data), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("not_enough_balance")) {
                        DialogUtils.showOkDialog(TransferMoneyActivity.this, getString(R.string.error), getString(R.string.transfer_error_not_enough), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("system_error")) {
                        DialogUtils.showOkDialog(TransferMoneyActivity.this, getString(R.string.error), getString(R.string.withdraw_error_invalid_data), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(TransferMoneyActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doTransfer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void getWalletInfo() {
        ApiClient.getApiService().getWalletInfo(UserManager.getUserToken()).enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                LogUtils.d(TAG, "getWalletInfo code : " + response.code());
                LogUtils.d(TAG, "getWalletInfo response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    walletResponse = response.body();
                    tvBalanceAccount.setText(getString(R.string.unit3, Utils.formatNumber(walletResponse.getBalanceAccount())));
                    tvBalanceMoney.setText(getString(R.string.unit3, Utils.formatNumber(walletResponse.getBalanceCash())));
                } else {
                    DialogUtils.showRetryDialog(TransferMoneyActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getWalletInfo();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                LogUtils.e(TAG, "getWalletInfo onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(TransferMoneyActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getWalletInfo();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.btn_accept:
                doTransfer();
                break;

        }
    }

}
