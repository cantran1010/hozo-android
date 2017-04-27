package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by LongBui.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, vn.tonish.hozo.R.style.Theme_CustomProgressDialog);

        setContentView(vn.tonish.hozo.R.layout.custom_progress_dialog);
        setCancelable(false);
    }
}