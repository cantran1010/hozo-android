package vn.tonish.hozo.utils;

import android.app.Activity;
import android.content.Context;

import vn.tonish.hozo.dialog.LoadingDialog;

public class ProgressDialogUtils {
//    public static ProgressDialog progressDialog;
    public static final String TAG = ProgressDialogUtils.class.getName();
    public static LoadingDialog mLoadingDialog;

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
            return;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
