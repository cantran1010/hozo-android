package vn.tonish.hozo.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.common.Constants.CODE;
import static vn.tonish.hozo.common.Constants.REFRESH_TOCKEN;
import static vn.tonish.hozo.common.Constants.USER_MOBILE;

/**
 * Created by LongBui.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    // network listener
    public interface NetworkListener {
        void onSuccess(JSONObject jsonResponse);

        void onError();
    }

    // callback refresh listener
    public interface RefreshListener {
        void onRefreshFinish(JSONObject jsonResponse);
    }

    //refresh token
    public static void RefreshToken(final Context context, final RefreshListener refreshListener) {
        HashMap<String, String> dataRequestToken = new HashMap<>();
        dataRequestToken.put(USER_MOBILE, UserManager.getUserLogin(context).getPhoneNumber());
        dataRequestToken.put(REFRESH_TOCKEN, UserManager.getUserLogin(context).getRefreshToken());

        postVolleyFormData(true, true, true, context, NetworkConfig.API_REFRESH_TOKEN, dataRequestToken, new NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                // save new token
                try {

                    if (jsonResponse.getInt(CODE) == 0) {
                        if (refreshListener != null) refreshListener.onRefreshFinish(jsonResponse);
                    } else {
                        // logout
                        Toast.makeText(context, context.getString(R.string.network_account_is_not_exist), Toast.LENGTH_SHORT).show();
                        UserManager.deleteAll();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

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

    public static void postVolleyFormData(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final HashMap<String, String> dataRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "postVolleyFormData url : " + url + " /////// data request : " + dataRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, "postVolleyFormData onResponse : " + response);
                        try {
                            networkListener.onSuccess(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (isDismissProgressDialog)
                            ProgressDialogUtils.dismissProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.e(TAG, "postVolleyFormData volleyError : " + error.toString());
                        LogUtils.e(TAG, "postVolleyFormData volleyError message : " + error.getMessage());

                        if (error.getMessage().equals(Constants.ERROR_AUTHENTICATION)) {
                            // HTTP Status Code: 401 Unauthorized
                            // Refresh token
                            RefreshToken(context, new RefreshListener() {
                                @Override
                                public void onRefreshFinish(JSONObject jsonResponse) {
                                    UserManager.insertUserLogin(new DataParse().getUserEntiny(context, jsonResponse), context);
                                    postVolleyFormData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, dataRequest, networkListener);
                                }
                            });
                        } else {
                            if (isShowDialogError)

                                DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                                    @Override
                                    public void onOkClick() {
                                        postVolleyFormData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, dataRequest, networkListener);
                                    }

                                    @Override
                                    public void onCancelClick() {

                                    }
                                });
                        }

                        if (isDismissProgressDialog)
                            ProgressDialogUtils.dismissProgressDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return dataRequest;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                LogUtils.d(TAG, "postVolleyFormData getHeaders token: " + UserManager.getUserToken(context));

                return headers;
            }
        };

        postRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        NetworkConfig.NETWORK_TIME_OUT,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(postRequest);

    }

    public static void postVolleyRawData(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "postVolleyRawData url : " + url + " /////// data request : " + jsonRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }

        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "postVolleyRawData onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, "postVolleyRawData volleyError : " + error.toString());
                LogUtils.e(TAG, error.networkResponse.statusCode + "");

                if (error.getMessage().equals(Constants.ERROR_AUTHENTICATION)) {
                    // HTTP Status Code: 401 Unauthorized
                    // Refresh token
                    RefreshToken(context, new RefreshListener() {
                        @Override
                        public void onRefreshFinish(JSONObject jsonResponse) {
                            UserManager.insertUserLogin(new DataParse().getUserEntiny(context, jsonResponse), context);
                            postVolleyRawData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                        }
                    });
                } else {
                    if (isShowDialogError)

                        DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                            @Override
                            public void onOkClick() {
                                postVolleyRawData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        });
                }

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                headers.put("Accept", "application/json");
                LogUtils.d(TAG, "postVolleyRawData getHeaders token: " + UserManager.getUserToken(context));

                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        NetworkConfig.NETWORK_TIME_OUT,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public static void getRequestVolleyFormData(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final HashMap<String, String> dataRequest, final NetworkListener networkListener) {
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        LogUtils.d(TAG, "getRequestVolleyFormData url : " + url);
        LogUtils.d(TAG, "getRequestVolleyFormData jsonRequest : " + dataRequest.toString());

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, "getRequestVolleyFormData onResponse : " + response);
                        try {
                            networkListener.onSuccess(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (isDismissProgressDialog)
                            ProgressDialogUtils.dismissProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, "getRequestVolleyFormData volleyError : " + error.toString());
                LogUtils.e(TAG, "getRequestVolleyFormData volleyError message : " + error.getMessage());


                if ((!(error == null)) || error.getMessage().equals(Constants.ERROR_AUTHENTICATION)) {

                    // HTTP Status Code: 401 Unauthorized
                    // Refresh token
                    RefreshToken(context, new RefreshListener() {
                        @Override
                        public void onRefreshFinish(JSONObject jsonResponse) {
                            // retry try call api

                            UserManager.insertUserLogin(new DataParse().getUserEntiny(context, jsonResponse), context);
                            getRequestVolleyFormData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, dataRequest, networkListener);
                        }

                    });
                } else {
                    if (isShowDialogError)
                        DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                            @Override
                            public void onOkClick() {
                                getRequestVolleyFormData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, dataRequest, networkListener);
                            }

                            @Override
                            public void onCancelClick() {

                            }
                        });
                }

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                LogUtils.d(TAG, "getRequestVolleyFormData getHeaders token: " + UserManager.getUserToken(context));
                return headers;
            }
        };

        getRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        NetworkConfig.NETWORK_TIME_OUT,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(getRequest);

    }

    public static void getRequestVolleyRawData(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        LogUtils.d(TAG, "getRequestVolleyRawData url : " + url);
        LogUtils.d(TAG, "getRequestVolleyRawData jsonRequest : " + jsonRequest.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "getRequestVolleyRawData onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);


                if (isDismissProgressDialog) ProgressDialogUtils.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, "getRequestVolleyRawData volleyError : " + volleyError.toString());

                if (isShowDialogError)
                    DialogUtils.showConfirmAlertDialog(context, context.getString(R.string.network_error_msg), new DialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onConfirmClick() {
                            networkListener.onError();
                        }
                    });

                DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                    @Override
                    public void onOkClick() {
                        getRequestVolleyRawData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                if (isDismissProgressDialog) ProgressDialogUtils.dismissProgressDialog();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                LogUtils.d(TAG, "getRequestVolleyRawData getHeaders token: " + UserManager.getUserToken(context));
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.NETWORK_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public static void deleteVolley(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "deleteVolley url : " + url + " /////// data request : " + jsonRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }

        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "postVolley onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, "postVolley volleyError : " + volleyError.toString());

                if (isShowDialogError)
                    DialogUtils.showConfirmAlertDialog(context, context.getString(R.string.network_error_msg), new DialogUtils.ConfirmDialogListener() {
                        @Override
                        public void onConfirmClick() {
                            networkListener.onError();
                        }
                    });

                DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                    @Override
                    public void onOkClick() {
                        deleteVolley(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.NETWORK_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

}
