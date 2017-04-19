package vn.tonish.hozo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.customview.NameView;
import vn.tonish.hozo.customview.OtpView;

import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by MAC2015 on 4/11/17.
 */

public class LoginScreen extends BaseActivity implements View.OnClickListener {
    private Context context;
    private EditText edtPhone;
    private TextView tvContinue;
    private FrameLayout frLogin, FrName;
    public NameView nameView;
    public OtpView otpView;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        context = LoginScreen.this;
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tvContinue = (TextView) findViewById(R.id.tv_continue);
        frLogin = (FrameLayout) findViewById(R.id.fr_login);
        FrName = (FrameLayout) findViewById(R.id.fr_otp);
        tvContinue.setOnClickListener(this);
        hideKeyBoard(this);

    }

    @Override
    protected void initData() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtPhone.getText().toString().trim().length() < 9 || edtPhone.getText().toString().trim().length() > 11 || !edtPhone.getText().toString().trim().substring(0, 1).equals("0")) {
                    tvContinue.setTextColor(getResources().getColor(R.color.white));
                    tvContinue.setEnabled(false);
                } else {
                    tvContinue.setTextColor(getResources().getColor(R.color.black));
                    tvContinue.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void resumeData() {

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

    public void toOtpView(String phone) {
        showViewFromRight(context, frLogin, 500, false);
        frLogin.setVisibility(View.GONE);
        if (otpView == null) {
            otpView = new OtpView(context, phone);
        }
        FrName.removeAllViews();
        FrName.addView(otpView);
        showViewFromRight(context, FrName, 1000, true);
    }

    public void showViewFromRight(Context context, final View view, int duration, boolean isShow) {
        if (isShow) {
            // Prepare the View for the animation
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.0f);

// Start the animation
            view.animate()
                    .translationY(view.getHeight())
                    .alpha(1.0f);
//            view.setVisibility(View.VISIBLE);
//            view.animate()
//                    .translationXBy(120)
//                    .translationX(0)
//                    .setDuration(duration);
//            animate(view).setDuration(duration).x(0);
        } else {
            view.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                        }
                    });
//            view.animate()
//                    .translationXBy(0)
//                    .translationX(120)
//                    .setDuration(duration);
////            animate(view).setDuration(duration).x(getScreenWidth());
//            new Handler().post(new Runnable() {
//
//                @Override
//                public void run() {
//                    view.setVisibility(View.GONE);
//                }
//            });
        }
    }


    private void login() {
        toOtpView(edtPhone.getText().toString().trim());

//        JSONObject jsonRequest = new JSONObject();
//        try {
//            jsonRequest.put("mobile", edtPhone.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        NetworkUtils.postVolley(true, true, true, this, NetworkConfig.API_OTP, jsonRequest, new NetworkUtils.NetworkListener() {
//            @Override
//            public void onSuccess(JSONObject jsonResponse) {
//                //
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });


    }
}
