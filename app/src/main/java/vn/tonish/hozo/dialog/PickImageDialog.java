package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import vn.tonish.hozo.R;

/**
 * Created by ADMIN on 4/24/2017.
 */

public class PickImageDialog extends BaseDialog {

    public PickImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_pick_image_dialog;
    }

    @Override
    protected void initData() {

    }
}
