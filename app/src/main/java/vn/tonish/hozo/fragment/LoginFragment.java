package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BlockActivity;
import vn.tonish.hozo.activity.GeneralInfoActivity;
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

/**
 * Created by CanTran on 4/11/17.
 */


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final String COUNTRY_CODE = "+84";
    private EdittextHozo edtPhone;
    private TextViewHozo tvContinue;
    private TextViewHozo tvPolicy;
    private ImageView imgClear;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        edtPhone = (EdittextHozo) findViewById(R.id.edt_phone);
        tvContinue = (TextViewHozo) findViewById(R.id.tv_continue);
        TextViewHozo tvHotLine = (TextViewHozo) findViewById(R.id.tv_hotline);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgClear.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
        tvHotLine.setOnClickListener(this);
        tvPolicy = (TextViewHozo) findViewById(R.id.tv_policy);
        tvContinue.setAlpha(0.5f);
        tvContinue.setEnabled(false);
        hideSoftKeyboard(getActivity(), edtPhone);
    }

    @Override
    protected void initData() {
        setUnderLinePolicy(tvPolicy);
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtPhone.length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);

                } else {
                    imgClear.setVisibility(View.GONE);
                }
                showbtnContinue();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    protected void resumeData() {
        LogUtils.d(TAG, "resum");
        showbtnContinue();
    }

        private void showbtnContinue() {
            String mb = edtPhone.getText().toString().trim();
            if (!mb.isEmpty() && isNumberValid("84", mb) == true) {
                tvContinue.setAlpha(1f);
                tvContinue.setEnabled(true);
                hideSoftKeyboard(getActivity(), edtPhone);
            } else {
                tvContinue.setAlpha(0.5f);
                tvContinue.setEnabled(false);

        }
    }

    private void setUnderLinePolicy(TextViewHozo textViewHozo) {
        String text = getContext().getString(R.string.tv_login_policy);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

        ClickableSpan conditionClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openGeneralInfoActivity(getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
            }
        };
        ClickableSpan nadClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openGeneralInfoActivity(getString(R.string.other_nad), "http://hozo.vn/chinh-sach-bao-mat/?ref=app");
            }
        };

        ssBuilder.setSpan(
                conditionClickableSpan, // Span to add
                text.indexOf(getContext().getString(R.string.login_policy_condition)), // Start of the span (inclusive)
                text.indexOf(getContext().getString(R.string.login_policy_condition)) + String.valueOf(getContext().getString(R.string.login_policy_condition)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#ffffff")), // Span to add
                text.indexOf(getContext().getString(R.string.login_policy_condition)), // Start of the span (inclusive)
                text.indexOf(getContext().getString(R.string.login_policy_condition)) + String.valueOf(getContext().getString(R.string.login_policy_condition)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                nadClickableSpan,
                text.indexOf(getContext().getString(R.string.login_policy_nad)),
                text.indexOf(getContext().getString(R.string.login_policy_nad)) + String.valueOf(getContext().getString(R.string.login_policy_nad)).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#ffffff")),
                text.indexOf(getContext().getString(R.string.login_policy_nad)),
                text.indexOf(getContext().getString(R.string.login_policy_nad)) + String.valueOf(getContext().getString(R.string.login_policy_nad)).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        textViewHozo.setText(ssBuilder);
        textViewHozo.setMovementMethod(LinkMovementMethod.getInstance());
        textViewHozo.setHighlightColor(Color.TRANSPARENT);
    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(getActivity(), GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_continue:
                login();
                break;
            case R.id.tv_hotline:
                callHotLine();
                break;
            case R.id.img_clear:
                edtPhone.setText("");
                break;
        }

    }

    private void callHotLine() {
        Utils.call(getContext(), getString(R.string.hot_line));
    }

    private void login() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        String mobile = edtPhone.getText().toString().trim();
        LogUtils.d(TAG, "mobile" + mobile);
        if (mobile.substring(0, 1).equals("0")) {
            mobile = COUNTRY_CODE + mobile.substring(1);
        } else {
            mobile = COUNTRY_CODE + mobile;
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
                    showbtnContinue();

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
                showbtnContinue();

            }
        });
    }


    public static boolean isNumberValid(String countryCode, String phNumber) {
        if (TextUtils.isEmpty(countryCode)) {// Country code could not be empty
            return false;
        }
        if (phNumber.length() < 6) {        // Phone number should be at least 6 digits
            return false;
        }
        boolean resultPattern = Patterns.PHONE.matcher(phNumber).matches();
        if (!resultPattern) {
            return false;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (Exception e) {
            return false;
        }

        return phoneNumberUtil.isValidNumber(phoneNumber);
    }

}
