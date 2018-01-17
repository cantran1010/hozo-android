package vn.tonish.hozo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/18/2017.
 */
abstract class BaseDialogFullScreenAnimFadeInOut extends Dialog {

    private final Context context;

    BaseDialogFullScreenAnimFadeInOut(@NonNull Context context) {
        super(context, R.style.DialogSlideAnimFullScreenFadeInOut);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
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

        if (context instanceof Activity)
            if (((Activity) context).isFinishing()) return;

        if (context == null) return;

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
