package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/8/2017.
 */

public class AlertDialogCancelTask extends BaseDialog implements View.OnClickListener {

    private TextViewHozo tvTitle;
    private TextViewHozo tvContent;

    public interface AlertConfirmDialogListener {
        void onOk();

        void onCancel();
    }

    private AlertConfirmDialogListener alertConfirmDialogListener;

    public AlertConfirmDialogListener getAlertConfirmDialogListener() {
        return alertConfirmDialogListener;
    }

    public void setAlertConfirmDialogListener(AlertConfirmDialogListener alertConfirmDialogListener) {
        this.alertConfirmDialogListener = alertConfirmDialogListener;
    }

    public AlertDialogCancelTask(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.alert_dialog_cancel_task;
    }

    @Override
    protected void initData() {

        TextViewHozo tvYes = (TextViewHozo) findViewById(R.id.tv_yes);
        tvYes.setOnClickListener(this);

        TextViewHozo tvNo = (TextViewHozo) findViewById(R.id.tv_no);
        tvNo.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvContent = (TextViewHozo) findViewById(R.id.tv_content);

    }

    private void updateTitleContent(String title, String content) {
        tvTitle.setText(title);
        tvContent.setText(content);
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
