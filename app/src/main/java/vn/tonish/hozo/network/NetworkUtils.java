package vn.tonish.hozo.network;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    // network listener
    public interface NetworkListener {
        public void onSuccess(JSONObject jsonResponse);
        public void onError();
    }

    public static void postVolley(final boolean isShowProgessDialog, final boolean isDismissProgessDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "postVolley url : " + url + " /////// data request : " + jsonRequest.toString());

        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }

        if (isShowProgessDialog)
            ProgressDialogUtils.showProgressDialog(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "postVolley onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgessDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, "postVolley volleyError : " + volleyError.toString());

                if (isShowDialogError)

                    DialogUtils.showRetryDialog(context, context.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            postVolley(isShowProgessDialog, isDismissProgessDialog, isShowDialogError, context, url, jsonRequest, networkListener);
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

                if (isDismissProgessDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer " + PreferUtils.getTokenUser(context));
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

    public static void getRequestVolley(final boolean isShowProgressDialog, final boolean isDismissProgessDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        if (isShowProgressDialog)
            ProgressDialogUtils.showProgressDialog(context);

        LogUtils.d(TAG, "getRequestVolley url : " + url);
        LogUtils.d(TAG, "getRequestVolley jsonRequest : " + jsonRequest.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "getRequestVolley onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgessDialog) ProgressDialogUtils.dismissProgressDialog();

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
                            getRequestVolley(isShowProgressDialog, isDismissProgessDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });

                if (isDismissProgessDialog) ProgressDialogUtils.dismissProgressDialog();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

//        jsonObjectRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        NetworkConfig.NETWORK_TIME_OUT,
//                        1,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.NETWORK_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public static void deleteVolley(final boolean isShowProgessDialog, final boolean isDismissProgessDialog, final boolean isShowDialogError, final Context context, final String url, final JSONObject jsonRequest, final NetworkListener networkListener) {
        LogUtils.d(TAG, "deleteVolley url : " + url + " /////// data request : " + jsonRequest.toString());


        if (context instanceof Activity) {
            Utils.hideKeyBoard((Activity) context);
        }

        if (isShowProgessDialog)
            ProgressDialogUtils.showProgressDialog(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonRequest, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.d(TAG, "postVolley onResponse : " + jsonObject.toString());
                networkListener.onSuccess(jsonObject);

                if (isDismissProgessDialog)
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
                            deleteVolley(isShowProgessDialog, isDismissProgessDialog, isShowDialogError, context, url, jsonRequest, networkListener);
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });

                if (isDismissProgessDialog)
                    ProgressDialogUtils.dismissProgressDialog();
            }

        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

//        jsonObjectRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        NetworkConfig.NETWORK_TIME_OUT,
//                        1,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                NetworkConfig.NETWORK_TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

}
