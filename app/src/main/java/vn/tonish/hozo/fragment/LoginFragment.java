package vn.tonish.hozo.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.common.Constants.USER_MOBILE;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by Can Tran on 4/11/17.
 */


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private EditText edtPhone;
    private TextView tvContinue;

    @Override
    protected int getLayout() {
        return R.layout.login_fragment;
    }

    @Override
    protected void initView() {
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tvContinue = (TextView) findViewById(R.id.tv_continue);
        tvContinue.setOnClickListener(this);
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
                        hideSoftKeyboard(getActivity(), (EdittextHozo) edtPhone);
                    }
                } else {
                    tvContinue.setAlpha(1f);
                    tvContinue.setEnabled(true);
                    hideSoftKeyboard(getActivity(), (EdittextHozo) edtPhone);
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
        }

    }

    private boolean checkNumberPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        } else if (number.length() == 10 && number.substring(0, 2).equals("09")) {
            return true;
        } else
            return number.length() == 10 && number.substring(0, 1).equals("1") || number.length() == 11 && number.substring(0, 2).equals("01") || number.length() == 9 && number.substring(0, 1).equals("9");
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
        ApiClient.getApiService().getOtpCode("XXXX", body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                if (response.isSuccessful() && response.code() == 204) {
                    LogUtils.d(TAG, "onResponse body : " + response.body());
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.USER_MOBILE, finalMobile);
                    openFragment(R.id.layout_container, OtpFragment.class, bundle, true, TransitionScreen.RIGHT_TO_LEFT);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            login();
                        }
                    });

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Toast.makeText(getContext(), error.message(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Void> call, Throwable t) {
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
