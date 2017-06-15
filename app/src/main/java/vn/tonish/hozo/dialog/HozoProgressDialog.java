package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import vn.tonish.hozo.R;

/**
 * Created by CanTran on 6/15/17.
 */

public class HozoProgressDialog extends Dialog {
    public HozoProgressDialog(@NonNull Context context) {
        super(context, R.style.full_screen_dialog);
        setContentView(R.layout.dialog_fill);
        setCancelable(false);
    }

}
