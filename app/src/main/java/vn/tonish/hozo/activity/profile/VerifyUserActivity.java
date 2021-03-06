package vn.tonish.hozo.activity.profile;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.VerifyCMNDActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

public class VerifyUserActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = VerifyUserActivity.class.getSimpleName();
    private TextViewHozo tvVerifyFaceBook, tvVerifyEmail, tvVerifyCMND;
    private EdittextHozo edtEmail;
    private CallbackManager callbackmanager;
    private LinearLayout layoutVerifyEmail;
    private TextViewHozo tvWaittingEmail;

    @Override
    protected int getLayout() {
        return R.layout.activity_give_info;
    }

    @Override
    protected void initView() {
        tvVerifyFaceBook = (TextViewHozo) findViewById(R.id.tv_verify_facebook);
        tvVerifyCMND = (TextViewHozo) findViewById(R.id.tv_verifyCMND);
        tvVerifyCMND.setOnClickListener(this);
        tvVerifyEmail = (TextViewHozo) findViewById(R.id.tv_verify_email);

        edtEmail = (EdittextHozo) findViewById(R.id.edt_verify_email);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        tvVerifyFaceBook.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        RelativeLayout layoutEmail = (RelativeLayout) findViewById(R.id.email_layout);
        layoutEmail.setOnClickListener(this);

        layoutVerifyEmail = (LinearLayout) findViewById(R.id.email_verify_layout);

        ButtonHozo btnSendEmail = (ButtonHozo) findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(this);

        tvWaittingEmail = (TextViewHozo) findViewById(R.id.tv_waitting_verify_email);

    }

    @Override
    protected void initData() {
        UserEntity mUserEntity = UserManager.getMyUser();
        LogUtils.d(TAG, "user info: " + mUserEntity.toString());
        String mEmail = mUserEntity.getEmail();
        String mFacebookID = mUserEntity.getFacebookId();

        if (!(mFacebookID.equalsIgnoreCase("") || mFacebookID == null)) {
            tvVerifyFaceBook.setText(R.string.verified);
            tvVerifyFaceBook.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
            tvVerifyFaceBook.setEnabled(false);
            tvVerifyFaceBook.setPadding(0, 0, 0, 0);
            Utils.setViewBackground(tvVerifyFaceBook, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
        } else {
            tvVerifyFaceBook.setText(R.string.verify);
            tvVerifyFaceBook.setTextColor(ContextCompat.getColor(this, R.color.tv_black_new));
            tvVerifyFaceBook.setEnabled(true);
            Utils.setViewBackground(tvVerifyFaceBook, ContextCompat.getDrawable(this, R.drawable.btn_green_selector_new));
        }


        if (!(mEmail.equalsIgnoreCase("") || mEmail == null) && mUserEntity.isEmailActive()) {
            tvVerifyEmail.setText(R.string.verified);
            tvVerifyEmail.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
            layoutVerifyEmail.setEnabled(false);
            tvVerifyEmail.setPadding(0, 0, 0, 0);
            Utils.setViewBackground(tvVerifyEmail, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
        } else {

            edtEmail.setText(mUserEntity.getEmail());

            if (TextUtils.isEmpty(mUserEntity.getEmail())) {
                tvWaittingEmail.setVisibility(View.GONE);
                tvVerifyEmail.setText(R.string.verify);
                tvVerifyEmail.setTextColor(ContextCompat.getColor(this, R.color.tv_black_new));
                Utils.setViewBackground(tvVerifyEmail, ContextCompat.getDrawable(this, R.drawable.btn_green_selector_new));
                layoutVerifyEmail.setEnabled(true);
            } else {
                tvWaittingEmail.setVisibility(View.VISIBLE);
                tvVerifyEmail.setText(R.string.verified_watting);
                tvVerifyEmail.setTextColor(ContextCompat.getColor(this, R.color.hozo_red));
                layoutVerifyEmail.setEnabled(true);
                tvVerifyEmail.setPadding(0, 0, 0, 0);
                Utils.setViewBackground(tvVerifyEmail, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
            }
        }

    }

    @Override
    protected void resumeData() {
        UserEntity mUserEntity = UserManager.getMyUser();
        if (mUserEntity != null) {
            if (mUserEntity.isWaitingIdentityVerify()) {
                tvVerifyEmail.setText(R.string.verified_watting);
                tvVerifyEmail.setTextColor(ContextCompat.getColor(this, R.color.hozo_red));
                layoutVerifyEmail.setEnabled(true);
                tvVerifyEmail.setPadding(0, 0, 0, 0);
                Utils.setViewBackground(tvVerifyEmail, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
            }
        }
        boolean isCMND = mUserEntity.isIdentityVerified();
        boolean iswaiting = mUserEntity.isWaitingIdentityVerify();
        if (isCMND) {
            tvVerifyCMND.setText(R.string.verified);
            tvVerifyCMND.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
            tvVerifyCMND.setEnabled(false);
            tvVerifyCMND.setPadding(0, 0, 0, 0);
            Utils.setViewBackground(tvVerifyCMND, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
        } else {
            if (iswaiting) {
                tvVerifyCMND.setText(R.string.verified_watting);
                tvVerifyCMND.setTextColor(ContextCompat.getColor(this, R.color.hozo_red));
                tvVerifyCMND.setEnabled(true);
                tvVerifyCMND.setPadding(0, 0, 0, 0);
                Utils.setViewBackground(tvVerifyEmail, ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));
            } else {
                tvVerifyCMND.setText(R.string.verify);
                tvVerifyCMND.setTextColor(ContextCompat.getColor(this, R.color.tv_black_new));
                tvVerifyCMND.setEnabled(true);
                Utils.setViewBackground(tvVerifyCMND, ContextCompat.getDrawable(this, R.drawable.btn_green_selector_new));
            }
        }

    }


    // Private method to handle Facebook login and callback
    private void onFblogin() {

//        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
////            Utils.showLongToast(this, getString(R.string.facebook_verify_done), false, true);
//            return;
//        }

        callbackmanager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        LogUtils.d(TAG, "registerCallback onsuccess , loginResult : " + loginResult.toString());
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.d(TAG, "registerCallback , accessToken : " + accessToken);
                        Log.d(TAG, "registerCallback , id : " + loginResult.getAccessToken().getUserId());
                        verifyUserInfo(loginResult.getAccessToken().getUserId(), null);
                    }

                    @Override
                    public void onCancel() {
                        LogUtils.d(TAG, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        LogUtils.d(TAG, error.toString());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void verifyUserInfo(final String facebookId, final String email) {

        if (email != null && !Utils.isValidEmail(email)) {
            Utils.showLongToast(this, getString(R.string.validate_email_error), true, false);
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        JSONObject jsonRequest = new JSONObject();
        try {
            if (facebookId != null)
                jsonRequest.put(Constants.PARAMETER_FACEBOOK_ID, facebookId);
            if (email != null)
                jsonRequest.put(Constants.PARAMETER_EMAIL, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "verifyUserInfo jsonRequest : " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                LogUtils.d(TAG, "verifyUserInfo onResponse body : " + response.body());
                LogUtils.d(TAG, "verifyUserInfo onResponse code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    Realm.getDefaultInstance().beginTransaction();
                    if (!(facebookId == null))
                        UserManager.getMyUser().setFacebookId(facebookId);
                    if (!(email == null))
                        UserManager.getMyUser().setEmail(email);
                    Realm.getDefaultInstance().commitTransaction();
                    if (!response.body().getEmail().equalsIgnoreCase("") && facebookId == null) {
                        Utils.showLongToast(VerifyUserActivity.this, getString(R.string.email_verify_done), true, true);
                    }
                    initData();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(VerifyUserActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            verifyUserInfo(facebookId, email);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(VerifyUserActivity.this);
                } else {
                    DialogUtils.showRetryDialog(VerifyUserActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            verifyUserInfo(facebookId, email);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "verifyUserInfo onFailure error : " + t.getMessage());
                DialogUtils.showRetryDialog(VerifyUserActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        verifyUserInfo(facebookId, email);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_verify_facebook:
                onFblogin();
                break;

            case R.id.tv_verifyCMND:
                if (!UserManager.getMyUser().isWaitingIdentityVerify())
                    startActivity(new Intent(this, VerifyCMNDActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_back:
                setResult(Constants.RESULT_CODE_VERIFY);
                finish();
                break;

            case R.id.btn_send_email:
                verifyUserInfo(null, edtEmail.getText().toString().trim());
                break;

            case R.id.email_layout:
                if (layoutVerifyEmail.getVisibility() == View.VISIBLE)
                    layoutVerifyEmail.setVisibility(View.GONE);
                else
                    layoutVerifyEmail.setVisibility(View.VISIBLE);
                break;

        }

    }


}
