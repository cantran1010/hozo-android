package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/8/2017.
 */

public class AlertConfirmDialog extends BaseDialog implements View.OnClickListener {

    private TextViewHozo tvYes, tvNo;

    public interface AlertConfirmDialogListener {
        public void onOk();

        public void onCancel();
    }

    private AlertConfirmDialogListener alertConfirmDialogListener;

    public AlertConfirmDialogListener getAlertConfirmDialogListener() {
        return alertConfirmDialogListener;
    }

    public void setAlertConfirmDialogListener(AlertConfirmDialogListener alertConfirmDialogListener) {
        this.alertConfirmDialogListener = alertConfirmDialogListener;
    }

    public AlertConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.alert_confirm_dialog;
    }

    @Override
    protected void initData() {

        tvYes = (TextViewHozo) findViewById(R.id.tv_yes);
        tvYes.setOnClickListener(this);

        tvNo = (TextViewHozo) findViewById(R.id.tv_no);
        tvNo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:
                if (alertConfirmDialogListener != null) alertConfirmDialogListener.onOk();
                break;

            case R.id.tv_no:
                if (alertConfirmDialogListener != null) alertConfirmDialogListener.onCancel();
                break;
        }
    }
}
