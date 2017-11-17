package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.text.TextUtils;
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

/**
 * Created by LongBui on 10/10/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeActivity.class.getSimpleName();
    private ImageView imgBack;
    private LinearLayout promotionLayout, atmLayout, visaLayout, inputVisaLayout, inputAtmLayout;
    private EdittextHozo edtVisa, edtAtm;
    private ButtonHozo btnVisa, btnAtm;
    private int MIN_MONEY = 10000;

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

        visaLayout = (LinearLayout) findViewById(R.id.visa_layout);
        visaLayout.setOnClickListener(this);

        inputVisaLayout = (LinearLayout) findViewById(R.id.input_visa_layout);
        inputAtmLayout = (LinearLayout) findViewById(R.id.input_atm_layout);

        edtVisa = (EdittextHozo) findViewById(R.id.edt_visa);
        edtAtm = (EdittextHozo) findViewById(R.id.edt_atm);

        btnVisa = (ButtonHozo) findViewById(R.id.btn_visa);
        btnVisa.setOnClickListener(this);

        btnAtm = (ButtonHozo) findViewById(R.id.btn_atm);
        btnAtm.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        edtAtm.addTextChangedListener(new NumberTextWatcher(edtAtm));
        edtVisa.addTextChangedListener(new NumberTextWatcher(edtVisa));

//        edtAtm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                edtAtm.setText(Utils.formatNumber(Long.valueOf(getLongEdittext(edtAtm))));
//            }
//        });
//
//        edtVisa.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                edtVisa.setText(Utils.formatNumber(Long.valueOf(getLongEdittext(edtAtm))));
//            }
//        });

    }

    @Override
    protected void resumeData() {

    }

    private String getLongEdittext(EdittextHozo edittextHozo) {
        return edittextHozo.getText().toString().replace(",", "").replace(".", "");
    }

    private void doAtm() {

        if (TextUtils.isEmpty(edtAtm.getText().toString()) || Integer.valueOf(getLongEdittext(edtAtm)) < MIN_MONEY) {
            Utils.showLongToast(this, getString(R.string.min_recharge_error, Utils.formatNumber(MIN_MONEY)), true, false);
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
                    Intent intent = new Intent(RechargeActivity.this, DepositActivity.class);
                    intent.putExtra(Constants.URL_EXTRA, response.body().getPaymentUrl());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
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
        if (TextUtils.isEmpty(edtVisa.getText().toString()) || Integer.valueOf(getLongEdittext(edtVisa)) < MIN_MONEY) {
            Utils.showLongToast(this, getString(R.string.min_recharge_error, Utils.formatNumber(MIN_MONEY)), true, false);
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("amount", Integer.valueOf(getLongEdittext(edtVisa)));
            jsonRequest.put("provider", "1pay");
            jsonRequest.put("method", "visa");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doVisa data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().deposit(UserManager.getUserToken(), body).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {

                LogUtils.d(TAG, "doVisa onResponse : " + response.body());
                LogUtils.d(TAG, "doVisa code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Intent intent = new Intent(RechargeActivity.this, DepositActivity.class);
                    intent.putExtra(Constants.URL_EXTRA, response.body().getPaymentUrl());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
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

    private void doExpandVisa() {
        if (inputVisaLayout.getVisibility() == View.VISIBLE)
            inputVisaLayout.setVisibility(View.GONE);
        else
            inputVisaLayout.setVisibility(View.VISIBLE);
    }

    private void doExpandAtm() {
        if (inputAtmLayout.getVisibility() == View.VISIBLE)
            inputAtmLayout.setVisibility(View.GONE);
        else
            inputAtmLayout.setVisibility(View.VISIBLE);
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
                doExpandAtm();
                break;

            case R.id.visa_layout:
                doExpandVisa();
                break;

            case R.id.btn_atm:
                doAtm();
                break;

            case R.id.btn_visa:
                doVisa();
                break;
        }
    }

}
