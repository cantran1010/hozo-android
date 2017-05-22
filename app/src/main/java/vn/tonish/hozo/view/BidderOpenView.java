package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
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
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.AcceptOfferResponse;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class BidderOpenView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo, tvPrice;
    private RatingBar rbRate;
    private ButtonHozo btnAssign;

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
        layoutInflater.inflate(R.layout.bidder_open_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);
        btnAssign = (ButtonHozo) findViewById(R.id.btn_assign);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
    }

    public void updateData(final Bidder bidder) {
        LogUtils.d(TAG, "updateData bidder : " + bidder.toString());

        Utils.displayImageAvatar(getContext(), imgAvatar, bidder.getAvatar());
        tvName.setText(bidder.getFullName());
        rbRate.setRating(bidder.getTaskerAverageRating());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(bidder.getBidedAt(), getContext()));

        btnAssign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doAcceptOffer();
            }

            private void doAcceptOffer() {
                ProgressDialogUtils.showProgressDialog(getContext());
                JSONObject jsonRequest = new JSONObject();
                try {
                    jsonRequest.put(Constants.PARAMETER_ACCEPT_OFFER, Constants.PARAMETER_ACCEPTED_OFFER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
                LogUtils.d(TAG, "acceptOffer jsonRequest : " + jsonRequest.toString());

                ApiClient.getApiService().acceptOffer(UserManager.getUserToken(), bidder.getId(),body).enqueue(new Callback<AcceptOfferResponse>() {
                    @Override
                    public void onResponse(Call<AcceptOfferResponse> call, Response<AcceptOfferResponse> response) {
                        LogUtils.d(TAG, "acceptOffer code : " + response.code());
                        LogUtils.d(TAG, "acceptOffer body : " + response.body());

                        if (response.code() == Constants.HTTP_CODE_OK) {

                        } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                            NetworkUtils.RefreshToken(getContext(), new NetworkUtils.RefreshListener() {
                                @Override
                                public void onRefreshFinish() {
                                    doAcceptOffer();
                                }
                            });
                        } else {
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
                    public void onFailure(Call<AcceptOfferResponse> call, Throwable t) {
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
    }
}
