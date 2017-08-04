package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

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
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/25/2017.
 */

public class RateActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = RateActivity.class.getSimpleName();
    private ImageView imgAvatar;
    private RatingBar ratingBar;
    private EdittextHozo edtContent;
    private int taskId, userId;
    private TextViewHozo tvName;

    @Override
    protected int getLayout() {
        return R.layout.activity_rate;
    }

    @Override
    protected void initView() {
        imgAvatar = findViewById(R.id.img_avatar);
        ratingBar = findViewById(R.id.rb_rate);
        edtContent = findViewById(R.id.edt_content);
        tvName = findViewById(R.id.tv_name);

        ButtonHozo btnRate = findViewById(R.id.btn_rate);
        btnRate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        userId = getIntent().getIntExtra(Constants.USER_ID_EXTRA, 0);

        Utils.displayImageAvatar(this, imgAvatar, getIntent().getStringExtra(Constants.AVATAR_EXTRA));
        tvName.setText(getIntent().getStringExtra(Constants.NAME_EXTRA));

        ratingBar.setStepSize(1.0f);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_rate:
                doRate();
                break;
        }
    }

    private void doRate() {

        if (ratingBar.getRating() == 0) {
            Utils.showLongToast(this, getString(R.string.rate_msg_no_content_error), true, false);
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", userId);
            jsonRequest.put("body", edtContent.getText().toString());
            jsonRequest.put("rating", ratingBar.getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doRate data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().rateTask(UserManager.getUserToken(), taskId, body).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                LogUtils.d(TAG, "doRate code : " + response.code());
                LogUtils.d(TAG, "doRate : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    setResult(Constants.RESPONSE_CODE_RATE);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "doRate errorBody" + error.toString());
                    DialogUtils.showOkDialog(RateActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(RateActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doRate();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(RateActivity.this);
                } else {
                    DialogUtils.showRetryDialog(RateActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doRate();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(RateActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doRate();
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
