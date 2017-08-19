package vn.tonish.hozo.fragment;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BlockActivity;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.USER_MOBILE;
import static vn.tonish.hozo.database.manager.UserManager.insertUser;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 4/11/17.
 */

public class OtpFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher, View.OnClickListener {
    private static final String TAG = OtpFragment.class.getName();

    private EdittextHozo mPinFirstDigitEditText;
    private EdittextHozo mPinSecondDigitEditText;
    private EdittextHozo mPinThirdDigitEditText;
    private EdittextHozo mPinForthDigitEditText;
    private TextViewHozo btnResetOtp;
    private EdittextHozo mPinHiddenEditText;
    private String mobile;
    private CountDownTimer countDownTimer;

    @Override
    protected int getLayout() {
        return R.layout.fragment_otp;
    }

    @Override
    protected void initView() {
        mPinFirstDigitEditText = (EdittextHozo) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EdittextHozo) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EdittextHozo) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EdittextHozo) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EdittextHozo) findViewById(R.id.pin_hidden_edittext);
        ImageView btnBack = (ImageView) findViewById(R.id.btnBack);
        btnResetOtp = (TextViewHozo) findViewById(R.id.btn_reset_otp);
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
        btnResetOtp.setOnClickListener(this);
        onFocusChange(mPinFirstDigitEditText, true);
        startTimer();
    }

    @Override
    protected void initData() {
        mobile = getArguments().getString(Constants.USER_MOBILE);
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
        } else if (s.length() == 1) {
            mPinFirstDigitEditText.setText(String.valueOf(s.charAt(0)));
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            mPinSecondDigitEditText.setText(String.valueOf(s.charAt(1)));
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            mPinThirdDigitEditText.setText(String.valueOf(s.charAt(2)));
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(String.valueOf(s.charAt(3)));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendOtp();
                }
            }, 200);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                countDownTimer.cancel();
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_reset_otp:
                resetOtp();
                break;
        }

    }


    private void resetOtp() {
        startTimer();
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(USER_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().getOtpCode("XXXX", body).enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                LogUtils.d(TAG, "onResponse body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    mPinFirstDigitEditText.setText("");
                    mPinSecondDigitEditText.setText("");
                    mPinThirdDigitEditText.setText("");
                    mPinForthDigitEditText.setText("");
                    mPinHiddenEditText.setText("");
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
                    if (error.status().equalsIgnoreCase(getActivity().getString(R.string.login_status_block)))
                        Utils.showLongToast(getActivity(), getActivity().getString(R.string.login_block_phone), true, false);
                    else
                        Utils.showLongToast(getActivity(), error.message(), true, false);
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        resetOtp();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void sendOtp() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        final String otpCode = mPinHiddenEditText.getText().toString().trim();
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(USER_MOBILE, mobile);
            jsonRequest.put(Constants.USER_OTP, otpCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "sendOtp data request : " + jsonRequest.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().senOtp(body).enqueue(new Callback<OtpReponse>() {

            @Override
            public void onResponse(Call<OtpReponse> call, Response<OtpReponse> response) {
                LogUtils.d(TAG, "onResponse code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    LogUtils.d(TAG, "onResponse body : " + response.body());
                    UserEntity user = response.body().getUser();
                    Token token = response.body().getToken();

                    user.setAccessToken(token.getAccessToken());
                    user.setRefreshToken(token.getRefreshToken());
                    user.setTokenExp(token.getTokenExpires());
                    insertUser(user, true);

                    LogUtils.d(TAG, "aaaa onResponse body : " + token.toString());

                    if (user.getFullName().isEmpty()) {
                        openFragment(R.id.layout_container, RegisterFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                    } else {
                        Utils.settingDefault(getActivity());
                        startActivityAndClearAllTask(new Intent(getActivity(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                    }
                    sendRegistrationToServer();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            sendOtp();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    Utils.showLongToast(getActivity(), getString(R.string.code_otp_is_invalid), true, false);
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
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

    private void sendRegistrationToServer() {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.UPDATE_TOKEN_DEVICE_TOKEN, FirebaseInstanceId.getInstance().getToken());
            jsonRequest.put(Constants.UPDATE_TOKEN_DEVICE_TYPE, Constants.UPDATE_TOKEN_DEVICE_TYPE_ANDROID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "sendRegistrationToServer , jsonRequest : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().updateDeviceToken(UserManager.getUserToken(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "sendRegistrationToServer , body : " + response.body());
                LogUtils.d(TAG, "sendRegistrationToServer , code : " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LogUtils.e(TAG, "sendRegistrationToServer , onFailure : " + t.getMessage());
            }
        });
    }

    private void startTimer() {
        btnResetOtp.setClickable(false);
        btnResetOtp.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
        countDownTimer = new CountDownTimer(60000, 1000) {
            long secondsLeft = 0;

            public void onTick(long ms) {
                secondsLeft = ms / 1000;
                String strOtp = getActivity().getString(R.string.login_resend_otp) + ": " + secondsLeft;
                btnResetOtp.setText(strOtp);
            }

            public void onFinish() {
                btnResetOtp.setClickable(true);
                btnResetOtp.setText(getActivity().getString(R.string.login_resend_otp));
                btnResetOtp.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        }.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        countDownTimer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}