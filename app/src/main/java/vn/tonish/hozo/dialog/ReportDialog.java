package vn.tonish.hozo.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.task_detail.ReportTaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/25/2017.
 */

public class ReportDialog extends BaseDialog implements View.OnClickListener {

    private RadioGroup rgReport;
    private final Comment comment;

    public ReportDialog(@NonNull Context context, Comment comment) {
        super(context);
        this.comment = comment;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_report;
    }

    @Override
    protected void initData() {
        TextViewHozo tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        TextViewHozo tvOk = (TextViewHozo) findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(this);

        rgReport = (RadioGroup) findViewById(R.id.rg_report);
        RadioButton rbSpam = (RadioButton) findViewById(R.id.rb_spam);
        RadioButton rbLanguage = (RadioButton) findViewById(R.id.rb_language);
        RadioButton rbPolicy = (RadioButton) findViewById(R.id.rb_policy);
        RadioButton rbOther = (RadioButton) findViewById(R.id.rb_other);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_cancel:
                hideView();
                break;

            case R.id.tv_ok:
                doReport();
                break;


        }
    }

    private void doReport() {
        int selectedId = rgReport.getCheckedRadioButtonId();
        RadioButton radioSelected = (RadioButton) findViewById(selectedId);

        switch (selectedId) {

            case R.id.rb_spam:
                sendToServer(radioSelected.getText().toString());
                break;

            case R.id.rb_language:
                sendToServer(radioSelected.getText().toString());
                break;

            case R.id.rb_policy:
                sendToServer(radioSelected.getText().toString());
                break;

            case R.id.rb_other:
                hideView();
                Intent intent = new Intent(getContext(), ReportTaskActivity.class);
                intent.putExtra(Constants.COMMENT_ID_EXTRA, comment.getId());
                getContext().startActivity(intent);
                break;

        }
    }

    private void sendToServer(final String reason) {
        ProgressDialogUtils.showProgressDialog(getContext());
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "report data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());


        ApiClient.getApiService().report(UserManager.getUserToken(), comment.getId(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "report , body : " + response.body());
                LogUtils.d(TAG, "report , code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    hideView();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            sendToServer(reason);
                        }
                    });
                }else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getContext());
                } else {
                    DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            sendToServer(reason);
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
                DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        sendToServer(reason);
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

