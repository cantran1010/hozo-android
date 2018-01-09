package vn.tonish.hozo.activity;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.TextPosition;
import com.facebook.accountkit.ui.UIManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.profile.RegisterActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.OtpReponse;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.HozoAccountKitUIManager;

import static vn.tonish.hozo.common.Constants.ACCOUNT_CODE;
import static vn.tonish.hozo.common.Constants.ACCOUNT_KIT_REQUEST_CODE;
import static vn.tonish.hozo.database.manager.UserManager.insertUser;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 5/9/17.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private int nextPermissionsRequestCode = 4000;
//    @SuppressLint("UseSparseArrays")
//    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

//    private interface OnCompleteListener {
//        void onComplete();
//    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Utils.cancelAllNotification(this);
        PreferUtils.setNewPushCount(this, 0);
        PreferUtils.setPushNewTaskCount(this, 0);
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        PreferUtils.setNewPushChatCount(this, 0);
        if (accessToken != null) {
            sendCodeAccountKit(accessToken.getToken());
        } else {
            loginHozo();
        }

    }

    @Override
    protected void resumeData() {
    }

    private void loginHozo() {
        final UIManager uiManager;
        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder
                = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN);
        //noinspection deprecation
        uiManager = new HozoAccountKitUIManager(ButtonType.NEXT, ButtonType.NEXT, TextPosition.ABOVE_BODY, LoginType.PHONE);
        configurationBuilder
                .setUIManager(uiManager)
                .setDefaultCountryCode("VN")
                .setReadPhoneStateEnabled(false)
                .setReceiveSMS(false)
                .setFacebookNotificationsEnabled(true)
                .setVoiceCallbackNotificationsEnabled(true);
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);
//        OnCompleteListener completeListener = new OnCompleteListener() {
//            @Override
//            public void onComplete() {
                startActivityForResult(intent, ACCOUNT_KIT_REQUEST_CODE);
//            }
//        };
//        if (configuration.isReceiveSMSEnabled() && !canReadSmsWithoutPermission()) {
//            final OnCompleteListener receiveSMSCompleteListener = completeListener;
//            completeListener = new OnCompleteListener() {
//                @Override
//                public void onComplete() {
//                    requestPermissions(
//                            Manifest.permission.RECEIVE_SMS,
//                            R.string.permissions_receive_sms_title,
//                            R.string.permissions_receive_sms_message,
//                            receiveSMSCompleteListener);
//                }
//            };
//        }
//        if (configuration.isReadPhoneStateEnabled() && !isGooglePlayServicesAvailable()) {
//            final OnCompleteListener readPhoneStateCompleteListener = completeListener;
//            completeListener = new OnCompleteListener() {
//                @Override
//                public void onComplete() {
//                    requestPermissions(
//                            Manifest.permission.READ_PHONE_STATE,
//                            R.string.permissions_read_phone_state_title,
//                            R.string.permissions_read_phone_state_message,
//                            readPhoneStateCompleteListener);
//                }
//            };
//        }
//
//        completeListener.onComplete();
    }


    private void sendCodeAccountKit(final String account_code) {
//        ProgressDialogUtils.showProgressDialog(this);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(ACCOUNT_CODE, account_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "sendCodeAccountKit " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().loginAccountKit(body).enqueue(new Callback<OtpReponse>() {

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
                    LogUtils.d(TAG, "sendCodeAccountKit onResponse body : " + token.toString());
                    if (user.getFullName().isEmpty()) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class), TransitionScreen.FADE_IN);
                    } else {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(Constants.TRANSITION_EXTRA, TransitionScreen.RIGHT_TO_LEFT);
                        startActivity(i, TransitionScreen.FADE_IN);
                    }
                    sendRegistrationToServer();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(LoginActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            sendCodeAccountKit(account_code);
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    Utils.showLongToast(LoginActivity.this, getString(R.string.code_otp_is_invalid), true, false);
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(LoginActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(LoginActivity.this, error.message(), true, false);
                }
//                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<OtpReponse> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
//                ProgressDialogUtils.dismissProgressDialog();
                showRetryDialog(LoginActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        sendCodeAccountKit(account_code);

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


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ACCOUNT_KIT_REQUEST_CODE) {
            return;
        }

        final String toastMessage;
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled()) {
            finish();
        } else if (loginResult.getError() != null) {
            AccountKit.logOut();
            toastMessage = loginResult.getError().getErrorType().getMessage();
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();

        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            if (accessToken != null) {
                sendCodeAccountKit(accessToken.getToken());
            } else {
                toastMessage = "Unknown response type";
                Toast.makeText(
                        this,
                        toastMessage,
                        Toast.LENGTH_LONG)
                        .show();
            }
        }


    }


//    private boolean isGooglePlayServicesAvailable() {
//        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
//        return googlePlayServicesAvailable == ConnectionResult.SUCCESS;
//    }
//
//    private boolean canReadSmsWithoutPermission() {
//        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
//        if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
//            return true;
//        }
//        //TODO we should also check for Android O here t18761104
//
//        return false;
//    }
//
//    private void requestPermissions(
//            final String permission,
//            final int rationaleTitleResourceId,
//            final int rationaleMessageResourceId,
//            final OnCompleteListener listener) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            if (listener != null) {
//                listener.onComplete();
//            }
//            return;
//        }
//
//        checkRequestPermissions(
//                permission,
//                rationaleTitleResourceId,
//                rationaleMessageResourceId,
//                listener);
//    }
//
//    @TargetApi(23)
//    private void checkRequestPermissions(
//            final String permission,
//            final int rationaleTitleResourceId,
//            final int rationaleMessageResourceId,
//            final OnCompleteListener listener) {
//        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//            if (listener != null) {
//                listener.onComplete();
//            }
//            return;
//        }
//
//        final int requestCode = nextPermissionsRequestCode++;
//        permissionsListeners.put(requestCode, listener);
//
//        if (shouldShowRequestPermissionRationale(permission)) {
//            new AlertDialog.Builder(this)
//                    .setTitle(rationaleTitleResourceId)
//                    .setMessage(rationaleMessageResourceId)
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, final int which) {
//                            requestPermissions(new String[]{permission}, requestCode);
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(final DialogInterface dialog, final int which) {
//                            // ignore and clean up the listener
//                            permissionsListeners.remove(requestCode);
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        } else {
//            requestPermissions(new String[]{permission}, requestCode);
//        }
//    }
//
//    @TargetApi(23)
//    @SuppressWarnings("unused")
//    @Override
//    public void onRequestPermissionsResult(final int requestCode,
//                                           final @NonNull String permissions[],
//                                           final @NonNull int[] grantResults) {
//        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
//        if (permissionsListener != null
//                && grantResults.length > 0
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            permissionsListener.onComplete();
//        }
//    }

}
