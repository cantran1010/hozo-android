package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.DepositResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.ExpandableLayout;

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private EdittextHozo edtVisa, edtAtm;
    private final int MIN_MONEY = 10000;
    private final int MAX_MONEY = 2000000;
    private LinearLayout atmLayout;
    private LinearLayout visaLayout;
    private ExpandableLayout inputAtmExpandableLayout, inputVisaExpandableLayout;
    private LinearLayout walletLayout, promotionLayout;

    @Override
    protected int getLayout() {
        return R.layout.recharge_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        promotionLayout = (LinearLayout) findViewById(R.id.promotion_layout);
        promotionLayout.setOnClickListener(this);

        walletLayout = (LinearLayout) findViewById(R.id.wallet_layout);
        walletLayout.setOnClickListener(this);

        atmLayout = (LinearLayout) findViewById(R.id.atm_layout);
        atmLayout.setOnClickListener(this);
        visaLayout = (LinearLayout) findViewById(R.id.visa_layout);
        visaLayout.setOnClickListener(this);
        inputAtmExpandableLayout = (ExpandableLayout) findViewById(R.id.input_atm_expandableLayout);
        inputVisaExpandableLayout = (ExpandableLayout) findViewById(R.id.input_visa_expandableLayout);
        edtVisa = (EdittextHozo) findViewById(R.id.edt_visa);
        edtAtm = (EdittextHozo) findViewById(R.id.edt_atm);
        ButtonHozo btnVisa = (ButtonHozo) findViewById(R.id.btn_visa);
        btnVisa.setOnClickListener(this);
        ButtonHozo btnAtm = (ButtonHozo) findViewById(R.id.btn_atm);
        btnAtm.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        if (getIntent().getIntExtra(Constants.PAYMENT_EXTRA, 1) == 1) {
            promotionLayout.setVisibility(View.VISIBLE);
            walletLayout.setVisibility(View.VISIBLE);
            atmLayout.setVisibility(View.GONE);
            visaLayout.setVisibility(View.GONE);
        } else {
            promotionLayout.setVisibility(View.GONE);
            walletLayout.setVisibility(View.GONE);
            atmLayout.setVisibility(View.VISIBLE);
            visaLayout.setVisibility(View.VISIBLE);
        }

        edtAtm.addTextChangedListener(new NumberTextWatcher(edtAtm));
        edtVisa.addTextChangedListener(new NumberTextWatcher(edtVisa));

        edtAtm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edtAtm.setError(null);
            }
        });

        edtVisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edtVisa.setError(null);
            }
        });

    }

    @Override
    protected void resumeData() {

    }

    private String getLongEdittext(EdittextHozo edittextHozo) {
        return edittextHozo.getText().toString().replace(",", "").replace(".", "");
    }

    private void doAtm() {

        if (TextUtils.isEmpty(edtAtm.getText().toString())) {
            edtAtm.requestFocus();
            edtAtm.setError(getString(R.string.null_recharge_error));
            return;
        }

        if (Integer.valueOf(getLongEdittext(edtAtm)) < MIN_MONEY) {
            edtAtm.requestFocus();
            edtAtm.setError(getString(R.string.min_recharge_error, Utils.formatNumber(MIN_MONEY)));
            return;
        }

        if (Integer.valueOf(getLongEdittext(edtAtm)) > MAX_MONEY) {
            edtAtm.requestFocus();
            edtAtm.setError(getString(R.string.max_recharge_error, Utils.formatNumber(MAX_MONEY)));
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("amount", Integer.valueOf(getLongEdittext(edtAtm)));
            jsonRequest.put("provider", "1pay");
            jsonRequest.put("method", "localbank");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doAtm data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().deposit(UserManager.getUserToken(), body).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {

                LogUtils.d(TAG, "doAtm onResponse : " + response.body());
                LogUtils.d(TAG, "doAtm code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Intent intent = new Intent(RechargeActivity.this, BankRechargeActivity.class);
                    intent.putExtra(Constants.URL_EXTRA, response.body().getPaymentUrl());
                    startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    DialogUtils.showOkDialog(RechargeActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(RechargeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAtm();
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
                        doAtm();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void doVisa() {

        if (TextUtils.isEmpty(edtVisa.getText().toString())) {
            edtVisa.requestFocus();
            edtVisa.setError(getString(R.string.null_recharge_error));
            return;
        }

        if (Integer.valueOf(getLongEdittext(edtVisa)) < MIN_MONEY) {
            edtVisa.requestFocus();
            edtVisa.setError(getString(R.string.min_recharge_error, Utils.formatNumber(MIN_MONEY)));
            return;
        }

        if (Integer.valueOf(getLongEdittext(edtVisa)) > MAX_MONEY) {
            edtVisa.requestFocus();
            edtVisa.setError(getString(R.string.max_recharge_error, Utils.formatNumber(MAX_MONEY)));
            return;
        }

        Intent intent = new Intent(RechargeActivity.this,RechargeStripeActivity.class);
        intent.putExtra(Constants.AMOUNT_EXTRA,Integer.valueOf(getLongEdittext(edtVisa)));
        startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);

//        ProgressDialogUtils.showProgressDialog(this);
//        final JSONObject jsonRequest = new JSONObject();
//        try {
//            jsonRequest.put("amount", Integer.valueOf(getLongEdittext(edtVisa)));
//            jsonRequest.put("provider", "1pay");
//            jsonRequest.put("method", "visa");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        LogUtils.d(TAG, "doVisa data request : " + jsonRequest.toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
//
//        ApiClient.getApiService().deposit(UserManager.getUserToken(), body).enqueue(new Callback<DepositResponse>() {
//            @Override
//            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
//
//                LogUtils.d(TAG, "doVisa onResponse : " + response.body());
//                LogUtils.d(TAG, "doVisa code : " + response.code());
//
//                if (response.code() == Constants.HTTP_CODE_OK) {
//                    Intent intent = new Intent(RechargeActivity.this, BankRechargeActivity.class);
//                    intent.putExtra(Constants.URL_EXTRA, response.body().getPaymentUrl());
//                    startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
//                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
//                    APIError error = ErrorUtils.parseError(response);
//                    DialogUtils.showOkDialog(RechargeActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//
//                        }
//                    });
//                } else {
//                    DialogUtils.showRetryDialog(RechargeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//                            doAtm();
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
//                }
//                ProgressDialogUtils.dismissProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<DepositResponse> call, Throwable t) {
//                DialogUtils.showRetryDialog(RechargeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
//                    @Override
//                    public void onSubmit() {
//                        doAtm();
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//                ProgressDialogUtils.dismissProgressDialog();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PROMOTION_REQUEST_CODE && resultCode == Constants.PROMOTION_RESULT_CODE) {
            setResult(Constants.PROMOTION_RESULT_CODE);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.promotion_layout:
                Intent intent = new Intent(RechargeActivity.this, PromotionCodeActivity.class);
                startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.atm_layout:
                edtVisa.setText("");
                edtVisa.setError(null);
                Utils.setViewBackground(visaLayout, ContextCompat.getDrawable(this, R.drawable.bg_rechange));
                expandableLayout(inputAtmExpandableLayout);
                break;

            case R.id.visa_layout:
                edtAtm.setText("");
                edtAtm.setError(null);
                Utils.setViewBackground(atmLayout, ContextCompat.getDrawable(this, R.drawable.bg_rechange));
                expandableLayout(inputVisaExpandableLayout);
                break;
            case R.id.btn_atm:
                doAtm();
                break;

            case R.id.btn_visa:
                doVisa();
                break;

            case R.id.wallet_layout:
                rechargeFromWallet();
                break;
        }
    }

    private void rechargeFromWallet() {
        Intent intent = new Intent(RechargeActivity.this, TransferMoneyActivity.class);
        startActivityForResult(intent, Constants.PROMOTION_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
    }

    private void expandableLayout(ExpandableLayout expan) {
        Utils.hideKeyBoard(this);
        expan.toggle();
        if (inputAtmExpandableLayout.isExpanded() && expan != inputAtmExpandableLayout)
            inputAtmExpandableLayout.toggle();
        if (inputVisaExpandableLayout.isExpanded() && expan != inputVisaExpandableLayout)
            inputVisaExpandableLayout.toggle();
        changeBachground();
    }

    private void changeBachground() {
        if (inputAtmExpandableLayout.isExpanded())
            Utils.setViewBackground(atmLayout, ContextCompat.getDrawable(this, R.drawable.btn_selector_payment));
        else
            Utils.setViewBackground(atmLayout, ContextCompat.getDrawable(this, R.drawable.btn_unselector_payment));
        if (inputVisaExpandableLayout.isExpanded())
            Utils.setViewBackground(visaLayout, ContextCompat.getDrawable(this, R.drawable.btn_selector_payment));
        else
            Utils.setViewBackground(visaLayout, ContextCompat.getDrawable(this, R.drawable.btn_unselector_payment));
    }


}
