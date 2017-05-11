package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/8/2017.
 */

public class AlertDialogOk extends BaseDialog implements View.OnClickListener {

    private TextViewHozo tvSubmit, tvTitle, tvContent;
    private String title, content, submit;

    public interface AlertDialogListener {
        public void onSubmit();
    }

    private AlertDialogListener alertDialogListener;

    public AlertDialogOk(@NonNull Context context, String title, String content, String submit, AlertDialogListener alertDialogListener) {
        super(context);
        this.title = title;
        this.content = content;
        this.submit = submit;
        this.alertDialogListener = alertDialogListener;

        showView();
    }

    @Override
    protected int getLayout() {
        return R.layout.alert_dialog_ok;
    }

    @Override
    protected void initData() {

        tvSubmit = (TextViewHozo) findViewById(R.id.tv_yes);
        tvSubmit.setOnClickListener(this);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvContent = (TextViewHozo) findViewById(R.id.tv_content);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvSubmit.setText(submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:
                if (alertDialogListener != null) alertDialogListener.onSubmit();
                hideView();
                break;
        }
    }
}