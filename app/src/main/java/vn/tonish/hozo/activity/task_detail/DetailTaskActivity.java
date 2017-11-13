package vn.tonish.hozo.activity.task_detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.BlockTaskActivity;
import vn.tonish.hozo.activity.ChatActivity;
import vn.tonish.hozo.activity.CommentsNewActivity;
import vn.tonish.hozo.activity.CreateTaskActivity;
import vn.tonish.hozo.activity.RateActivity;
import vn.tonish.hozo.activity.image.PreviewImageListActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.adapter.AssignerAdapter;
import vn.tonish.hozo.adapter.CommentTaskAdapter;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
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
 * Created by LongBui on 11/11/17.
 */

public class DetailTaskActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DetailTaskActivity.class.getSimpleName();
    private TaskResponse taskResponse;
    private CircleImageView imgAvatar;
    private RatingBar rbRate;
    private TextViewHozo tvStatus, tvTimeAgo, tvMap, tvSeeMoreDetail, tvSeeMoreFooter;
    private TextViewHozo tvName, tvTitle, tvAddress, tvDate, tvTime, tvHour, tvDes, tvAge, tvSex, tvBudget, tvWorkerCount, tvAssignerCount, tvEmptyCount, tvAgeLbl, tvSexLbl;
    private ButtonHozo btnOffer, btnContact, btnRatePoster;
    private Call<TaskResponse> call;
    private MyGridView myGridView;
    private int taskId = 0;
    private ImageView imgMenu;
    private LinearLayout moreDetailLayout, moreFooterLayout;
    private PopupMenu popup;

    private RecyclerView rcvBidder;
    private ArrayList<Bidder> bidders = new ArrayList<>();

    private RecyclerView rcvAssign;
    private ArrayList<Assigner> assigners = new ArrayList<>();

    private TextViewHozo tvSeeMoreBidders, tvSeeMoreAssigners;
    private TextViewHozo tvBidderCount, tvAssignCount, tvNoBidder;

    private String bidderType = "";
    private String assigerType = "";
    private static final int MAX_WORKER = 5;

    // comment
    private RecyclerView rcvComment;
    private ArrayList<Comment> mComments = new ArrayList<>();
    private CommentTaskAdapter commentsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.detail_task_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);

        tvSeeMoreDetail = (TextViewHozo) findViewById(R.id.tv_see_more_detail);
        tvSeeMoreDetail.setOnClickListener(this);

        moreDetailLayout = (LinearLayout) findViewById(R.id.more_detail_layout);

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

        btnOffer = (ButtonHozo) findViewById(R.id.btn_bid);
        btnOffer.setOnClickListener(this);

        myGridView = (MyGridView) findViewById(R.id.gr_image);

        tvAgeLbl = (TextViewHozo) findViewById(R.id.tv_age_lbl);
        tvSexLbl = (TextViewHozo) findViewById(R.id.tv_sex_lbl);

        tvMap = (TextViewHozo) findViewById(R.id.tv_map);
        tvMap.setOnClickListener(this);

        rcvBidder = (RecyclerView) findViewById(R.id.rcv_bidders);
        tvBidderCount = (TextViewHozo) findViewById(R.id.tv_bidder_count);
        tvSeeMoreBidders = (TextViewHozo) findViewById(R.id.tv_see_more_bidders);
        tvSeeMoreBidders.setOnClickListener(this);

        rcvAssign = (RecyclerView) findViewById(R.id.rcv_assign);
        tvAssignCount = (TextViewHozo) findViewById(R.id.tv_assign_count);
        tvSeeMoreAssigners = (TextViewHozo) findViewById(R.id.tv_see_more_assigns);
        tvSeeMoreAssigners.setOnClickListener(this);

        rcvComment = (RecyclerView) findViewById(R.id.rcv_comment);

        btnContact = (ButtonHozo) findViewById(R.id.btn_contact);
        btnContact.setOnClickListener(this);

        btnRatePoster = (ButtonHozo) findViewById(R.id.btn_rate);
        btnRatePoster.setOnClickListener(this);

        tvSeeMoreFooter = (TextViewHozo) findViewById(R.id.tv_see_more_detail_footer);
        tvSeeMoreFooter.setOnClickListener(this);

        moreFooterLayout = (LinearLayout) findViewById(R.id.more_footer_layout);
    }

    @Override
    protected void initData() {
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        getData();
    }

    @Override
    protected void resumeData() {
        registerReceiver(broadcastReceiver, new IntentFilter("MyBroadcast"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        ProgressDialogUtils.showProgressDialog(this);
        LogUtils.d(TAG, "getDetailTask , taskId : " + taskId);
        LogUtils.d(TAG, "getDetailTask , UserManager.getUserToken() : " + UserManager.getUserToken());

        call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskId);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    imgMenu.setVisibility(View.VISIBLE);
                    taskResponse = response.body();
                    Utils.updateRole(taskResponse);
                    updateUi();
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "getDetailTask errorBody" + error.toString());
                    if (error.status().equals(Constants.TASK_DETAIL_INPUT_REQUIRE) || error.status().equals(Constants.TASK_DETAIL_NO_EXIT)) {
                        DialogUtils.showOkDialog(DetailTaskActivity.this, getString(R.string.task_detail_no_exit), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.TASK_DETAIL_BLOCK)) {
                        finish();
                        Intent intent = new Intent(DetailTaskActivity.this, BlockTaskActivity.class);
                        intent.putExtra(Constants.TITLE_INFO_EXTRA, getString(R.string.task_detail_block));
                        intent.putExtra(Constants.CONTENT_INFO_EXTRA, getString(R.string.task_detail_block_reasons) + " " + error.message());
                        startActivity(intent, TransitionScreen.FADE_IN);
                    } else {
                        DialogUtils.showOkDialog(DetailTaskActivity.this, getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getData();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
//                onStopRefresh();
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getData();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
//                onStopRefresh();
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

    private void updateUi() {

        updateByStatus();

        Utils.displayImageAvatar(this, imgAvatar, taskResponse.getPoster().getAvatar());
        tvName.setText(taskResponse.getPoster().getFullName());
        rbRate.setRating(taskResponse.getPoster().getPosterAverageRating());

        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), this));

        tvTitle.setText(taskResponse.getTitle());

        if (taskResponse.isOnline())
            tvAddress.setText(getString(R.string.online_task_address));
        else {
            if (taskResponse.getAddress().endsWith(Constants.VN1))
                tvAddress.setText(taskResponse.getAddress().replace(Constants.VN1, ""));
            else if (taskResponse.getAddress().endsWith(Constants.VN2))
                tvAddress.setText(taskResponse.getAddress().replace(Constants.VN2, ""));
            else
                tvAddress.setText(taskResponse.getAddress());
        }


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

        if (taskResponse.getDescription().trim().equals(""))
            tvDes.setVisibility(View.GONE);
        else {
            tvDes.setVisibility(View.VISIBLE);
            tvDes.setText(taskResponse.getDescription());
        }

        tvBudget.setText(getString(R.string.my_task_price, Utils.formatNumber(taskResponse.getWorkerRate())));

        tvWorkerCount.setText(String.valueOf(taskResponse.getWorkerCount()));
        tvAssignerCount.setText(String.valueOf(taskResponse.getAssigneeCount()));
        tvEmptyCount.setText(String.valueOf(taskResponse.getWorkerCount() - taskResponse.getAssigneeCount()));

        final ArrayList<String> attachments = (ArrayList<String>) taskResponse.getAttachments();
        LogUtils.d(TAG, "updateWork , attachments : " + attachments.toString());

        if (attachments.size() > 0) {
            ImageDetailTaskAdapter imageDetailTaskAdapter = new ImageDetailTaskAdapter(this, attachments);
            myGridView.setAdapter(imageDetailTaskAdapter);

            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(DetailTaskActivity.this, PreviewImageListActivity.class);
                    intent.putStringArrayListExtra(Constants.IMAGE_ATTACHS_EXTRA, attachments);
                    intent.putExtra(Constants.IMAGE_POSITITON_EXTRA, position);
                    startActivity(intent);
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

        tvBidderCount.setText(getString(R.string.bidder_count_lbl, taskResponse.getBidderCount()));
        tvAssignCount.setText(getString(R.string.bidder_count_lbl, taskResponse.getAssigneeCount()));

        // update bidder list
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        refreshBidderList(bidderType);

        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        refreshAssignerList(assigerType);

        refreshComment();

    }

    private void refreshBidderList(String bidderType) {
        PosterOpenAdapter posterOpenAdapter;
        if (bidders.size() > MAX_WORKER) {

            ArrayList<Bidder> biddersNew = new ArrayList<Bidder>();
            biddersNew.addAll(bidders.subList(0, MAX_WORKER));

            posterOpenAdapter = new PosterOpenAdapter(biddersNew, bidderType);
        } else {
            tvSeeMoreBidders.setVisibility(View.GONE);
            posterOpenAdapter = new PosterOpenAdapter(bidders, bidderType);
        }
        rcvBidder.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBidder.setLayoutManager(linearLayoutManager);
        posterOpenAdapter.setTaskId(taskResponse.getId());
        rcvBidder.setAdapter(posterOpenAdapter);
    }

    private void refreshAssignerList(String assignType) {
        AssignerAdapter assignerAdapter;
        if (assigners.size() > MAX_WORKER) {

            ArrayList<Assigner> assignersNew = new ArrayList<>();
            assignersNew.addAll(assigners.subList(0, MAX_WORKER));

            assignerAdapter = new AssignerAdapter(assignersNew, assignType);
        } else {
            tvSeeMoreAssigners.setVisibility(View.GONE);
            assignerAdapter = new AssignerAdapter(assigners, assignType);
        }

        rcvAssign.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskResponse.getId());
        rcvAssign.setAdapter(assignerAdapter);
    }

    private void refreshComment() {
        mComments = (ArrayList<Comment>) taskResponse.getComments();
        commentsAdapter = new CommentTaskAdapter(this, mComments);
        commentsAdapter.stopLoadMore();
        final LinearLayoutManager lvManager = new LinearLayoutManager(this);
        lvManager.setReverseLayout(true);
        lvManager.setStackFromEnd(true);
        rcvComment.setLayoutManager(lvManager);
//        commentsAdapter.setCommentType(layoutFooter.getVisibility());
        rcvComment.setAdapter(commentsAdapter);

        commentsAdapter.setAnswerListener(new CommentTaskAdapter.AnswerListener() {
            @Override
            public void onAnswer(int position) {
                Intent intentAnswer = new Intent(DetailTaskActivity.this, CommentsNewActivity.class);
                String commentType = getString(R.string.comment_setting_visible);
//                intentAnswer.putExtra(Constants.TASK_ID_EXTRA, mComments.get(position).getTaskId());
//                intentAnswer.putExtra(Constants.COMMENT_STATUS_EXTRA, commentType);
//                intentAnswer.putExtra(Constants.COMMENT_VISIBILITY, View.VISIBLE);
                intentAnswer.putExtra(Constants.COMMENT_EXTRA, mComments.get(position));
//                intentAnswer.putExtra(Constants.COMMENT_INPUT_EXTRA, layoutFooter.getVisibility());
                startActivityForResult(intentAnswer, Constants.COMMENT_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });

    }

    private void updateByStatus() {

        //fix bug fabric
        if (taskResponse.getStatus() == null) return;

        bidderType = "";
        assigerType = "";

        boolean isShowCancel = true;
        boolean isDelete = true;
        boolean isReportTask = true;
        boolean isEditTask = false;
        boolean isFollow = true;

        //poster
        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.my_task_status_poster_open), ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            //menu popup
            isDelete = false;
            isReportTask = false;
            if (taskResponse.getBidderCount() == 0 && taskResponse.getAssigneeCount() == 0)
                isEditTask = true;
            isFollow = false;

            bidderType = getString(R.string.assign);
            assigerType = getString(R.string.call);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received_poster));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = false;
            isFollow = false;

            assigerType = getString(R.string.call);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isDelete = false;
            isReportTask = false;
            isFollow = false;

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

        }

        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            updateStatusTask(true, getString(R.string.my_task_status_worker_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN)) {
            updateStatusTask(true, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            updateStatusTask(true, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            updateStatusTask(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            updateStatusTask(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
            updateStatusTask(false, getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.VISIBLE);
            moreDetailLayout.setVisibility(View.GONE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.VISIBLE);
            btnOffer.setText(getString(R.string.wait_accept));
            Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(this, R.drawable.btn_wait_accept));
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isFollow = false;

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            updateStatusTask(true, getString(R.string.received), ContextCompat.getDrawable(this, R.drawable.bg_border_received));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.VISIBLE);
            btnRatePoster.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isFollow = false;
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            updateStatusTask(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.VISIBLE);
            moreDetailLayout.setVisibility(View.GONE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.VISIBLE);
            moreFooterLayout.setVisibility(View.GONE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.VISIBLE);

            isDelete = false;
            isReportTask = false;
            isShowCancel = false;
            isFollow = false;
        }
        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            updateStatusTask(true, getString(R.string.make_an_offer_status), ContextCompat.getDrawable(this, R.drawable.bg_border_offer));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.VISIBLE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isShowCancel = false;

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() != UserManager.getMyUser().getId()) {
            updateStatusTask(true, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received));

            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isShowCancel = false;
            isFollow = false;
        }

        //Creating the instance of PopupMenu
        popup = new PopupMenu(this, imgMenu);
        popup.getMenuInflater().inflate(R.menu.menu_detail, popup.getMenu());

        if (isShowCancel)
            popup.getMenu().findItem(R.id.cancel_task).setVisible(true);
        else
            popup.getMenu().findItem(R.id.cancel_task).setVisible(false);

        if (isDelete)
            popup.getMenu().findItem(R.id.delete_task).setVisible(true);
        else
            popup.getMenu().findItem(R.id.delete_task).setVisible(false);

        if (isReportTask)
            popup.getMenu().findItem(R.id.report_task).setVisible(true);
        else
            popup.getMenu().findItem(R.id.report_task).setVisible(false);

        if (isEditTask)
            popup.getMenu().findItem(R.id.edit_task).setVisible(true);
        else
            popup.getMenu().findItem(R.id.edit_task).setVisible(false);

        if (isFollow)
            popup.getMenu().findItem(R.id.follow_task).setVisible(true);
        else
            popup.getMenu().findItem(R.id.follow_task).setVisible(false);

        if (taskResponse.isFollowed())
            popup.getMenu().findItem(R.id.follow_task).setTitle(getString(R.string.un_follow_task));
        else
            popup.getMenu().findItem(R.id.follow_task).setTitle(getString(R.string.follow_task));

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.share:
                        Utils.shareTask(DetailTaskActivity.this, taskId);
                        break;

                    case R.id.cancel_task:
                        DialogUtils.showOkAndCancelDialog(
                                DetailTaskActivity.this, getString(R.string.title_cancel_task), getString(R.string.cancel_task_content), getString(R.string.cancel_task_ok),
                                getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        doCacelTask();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                        break;

                    case R.id.delete_task:
                        DialogUtils.showOkAndCancelDialog(
                                DetailTaskActivity.this, getString(R.string.title_delete_task), getString(R.string.content_detete_task), getString(R.string.cancel_task_ok),
                                getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        doDeleteTask();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                        break;

                    case R.id.copy_task:
                        Intent intent = new Intent(DetailTaskActivity.this, CreateTaskActivity.class);
                        intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                        intent.putExtra(Constants.TASK_EDIT_EXTRA, Constants.TASK_COPY);
                        startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                    case R.id.edit_task:
                        Intent intentEdit = new Intent(DetailTaskActivity.this, CreateTaskActivity.class);
                        intentEdit.putExtra(Constants.EXTRA_TASK, taskResponse);
                        intentEdit.putExtra(Constants.TASK_EDIT_EXTRA, Constants.TASK_EDIT);
                        startActivityForResult(intentEdit, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                    case R.id.report_task:
                        doReportTask();
                        break;

                    case R.id.follow_task:
                        doFollowTask();
                        break;

                }
                return true;
            }
        });
    }

    private void doCacelTask() {
        ProgressDialogUtils.showProgressDialog(this);

        ApiClient.getApiService().cancelTask(UserManager.getUserToken(), taskId).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "doCacelTask , code : " + response.code());
                LogUtils.d(TAG, "doCacelTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(DetailTaskActivity.this, getString(R.string.cancel_task_success_msg), false, false);
                    taskResponse = response.body();
                    Utils.updateRole(taskResponse);
                    onBackPressed();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCacelTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {

                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(DetailTaskActivity.this, error.message(), false, true);
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doCacelTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void doDeleteTask() {
        ProgressDialogUtils.showProgressDialog(this);
        ApiClient.getApiService().deleteTask(UserManager.getUserToken(), taskId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "doDeleteTask , code : " + response.code());
                LogUtils.d(TAG, "doDeleteTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    Utils.showLongToast(DetailTaskActivity.this, getString(R.string.delete_task_success_msg), false, false);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                    setResult(Constants.RESULT_CODE_TASK_DELETE, intent);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDeleteTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(DetailTaskActivity.this, error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doDeleteTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void doReportTask() {
        DialogUtils.showOkAndCancelDialog(DetailTaskActivity.this, getString(R.string.report_task_title), getString(R.string.report_task_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                doSendReport();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void doSendReport() {
        ProgressDialogUtils.showProgressDialog(DetailTaskActivity.this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("reason", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doSendReport data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().reportTask(UserManager.getUserToken(), taskId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "doSendReport , body : " + response.body());
                LogUtils.d(TAG, "doSendReport , code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    Utils.showLongToast(DetailTaskActivity.this, getString(R.string.report_task_success), false, false);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doSendReport();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doSendReport();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doSendReport();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }


    private void doFollowTask() {

        if (taskResponse.isFollowed()) {
            ProgressDialogUtils.showProgressDialog(DetailTaskActivity.this);

            ApiClient.getApiService().unFollowTask(UserManager.getUserToken(), taskId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    LogUtils.d(TAG, "unFollowTask , body : " + response.body());
                    LogUtils.d(TAG, "unFollowTask , code : " + response.code());

                    if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                        taskResponse.setFollowed(false);
                        popup.getMenu().findItem(R.id.follow_task).setTitle(getString(R.string.follow_task));
                        Utils.showLongToast(DetailTaskActivity.this, getString(R.string.un_follow_task_success), false, false);
                    } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                        NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                doFollowTask();
                            }
                        });
                    } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                        Utils.blockUser(DetailTaskActivity.this);
                    } else {
                        DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                doFollowTask();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doFollowTask();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                }
            });
        } else {
            ProgressDialogUtils.showProgressDialog(DetailTaskActivity.this);

            ApiClient.getApiService().followTask(UserManager.getUserToken(), taskId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    LogUtils.d(TAG, "doFollowTask , body : " + response.body());
                    LogUtils.d(TAG, "doFollowTask , code : " + response.code());

                    if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                        taskResponse.setFollowed(true);
                        popup.getMenu().findItem(R.id.follow_task).setTitle(getString(R.string.un_follow_task));
                        Utils.showLongToast(DetailTaskActivity.this, getString(R.string.follow_task_success), false, false);
                    } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                        NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                doFollowTask();
                            }
                        });
                    } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                        Utils.blockUser(DetailTaskActivity.this);
                    } else {
                        DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                doFollowTask();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doFollowTask();
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


    public void updateStatusTask(boolean isShow, String status, Drawable drawable) {
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

    private void doSeeMoreDetail() {

        if (moreDetailLayout.getVisibility() == View.VISIBLE)
            moreDetailLayout.setVisibility(View.GONE);
        else
            moreDetailLayout.setVisibility(View.VISIBLE);

    }

    private void doMoreFooter() {

        if (moreFooterLayout.getVisibility() == View.VISIBLE)
            moreFooterLayout.setVisibility(View.GONE);
        else
            moreFooterLayout.setVisibility(View.VISIBLE);

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.hasExtra(Constants.EXTRA_TASK)) {
                taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
                Utils.updateRole(taskResponse);
                updateUi();
            } else if (intent.hasExtra(Constants.ASSIGNER_RATE_EXTRA)) {
                Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_RATE_EXTRA);
                Intent intentRate = new Intent(DetailTaskActivity.this, RateActivity.class);
                intentRate.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intentRate.putExtra(Constants.USER_ID_EXTRA, assigner.getId());
                intentRate.putExtra(Constants.AVATAR_EXTRA, assigner.getAvatar());
                intentRate.putExtra(Constants.NAME_EXTRA, assigner.getFullName());
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            } else if (intent.hasExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA)) {
                DialogUtils.showOkAndCancelDialog(DetailTaskActivity.this, getString(R.string.cancel_bid_title), getString(R.string.cancel_bid_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA);
                        doCancelBid(assigner);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else if (intent.hasExtra(Constants.ASSIGNER_CONTACT_EXTRA)) {
                Intent intentContact = new Intent(DetailTaskActivity.this, ChatActivity.class);
                intentContact.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intentContact.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
                intentContact.putExtra(Constants.TITLE_INFO_EXTRA, taskResponse.getTitle());
                startActivityForResult(intentContact, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
            }

        }
    };

    private void doCancelBid(final Assigner assigner) {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", assigner.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doCancelBid data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().cancelBid(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {

                LogUtils.d(TAG, "doCancelBid onResponse : " + response.body());
                LogUtils.d(TAG, "doCancelBid code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    Utils.updateRole(taskResponse);
                    updateUi();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCancelBid(assigner);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doCancelBid(assigner);
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

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.tv_map:

                break;

            case R.id.tv_see_more_detail:
                doSeeMoreDetail();
                break;

            case R.id.img_menu:
                popup.show();//showing popup menu
                break;

            case R.id.img_avatar:
            case R.id.rb_rate:
            case R.id.tv_name:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, taskResponse.getPoster().getId());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_bid:

                break;

            case R.id.btn_contact:

                break;

            case R.id.btn_rate:

                break;

            case R.id.tv_see_more_detail_footer:
                doMoreFooter();
                break;


        }
    }
}
