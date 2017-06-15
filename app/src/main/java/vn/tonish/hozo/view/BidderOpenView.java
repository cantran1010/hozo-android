package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class BidderOpenView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo;
    private RatingBar rbRate;
    private ButtonHozo btnAssign;
    private Bidder bidder;
    private int taskId;

    public BidderOpenView(Context context) {
        super(context);
        initView();
    }

    public BidderOpenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BidderOpenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BidderOpenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_bidder_open, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);
        btnAssign = (ButtonHozo) findViewById(R.id.btn_assign);
    }

    public void updateData(final Bidder bidder, String type) {
        LogUtils.d(TAG, "updateData bidder : " + bidder.toString());
        this.bidder = bidder;
        Utils.displayImageAvatar(getContext(), imgAvatar, bidder.getAvatar());
        tvName.setText(bidder.getFullName());
        rbRate.setRating(bidder.getTaskerAverageRating());
        if (bidder.getBidedAt() != null)
            tvTimeAgo.setText(DateTimeUtils.getTimeAgo(bidder.getBidedAt(), getContext()));

        if (type.equals(getContext().getString(R.string.assign))) {
            btnAssign.setVisibility(View.VISIBLE);
            btnAssign.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAcceptOffer();
                }

                private void doAcceptOffer() {
                    ProgressDialogUtils.showProgressDialog(getContext());
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put(Constants.PARAMETER_ACCEPTED_OFFER_USER_ID, bidder.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
                    LogUtils.d(TAG, "acceptOffer jsonRequest : " + jsonRequest.toString());
                    LogUtils.d(TAG, "acceptOffer jsonRequest : " + jsonRequest.toString() + " task Id : " + getTaskId());

                    ApiClient.getApiService().acceptOffer(UserManager.getUserToken(), getTaskId(), body).enqueue(new Callback<TaskResponse>() {
                        @Override
                        public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                            LogUtils.d(TAG, "acceptOffer code : " + response.code());
                            LogUtils.d(TAG, "acceptOffer body : " + response.body());

                            if (response.code() == Constants.HTTP_CODE_OK) {
                                Intent intentAnswer = new Intent();
                                intentAnswer.setAction("MyBroadcast");
                                intentAnswer.putExtra(Constants.EXTRA_TASK, response.body());
                                getContext().sendBroadcast(intentAnswer);
                            } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                                NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                                    @Override
                                    public void onRefreshFinish() {
                                        doAcceptOffer();
                                    }
                                });
                            } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                                APIError error = ErrorUtils.parseError(response);
                                LogUtils.e(TAG, "acceptOffer errorBody" + error.toString());
                                DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), error.message(), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {

                                    }
                                });
                            } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                                Utils.blockUser(getContext());
                            }else {
                                DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        doAcceptOffer();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                            }

                            ProgressDialogUtils.dismissProgressDialog();
                        }

                        @Override
                        public void onFailure(Call<TaskResponse> call, Throwable t) {
                            DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                                @Override
                                public void onSubmit() {
                                    doAcceptOffer();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            ProgressDialogUtils.dismissProgressDialog();
                        }
                    });
                }
            });
        } else {
            btnAssign.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, bidder.getId());
                intent.putExtra(Constants.IS_MY_USER, bidder.getId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;

        }
    }

    private int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
