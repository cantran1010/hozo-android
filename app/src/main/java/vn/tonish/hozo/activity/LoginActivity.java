package vn.tonish.hozo.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.customview.NameView;
import vn.tonish.hozo.customview.OtpView;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;

import static vn.tonish.hozo.common.Constants.OTP_VIEW;
import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by MAC2015 on 4/11/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getName();
    private Context context;
    private EditText edtPhone;
    private TextView tvContinue;
    private FrameLayout viewLevel1, viewLevel2, viewLevel3;
    public boolean registed = false;
    public String phone = "";
    public int viewLevel = 0;
    public int duration = 200;
    public Animation mLoadAnimation, rtAnimation, lanimation;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        context = LoginActivity.this;
        mLoadAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_edittext);
        rtAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        lanimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
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
                String error;
                if (!checkNumberPhone(edtPhone.getText().toString().trim())) {
                    tvContinue.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    tvContinue.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    tvContinue.setEnabled(false);
                    if (CheckErrorEditText(edtPhone.getText().toString().trim())) {
                        error = getResources().getString(R.string.login_erro_phone);
                        edtPhone.startAnimation(mLoadAnimation);
                        edtPhone.setError(error);
                    }
                } else {
                    tvContinue.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    tvContinue.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    tvContinue.setEnabled(true);
                    hideSoftKeyboard(context, edtPhone);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void resumeData() {
        if (checkNumberPhone(edtPhone.getText().toString().trim()))
            tvContinue.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        tvContinue.setEnabled(true);
    }

    public OtpView getOtpView() {
        return new OtpView(context, registed, phone);
    }

    public NameView getNameView() {
        return new NameView(context);
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
        View view;
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

        } catch (Exception ignored) {

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
            view.startAnimation(rtAnimation);
        } else {
            view.startAnimation(lanimation);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, duration);
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

    public boolean CheckErrorEditText(String number) {
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

    public void login() {
        phone = edtPhone.getText().toString().trim();
        HashMap<String, String> dataRequest = new HashMap<>();
        dataRequest.put("mobile", phone);
        NetworkUtils.postVolleyFormData(true, true, true, context, NetworkConfig.API_OTP, dataRequest, new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                try {
                    if (jsonResponse.getInt("code") == 0) {
                        JSONObject jData = jsonResponse.getJSONObject("data");
                        if (getStringInJsonObj(jData, "registed").equals("true")) {
                            registed = true;
                        } else {
                            registed = false;
                        }
                        showExtendView(OTP_VIEW);
                    } else if (jsonResponse.getInt("code") == 1) {
                        Toast.makeText(context, "Mobile is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });


    }
}
