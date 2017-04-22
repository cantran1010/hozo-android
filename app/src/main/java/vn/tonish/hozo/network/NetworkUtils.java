package vn.tonish.hozo.network;

import android.app.Activity;
import android.content.Context;

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

import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    // network listener
    public interface NetworkListener {
        void onSuccess(JSONObject jsonResponse);

        void onError();
    }

    public static void postVolleyFormData(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final HashMap<String, String> dataRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "postVolley url : " + url + " /////// data request : " + dataRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.d(TAG, "postVolley onResponse : " + response);
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
                        LogUtils.e(TAG, "postVolley volleyError : " + error.toString());
                        LogUtils.e(TAG, "postVolley volleyError message : " + error.getMessage());

//                        //stupid volley ,can't catch exception
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Utils.showLongToast(context, "TimeoutError err ...");
//                        } else if (error instanceof AuthFailureError) {
//                            Utils.showLongToast(context, "AuthFailureError ...");
//                        } else if (error instanceof ServerError) {
//                            Utils.showLongToast(context, "ServerError ...");
//                        } else if (error instanceof NetworkError) {
//                            Utils.showLongToast(context, "NetworkError ...");
//                        } else if (error instanceof ParseError) {
//                            Utils.showLongToast(context, "ParseError ...");
//                        }else{
//                            Utils.showLongToast(context, "ParseError ...");
//                        }

//                        NetworkResponse response = error.networkResponse;
//                        if (response != null && response.data != null) {
//                            LogUtils.d(TAG, "postVolley response.statusCode : " + response.statusCode);
//                        }

                        if (error.getMessage().equals(Constants.ERROR_AUTHEN)) {
                            // HTTP Status Code: 401 Unauthorized
                            // Refresh token

                            HashMap<String, String> dataRequestToken = new HashMap<>();
                            dataRequestToken.put("mobile", UserManager.getUserLogin(context).getPhoneNumber());
                            dataRequestToken.put("refresh_token", UserManager.getUserToken(context));

                            postVolleyFormData(true, true, true, context, NetworkConfig.API_REFRESH_TOKEN, dataRequestToken, new NetworkListener() {
                                @Override
                                public void onSuccess(JSONObject jsonResponse) {
                                    // save new token
                                    UserEntity userEntity = new UserEntity();
                                    try {

                                        if (jsonResponse.getInt("code") == 0) {
                                            JSONObject object = null;

                                            object = new JSONObject(getStringInJsonObj(jsonResponse, "data"));

                                            JSONObject mObject = new JSONObject(getStringInJsonObj(object, "user"));
                                            userEntity.setId(Integer.parseInt(getStringInJsonObj(mObject, "id")));

                                            JSONObject jsonToken = new JSONObject(getStringInJsonObj(object, "token"));
                                            userEntity.setToken(getStringInJsonObj(jsonToken, "access_token"));
                                            userEntity.setRefreshToken(getStringInJsonObj(jsonToken, "refresh_token"));

                                            userEntity.setTokenExp(getStringInJsonObj(object, "token_exp"));
                                            userEntity.setFullName(getStringInJsonObj(mObject, "full_name"));
                                            userEntity.setPhoneNumber(getStringInJsonObj(mObject, "mobile"));
                                            userEntity.setLoginAt(getStringInJsonObj(mObject, "login_at"));
                                            UserManager.insertUserLogin(userEntity, context);

                                            // retry try call api
                                            postVolleyFormData(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, dataRequest, networkListener);

                                        } else {
                                            //logout

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError() {

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
                final HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                LogUtils.d(TAG, "postVolley getHeaders token: " + UserManager.getUserToken(context));

                return headers;
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                int mStatusCode = response.statusCode;
//                LogUtils.d(TAG,"postVolley parseNetworkResponse , mStatusCode : " + mStatusCode);
//                return super.parseNetworkResponse(response);
//            }

//            @Override
//            protected VolleyError parseNetworkError(VolleyError volleyError) {
//                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//                    volleyError = error;
//                    LogUtils.d(TAG, "parseNetworkError networkResponse");
//                }
//                return volleyError;
//            }

        };

        postRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        NetworkConfig.NETWORK_TIME_OUT,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(postRequest);

    }

    public static void postVolleyRaw(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "postVolley url : " + url + " /////// data request : " + jsonRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }

        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

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
                LogUtils.e(TAG, volleyError.networkResponse.statusCode + "");

                if (isShowDialogError)

                    DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            postVolleyRaw(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });

//                    DialogUtils.showConfirmAlertDialog(context, context.getString(R.string.network_error_msg), new DialogUtils.ConfirmDialogListener() {
//                        @Override
//                        public void onConfirmClick() {
//                            networkListener.onError();
//                        }
//                    });

                if (isDismissProgressDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
                headers.put("Accept", "application/json");

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

    public static void getRequestVolley(final boolean isShowProgressDialog, final boolean isDismissProgressDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        LogUtils.d(TAG, "getRequestVolley url : " + url);
        LogUtils.d(TAG, "getRequestVolley jsonRequest : " + jsonRequest.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "getRequestVolley onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgressDialog) ProgressDialogUtils.dismissProgressDialog();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, "getRequestVolley volleyError : " + volleyError.toString());

                if (isShowDialogError)
//                    DialogUtils.showConfirmAlertDialog(context, context.getString(R.string.network_error_msg), new DialogUtils.ConfirmDialogListener() {
//                        @Override
//                        public void onConfirmClick() {
//                            networkListener.onError();
//                        }
//                    });

                    DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            getRequestVolley(isShowProgressDialog, isDismissProgressDialog, isShowDialogError, context, url, jsonRequest, networkListener);
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
                return headers;
            }
        };

//        jsonObjectRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        NetworkConfig.NETWORK_TIME_OUT,
//                        1,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULTI));

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
//                    DialogUtils.showConfirmAlertDialog(context, context.getString(R.string.network_error_msg), new DialogUtils.ConfirmDialogListener() {
//                        @Override
//                        public void onConfirmClick() {
//                            networkListener.onError();
//                        }
//                    });

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

//        jsonObjectRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        NetworkConfig.NETWORK_TIME_OUT,
//                        1,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULTI));

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.NETWORK_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

}
