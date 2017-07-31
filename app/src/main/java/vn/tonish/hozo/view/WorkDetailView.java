package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import vn.tonish.hozo.activity.ProfileActivity;
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
import vn.tonish.hozo.utils.ProgressDialogUtils;
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
    private ButtonHozo btnOffer;
    private MyGridView myGridView;
    private TaskProgressView taskProgressView;
    private TaskResponse taskResponse;
    private ImageView imgFbVerify, imgEmailVerify;

    public interface WorkDetailViewRateListener {
        void onRate();
    }

    private WorkDetailViewRateListener workDetailViewRateListener;

    public WorkDetailViewRateListener getWorkDetailViewRateListener() {
        return workDetailViewRateListener;
    }

    public void setWorkDetailViewRateListener(WorkDetailViewRateListener workDetailViewRateListener) {
        this.workDetailViewRateListener = workDetailViewRateListener;
    }

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
        layoutInflater.inflate(R.layout.view_work_detail, this, true);

        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);

        imgFbVerify = findViewById(R.id.fb_verify);
        imgEmailVerify = findViewById(R.id.email_verify);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
//        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvDescription = (TextViewHozo) findViewById(R.id.tv_description);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);

        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);

        btnOffer = (ButtonHozo) findViewById(R.id.btn_offer);
        btnOffer.setOnClickListener(this);

        tvStatus = (TextViewHozo) findViewById(R.id.tv_status);

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

        if (taskResponse.getPoster().getFacebookId() != null && !taskResponse.getPoster().getFacebookId().trim().equals(""))
            imgFbVerify.setImageResource(R.drawable.fb_on);
        else imgFbVerify.setImageResource(R.drawable.fb_off);

        if (taskResponse.getPoster().isEmailActive())
            imgEmailVerify.setImageResource(R.drawable.email_on);
        else imgEmailVerify.setImageResource(R.drawable.email_off);

        if (CategoryManager.getCategoryById(taskResponse.getCategoryId()) != null)
            tvTimeAgo.setText(getContext().getString(R.string.detail_task_time_ago, DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), getContext()), CategoryManager.getCategoryById(taskResponse.getCategoryId()).getName()));
        else tvTimeAgo.setText("");

        tvDescription.setText(taskResponse.getDescription());

        taskProgressView.updateData(taskResponse.getBidderCount(), (taskResponse.getWorkerCount() - taskResponse.getAssigneeCount()), taskResponse.getAssigneeCount());
        tvPrice.setText(getContext().getString(R.string.my_task_price, Utils.formatNumber(taskResponse.getWorkerRate())));
        if (taskResponse.getStartTime() != null)
            tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
        else tvDate.setText("");

        if (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
            tvTime.setText(getContext().getString(R.string.task_detail_time, DateTimeUtils.getHourMinuteFromIso(taskResponse.getStartTime()), DateTimeUtils.getHourMinuteFromIso(taskResponse.getEndTime())));
        else tvTime.setText("");

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

    public void updateBtnOffer(String status) {

        switch (status) {
            case Constants.OFFER_ACTIVE:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getContext().getString(R.string.work_detail_view_bit));
                btnOffer.setClickable(true);
                Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getContext(), R.drawable.btn_selector));
                btnOffer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doOffer();
                    }
                });
                break;

            case Constants.OFFER_PENDING:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getContext().getString(R.string.work_detail_view_bit_pending));
                btnOffer.setClickable(false);
                Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getContext(), R.drawable.btn_press));
                break;

            case Constants.OFFER_CALL:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getContext().getString(R.string.call));
                btnOffer.setClickable(true);
                Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getContext(), R.drawable.btn_selector));
                btnOffer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.call(getContext(), taskResponse.getPoster().getPhone());
                    }
                });
                break;

            case Constants.OFFER_GONE:
                btnOffer.setVisibility(View.GONE);
                btnOffer.setClickable(false);
                break;

            case Constants.OFFER_RATTING:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getContext().getString(R.string.rate));
                btnOffer.setClickable(true);
                btnOffer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (workDetailViewRateListener != null)
                            workDetailViewRateListener.onRate();
                    }
                });
                break;

            default:
                btnOffer.setVisibility(View.GONE);
                btnOffer.setClickable(false);
                break;

        }

//        if
//                (isShow) btnOffer.setVisibility(View.VISIBLE);
//        else
//            btnOffer.setVisibility(View.GONE);
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

//    public void updateTaskProgressViewVisibility(boolean isVisibility) {
//        if (isVisibility) {
//            vTaskProgressView.setVisibility(View.VISIBLE);
//            taskProgressView.setVisibility(View.VISIBLE);
//        } else {
//            vTaskProgressView.setVisibility(View.GONE);
//            taskProgressView.setVisibility(View.GONE);
//        }
//
//    }

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
        ProgressDialogUtils.showProgressDialog(getContext());
        ApiClient.getApiService().bidsTask(UserManager.getUserToken(), taskResponse.getId()).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, final Response<TaskResponse> response) {
                LogUtils.d(TAG, "bidsTask status code : " + response.code());
                LogUtils.d(TAG, "bidsTask body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    final BidSuccessDialog bidSuccessDialog = new BidSuccessDialog(getContext());
                    bidSuccessDialog.showView();

                    btnOffer.setText(getContext().getString(R.string.work_detail_view_bit_pending));
                    btnOffer.setClickable(false);
                    Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getContext(), R.drawable.btn_press));

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
                    if (error.status().equals(Constants.BID_ERROR_SAME_TIME)) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.offer_error_time), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.BID_ERROR_INVALID_DATA)) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.offer_invalid_data), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.offer_system_error), error.message(), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doOffer();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getContext());
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
                ProgressDialogUtils.dismissProgressDialog();
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
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }
}
