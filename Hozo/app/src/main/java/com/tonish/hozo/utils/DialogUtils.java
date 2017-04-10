package com.tonish.hozo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tonish.hozo.R;


/**
 * Created by ADMIN on 11/5/2015.
 */
public class DialogUtils {

    public interface ConfirmDialogListener {
        public void onConfirmClick();
    }

    public interface ConfirmDialogOkCancelListener {
        public void onOkClick();

        public void onCancelClick();
    }

    public static final AlertDialog showConfirmAlertDialog(final Context context, final String msg, final ConfirmDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        if (listener != null) {
            builder.setPositiveButton(context.getString(R.string.OK),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            listener.onConfirmClick();

                        }
                    });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static final AlertDialog showConfirmAndCancelAlertDialog(final Context context, final String msg, final ConfirmDialogOkCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        if (listener != null) {
            builder.setPositiveButton(context.getString(R.string.OK),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            listener.onOkClick();

                        }
                    });

            builder.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onCancelClick();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static final AlertDialog showConfirmAndCancelAlertDialogNew(final Context context, final String msg, final ConfirmDialogOkCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        if (listener != null) {
            builder.setPositiveButton("네",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            listener.onOkClick();

                        }
                    });

            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onCancelClick();
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static final AlertDialog showRetryDialog(final Context context, final String msg, final ConfirmDialogOkCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.retry),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        listener.onOkClick();
                    }
                });

        builder.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancelClick();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }


}
