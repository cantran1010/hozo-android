package vn.tonish.hozo.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.ReviewsActivity;
import vn.tonish.hozo.activity.payment.MyWalletActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.BidResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.ExpandableLayout;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 11/21/17.
 */

public class ViewPageAssignAdapter extends PagerAdapter {
    private static final String TAG = ViewPageAssignAdapter.class.getSimpleName();
    // Declare Variables
    private final Context context;
    private final int taskID;
    private final int workerCount;
    private int assignerCount;
    private final List<BidResponse> bidResponses;


    public ViewPageAssignAdapter(Context context, List<BidResponse> bidResponses, int taskID, int workerCount, int assignerCount) {
        this.context = context;
        this.bidResponses = bidResponses;
        this.taskID = taskID;
        this.workerCount = workerCount;
        this.assignerCount = assignerCount;
    }

    @Override
    public int getCount() {
        return bidResponses.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final BidResponse bidResponse = bidResponses.get(position);
        final TextViewHozo tvAssigner, tvBidderCount, tvAssignerCount, tvName, tvDes;
        final TextViewHozo tvRatingCount, tvBidsCount, tvComplex, tvWorker;
        final TextViewHozo tv5star, tv4star, tv3star, tv2star, tv1star;
        final TextViewHozo tvPrice, tvDesMsg, tvShowReviews, tvMoreReviews, btnAssiged;
        final ProgressBar assignProgress;
        final ReviewsListView reviewsListView;
        final RatingBar rbRating, rbRating5, rbRating4, rbRating3, rbRating2, rbRating1;
        final CircleImageView imgAvatarAssign, imgAvatarDes;
        final RelativeLayout layoutShowReviews;
        final ExpandableLayout layoutExpand;
        final ImageView imgArrowDown;
        final NestedScrollView scrollView;
        final RelativeLayout layoutSms;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View itemView = inflater.inflate(R.layout.viewpager_assign_item, container,
                false);
        tvAssigner = itemView.findViewById(R.id.tv_assigner);
        tvWorker = itemView.findViewById(R.id.tv_worker_count);
        tvBidderCount = itemView.findViewById(R.id.tv_bidder_count);
        tvAssignerCount = itemView.findViewById(R.id.tv_count_assigner);
        tvName = itemView.findViewById(R.id.tv_name);
        tvDes = itemView.findViewById(R.id.tv_des);
        tvRatingCount = itemView.findViewById(R.id.tv_rating_count);
        tvBidsCount = itemView.findViewById(R.id.tv_bids_count);
        tvComplex = itemView.findViewById(R.id.tv_complex);
        tv5star = itemView.findViewById(R.id.tv_5star);
        tv4star = itemView.findViewById(R.id.tv_4star);
        tv3star = itemView.findViewById(R.id.tv_3star);
        tv2star = itemView.findViewById(R.id.tv_2star);
        tv1star = itemView.findViewById(R.id.tv_1star);
        tvPrice = itemView.findViewById(R.id.tv_price);
        tvDesMsg = itemView.findViewById(R.id.tv_des_msg);
        tvShowReviews = itemView.findViewById(R.id.tv_show_reviews);
        tvMoreReviews = itemView.findViewById(R.id.tv_more_reviews);
        btnAssiged = itemView.findViewById(R.id.btn_next);
        assignProgress = itemView.findViewById(R.id.assign_progress);
        reviewsListView = itemView.findViewById(R.id.rcv_reviews);
        rbRating = itemView.findViewById(R.id.rb_rating);
        rbRating5 = itemView.findViewById(R.id.rb_rating5);
        rbRating4 = itemView.findViewById(R.id.rb_rating4);
        rbRating3 = itemView.findViewById(R.id.rb_rating3);
        rbRating2 = itemView.findViewById(R.id.rb_rating2);
        rbRating1 = itemView.findViewById(R.id.rb_rating1);
        imgAvatarAssign = itemView.findViewById(R.id.img_avatar_assign);
        imgAvatarDes = itemView.findViewById(R.id.avatar_des);
        layoutShowReviews = itemView.findViewById(R.id.layout_show_reviews);
        layoutExpand = itemView.findViewById(R.id.layout_expand);
        imgArrowDown = itemView.findViewById(R.id.img_arrow_down);
        scrollView = itemView.findViewById(R.id.scroll_View);
        layoutSms = itemView.findViewById(R.id.layout_sms);
        LogUtils.d(TAG, "assigner adapter" + assignerCount + ":" + workerCount);
        if (!bidResponse.isAccept()) {
            btnAssiged.setEnabled(true);
            Utils.setViewBackground(btnAssiged, ContextCompat.getDrawable(context, R.drawable.btn_new_selector));
        } else {
            btnAssiged.setEnabled(false);
            btnAssiged.setText(context.getString(R.string.assigned_done));
            Utils.setViewBackground(btnAssiged, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
        }
        btnAssiged.setText(context.getString(R.string.assign));
        tvAssigner.setText(String.valueOf(assignerCount));
        tvWorker.setText(String.valueOf(workerCount));
        assignProgress.setMax(workerCount);
        assignProgress.setProgress(assignerCount);
        String title = context.getString(R.string.slash) + formatTitle(getCount());
        tvBidderCount.setText(formatTitle(position + 1));
        tvAssignerCount.setText(title);
        Utils.displayImageAvatar(context, imgAvatarAssign, bidResponse.getAvatar());
        tvName.setText(bidResponse.getFullName());
        if (bidResponse.getDescription() == null || bidResponse.getDescription().isEmpty()) {
            tvDes.setVisibility(View.GONE);
        } else {
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText(bidResponse.getDescription());
        }
        rbRating.setRating(bidResponse.getTaskerAverageRating());
        tvRatingCount.setText(context.getString(R.string.reviews_count, bidResponse.getTaskerRatingCount()));
        tvBidsCount.setText(context.getString(R.string.tasks_count, bidResponse.getTaskerCount()));
        String percentDone = (int) (bidResponse.getTaskerDoneRate() * 100) + "% " + context.getString(R.string.tasks_complex);
        tvComplex.setText(percentDone);
        if (bidResponse.getTaskerRatings().get(0) > 0) {
            rbRating1.setRating(5);
            tv1star.setText(String.valueOf(bidResponse.getTaskerRatings().get(0)));
        } else {
            rbRating1.setRating(0);
            tv1star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(1) > 0) {
            rbRating2.setRating(5);
            tv2star.setText(String.valueOf(bidResponse.getTaskerRatings().get(1)));
        } else {
            rbRating2.setRating(0);
            tv2star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(2) > 0) {
            rbRating3.setRating(5);
            tv3star.setText(String.valueOf(bidResponse.getTaskerRatings().get(2)));
        } else {
            rbRating3.setRating(0);
            tv3star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(3) > 0) {
            rbRating4.setRating(5);
            tv4star.setText(String.valueOf(bidResponse.getTaskerRatings().get(3)));
        } else {
            rbRating4.setRating(0);
            tv4star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(4) > 0) {
            rbRating5.setRating(5);
            tv5star.setText(String.valueOf(bidResponse.getTaskerRatings().get(4)));
        } else {
            rbRating5.setRating(0);
            tv5star.setText("0");
        }
        tvPrice.setText(Utils.formatNumber(bidResponse.getPrice()));
        if (bidResponse.getMessage() == null || bidResponse.getMessage().isEmpty()) {
            layoutSms.setVisibility(View.GONE);
        } else {
            layoutSms.setVisibility(View.VISIBLE);
            tvDesMsg.setText(bidResponse.getMessage());
        }
        Utils.displayImageAvatar(context, imgAvatarDes, bidResponse.getAvatar());
        if (bidResponse.getTaskerRatingCount() > 0) {
            layoutShowReviews.setVisibility(View.VISIBLE);
            tvShowReviews.setText(context.getString(R.string.view_reviews));
            reviewsListView.updateData((ArrayList<ReviewEntity>) bidResponse.getReviews());
            if (bidResponse.getTaskerRatingCount() > 5)
                tvMoreReviews.setVisibility(View.VISIBLE);
            else tvMoreReviews.setVisibility(View.GONE);
        } else {
            layoutShowReviews.setVisibility(View.GONE);
        }
        layoutShowReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout(layoutExpand, imgArrowDown);
                if (layoutExpand.isExpanded()) {
                    int[] coords = {0, 0};
                    scrollView.getLocationOnScreen(coords);
                    int absoluteBottom = coords[1] + scrollView.getHeight();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", absoluteBottom).setDuration(1000);
                    objectAnimator.start();
                }
            }
        });
        btnAssiged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doAcceptOffer(bidResponse.getId(), assignProgress, btnAssiged, tvAssigner, position, false);
            }
        });
        tvMoreReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewsActivity.class);
                intent.putExtra(Constants.USER_ID, bidResponse.getId());
                intent.putExtra(Constants.TAB_EXTRA, false);
                ((BaseActivity) context).startActivity(intent, TransitionScreen.DOWN_TO_UP);
            }
        });

        imgAvatarAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, bidResponse.getId());
                intent.putExtra(Constants.IS_MY_USER, bidResponse.getId() == UserManager.getMyUser().getId());
                context.startActivity(intent);

            }
        });
        imgAvatarDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, bidResponse.getId());
                intent.putExtra(Constants.IS_MY_USER, bidResponse.getId() == UserManager.getMyUser().getId());
                context.startActivity(intent);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    private void expandableLayout(ExpandableLayout expan, ImageView img) {
        Animation anim_down = AnimationUtils.loadAnimation(context,
                R.anim.rotate_down);
        Animation anim_up = AnimationUtils.loadAnimation(context,
                R.anim.rotate_up);
        if (expan.isExpanded()) {
            img.startAnimation(anim_down);
        } else {
            img.startAnimation(anim_up);
        }
        expan.toggle();

    }

    private void doAcceptOffer(final int bidderID, final ProgressBar progressBar, final TextViewHozo tvAss, final TextViewHozo textViewHozo, final int position, final boolean addPrepay) {
        ProgressDialogUtils.showProgressDialog(context);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_ACCEPTED_OFFER_USER_ID, bidderID);
            if (addPrepay) jsonRequest.put(Constants.PARAMETER_ADD_PREPAY, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "acceptOffer jsonRequest : " + jsonRequest.toString());
        LogUtils.d(TAG, "acceptOffer jsonRequest task Id : " + taskID);

        ApiClient.getApiService().acceptOffer(UserManager.getUserToken(), taskID, body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "acceptOffer code : " + response.code());
                LogUtils.d(TAG, "acceptOffer body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    assignerCount = response.body().getAssigneeCount();
                    Utils.showLongToast(context, context.getString(R.string.assiger_done), false, false);
                    textViewHozo.setText(String.valueOf(assignerCount));
                    progressBar.setProgress(assignerCount);
                    tvAss.setEnabled(false);
                    tvAss.setText(context.getString(R.string.assigned_done));
                    Utils.setViewBackground(tvAss, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
                    bidResponses.get(position).setAccept(true);
                    if (assignerCount == workerCount || isCheckDone())
                        ((BaseActivity) context).finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(context, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAcceptOffer(bidderID, progressBar, textViewHozo, tvAss, position, addPrepay);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(context);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    switch (error.status()) {
                        case Constants.NOT_ENOUGH_PREPAID:
                            LogUtils.d(TAG, "error response" + error.toString());
                            DialogUtils.showOkAndCancelDialog(context, context.getString(R.string.notification), context.getString(R.string.not_enough_prepay, Utils.formatNumber(Integer.parseInt(error.message()))), context.getString(R.string.cancel_task_ok), context.getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                @Override
                                public void onSubmit() {
                                    doAcceptOffer(bidderID, progressBar, textViewHozo, tvAss, position, true);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            break;
                        case Constants.NOT_ENOUGH_BALANCE:
                            DialogUtils.showOkAndCancelDialog(context, context.getString(R.string.notification), context.getString(R.string.not_enough_balance), context.getString(R.string.cancel_task_ok), context.getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                @Override
                                public void onSubmit() {
                                    Intent intentWallet = new Intent(context, MyWalletActivity.class);
                                    ((BaseActivity) context).startActivity(intentWallet, TransitionScreen.RIGHT_TO_LEFT);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                            break;
                        default:
                            DialogUtils.showOkDialog(context, context.getString(R.string.error), error.message(), context.getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                            break;

                    }
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.d(TAG, "onFailure" + t.getMessage());
                DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doAcceptOffer(bidderID, progressBar, textViewHozo, tvAss, position, addPrepay);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    private boolean isCheckDone() {
        for (BidResponse response : bidResponses) {
            if (!response.isAccept())
                return false;
        }
        return true;
    }

    private String formatTitle(int pos) {
        if (pos < 10) return "0" + pos;
        else return String.valueOf(pos);
    }


}