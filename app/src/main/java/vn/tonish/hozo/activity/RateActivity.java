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
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/25/2017.
 */

public class RateActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = RateActivity.class.getSimpleName();
    private ImageView imgRate;
    private TextViewHozo tvRateContent;
    private RatingBar ratingBar;
    private ButtonHozo btnRate;
    private int taskId, userId;

    @Override
    protected int getLayout() {
        return R.layout.rate_activity;
    }

    @Override
    protected void initView() {
        imgRate = (ImageView) findViewById(R.id.img_rate);
        tvRateContent = (TextViewHozo) findViewById(R.id.tv_rate_content);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);

        btnRate = (ButtonHozo) findViewById(R.id.btn_rate);
        btnRate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        userId = getIntent().getIntExtra(Constants.USER_ID_EXTRA, 0);

        ratingBar.setStepSize(1.0f);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if (rating == 1) {
                    imgRate.setImageResource(R.drawable.img_rate_1_star);
                    tvRateContent.setText(getString(R.string.rate_content_1_star));
                } else if (rating == 2) {
                    tvRateContent.setText(getString(R.string.rate_content_2_star));
                    imgRate.setImageResource(R.drawable.img_rate_2_star);
                } else if (rating == 3) {
                    tvRateContent.setText(getString(R.string.rate_content_3_star));
                    imgRate.setImageResource(R.drawable.img_rate_3_star);
                } else if (rating == 4) {
                    tvRateContent.setText(getString(R.string.rate_content_4_star));
                    imgRate.setImageResource(R.drawable.img_rate_4_star);
                } else if (rating == 5) {
                    tvRateContent.setText(getString(R.string.rate_content_5_star));
                    imgRate.setImageResource(R.drawable.img_rate_5_star);
                }

//                Toast.makeText(getApplicationContext(), "Your Selected Ratings  : " + String.valueOf(rating), Toast.LENGTH_LONG).show();

            }
        });
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
            Utils.showLongToast(this, getString(R.string.rate_msg_no_content_error));
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", userId);
            jsonRequest.put("body", tvRateContent.getText().toString());
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
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(RateActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doRate();
                        }
                    });
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
