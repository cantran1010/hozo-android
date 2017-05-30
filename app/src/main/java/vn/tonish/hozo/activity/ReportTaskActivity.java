package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 4/25/2017.
 */

public class
ReportTaskActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ReportTaskActivity.class.getSimpleName();

    private ButtonHozo btnReport;
    private EdittextHozo edtContent;
    private ImageView imgClose;
    private int commentId;

    @Override
    protected int getLayout() {
        return R.layout.activity_report_task;
    }

    @Override
    protected void initView() {

        edtContent = (EdittextHozo) findViewById(R.id.edt_content);

        btnReport = (ButtonHozo) findViewById(R.id.btn_report);
        btnReport.setOnClickListener(this);

        imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        commentId = getIntent().getIntExtra(Constants.COMMENT_ID_EXTRA, 0);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_report:
                doReport();
                break;

            case R.id.img_close:
                finish();
                break;
        }
    }

    private void doReport() {

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("reason", edtContent.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "report data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());


        ApiClient.getApiService().report(UserManager.getUserToken(), commentId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "report , body : " + response.body());
                LogUtils.d(TAG, "report , code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ReportTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doReport();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(ReportTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doReport();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(ReportTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doReport();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }
}
