package vn.tonish.hozo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.HozoAccountKitUIManager;

/**
 * Created by CanTran on 10/5/17.
 */

public class LoginActivity extends Activity {
    private static final int FRAMEWORK_REQUEST_CODE = 1;
    private int nextPermissionsRequestCode = 4000;
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private interface OnCompleteListener {
        void onComplete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
//                try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo("vn.tonish.hozo", PackageManager.GET_SIGNATURES);
//            for(Signature signature: packageInfo.signatures){
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
//            }
//        }catch (Exception e){
//        }
        loginHozo();
    }

//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_login;
//    }
//
//    @Override
//    protected void initView() {
//
//    }
//
//    @Override
//    protected void initData() {
//        AccountKit.initialize(getApplicationContext());
//        loginHozo();
//
//    }
//
//    @Override
//    protected void resumeData() {
//
//    }

    private void loginHozo() {
        final UIManager uiManager;
        final Intent intent = new Intent(this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder
                = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN);

        uiManager = new HozoAccountKitUIManager(ButtonType.CONTINUE, ButtonType.CONTINUE, TextPosition.BELOW_BODY, LoginType.PHONE);
        configurationBuilder.setUIManager(uiManager);

        configurationBuilder.setDefaultCountryCode("VN").setReadPhoneStateEnabled(true).setReceiveSMS(true);
        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);
        OnCompleteListener completeListener = new OnCompleteListener() {
            @Override
            public void onComplete() {
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
            }
        };
        if (configuration.isReceiveSMSEnabled() && !canReadSmsWithoutPermission()) {
            final OnCompleteListener receiveSMSCompleteListener = completeListener;
            completeListener = new OnCompleteListener() {
                @Override
                public void onComplete() {
                    requestPermissions(
                            Manifest.permission.RECEIVE_SMS,
                            R.string.permissions_receive_sms_title,
                            R.string.permissions_receive_sms_message,
                            receiveSMSCompleteListener);
                }
            };
        }
        if (configuration.isReadPhoneStateEnabled() && !isGooglePlayServicesAvailable()) {
            final OnCompleteListener readPhoneStateCompleteListener = completeListener;
            completeListener = new OnCompleteListener() {
                @Override
                public void onComplete() {
                    requestPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            R.string.permissions_read_phone_state_title,
                            R.string.permissions_read_phone_state_message,
                            readPhoneStateCompleteListener);
                }
            };
        }

        completeListener.onComplete();
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != FRAMEWORK_REQUEST_CODE) {
            return;
        }

        final String toastMessage;
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled()) {
            toastMessage = "Thoát";
        } else if (loginResult.getError() != null) {
            toastMessage = loginResult.getError().getErrorType().getMessage();
//            final Intent intent = new Intent(this, ErrorActivity.class);
//            intent.putExtra(ErrorActivity.HELLO_TOKEN_ACTIVITY_ERROR_EXTRA, loginResult.getError());

//            startActivity(intent);
        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            final long tokenRefreshIntervalInSeconds =
                    loginResult.getTokenRefreshIntervalInSeconds();
            if (accessToken != null) {
                toastMessage = "Success:" + accessToken.getAccountId()
                        + tokenRefreshIntervalInSeconds;
//                startActivity(new Intent(this, TokenActivity.class));
            } else {
                toastMessage = "Unknown response type";
            }
        }

        Toast.makeText(
                this,
                toastMessage,
                Toast.LENGTH_LONG)
                .show();
    }


    private boolean isGooglePlayServicesAvailable() {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS;
    }

    private boolean canReadSmsWithoutPermission() {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        //TODO we should also check for Android O here t18761104

        return false;
    }

    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // ignore and clean up the listener
                            permissionsListeners.remove(requestCode);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

}
