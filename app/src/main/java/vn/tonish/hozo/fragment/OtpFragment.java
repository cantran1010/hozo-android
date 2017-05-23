package vn.tonish.hozo.fragment;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.common.Constants.USER_MOBILE;
import static vn.tonish.hozo.database.manager.UserManager.insertUser;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by Can Tran on 4/11/17.
 */

public class OtpFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher, View.OnClickListener {
    private static final String TAG = OtpFragment.class.getName();

    private EdittextHozo mPinFirstDigitEditText;
    private EdittextHozo mPinSecondDigitEditText;
    private EdittextHozo mPinThirdDigitEditText;
    private EdittextHozo mPinForthDigitEditText;
    private EdittextHozo mPinHiddenEditText;
    private TextView btnSigIn;
    private ImageView btnBack;
    private TextView btnResetOtp;
    private String mobile;

    @Override
    protected int getLayout() {
        return R.layout.otp_fragment;
    }

    @Override
    protected void initView() {
        mPinFirstDigitEditText = (EdittextHozo) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EdittextHozo) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EdittextHozo) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EdittextHozo) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EdittextHozo) findViewById(R.id.pin_hidden_edittext);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnSigIn = (TextView) findViewById(R.id.btn_sigin);
        btnResetOtp = (TextView) findViewById(R.id.btn_reset_otp);
        mPinHiddenEditText.addTextChangedListener(this);
        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);


        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
        btnBack.setOnClickListener(this);
        btnSigIn.setOnClickListener(this);
        btnResetOtp.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        mobile = getArguments().getString(USER_MOBILE);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            mPinFirstDigitEditText.setText("");
            btnSigIn.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (s.length() == 1) {
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            btnSigIn.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (s.length() == 2) {
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            btnSigIn.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (s.length() == 3) {
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
            btnSigIn.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendOtp();
                }
            }, 200);
        } else {
            btnSigIn.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            btnSigIn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;


            default:
                break;
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private static void setFocus(EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_sigin:
                sendOtp();
                break;
            case R.id.btn_reset_otp:
                resetOtp();
                break;
        }

    }

    private void resetOtp() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(USER_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().getOtpCode("XXXX", body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                LogUtils.d(TAG, "onResponse body : " + response.body());
                mPinFirstDigitEditText.setText("");
                mPinSecondDigitEditText.setText("");
                mPinThirdDigitEditText.setText("");
                mPinForthDigitEditText.setText("");
                mPinHiddenEditText.setText("");
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void sendOtp() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        String otpCode = mPinHiddenEditText.getText().toString().trim();
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(USER_MOBILE, mobile);
            jsonRequest.put(Constants.USER_OTP, otpCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().senOtp(body).enqueue(new Callback<OtpReponse>() {

            @Override
            public void onResponse(Call<OtpReponse> call, Response<OtpReponse> response) {
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                LogUtils.d(TAG, "onResponse body : " + response.body().toString());
                UserEntity user = response.body().getUser();
                Token token = response.body().getToken();

                user.setAccessToken(token.getAccessToken());
                user.setRefreshToken(token.getRefreshToken());
                user.setTokenExp(token.getTokenExpires());
                // inser token
                insertUser(user, true);
                // create account or login
                if (response.code() == 201) {
                    openFragment(R.id.layout_container, VerifyNameFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                } else {
                    startActivityAndClearAllTask(new Intent(getActivity(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<OtpReponse> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
                showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        sendOtp();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }


}