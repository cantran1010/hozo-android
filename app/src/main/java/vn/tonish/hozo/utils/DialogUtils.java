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
        new AlertDialogOkAndCancel(context, context.getString(R.string.retry_title), context.getString(R.string.retry_content), context.getString(R.string.retry), context.getString(R.string.report_cancel), alertDialogListener);
    }

    public static void showOkAndCancelDialog(Context context, String title, String content, String submit, String cancel, AlertDialogOkAndCancel.AlertDialogListener alertDialogListener) {
        new AlertDialogOkAndCancel(context, title, content, submit, cancel, alertDialogListener);
    }

    public static void showOkDialog(Context context, String title, String content, String submit, AlertDialogOk.AlertDialogListener alertDialogListener) {
        new AlertDialogOk(context, title, content, submit, alertDialogListener);
    }

    public static void showOkDialogFullScreen(Context context, String title, String content, String submit, AlertDialogOkFullScreen.AlertDialogListener alertDialogListener) {
        new AlertDialogOkFullScreen(context, title, content, submit, alertDialogListener);
    }

}
