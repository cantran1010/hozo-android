package vn.tonish.hozo.network;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.HomeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by LongBui on 5/9/2017.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    // callback refresh listener
    public interface RefreshListener {
        void onRefreshFinish();
    }

    //    //refresh token
    public static void refreshToken(final Context context, final RefreshListener refreshListener) {

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("refresh_token", UserManager.getMyUser().getRefreshToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "refreshToken jsonRequest: " + jsonRequest.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().refreshToken(body).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                LogUtils.d(TAG, "refreshToken onResponse body : " + response.body());
                LogUtils.d(TAG, "refreshToken onResponse status code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    //retry call api after refresh success
                    Token token = response.body();
                    //update new token to database - user table

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    UserManager.getMyUser().setAccessToken(token.getAccessToken());
                    UserManager.getMyUser().setRefreshToken(token.getRefreshToken());
                    UserManager.getMyUser().setTokenExp(token.getTokenExpires());
                    realm.commitTransaction();

                    if (refreshListener != null) refreshListener.onRefreshFinish();
                } else {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.commitTransaction();
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                // show error dialog
                LogUtils.e(TAG, "refreshToken error : " + t.getMessage());

                showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        refreshToken(context, refreshListener);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    public static void logOut(final Context context) {
        DialogUtils.showOkAndCancelDialog(context, context.getString(R.string.msg_logOut), context.getString(R.string.msg_contten_logOut), "Có", "huỷ", new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                ProgressDialogUtils.showProgressDialog(context);
                ApiClient.getApiService().logOut(UserManager.getUserToken()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.deleteAll();
                                realm.commitTransaction();
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        logOut(context);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });

                            }
                        } else {
                            APIError error = ErrorUtils.parseError(response);
                            LogUtils.d(TAG, "errorBody" + error.toString());
//                            Toast.makeText(context, error.message(), Toast.LENGTH_SHORT).show();
                            Utils.showLongToast(context,error.message(),true,false);

                        }
                        ProgressDialogUtils.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        ProgressDialogUtils.dismissProgressDialog();
                        showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                logOut(context);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                    }
                });

            }

            @Override
            public void onCancel() {

            }
        });

    }

}
