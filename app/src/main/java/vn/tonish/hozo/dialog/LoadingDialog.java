package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;

import vn.tonish.hozo.R;

/**
 * Created by LongBui.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.Theme_CustomProgressDialog);

        setContentView(R.layout.dialog_custom_progress);
        setCancelable(false);
    }
}