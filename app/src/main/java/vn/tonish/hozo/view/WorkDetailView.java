package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PreviewImageListActivity;
import vn.tonish.hozo.activity.RateActivity;
import vn.tonish.hozo.activity.other.ProfileActivity;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.BidSuccessDialog;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class WorkDetailView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;

    private TextViewHozo tvName, tvTitle, tvTimeAgo, tvDescription, tvImageAttachTitle;
    private RatingBar rbRate;
    private TextViewHozo tvPrice, tvDate, tvTime, tvAddress, tvStatus;
    private ButtonHozo btnOffer, btnCallRate;
    private MyGridView myGridView;
    private TaskProgressView taskProgressView;
    private TaskResponse taskResponse;
    private View vTaskProgressView;

    public interface WorkDetailViewListener {
        void onWorkDetailViewListener(TaskResponse taskResponse);
    }

    private WorkDetailViewListener workDetailViewListener;

    public WorkDetailViewListener getWorkDetailViewListener() {
        return workDetailViewListener;
    }

    public void setWorkDetailViewListener(WorkDetailViewListener workDetailViewListener) {
        this.workDetailViewListener = workDetailViewListener;
    }

    public WorkDetailView(Context context) {
        super(context);
        init();
    }

    public WorkDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WorkDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.work_detail_view, this, true);

        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
//        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvDescription = (TextViewHozo) findViewById(R.id.tv_description);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);

        ImageView imgMobile = (ImageView) findViewById(R.id.img_mobile_verify);
        ImageView imgEmail = (ImageView) findViewById(R.id.img_email_verify);
        ImageView imgFacebook = (ImageView) findViewById(R.id.img_facebook_verify);

        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);

        btnOffer = (ButtonHozo) findViewById(R.id.btn_offer);
        btnOffer.setOnClickListener(this);

        tvStatus = (TextViewHozo) findViewById(R.id.tv_status);

        btnCallRate = (ButtonHozo) findViewById(R.id.btn_call_rate);

        myGridView = (MyGridView) findViewById(R.id.gr_image);

        tvImageAttachTitle = (TextViewHozo) findViewById(R.id.tv_img_attach_title);

        taskProgressView = (TaskProgressView) findViewById(R.id.task_progress_view);
        View vTaskProgressView = findViewById(R.id.v_task_progress_view);

    }

    public void updateWork(TaskResponse taskResponse) {
        this.taskResponse = taskResponse;

        LogUtils.d(TAG, "updateWork , taskResponse : " + taskResponse.toString());

        Utils.displayImageAvatar(getContext(), imgAvatar, taskResponse.getPoster().getAvatar());
        tvName.setText(taskResponse.getPoster().getFullName());
        rbRate.setRating(taskResponse.getPoster().getPosterAverageRating());
        tvTitle.setText(taskResponse.getTitle());
        tvTime.setText(taskResponse.getTitle());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), getContext())
                + " . " + getContext().getString(R.string.task_detail_category_type) + " " + CategoryManager.getCategoryById(taskResponse.getCategoryId()).getName());
//        tvWorkType.setText(getContext().getString(R.string.task_detail_category_type) + " " + CategoryManager.getCategoryById(taskResponse.getCategoryId()).getName());
        tvDescription.setText(taskResponse.getDescription());

        taskProgressView.updateData(taskResponse.getBidderCount(), (taskResponse.getWorkerCount() - taskResponse.getAssigneeCount()), taskResponse.getAssigneeCount());

        tvPrice.setText(Utils.formatNumber(taskResponse.getWorkerCount() * taskResponse.getWorkerRate()) + " VND");
        tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
        tvTime.setText(DateTimeUtils.getHourMinuteFromIso(taskResponse.getStartTime()) + " " + getContext().getString(R.string.detail_task_time_to) + " " + DateTimeUtils.getHourMinuteFromIso(taskResponse.getEndTime()));
        tvAddress.setText(taskResponse.getAddress());

        final ArrayList<String> attachments = (ArrayList<String>) taskResponse.getAttachments();
        LogUtils.d(TAG, "updateWork , attachments : " + attachments.toString());

        if (attachments.size() > 0) {
            ImageDetailTaskAdapter imageDetailTaskAdapter = new ImageDetailTaskAdapter(getContext(), attachments);
            myGridView.setAdapter(imageDetailTaskAdapter);

            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), PreviewImageListActivity.class);
                    intent.putStringArrayListExtra(Constants.IMAGE_ATTACHS_EXTRA, attachments);
                    intent.putExtra(Constants.IMAGE_POSITITON_EXTRA, position);
                    getContext().startActivity(intent);
                }
            });
            imageDetailTaskAdapter.notifyDataSetChanged();
            tvImageAttachTitle.setVisibility(View.VISIBLE);
            myGridView.setVisibility(View.VISIBLE);
        } else {
            tvImageAttachTitle.setVisibility(View.GONE);
            myGridView.setVisibility(View.GONE);
        }

    }

    public void updateBtnOffer(boolean isShow) {
        if
                (isShow) btnOffer.setVisibility(View.VISIBLE);
        else
            btnOffer.setVisibility(View.GONE);
    }

    public void updateStatus(boolean isShow, String status, Drawable drawable) {
        if (isShow) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(status);
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                tvStatus.setBackgroundDrawable(drawable);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvStatus.setBackground(drawable);
            }
        } else tvStatus.setVisibility(View.GONE);
    }

    public void updateTaskProgressViewVisibility(boolean isVisibility) {
        if (isVisibility) {
            vTaskProgressView.setVisibility(View.VISIBLE);
            taskProgressView.setVisibility(View.VISIBLE);
        } else {
            vTaskProgressView.setVisibility(View.GONE);
            taskProgressView.setVisibility(View.GONE);
        }

    }

    public void updateBtnCallRate(boolean isShow, boolean isCall, String text) {
        if (isShow) {
            btnCallRate.setVisibility(View.VISIBLE);
            btnCallRate.setText(text);
            if (isCall) {
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.call(getContext(), taskResponse.getPoster().getPhone());
                    }
                });
            } else {
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), RateActivity.class);
                        intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                        intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
                        getContext().startActivity(intent);
                    }
                });
            }
        } else {
            btnCallRate.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_offer:
                doOffer();
                break;

            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, taskResponse.getPoster().getId());
                intent.putExtra(Constants.IS_MY_USER, taskResponse.getPoster().getId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;

        }
    }

    private void doOffer() {
        ApiClient.getApiService().bidsTask(UserManager.getUserToken(), taskResponse.getId()).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, final Response<TaskResponse> response) {
                LogUtils.d(TAG, "bidsTask status code : " + response.code());
                LogUtils.d(TAG, "bidsTask body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    final BidSuccessDialog bidSuccessDialog = new BidSuccessDialog(getContext());
                    bidSuccessDialog.showView();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (workDetailViewListener != null)
                                workDetailViewListener.onWorkDetailViewListener(response.body());
                            bidSuccessDialog.hideView();
//                            btnOffer.setVisibility(View.GONE);
                        }
                    }, 1000);

                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), error.message(), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doOffer();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doOffer();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doOffer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }
}
