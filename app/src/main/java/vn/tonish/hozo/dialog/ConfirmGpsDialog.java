package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.view.CheckBoxHozo;

/**
 * Created by CanTran on 6/14/18.
 */
public class ConfirmGpsDialog extends BaseDialogFullScreenAnimFadeInOut implements View.OnClickListener {

    private static final String TAG = ConfirmGpsDialog.class.getSimpleName();
    private CheckBoxHozo ckBoxGps;
    private Context context;

    public ConfirmGpsDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
        this.context = context;
    }

    public interface ConfirmGpsListener {
        void onConfirm();
    }

    private ConfirmGpsListener confirmGpsListener;

    public void setConfirmGpsListener(ConfirmGpsListener confirmGpsListener) {
        this.confirmGpsListener = confirmGpsListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_confirm_gps;
    }

    @Override
    protected void initData() {
        ckBoxGps = findViewById(R.id.ckBox_gps);
        findViewById(R.id.img_close).setOnClickListener(this);
        findViewById(R.id.btn_oke).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                hideView();
                break;

            case R.id.btn_oke:
                PreferUtils.setUpDateGps(context, !ckBoxGps.isChecked());
                if (confirmGpsListener != null)
                    confirmGpsListener.onConfirm();
                hideView();
                break;
            case R.id.btn_cancel:
                PreferUtils.setUpDateGps(context, !ckBoxGps.isChecked());
                hideView();
                break;

        }
    }

}
