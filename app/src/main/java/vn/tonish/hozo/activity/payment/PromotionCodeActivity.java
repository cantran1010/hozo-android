package vn.tonish.hozo.activity.payment;

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
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.PromotionResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 10/30/17.
 */

public class PromotionCodeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PromotionCodeActivity.class.getSimpleName();
    private ButtonHozo btnPromotion;
    private LinearLayout progressLayout;
    private EdittextHozo edtCode;

    @Override
    protected int getLayout() {
        return R.layout.promotion_code_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnPromotion = (ButtonHozo) findViewById(R.id.btn_promotion);
        btnPromotion.setOnClickListener(this);

        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

        edtCode = (EdittextHozo) findViewById(R.id.edt_code);

        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edtCode.setError(null);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    private void doPromotionCode() {

        if (TextUtils.isEmpty(edtCode.getText().toString().trim())) {
            edtCode.requestFocus();
            edtCode.setError(getString(R.string.promotion_error_nil));
            return;
        }

        progressLayout.setVisibility(View.VISIBLE);
        btnPromotion.setEnabled(false);

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("code", edtCode.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doPromotionCode data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        Call<PromotionResponse> call = ApiClient.getApiService().promotionCode(UserManager.getUserToken(), body);
        call.enqueue(new Callback<PromotionResponse>() {
            @Override
            public void onResponse(Call<PromotionResponse> call, Response<PromotionResponse> response) {
                LogUtils.d(TAG, "doPromotionCode code : " + response.code());
                LogUtils.d(TAG, "doPromotionCode body : " + response.body());

                progressLayout.setVisibility(View.GONE);
                btnPromotion.setEnabled(true);

                if (response.code() == Constants.HTTP_CODE_OK) {
                    LogUtils.d(TAG, "doPromotionCode success");
                    DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_success), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            setResult(Constants.PROMOTION_RESULT_CODE);
                            finish();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "doPromotionCode errorBody" + error.toString());
                    if (error.status().equals(Constants.PROMOTION_ERROR_EMPTY)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_empty), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.PROMOTION_ERROR_NO)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_no), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.PROMOTION_ERROR_NOT_STARTED)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_not_started), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.PROMOTION_ERROR_EXPRIED)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_expried), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.PROMOTION_ERROR_LIMITED)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_limmited), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.PROMOTION_ERROR_USED)) {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_used), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else {
                        DialogUtils.showOkDialog(PromotionCodeActivity.this, getString(R.string.app_name), getString(R.string.promotion_error_no), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<PromotionResponse> call, Throwable t) {

                progressLayout.setVisibility(View.GONE);
                btnPromotion.setEnabled(true);

                LogUtils.e(TAG, "doPromotionCode , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(PromotionCodeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doPromotionCode();
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

            case R.id.progress_layout:

                break;

            case R.id.btn_promotion:
                doPromotionCode();
                break;

        }
    }
}
