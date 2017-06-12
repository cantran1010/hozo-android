package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.other.GeneralInfoActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 5/10/17.
 */

public class VerifyNameFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = VerifyNameFragment.class.getName();
    private EdittextHozo edtName;
    private TextViewHozo btnSave, tvPolicy;


    @Override
    protected int getLayout() {
        return R.layout.verify_name_fragment;
    }

    @Override
    protected void initView() {
        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        btnSave = (TextViewHozo) findViewById(R.id.btn_save);
        tvPolicy = (TextViewHozo) findViewById(R.id.tv_policy);
        btnSave.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        setUnderLinePolicy(tvPolicy);
        btnSave.setOnClickListener(this);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtName.getText().toString().trim().length() > 4 && (edtName.getText().toString().trim().length() < 50)) {
                    btnSave.setAlpha(1);
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setAlpha(0.5f);
                    btnSave.setEnabled(false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                updateFullName();
                break;
        }

    }


    private void updateFullName() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.USER_ID, UserManager.getMyUser().getId());
            jsonRequest.put(Constants.USER_MOBILE, UserManager.getMyUser().getPhone());
            jsonRequest.put(Constants.USER_FULL_NAME, edtName.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "aaaa onResponse updateFullName UserManager.getUserToken() : " + UserManager.getUserToken());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse updateFullName code : " + response.code());

                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "onResponse updateFullName body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        if (response.body() != null) {
                            UserEntity myUser = UserManager.getMyUser();
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            myUser.setFullName(edtName.getText().toString());
                            realm.commitTransaction();
                        }
                        startActivityAndClearAllTask(new Intent(getContext(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateFullName();
                        }
                    });

                } else {
                    btnSave.setAlpha(0.5f);
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
//                    Toast.makeText(getContext(), error.message(), Toast.LENGTH_SHORT).show();
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
                showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateFullName();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void setUnderLinePolicy(TextViewHozo textViewHozo) {
        SpannableString ss = new SpannableString(getContext().getString(R.string.tv_login_policy));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openGeneralInfoActivity(getString(R.string.other_condition));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 61, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewHozo.setText(ss);
        textViewHozo.setMovementMethod(LinkMovementMethod.getInstance());
        textViewHozo.setHighlightColor(Color.TRANSPARENT);
    }

    private void openGeneralInfoActivity(String title) {
        Intent intent = new Intent(getActivity(), GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, "https://tonish.vn/terms.html");
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }
}
