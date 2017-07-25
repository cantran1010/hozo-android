package vn.tonish.hozo.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
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
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.isValidEmail;

public class GiveInforActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GiveInforActivity.class.getSimpleName();
    private TextViewHozo btnVerifyFaceBook, btnVerifyEmail;
    private EdittextHozo edtEmail;
    private CallbackManager callbackmanager;
    private ImageView imgBack;
    private UserEntity mUserEntity;
    private String mEmail, mFacebookID;

    @Override
    protected int getLayout() {
        return R.layout.activity_give_info;
    }

    @Override
    protected void initView() {
        btnVerifyFaceBook = findViewById(R.id.btn_verify_facebook);
        btnVerifyEmail = findViewById(R.id.btn_verify_email);
        edtEmail = findViewById(R.id.edt_verify_email);
        imgBack = findViewById(R.id.img_back);
        btnVerifyFaceBook.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnVerifyEmail.setOnClickListener(this);
        btnVerifyEmail.setAlpha(0.5f);
        btnVerifyEmail.setEnabled(false);

    }

    @Override
    protected void initData() {
        mUserEntity = UserManager.getMyUser();
        mEmail = mUserEntity.getEmail();
        LogUtils.d(TAG, "user info: " + mUserEntity.toString());
        mFacebookID = mUserEntity.getFacebookId();
        if (!(mFacebookID.isEmpty() || mFacebookID == null)) {
            btnVerifyFaceBook.setText(R.string.update_version);
        } else {
            btnVerifyFaceBook.setText(R.string.verify);
        }

        if (!(mEmail.isEmpty() || mEmail == null)) {
            btnVerifyEmail.setText(R.string.update_version);
            edtEmail.setText(mEmail);
        } else {
            btnVerifyEmail.setText(R.string.verify);
        }
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = edtEmail.getText().toString().trim();
                if (isValidEmail(email) && !email.equalsIgnoreCase(mEmail)) {
                    btnVerifyEmail.setAlpha(1f);
                    btnVerifyEmail.setEnabled(true);
                } else {
                    btnVerifyEmail.setAlpha(0.5f);
                    btnVerifyEmail.setEnabled(false);

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


    // Private method to handle Facebook login and callback
    private void onFblogin() {

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            Utils.showLongToast(this, getString(R.string.facebook_verify_done), false, true);
            return;
        }

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
                        verifyFacebook(loginResult.getAccessToken().getUserId(), null);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_facebook:
                onFblogin();
                break;
            case R.id.img_back:
                setResult(Constants.RESULT_CODE_VERIFY);
                finish();
                break;
            case R.id.btn_verify_email:
//               getOpenFacebookIntent(this, "1952073665068033");
                break;
        }

    }

    private void verifyFacebook(final String facebookId, final String email) {
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
        LogUtils.d(TAG, "verifyFacebook jsonRequest : " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                LogUtils.d(TAG, "verifyFacebook onResponse body : " + response.body());
                LogUtils.d(TAG, "verifyFacebook onResponse code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    Realm.getDefaultInstance().beginTransaction();
                    UserManager.getMyUser().setFacebookId(facebookId);
                    Realm.getDefaultInstance().commitTransaction();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(GiveInforActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            verifyFacebook(facebookId, email);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(GiveInforActivity.this);
                } else {
                    DialogUtils.showRetryDialog(GiveInforActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            verifyFacebook(facebookId, email);
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
                LogUtils.e(TAG, "verifyFacebook onFailure error : " + t.getMessage());
                DialogUtils.showRetryDialog(GiveInforActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        verifyFacebook(facebookId, email);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

}
