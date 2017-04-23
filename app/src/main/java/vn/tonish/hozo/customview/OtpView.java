package vn.tonish.hozo.customview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.LoginScreen;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.common.Constants.NAME_VIEW;
import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;

/**
 * Created by CanTran on 18/04/2017.
 */

public class OtpView extends FrameLayout implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher, View.OnClickListener {
    private static final String TAG = "OtpView";

    private Context context;
    private View rootView;
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
    private TextView btnSigin;
    private TextView btnBack;
    private boolean registed;
    private String phone;

    public OtpView(Context context, boolean registed, String phone) {
        super(context);
        this.context = context;
        this.registed = registed;
        this.phone = phone;
        initView();
        initData();
    }

    private void initData() {
        setPINListeners();
    }


    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_otp, null);
        addView(rootView);
        init();
        btnBack = (TextView) rootView.findViewById(R.id.btnBack);
        btnSigin = (TextView) rootView.findViewById(R.id.btn_sigin);
        btnBack.setOnClickListener(this);
        btnSigin.setOnClickListener(this);

    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
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
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 3) {
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
            btnSigin.setEnabled(true);
            btnSigin.setTextColor(getResources().getColor(R.color.black));


        }
    }


    /**
     * Initialize EditText fields.
     */
    private void init() {
        mPinFirstDigitEditText = (EditText) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
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

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }


    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");
                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                ((LoginScreen) context).closeExtendView();
                break;
            case R.id.btn_sigin:
                login();
                break;
        }

    }

    private void login() {
        String otpcode = mPinHiddenEditText.getText().toString().trim();
        HashMap<String, String> dataRequest = new HashMap<>();
        dataRequest.put("mobile", phone);
        dataRequest.put("otpcode", otpcode);
        NetworkUtils.postVolleyFormData(true, true, true, context, NetworkConfig.API_LOGIN, dataRequest, new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                LogUtils.d(TAG, "dataRequest" + jsonResponse.toString());
                try {
                    UserEntity userEntity = new UserEntity();
                    if (jsonResponse.getInt("code") == 0) {
                        JSONObject object = new JSONObject(getStringInJsonObj(jsonResponse, "data"));
                        JSONObject mObject = new JSONObject(getStringInJsonObj(object, "user"));
                        userEntity.setId(Integer.parseInt(getStringInJsonObj(mObject, "id")));

                        JSONObject jsonToken = new JSONObject(getStringInJsonObj(object, "token"));
                        userEntity.setToken(getStringInJsonObj(jsonToken, "access_token"));
                        userEntity.setRefreshToken(getStringInJsonObj(jsonToken, "refresh_token"));

                        userEntity.setTokenExp(getStringInJsonObj(object, "token_exp"));
                        userEntity.setFullName(getStringInJsonObj(mObject, "full_name"));
                        userEntity.setPhoneNumber(getStringInJsonObj(mObject, "mobile"));
                        userEntity.setLoginAt(getStringInJsonObj(mObject, "login_at"));
                        UserManager.insertUserLogin(userEntity, getContext());
                        if ((getStringInJsonObj(mObject, "full_name").trim()).equalsIgnoreCase("") || getStringInJsonObj(mObject, "full_name").trim() == null) {
                            LogUtils.d(TAG, "name_check" + getStringInJsonObj(mObject, "full_name").trim());
                            ((LoginScreen) context).showExtendView(NAME_VIEW);
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            ((LoginScreen) context).startActivityAndClearAllTask(intent);
                        }
                    } else if (jsonResponse.getInt("code") == 1) {
                        Toast.makeText(context, "Mobile is empty", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Otp code is invalid", Toast.LENGTH_SHORT).show();
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
