package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BlockTaskActivity;
import vn.tonish.hozo.activity.PreviewImageListActivity;
import vn.tonish.hozo.activity.ProfileActivity;
import vn.tonish.hozo.activity.RateActivity;
import vn.tonish.hozo.activity.TaskDetailNewActivity;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.common.Constants;
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
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 8/22/17.
 */

public class TaskDetailTab1Fragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = TaskDetailTab1Fragment.class.getSimpleName();
    private TaskResponse taskResponse;
    private CircleImageView imgAvatar;
    private RatingBar rbRate;
    private TextViewHozo tvStatus, tvTimeAgo;
    private TextViewHozo tvName, tvTitle, tvAddress, tvDate, tvTime, tvHour, tvDes, tvAge, tvSex, tvBudget, tvWorkerCount, tvAssignerCount, tvEmptyCount, tvSeeMore, tvSeeMoreHide;
    private LinearLayout layoutMore;
    private ButtonHozo btnOffer;
    private Call<TaskResponse> call;
    private MyGridView myGridView;

    @Override
    protected int getLayout() {
        return R.layout.task_detail_tab1_layout;
    }

    @Override
    protected void initView() {
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);

        imgAvatar.setOnClickListener(this);
        rbRate.setOnClickListener(this);
        tvName.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);

        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvHour = (TextViewHozo) findViewById(R.id.tv_hour);

        tvDes = (TextViewHozo) findViewById(R.id.tv_des);
        tvAge = (TextViewHozo) findViewById(R.id.tv_age);
        tvSex = (TextViewHozo) findViewById(R.id.tv_sex);

        tvStatus = (TextViewHozo) findViewById(R.id.tv_status);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);

        tvBudget = (TextViewHozo) findViewById(R.id.tv_budget);
        tvWorkerCount = (TextViewHozo) findViewById(R.id.tv_worker_count);
        tvAssignerCount = (TextViewHozo) findViewById(R.id.tv_assigner_count);
        tvEmptyCount = (TextViewHozo) findViewById(R.id.tv_empty_count);

        tvSeeMore = (TextViewHozo) findViewById(R.id.tv_see_more);
        tvSeeMore.setOnClickListener(this);

        tvSeeMoreHide = (TextViewHozo) findViewById(R.id.tv_see_more_hide);
        tvSeeMoreHide.setOnClickListener(this);

        layoutMore = (LinearLayout) findViewById(R.id.layout_more);

        btnOffer = (ButtonHozo) findViewById(R.id.btn_bid);

        myGridView = (MyGridView) findViewById(R.id.gr_image);
    }

    @Override
    protected void initData() {
        taskResponse = ((TaskDetailNewActivity) getActivity()).getTaskResponse();
        LogUtils.d(TAG, "TaskDetailTab1Fragment , taskResponse : " + taskResponse);
        updateUi();
    }

    @Override
    protected void resumeData() {

    }

    private void updateUi() {
        Utils.displayImageAvatar(getActivity(), imgAvatar, taskResponse.getPoster().getAvatar());
        tvName.setText(taskResponse.getPoster().getFullName());
        rbRate.setRating(taskResponse.getPoster().getPosterAverageRating());

        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), getActivity()));
        updateStatusTask();

        tvTitle.setText(taskResponse.getTitle());

        if (taskResponse.isOnline())
            tvAddress.setText(getString(R.string.online_task_address));
        else
            tvAddress.setText(taskResponse.getAddress());

        if (taskResponse.getStartTime() != null)
            tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
        else tvDate.setText("");

        if (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
            tvTime.setText(DateTimeUtils.getOnlyHourFromIso(taskResponse.getStartTime()));
        else tvTime.setText("");

        try {
            tvHour.setText(String.valueOf(DateTimeUtils.hoursBetween(DateTimeUtils.toCalendar(taskResponse.getStartTime()), DateTimeUtils.toCalendar(taskResponse.getEndTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvDes.setText(taskResponse.getDescription());
        tvBudget.setText(getString(R.string.my_task_price, Utils.formatNumber(taskResponse.getWorkerRate())));

        tvWorkerCount.setText(String.valueOf(taskResponse.getWorkerCount()));
        tvAssignerCount.setText(String.valueOf(taskResponse.getAssigneeCount()));
        tvEmptyCount.setText(String.valueOf(taskResponse.getWorkerCount() - taskResponse.getAssigneeCount()));

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
            myGridView.setVisibility(View.VISIBLE);
        } else {
            myGridView.setVisibility(View.GONE);
        }

        tvAge.setText(getString(R.string.post_a_task_age, taskResponse.getMinAge(), taskResponse.getMaxAge()));

        if (taskResponse.getGender().equals(Constants.GENDER_MALE)) {
            tvSex.setText(getString(R.string.gender_male_vn));
        } else if (taskResponse.getGender().equals(Constants.GENDER_FEMALE)) {
            tvSex.setText(getString(R.string.gender_female_vn));
        } else {
            tvSex.setText(getString(R.string.gender_non_vn));
        }

        if (taskResponse.isAdvance())
            tvSeeMore.setVisibility(View.VISIBLE);
        else
            tvSeeMore.setVisibility(View.GONE);

    }

    private void updateStatusTask() {

        //poster
        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.update_task), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_recruitment));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.delivered), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_received));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_done));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        }

        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            updateStatus(true, getString(R.string.my_task_status_worker_canceled), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            updateStatus(true, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_missed));
            updateBtnOffer(Constants.OFFER_GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
            updateStatus(false, getString(R.string.recruitment), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_recruitment));
            updateBtnOffer(Constants.OFFER_PENDING);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            updateStatus(true, getString(R.string.received), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_received));
            updateBtnOffer(Constants.OFFER_CALL);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_done));
            updateBtnOffer(Constants.OFFER_RATTING);
        }
        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            updateStatus(false, "", ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_done));
            updateBtnOffer(Constants.OFFER_ACTIVE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() != UserManager.getMyUser().getId()) {
            updateStatus(true, getString(R.string.delivered), ContextCompat.getDrawable(getActivity(), R.drawable.bg_border_received));
            updateBtnOffer(Constants.OFFER_GONE);
        }
    }

    public void updateBtnOffer(String status) {

        switch (status) {
            case Constants.OFFER_ACTIVE:
                btnOffer.setVisibility(View.VISIBLE);

                if (taskResponse.getBidderCount() >= 3 * taskResponse.getWorkerCount()) {
                    btnOffer.setText(getString(R.string.bid_limited));
                    btnOffer.setClickable(false);
                    Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getActivity(), R.drawable.btn_press));
                } else {
                    btnOffer.setText(getString(R.string.work_detail_view_bit));
                    btnOffer.setClickable(true);
                    Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getActivity(), R.drawable.btn_selector));
                    btnOffer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doOffer();
                        }
                    });
                }

                break;

            case Constants.OFFER_PENDING:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getString(R.string.work_detail_view_bit_pending));
                btnOffer.setClickable(false);
                Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getActivity(), R.drawable.btn_press));
                break;

            case Constants.OFFER_CALL:
                btnOffer.setVisibility(View.VISIBLE);
                btnOffer.setText(getString(R.string.call));
                btnOffer.setClickable(true);
                Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getActivity(), R.drawable.btn_selector));
                btnOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.call(getActivity(), taskResponse.getPoster().getPhone());
                    }
                });
                break;

            case Constants.OFFER_GONE:
                btnOffer.setVisibility(View.GONE);
                btnOffer.setClickable(false);
                break;

            case Constants.OFFER_RATTING:
                if (taskResponse.isRatePoster()) {
                    btnOffer.setVisibility(View.GONE);
                    btnOffer.setClickable(false);
                } else {
                    btnOffer.setVisibility(View.VISIBLE);
                    btnOffer.setText(getString(R.string.rate));
                    btnOffer.setClickable(true);
                    btnOffer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doRate();
                        }
                    });
                }
                break;

            default:
                btnOffer.setVisibility(View.GONE);
                btnOffer.setClickable(false);
                break;

        }
    }

    private void doRate() {
        Intent intent = new Intent(getActivity(), RateActivity.class);
        intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
        intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
        intent.putExtra(Constants.AVATAR_EXTRA, taskResponse.getPoster().getAvatar());
        intent.putExtra(Constants.NAME_EXTRA, taskResponse.getPoster().getFullName());
        startActivityForResult(intent, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_RATE && resultCode == Constants.RESPONSE_CODE_RATE) {
            getData();
        }
    }

    private void getData() {
        call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskResponse.getId());
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    Utils.updateRole(taskResponse);
                    updateUi();
                    ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    if (error.status().equals(Constants.TASK_DETAIL_INPUT_REQUIRE) || error.status().equals(Constants.TASK_DETAIL_NO_EXIT)) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.task_detail_no_exit), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.TASK_DETAIL_BLOCK)) {
                        Intent intent = new Intent(getActivity(), BlockTaskActivity.class);
                        intent.putExtra(Constants.TITLE_INFO_EXTRA, getString(R.string.task_detail_block));
                        intent.putExtra(Constants.CONTENT_INFO_EXTRA, getString(R.string.task_detail_block_reasons) + " " + error.message());
                        startActivity(intent, TransitionScreen.FADE_IN);
                    } else {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getData();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getData();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

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

    private void doOffer() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        ApiClient.getApiService().bidsTask(UserManager.getUserToken(), taskResponse.getId()).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, final Response<TaskResponse> response) {
                LogUtils.d(TAG, "bidsTask status code : " + response.code());
                LogUtils.d(TAG, "bidsTask body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    final BidSuccessDialog bidSuccessDialog = new BidSuccessDialog(getActivity());
                    bidSuccessDialog.showView();

                    btnOffer.setText(getString(R.string.work_detail_view_bit_pending));
                    btnOffer.setClickable(false);
                    Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(getActivity(), R.drawable.btn_press));

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            taskResponse = response.body();
                            Utils.updateRole(taskResponse);
                            bidSuccessDialog.hideView();
                            updateUi();
                            Utils.updateRole(taskResponse);
                            ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
                            ((TaskDetailNewActivity) getActivity()).showMenu();
                        }
                    }, 1000);

                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    if (error.status().equals(Constants.BID_ERROR_SAME_TIME)) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.error), getString(R.string.offer_error_time), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.BID_ERROR_INVALID_DATA)) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.error), getString(R.string.offer_invalid_data), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doOffer();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_see_more:
                tvSeeMore.setVisibility(View.GONE);
                layoutMore.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_see_more_hide:
                tvSeeMore.setVisibility(View.VISIBLE);
                layoutMore.setVisibility(View.GONE);
                break;

            case R.id.img_avatar:
            case R.id.rb_rate:
            case R.id.tv_name:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, taskResponse.getPoster().getId());
                intent.putExtra(Constants.IS_MY_USER, taskResponse.getPoster().getId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;

        }
    }

}
