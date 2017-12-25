package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.CircleImageView;
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
    private final int bidderCount;
    private final int assignerCount;
    private final int assigned;


    public ViewPageAssignAdapter(Context context, int taskID, int bidderCount, int assignerCount, int assigned) {
        this.context = context;
        this.taskID = taskID;
        this.bidderCount = bidderCount;
        this.assignerCount = assignerCount;
        this.assigned = assigned;
    }

    @Override
    public int getCount() {
        return assigned;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final TextViewHozo tvAssigner, tvAssignerCount, tvBidderCount, tvName, tvDes;
        final TextViewHozo tvRatingCount, tvBidsCount, tvComplex;
        final TextViewHozo tv5star, tv4star, tv3star, tv2star, tv1star;
        final TextViewHozo tvPrice, tvDesMsg, tvShowReviews, tvMoreReviews;
        final ProgressBar assignProgress;
        final ReviewsListView reviewsListView;
        final RatingBar rbRating, rbRating5, rbRating4, rbRating3, rbRating2, rbRating1;
        final CircleImageView imgAvatarAssign, imgAvatarDes;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View itemView = inflater.inflate(R.layout.viewpager_assign_item, container,
                false);
        tvAssigner = (TextViewHozo) itemView.findViewById(R.id.tv_assigner);
        tvAssignerCount = (TextViewHozo) itemView.findViewById(R.id.tv_assigner_count);
        tvBidderCount = (TextViewHozo) itemView.findViewById(R.id.tv_bidder_count);
        tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
        tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_des);
        tvRatingCount = (TextViewHozo) itemView.findViewById(R.id.tv_rating_count);
        tvBidsCount = (TextViewHozo) itemView.findViewById(R.id.tv_bids_count);
        tvComplex = (TextViewHozo) itemView.findViewById(R.id.tv_complex);
        tv5star = (TextViewHozo) itemView.findViewById(R.id.tv_5star);
        tv4star = (TextViewHozo) itemView.findViewById(R.id.tv_4star);
        tv3star = (TextViewHozo) itemView.findViewById(R.id.tv_3star);
        tv2star = (TextViewHozo) itemView.findViewById(R.id.tv_2star);
        tv1star = (TextViewHozo) itemView.findViewById(R.id.tv_1star);
        tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
        tvDesMsg = (TextViewHozo) itemView.findViewById(R.id.tv_des_msg);
        tvShowReviews = (TextViewHozo) itemView.findViewById(R.id.tv_show_reviews);
        tvMoreReviews = (TextViewHozo) itemView.findViewById(R.id.tv_more_reviews);
        assignProgress = (ProgressBar) itemView.findViewById(R.id.assign_progress);
        reviewsListView = (ReviewsListView) itemView.findViewById(R.id.rcv_reviews);
        rbRating = (RatingBar) itemView.findViewById(R.id.rb_rating);
        rbRating5 = (RatingBar) itemView.findViewById(R.id.rb_rating5);
        rbRating4 = (RatingBar) itemView.findViewById(R.id.rb_rating4);
        rbRating3 = (RatingBar) itemView.findViewById(R.id.rb_rating3);
        rbRating2 = (RatingBar) itemView.findViewById(R.id.rb_rating2);
        rbRating1 = (RatingBar) itemView.findViewById(R.id.rb_rating1);

        imgAvatarAssign = (CircleImageView) itemView.findViewById(R.id.img_avatar_assign);
        imgAvatarDes = (CircleImageView) itemView.findViewById(R.id.avatar_des);
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    private String formatTitle(int pos) {
        if (pos < 10) return "0" + pos;
        else return String.valueOf(pos);
    }

//    private void doRate(final int position, final int userId, final float rb, final String reviews, final boolean confirm, final TextViewHozo tvHozo, final EdittextHozo edHozo, final RatingBar rbBar) {
//
//        if (rb == 0f) {
//            Utils.showLongToast(context, context.getString(R.string.rate_msg_no_content_error), true, false);
//            return;
//        }
//        ProgressDialogUtils.showProgressDialog(context);
//        final JSONObject jsonRequest = new JSONObject();
//        try {
//            jsonRequest.put("user_id", userId);
//            jsonRequest.put("body", reviews);
//            jsonRequest.put("rating", rb);
//            jsonRequest.put("confirm", confirm);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        LogUtils.d(TAG, "doRate data request : " + jsonRequest.toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
//        ApiClient.getApiService().rateTask(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<RateResponse>() {
//            @Override
//            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
//                APIError error = ErrorUtils.parseError(response);
//                LogUtils.d(TAG, "doRate code : " + response.code());
//                LogUtils.d(TAG, "doRate : " + error.status() + "sms" + error.message() + "task iD" + taskResponse.getId());
//                if (response.code() == Constants.HTTP_CODE_OK) {
//                    taskResponse.getAssignees().get(position).setRating(response.body().getRating());
//                    updateUI(true, tvHozo, edHozo, rbBar, response.body().getBody(), response.body().getRating());
//                    ratingListener.success();
//                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
//                    if (error.status().equals(Constants.INVALID_DATA)) {
//                        Utils.showLongToast(context, context.getString(R.string.rating_invalid_data), true, false);
//                    } else if (error.status().equals(Constants.NO_EXIST)) {
//                        Utils.showLongToast(context, context.getString(R.string.task_no_exist), true, false);
//                    } else if (error.status().equals(Constants.NO_PERMISSION)) {
//                        Utils.showLongToast(context, context.getString(R.string.rating_no_permission), true, false);
//                    } else if (error.status().equals(Constants.SYSTEM_ERROR)) {
//                        Utils.showLongToast(context, context.getString(R.string.rating_system_error), true, false);
//                    }
//                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
//                    NetworkUtils.refreshToken(context, new NetworkUtils.RefreshListener() {
//                        @Override
//                        public void onRefreshFinish() {
//                            doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
//                        }
//                    });
//                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
//                    Utils.blockUser(context);
//                } else {
//                    DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//                            doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
//                }
//                ProgressDialogUtils.dismissProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<RateResponse> call, Throwable t) {
//                DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
//                    @Override
//                    public void onSubmit() {
//                        doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//                ProgressDialogUtils.dismissProgressDialog();
//            }
//        });
//    }


}