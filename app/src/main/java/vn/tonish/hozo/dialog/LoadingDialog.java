package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;

public class LoadingDialog extends Dialog {
    /**
     * �R���X�g���N�^
     *
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, vn.tonish.hozo.R.style.Theme_CustomProgressDialog);

        setContentView(vn.tonish.hozo.R.layout.custom_progress_dialog);
        setCancelable(false);
    }
}