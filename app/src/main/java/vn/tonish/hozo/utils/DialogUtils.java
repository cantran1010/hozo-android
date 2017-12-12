package vn.tonish.hozo.utils;

import android.app.Activity;
import android.content.Context;

import vn.tonish.hozo.R;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.AlertDialogOkFullScreen;
import vn.tonish.hozo.dialog.AlertDialogOkNonTouch;
import vn.tonish.hozo.dialog.AlertDialogSubmit;
import vn.tonish.hozo.dialog.AlertDialogSubmitAndCancel;

/**
 * Created by LongBui on 4/4/2017.
 */
public class DialogUtils {

    public static void showRetryDialog(Context context, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkAndCancel(context, context.getString(R.string.retry_title), context.getString(R.string.retry_content), context.getString(R.string.retry), context.getString(R.string.report_cancel), alertDialogListener);
    }

    public static void showOkAndCancelDialog(Context context, String title, String content, String submit, String cancel, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkAndCancel(context, title, content, submit, cancel, alertDialogListener);
    }

    public static void showSubmitAndCancelDialog(Context context, String title, String content, String submit, String cancel, AlertDialogSubmitAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogSubmitAndCancel(context, title, content, submit, cancel, alertDialogListener);
    }

    public static void showOkDialog(Context context, String title, String content, String submit, AlertDialogOk.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOk(context, title, content, submit, alertDialogListener);
    }

    public static void showOkDialogNonTouch(Context context, String title, String content, String submit, AlertDialogOkNonTouch.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkNonTouch(context, title, content, submit, alertDialogListener);
    }

    public static void showSubmitDialog(Context context, String title, String content, String submit, AlertDialogSubmit.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogSubmit(context, title, content, submit, alertDialogListener);
    }

    public static void showOkDialogFullScreen(Context context, String title, String content, String submit, AlertDialogOkFullScreen.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkFullScreen(context, title, content, submit, alertDialogListener);
    }

    public static void showReCommendUpdateDialog(Context context, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkAndCancel(context, context.getString(R.string.update_version_hozo), context.getString(R.string.update_content), context.getString(R.string.oke), context.getString(R.string.report_cancel), alertDialogListener);
    }

    public static void showForceUpdateDialog(Context context, AlertDialogOkFullScreen.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        new AlertDialogOkFullScreen(context, context.getString(R.string.update_version_hozo), context.getString(R.string.update_force_content), context.getString(R.string.update_oke), alertDialogListener);
    }

}
