package vn.tonish.hozo.utils;

import android.app.Activity;
import android.content.Context;

import vn.tonish.hozo.dialog.HozoProgressDialog;
import vn.tonish.hozo.dialog.LoadingDialog;

/**
 * Created by LongBui.
 */
public class ProgressDialogUtils {
    private static final String TAG = ProgressDialogUtils.class.getName();
    private static LoadingDialog mLoadingDialog;
    private static HozoProgressDialog progressDialog;

    public static void showProgressDialog(Context context) {
        LogUtils.d(TAG, "------- ProgressDialog showProgressDialog start ------");

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        if (context == null) return;

        try {
            if (mLoadingDialog != null) {
                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
            } else {
                mLoadingDialog = new LoadingDialog(context);
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLoadingDialog = null;
            showProgressDialog(context);
        }
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.setCancelable(false);
    }

    public static void dismissProgressDialog() {

        LogUtils.d(TAG, "------- ProgressDialog dismissProgressDialog start ------");

        try {
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showHozoProgressDialog(Context context) {

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        if (context == null) return;

        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog = new HozoProgressDialog(context);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog = null;
            showHozoProgressDialog(context);
        }
    }


}
