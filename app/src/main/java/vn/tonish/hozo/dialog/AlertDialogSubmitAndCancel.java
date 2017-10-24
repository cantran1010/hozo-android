package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/8/2017.
 */

public class AlertDialogSubmitAndCancel extends BaseDialog implements View.OnClickListener {

    private final String title;
    private final String content;
    private final String submit;
    private final String cancel;

    public interface AlertDialogListener {
        void onSubmit();

        void onCancel();
    }

    private final AlertDialogListener alertDialogListener;

    public AlertDialogSubmitAndCancel(@NonNull Context context, String title, String content, String submit, String cancel, AlertDialogListener alertDialogListener) {
        super(context);
        this.title = title;
        this.content = content;
        this.submit = submit;
        this.cancel = cancel;
        this.alertDialogListener = alertDialogListener;

        showView();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_alert_submit_and_cancel;
    }

    @Override
    protected void initData() {

        TextViewHozo tvSubmit = (TextViewHozo) findViewById(R.id.tv_yes);
        tvSubmit.setOnClickListener(this);

        TextViewHozo tvCancel = (TextViewHozo) findViewById(R.id.tv_no);
        tvCancel.setOnClickListener(this);

        TextViewHozo tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        TextViewHozo tvContent = (TextViewHozo) findViewById(R.id.tv_content);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvSubmit.setText(submit);
        tvCancel.setText(cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:
                if (alertDialogListener != null) alertDialogListener.onSubmit();
                hideView();
                break;

            case R.id.tv_no:
                if (alertDialogListener != null) alertDialogListener.onCancel();
                hideView();
                break;
        }
    }
}
