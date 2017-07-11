package vn.tonish.hozo.utils;

import android.content.Context;

import vn.tonish.hozo.R;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkFullScreen;

/**
 * Created by LongBui on 4/4/2017.
 */
public class DialogUtils {

    public static void showRetryDialog(Context context, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOkAndCancel(context, context.getString(R.string.retry_title), context.getString(R.string.retry_content), context.getString(R.string.retry), context.getString(R.string.report_cancel), alertDialogListener);
    }

    public static void showOkAndCancelDialog(Context context, String title, String content, String submit, String cancel, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOkAndCancel(context, title, content, submit, cancel, alertDialogListener);
    }

    public static void showOkDialog(Context context, String title, String content, String submit, AlertDialogOk.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOk(context, title, content, submit, alertDialogListener);
    }

    public static void showOkDialogFullScreen(Context context, String title, String content, String submit, AlertDialogOkFullScreen.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOkFullScreen(context, title, content, submit, alertDialogListener);
    }

    public static void showReCommendUpdateDialog(Context context, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOkAndCancel(context, context.getString(R.string.update_version), context.getString(R.string.update_content), context.getString(R.string.oke), context.getString(R.string.report_cancel), alertDialogListener);
    }

    public static void showForceUpdateDialog(Context context, AlertDialogOkFullScreen.AlertDialogListener alertDialogListener) {
        if (context == null) return;
        new AlertDialogOkFullScreen(context, context.getString(R.string.update_version), context.getString(R.string.update_force_content), context.getString(R.string.update_oke), alertDialogListener);
    }

}
