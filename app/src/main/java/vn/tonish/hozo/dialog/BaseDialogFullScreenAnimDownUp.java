package vn.tonish.hozo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/25/2017.
 */
abstract class BaseDialogFullScreenAnimDownUp extends Dialog {

    public BaseDialogFullScreenAnimDownUp(@NonNull Context context) {
        super(context, R.style.DialogSlideAnimFullScreenAnimDownUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(getLayout());
        initData();
    }

    protected abstract int getLayout();

    protected abstract void initData();

    /**
     * Show view
     */
    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    /**
     * hide view
     */
    public void hideView() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

}
