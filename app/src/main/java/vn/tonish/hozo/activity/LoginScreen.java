package vn.tonish.hozo.activity;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.customview.NameView;
import vn.tonish.hozo.customview.OtpView;

import static android.support.v4.view.ViewCompat.animate;
import static vn.tonish.hozo.common.Constants.OTP_VIEW;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by MAC2015 on 4/11/17.
 */

public class LoginScreen extends BaseActivity implements View.OnClickListener {
    private Context context;
    private EditText edtPhone;
    private TextView tvContinue;
    private FrameLayout viewLevel1, viewLevel2, viewLevel3;
    public NameView nameView;
    public OtpView otpView;
    public int viewLevel = 0;
    public int duration = 200;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        context = LoginScreen.this;
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tvContinue = (TextView) findViewById(R.id.tv_continue);
        viewLevel2 = (FrameLayout) findViewById(R.id.view_Level2);
        viewLevel3 = (FrameLayout) findViewById(R.id.view_Level3);
        viewLevel1 = (FrameLayout) findViewById(R.id.view_level1);
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
                if (!checkNumberPhone(edtPhone.getText().toString().trim())) {
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

    public OtpView getOtpView() {
        if (otpView == null) {
            otpView = new OtpView(context);
        }
        return otpView;
    }

    public NameView getNameView() {
        if (nameView == null) {
            nameView = new NameView(context);
        }
        return nameView;
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

    public void showExtendView(String TAG_VIEW) {
        hideKeyBoard(this);
        View view = null;
        if (TAG_VIEW.equalsIgnoreCase(OTP_VIEW)) {
            view = getOtpView();
        } else {
            view = getNameView();
        }
        try {
            viewLevel++;
            if (viewLevel == 1) {
                showViewFromRight(context, viewLevel1, duration, false);
                viewLevel2.removeAllViews();
                viewLevel2.addView(view);
                showViewFromRight(context, viewLevel2, duration, true);
            } else if (viewLevel == 2) {
                showViewFromRight(context, viewLevel2, duration, false);
                viewLevel3.removeAllViews();
                viewLevel3.addView(view);
                showViewFromRight(context, viewLevel3, duration, true);
            }

        } catch (Exception e) {

        }
    }


    public void closeExtendView() {

        hideKeyBoard(this);
        if (viewLevel == 0) {
            return;
        }
        if (viewLevel == 1) {
            showViewFromRight(context, viewLevel2, duration, false);
            showViewFromRight(context, viewLevel1, duration, true);
            tvContinue.setEnabled(true);
        } else if (viewLevel == 2) {
            showViewFromRight(context, viewLevel3, duration, false);
            showViewFromRight(context, viewLevel2, duration, true);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewLevel == 1) {
                    viewLevel2.removeAllViews();
                } else if (viewLevel == 2) {
                    viewLevel3.removeAllViews();
                }
                viewLevel--;
            }
        }, duration);
    }

    public void showViewFromRight(Context context, final View view, int duration, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
            animate(view).setDuration(duration).x(0);
        } else {
            animate(view).setDuration(duration).x(view.getWidth());
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    public boolean checkNumberPhone(String number) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return false;
        } else if (number.length() == 10 && number.substring(0, 2).equals("09")) {
            return true;
        } else if (number.length() == 10 && number.substring(0, 1).equals("1")) {
            return true;
        } else if (number.length() == 11 && number.substring(0, 2).equals("01")) {
            return true;
        } else if (number.length() == 9 && number.substring(0, 1).equals("9")) {
            return true;
        } else {
            return false;
        }
    }
    private void login() {
        showExtendView(OTP_VIEW);

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
