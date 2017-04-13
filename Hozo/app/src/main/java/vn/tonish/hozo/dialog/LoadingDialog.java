package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;

import vn.tonish.hozo.R;

public class LoadingDialog extends Dialog {
    /**
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, R.style.Theme_CustomProgressDialog);

        setContentView(R.layout.custom_progress_dialog);
        setCancelable(false);
    }
}