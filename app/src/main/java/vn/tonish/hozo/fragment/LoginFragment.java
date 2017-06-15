package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BlockActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.USER_MOBILE;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;
import static vn.tonish.hozo.utils.Utils.showSoftKeyboard;

/**
 * Created by CanTran on 4/11/17.
 */


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private EdittextHozo edtPhone;
    private TextViewHozo tvContinue, tvHotLine;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        edtPhone = (EdittextHozo) findViewById(R.id.edt_phone);
        tvContinue = (TextViewHozo) findViewById(R.id.tv_continue);
        tvHotLine = (TextViewHozo) findViewById(R.id.tv_hotline);
        tvContinue.setOnClickListener(this);
        tvHotLine.setOnClickListener(this);
        showSoftKeyboard(getContext(), edtPhone);
    }

    @Override
    protected void initData() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String error;
                if (!checkNumberPhone(edtPhone.getText().toString().trim())) {
                    tvContinue.setAlpha(0.5f);
                    tvContinue.setEnabled(false);
                    if (CheckErrorEditText(edtPhone.getText().toString().trim())) {
                        error = getResources().getString(R.string.login_erro_phone);
                        edtPhone.setError(error);
                        hideSoftKeyboard(getActivity(), edtPhone);
                    }
                } else {
                    tvContinue.setAlpha(1f);
                    tvContinue.setEnabled(true);
                    hideSoftKeyboard(getActivity(), edtPhone);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    protected void resumeData() {
        LogUtils.d(TAG, "resum");
        if (checkNumberPhone(edtPhone.getText().toString().trim())) {
            tvContinue.setAlpha(1f);
            tvContinue.setEnabled(true);
        } else {
            tvContinue.setAlpha(0.5f);
            tvContinue.setEnabled(false);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_continue:
                login();
                tvContinue.setEnabled(false);
                break;
            case R.id.tv_hotline:
                callHotLine();
                break;
        }

    }

    private void callHotLine() {
        Utils.call(getContext(), getString(R.string.hot_line));
    }

    private boolean checkNumberPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches() && (number.length() == 10 && number.substring(0, 2).equals("09") || number.length() == 10 && number.substring(0, 1).equals("1") || number.length() == 11 && number.substring(0, 2).equals("01") || number.length() == 9 && number.substring(0, 1).equals("9"));
    }

    private boolean CheckErrorEditText(String number) {
        boolean ck = false;
        if (number.length() == 1 && !(number.substring(0, 1).equals("9") || number.substring(0, 1).equals("1") || number.substring(0, 1).equals("0"))) {
            ck = true;
        }
        if (number.length() == 2 && number.substring(0, 1).equals("0")) {
            if (!(number.substring(0, 2).equals("09") || number.substring(0, 2).equals("01"))) {
                ck = true;
            }
        }
        if (number.length() > 10 && (number.substring(0, 2).equals("09") || number.substring(0, 1).equals("1"))) {
            ck = true;
        }
        if (number.length() > 11 && (number.substring(0, 2).equals("01"))) {
            ck = true;
        }
        if (number.length() > 9 && (number.substring(0, 2).equals("9"))) {
            ck = true;
        }
        return ck;
    }

    private void login() {

        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        String mobile = edtPhone.getText().toString().trim();
        LogUtils.d(TAG, "mobile" + mobile);
        if (mobile.substring(0, 1).equals("0")) {
            mobile = "+84" + mobile.substring(1);
        } else {
            mobile = "+84" + mobile;
        }
        LogUtils.d(TAG, "mobile" + mobile);
        try {
            jsonRequest.put(USER_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        final String finalMobile = mobile;
        ApiClient.getApiService().getOtpCode("XXXX", body).enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    LogUtils.d(TAG, "onResponse body : " + response.body());
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.USER_MOBILE, finalMobile);
                    openFragment(R.id.layout_container, OtpFragment.class, bundle, true, TransitionScreen.RIGHT_TO_LEFT);
                } else if (response.code() == Constants.HTTP_CODE_OK) {

                    BlockResponse blockResponse = response.body();
                    if (blockResponse.getIsBlock()) {
                        Intent intent = new Intent(getActivity(), BlockActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.BLOCK_EXTRA, blockResponse);
                        startActivity(intent, TransitionScreen.FADE_IN);
                    }


                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            login();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        login();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }

}
