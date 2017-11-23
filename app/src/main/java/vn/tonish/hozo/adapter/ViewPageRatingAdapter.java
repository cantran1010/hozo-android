package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
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
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.Poster;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.RadioButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 11/21/17.
 */

public class ViewPageRatingAdapter extends PagerAdapter {
    private static final String TAG = ViewPageRatingAdapter.class.getSimpleName();
    // Declare Variables
    private Context context;
    private TaskResponse taskResponse;
    private String type;
    private LayoutInflater inflater;
    private RatingListener ratingListener;

    public interface RatingListener {
        void onClick(int position);
    }

    public RatingListener getRatingListener() {
        return ratingListener;
    }

    public void setRatingListener(RatingListener ratingListener) {
        this.ratingListener = ratingListener;
    }

    public ViewPageRatingAdapter(Context context, TaskResponse taskResponse, String type) {
        this.context = context;
        this.taskResponse = taskResponse;
        this.type = type;

    }

    @Override
    public int getCount() {
        if (type.equals(Constants.ROLE_TASKER))
            return 1;
        else return taskResponse.getAssignees().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        // Declare Variables

        CircleImageView imgAvatar;
        TextViewHozo tvName, tvTitle;
        final RatingBar ratingBar;
        final RadioButtonHozo ckDone, ckNot;
        final RadioGroup radioGroup;
        final EdittextHozo edtReviews;
        final TextViewHozo btnSend;
        final int userID;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_rating_item, container,
                false);
        imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
        tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
        tvTitle = (TextViewHozo) itemView.findViewById(R.id.tv_title);
        ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
        ckDone = (RadioButtonHozo) itemView.findViewById(R.id.ckeckbox_confirm_yes);
        edtReviews = (EdittextHozo) itemView.findViewById(R.id.edt_reviews);
        btnSend = (TextViewHozo) itemView.findViewById(R.id.btn_Send);
        ratingBar.setStepSize(1.0f);
        // Capture position and set to the TextViews
        if (type.equals(Constants.ROLE_POSTER)) {
            Assigner assigner = taskResponse.getAssignees().get(position);
            tvTitle.setText(formatTitle(position + 1) + "/" + formatTitle(taskResponse.getAssigneeCount()));
            Utils.displayImageAvatar(context, imgAvatar, assigner.getAvatar());
            tvName.setText(assigner.getFullName());
            userID = assigner.getId();

            if (assigner.getRating() != 0) {
                updateUI(true, btnSend, edtReviews, ratingBar, "", (int) assigner.getRating());
            } else {
                updateUI(false, btnSend, edtReviews, ratingBar, "", (int) assigner.getRating());
            }
        } else {
            Poster poster = taskResponse.getPoster();
            tvTitle.setText("");
            Utils.displayImageAvatar(context, imgAvatar, poster.getAvatar());
            tvName.setText(poster.getFullName());
            userID = poster.getId();

            if (taskResponse.isRatePoster()) {
                updateUI(true, btnSend, edtReviews, ratingBar, "", (int) taskResponse.getPoster().getPosterAverageRating());
            } else {
                updateUI(false, btnSend, edtReviews, ratingBar, "", (int) taskResponse.getPoster().getPosterAverageRating());
            }

        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRate(position, userID, ratingBar.getRating(), edtReviews.getText().toString().trim(), ckDone.isChecked(), btnSend, edtReviews, ratingBar);
            }
        });
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

    private void doRate(final int position, final int userId, final float rb, final String reviews, final boolean confirm, final TextViewHozo tvHozo, final EdittextHozo edHozo, final RatingBar rbBar) {

        if (rb == 0f) {
            Utils.showLongToast(context, context.getString(R.string.rate_msg_no_content_error), true, false);
            return;
        }
        ProgressDialogUtils.showProgressDialog(context);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", userId);
            jsonRequest.put("body", reviews);
            jsonRequest.put("rating", rb);
            jsonRequest.put("confirm", confirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "doRate data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().rateTask(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                notifyDataSetChanged();

                APIError error = ErrorUtils.parseError(response);
                LogUtils.d(TAG, "doRate code : " + response.code());
                LogUtils.d(TAG, "doRate : " + error.status() + "sms" + error.message() + "task iD" + taskResponse.getId());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse.getAssignees().get(position).setRating(response.body().getRating());
                    updateUI(true, tvHozo, edHozo, rbBar, response.body().getBody(), response.body().getRating());

//                    if (context instanceof Activity) {
//                        ((Activity) context).finish();
//                    }

                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    if (error.status().equals(Constants.INVALID_DATA)) {
                        Utils.showLongToast(context, context.getString(R.string.rating_invalid_data), true, false);
                    } else if (error.status().equals(Constants.NO_EXIST)) {
                        Utils.showLongToast(context, context.getString(R.string.task_no_exist), true, false);
                    } else if (error.status().equals(Constants.NO_PERMISSION)) {
                        Utils.showLongToast(context, context.getString(R.string.rating_no_permission), true, false);
                    } else if (error.status().equals(Constants.SYSTEM_ERROR)) {
                        Utils.showLongToast(context, context.getString(R.string.rating_system_error), true, false);
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(context, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(context);
                } else {
                    DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
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
                DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doRate(position, userId, rb, reviews, confirm, tvHozo, edHozo, rbBar);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void updateUI(boolean isOk, TextViewHozo tv, EdittextHozo ed, RatingBar rb, String reviews, int value) {
        if (isOk) {
            tv.setEnabled(false);
            tv.setText(context.getString(R.string.send_done));
            Utils.setViewBackground(tv, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
            ed.setText(reviews);
            rb.setRating(value);
        } else {
            tv.setEnabled(true);
            tv.setText(context.getString(R.string.send));
            Utils.setViewBackground(tv, ContextCompat.getDrawable(context, R.drawable.btn_new_selector));
        }
    }


}