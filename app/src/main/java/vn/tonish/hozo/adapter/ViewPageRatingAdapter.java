package vn.tonish.hozo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.RateResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables

        CircleImageView imgAvatar;
        TextViewHozo tvName, tvAge, tvTitle;
        final RatingBar ratingBar;
        final CheckBoxHozo ckDone;
        final EdittextHozo edtReviews;
        TextViewHozo btnSend;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_rating_item, container,
                false);
        imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
        tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
        tvAge = (TextViewHozo) itemView.findViewById(R.id.tv_age);
        tvTitle = (TextViewHozo) itemView.findViewById(R.id.tv_title);
        ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
        ckDone = (CheckBoxHozo) itemView.findViewById(R.id.ckeckbox_confirm);
        edtReviews = (EdittextHozo) itemView.findViewById(R.id.edt_reviews);
        btnSend = (TextViewHozo) itemView.findViewById(R.id.btn_Send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRate(taskResponse.getPoster().getId(), ratingBar.getRating(), edtReviews.getText().toString().trim(), ckDone.isChecked());
            }
        });

        // Capture position and set to the TextViews
        if (type.equals(Constants.ROLE_POSTER)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(position + "/" + taskResponse.getAssigneeCount());
            Utils.displayImageAvatar(context, imgAvatar, taskResponse.getAssignees().get(position).getAvatar());
            tvName.setText(taskResponse.getAssignees().get(position).getFullName());
            tvAge.setText(taskResponse.getAssignees().get(position).getAge() + " - " + taskResponse.getAssignees().get(position).getGender() + context.getString(R.string.create_task_age));
            ratingBar.setRating(taskResponse.getAssignees().get(position).getRating());
        } else {
            tvTitle.setVisibility(View.GONE);
            Utils.displayImageAvatar(context, imgAvatar, taskResponse.getPoster().getAvatar());
            tvName.setText(taskResponse.getPoster().getFullName());
            tvAge.setText(taskResponse.getPoster().getAge() + " - " + taskResponse.getPoster().getGender() + context.getString(R.string.create_task_age));
        }

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }


    private void doRate(final int userId, final float rb, final String reviews, final boolean confirm) {

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
                LogUtils.d(TAG, "doRate code : " + response.code());
                LogUtils.d(TAG, "doRate : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
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
                            doRate(userId, rb, reviews, confirm);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(context);
                } else {
                    DialogUtils.showRetryDialog(context, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doRate(userId, rb, reviews, confirm);
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
                        doRate(userId, rb, reviews, confirm);
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