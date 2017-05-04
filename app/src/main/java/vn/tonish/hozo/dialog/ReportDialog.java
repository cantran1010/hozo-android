package vn.tonish.hozo.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import vn.tonish.hozo.view.ButtonHozo;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ReplyActivity;
import vn.tonish.hozo.activity.ReportTaskActivity;

/**
 * Created by LongBui on 4/25/2017.
 */

public class ReportDialog extends BaseDialogFullScreenAnimDownUp implements View.OnClickListener {

    private ButtonHozo btnReply, btnReport, btnCancel;

    public ReportDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.report_dialog;
    }

    @Override
    protected void initData() {
        btnReply = (ButtonHozo) findViewById(R.id.btn_reply);
        btnReply.setOnClickListener(this);

        btnReport = (ButtonHozo) findViewById(R.id.btn_report);
        btnReport.setOnClickListener(this);

        btnCancel = (ButtonHozo) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_reply:
                Intent intentReply = new Intent(getContext(), ReplyActivity.class);
                getContext().startActivity(intentReply);
                hideView();
                break;

            case R.id.btn_report:
                Intent intentReport = new Intent(getContext(), ReportTaskActivity.class);
                getContext().startActivity(intentReport);
                hideView();
                break;

            case R.id.btn_cancel:
                hideView();
                break;


        }
    }
}
