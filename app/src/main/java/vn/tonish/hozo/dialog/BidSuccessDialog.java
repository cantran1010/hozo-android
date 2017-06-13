package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 5/16/17.
 */

public class BidSuccessDialog extends BaseDialogFullScreenAnimFadeInOut {
    public BidSuccessDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_bid_success;
    }

    @Override
    protected void initData() {

    }
}
