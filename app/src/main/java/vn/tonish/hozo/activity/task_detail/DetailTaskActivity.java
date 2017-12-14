package vn.tonish.hozo.activity.task_detail;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AssignersActivity;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.BiddersActivity;
import vn.tonish.hozo.activity.BlockTaskActivity;
import vn.tonish.hozo.activity.ChatActivity;
import vn.tonish.hozo.activity.RatingActivity;
import vn.tonish.hozo.activity.SupportActivity;
import vn.tonish.hozo.activity.comment.CommentAllActivity;
import vn.tonish.hozo.activity.comment.CommentsAnswerActivity;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageListActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.activity.task.PostTaskActivity;
import vn.tonish.hozo.adapter.AssignerAdapter;
import vn.tonish.hozo.adapter.CommentTaskAdapter;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
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
    private TextViewHozo tvStatus;
    private TextViewHozo tvTimeAgo;
    private TextViewHozo tvSeeMoreDetail;
    private TextViewHozo tvSeeMoreFooter;
    private TextViewHozo tvName;
    private TextViewHozo tvTitle;
    private TextViewHozo tvAddress;
    private TextViewHozo tvDate;
    private TextViewHozo tvTime;
    private TextViewHozo tvHour;
    private TextViewHozo tvDes;
    private TextViewHozo tvAge;
    private TextViewHozo tvSex;
    private TextViewHozo tvBudget;
    private TextViewHozo tvWorkerCount;
    private TextViewHozo tvAssignerCount;
    private TextViewHozo tvEmptyCount;
    private ButtonHozo btnOffer;
    private ButtonHozo btnContact;
    private ButtonHozo btnRatePoster;
    private ButtonHozo btnContactHozo;
    private ButtonHozo btnContactHozoWorker;
    private MyGridView myGridView;
    private int taskId = 0;
    private ImageView imgMenu;
    private LinearLayout moreDetailLayout, moreFooterLayout, layoutInputComment;
    private PopupMenu popup;

    private RecyclerView rcvBidder;
    private ArrayList<Bidder> bidders = new ArrayList<>();

    private RecyclerView rcvAssign;
    private ArrayList<Assigner> assigners = new ArrayList<>();

    private TextViewHozo tvSeeMoreBidders, tvSeeMoreAssigners;
    private TextViewHozo tvBidderCount, tvAssignCount, tvCommentCount, tvSeeMoreComment;

    private String bidderType = "";
    private String assigerType = "";
    private static final int MAX_WORKER = 5;

    // comment
    private RecyclerView rcvComment;
    private ArrayList<Comment> mComments = new ArrayList<>();
    private CommentTaskAdapter commentsAdapter;
    private CircleImageView imgAvatarComment;

    private EdittextHozo edtComment;
    private String imgPath;
    private int tempId = 0;
    private RelativeLayout imgLayout;
    private ImageView imgAttached;
    private File fileAttach;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private View vLineCommentList;
    private int moreDetailVisibility = -1;
    private int moreDFooterVisibility = -1;
    private String notificationEvent = "";
    private boolean isScroll = true;
    private ProgressBar progressBar;

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

        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

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

        TextViewHozo tvAgeLbl = (TextViewHozo) findViewById(R.id.tv_age_lbl);
        TextViewHozo tvSexLbl = (TextViewHozo) findViewById(R.id.tv_sex_lbl);

        TextViewHozo tvMap = (TextViewHozo) findViewById(R.id.tv_map);
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

        btnContactHozoWorker = (ButtonHozo) findViewById(R.id.btn_contact_worker);
        btnContactHozoWorker.setOnClickListener(this);

        tvSeeMoreFooter = (TextViewHozo) findViewById(R.id.tv_see_more_detail_footer);
        tvSeeMoreFooter.setOnClickListener(this);

        moreFooterLayout = (LinearLayout) findViewById(R.id.more_footer_layout);

        imgAvatarComment = (CircleImageView) findViewById(R.id.img_avatar_cm);
        imgAvatarComment.setOnClickListener(this);

        ButtonHozo btnComment = (ButtonHozo) findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);

        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);
        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);
        imgAttached = (ImageView) findViewById(R.id.img_attached);

        TextViewHozo tvAttach = (TextViewHozo) findViewById(R.id.tv_attach);
        tvAttach.setOnClickListener(this);

        ImageView imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(this);

        tvCommentCount = (TextViewHozo) findViewById(R.id.tv_comment_count);

        tvSeeMoreComment = (TextViewHozo) findViewById(R.id.tv_see_more_comment);
        tvSeeMoreComment.setOnClickListener(this);

        layoutInputComment = (LinearLayout) findViewById(R.id.comment_input_layout);

        vLineCommentList = findViewById(R.id.v_line_comment_list);

        btnContactHozo = (ButtonHozo) findViewById(R.id.btn_contact_hozo);
        btnContactHozo.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        taskId = intent.getIntExtra(Constants.TASK_ID_EXTRA, 0);
        if (intent.hasExtra(Constants.EVENT_NOTIFICATION_EXTRA))
            notificationEvent = intent.getStringExtra(Constants.EVENT_NOTIFICATION_EXTRA);
    }

    @Override
    protected void resumeData() {
        getData();
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

    @Override
    protected void onStop() {
        super.onStop();

        if (moreDetailLayout.getVisibility() == View.VISIBLE) {
            moreDetailVisibility = View.VISIBLE;
        } else
            moreDetailVisibility = View.GONE;

        if (moreFooterLayout.getVisibility() == View.VISIBLE) {
            moreDFooterVisibility = View.VISIBLE;
        } else
            moreDFooterVisibility = View.GONE;
    }

    private void getData() {
//        ProgressDialogUtils.showProgressDialog(this);
        LogUtils.d(TAG, "getDetailTask , taskId : " + taskId);
        LogUtils.d(TAG, "getDetailTask , UserManager.getUserToken() : " + UserManager.getUserToken());

        Map<String, Boolean> option = new HashMap<>();
        option.put("viewer", true);

        Call<TaskResponse> call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskId, option);
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
//                ProgressDialogUtils.dismissProgressDialog();
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
//                ProgressDialogUtils.dismissProgressDialog();
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

        tvBudget.setText(Utils.formatNumber(taskResponse.getWorkerRate()));

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

        switch (taskResponse.getGender()) {
            case Constants.GENDER_MALE:
                tvSex.setText(getString(R.string.gender_male_vn));
                break;
            case Constants.GENDER_FEMALE:
                tvSex.setText(getString(R.string.gender_female_vn));
                break;
            default:
                tvSex.setText(getString(R.string.gender_non_vn));
                break;
        }

        tvBidderCount.setText(getString(R.string.bidder_count_lbl, taskResponse.getBidderCount()));
        tvAssignCount.setText(getString(R.string.bidder_count_lbl, taskResponse.getAssigneeCount()));

        // update bidder list
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        refreshBidderList(bidderType);

        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        refreshAssignerList(assigerType);

        // comment
        Utils.displayImageAvatar(this, imgAvatarComment, taskResponse.getPoster().getAvatar());
        refreshComment();

        tvCommentCount.setText(String.valueOf(taskResponse.getCommentsCount()));

        if (taskResponse.getCommentsCount() > 2) {
            tvSeeMoreComment.setText(getString(R.string.see_all_comment, taskResponse.getCommentsCount() - 2));
            tvSeeMoreComment.setVisibility(View.VISIBLE);
        } else {
            tvSeeMoreComment.setVisibility(View.GONE);
        }

        // scroll to event when open from notification
        if (notificationEvent.equals(Constants.PUSH_TYPE_BID_RECEIVED) && isScroll) {
            isScroll = false;
            rcvBidder.getParent().requestChildFocus(rcvBidder, rcvBidder);
        } else if (notificationEvent.equals(Constants.PUSH_TYPE_COMMENT_RECEIVED) && isScroll) {
            isScroll = false;
            rcvComment.getParent().requestChildFocus(rcvComment, rcvComment);
        } else if (notificationEvent.equals(Constants.PUSH_TYPE_BIDDER_CANCELED) && isScroll) {
            isScroll = false;
            rcvBidder.getParent().requestChildFocus(rcvBidder, rcvBidder);
        } else if (notificationEvent.equals(Constants.PUSH_TYPE_TASK_COMPLETE) && isScroll) {
            isScroll = false;
            rcvAssign.getParent().requestChildFocus(rcvAssign, rcvAssign);
        }

    }

    private void refreshBidderList(String bidderType) {
        PosterOpenAdapter posterOpenAdapter;
        if (bidders.size() > MAX_WORKER) {

            ArrayList<Bidder> biddersNew = new ArrayList<>();
            biddersNew.addAll(bidders.subList(0, MAX_WORKER));

            posterOpenAdapter = new PosterOpenAdapter(biddersNew, bidderType);
            tvSeeMoreBidders.setVisibility(View.VISIBLE);
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
            tvSeeMoreAssigners.setVisibility(View.VISIBLE);
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
        commentsAdapter.setCommentType(layoutInputComment.getVisibility());
        rcvComment.setAdapter(commentsAdapter);
        commentsAdapter.setPosterId(taskResponse.getPoster().getId());

        commentsAdapter.setAnswerListener(new CommentTaskAdapter.AnswerListener() {
            @Override
            public void onAnswer(int position) {
                Intent intentAnswer = new Intent(DetailTaskActivity.this, CommentsAnswerActivity.class);
//                String commentType = getString(R.string.comment_setting_visible);
//                intentAnswer.putExtra(Constants.TASK_ID_EXTRA, mComments.get(position).getTaskId());
//                intentAnswer.putExtra(Constants.COMMENT_STATUS_EXTRA, commentType);
//                intentAnswer.putExtra(Constants.COMMENT_VISIBILITY, View.VISIBLE);
                intentAnswer.putExtra(Constants.COMMENT_EXTRA, mComments.get(position));
                intentAnswer.putExtra(Constants.COMMENT_INPUT_EXTRA, layoutInputComment.getVisibility());
                intentAnswer.putExtra(Constants.POSTER_ID_EXTRA, taskResponse.getPoster().getId());
                startActivityForResult(intentAnswer, Constants.COMMENT_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });

        if (mComments.size() > 0)
            vLineCommentList.setVisibility(View.VISIBLE);
        else
            vLineCommentList.setVisibility(View.GONE);

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
            updateStatusTask(true, true, getString(R.string.my_task_status_poster_open), ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.VISIBLE);

            //menu popup
            isDelete = false;
            isReportTask = false;
            if (taskResponse.getBidderCount() == 0 && taskResponse.getAssigneeCount() == 0)
                isEditTask = true;
            isFollow = false;

            bidderType = getString(R.string.assign);
            assigerType = getString(R.string.call);

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received_poster));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.VISIBLE);

            isDelete = false;
            isReportTask = false;
            isFollow = false;

            assigerType = getString(R.string.call);

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));

            showExpand(false);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.VISIBLE);
            btnRatePoster.setText(getString(R.string.poster_ratting_complete));
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isDelete = false;
            isReportTask = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_AWAIT_APPROVAL) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_await_approval), ContextCompat.getDrawable(this, R.drawable.bg_border_await_approval));

            showExpand(false);

            btnOffer.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.VISIBLE);
            btnRatePoster.setText(getString(R.string.poster_ratting));
            btnContactHozo.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);

            try {
                if (DateTimeUtils.daysBetween(DateTimeUtils.toCalendar(taskResponse.getEndTime()), Calendar.getInstance()) <= 2
                        && DateTimeUtils.daysBetween(DateTimeUtils.toCalendar(taskResponse.getEndTime()), Calendar.getInstance()) >= 0)
                    btnContactHozoWorker.setVisibility(View.VISIBLE);
                else
                    btnContactHozoWorker.setVisibility(View.GONE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        }

        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_worker_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(false);

            btnOffer.setVisibility(View.VISIBLE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            btnOffer.setText(getString(R.string.btn_cancel_task));
            Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            btnOffer.setEnabled(false);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isShowCancel = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
            updateStatusTask(true, false, getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));

            showExpand(false);

            btnOffer.setVisibility(View.VISIBLE);
            btnOffer.setText(getString(R.string.wait_accept));
            Utils.setViewBackground(btnOffer, ContextCompat.getDrawable(this, R.drawable.btn_wait_accept));
            btnOffer.setEnabled(false);

            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED)
                && (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) || taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED))) {
            updateStatusTask(true, false, getString(R.string.received), ContextCompat.getDrawable(this, R.drawable.bg_border_received));

            showExpand(false);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.VISIBLE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_AWAIT_APPROVAL)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_await_approval), ContextCompat.getDrawable(this, R.drawable.bg_border_await_approval));

            showExpand(false);

            btnOffer.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.VISIBLE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            try {
                if (DateTimeUtils.daysBetween(DateTimeUtils.toCalendar(taskResponse.getEndTime()), Calendar.getInstance()) <= 2
                        && DateTimeUtils.daysBetween(DateTimeUtils.toCalendar(taskResponse.getEndTime()), Calendar.getInstance()) >= 0)
                    btnContact.setVisibility(View.VISIBLE);
                else
                    btnContact.setVisibility(View.GONE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (taskResponse.isRatePoster())
                btnRatePoster.setText(getString(R.string.worker_ratting_done));
            else
                btnRatePoster.setText(getString(R.string.worker_ratting));

            isDelete = false;
            isReportTask = false;
            isShowCancel = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_NOT_APPROVED)) {
            updateStatusTask(true, false, getString(R.string.my_task_status_poster_not_approved), ContextCompat.getDrawable(this, R.drawable.bg_border_not_approved));

            showExpand(false);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.VISIBLE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = false;
            isShowCancel = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_COMPLETED)) {
            updateStatusTask(true, false, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = false;
            isShowCancel = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.GONE);
        }

        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            updateStatusTask(true, true, getString(R.string.make_an_offer_status), ContextCompat.getDrawable(this, R.drawable.bg_border_transparent));

            showExpand(true);

            btnOffer.setVisibility(View.VISIBLE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isShowCancel = false;

            layoutInputComment.setVisibility(View.VISIBLE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() != UserManager.getMyUser().getId()) {
            updateStatusTask(true, false, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received_non));

            showExpand(true);

            btnOffer.setVisibility(View.GONE);
            btnContact.setVisibility(View.GONE);
            btnRatePoster.setVisibility(View.GONE);
            btnContactHozo.setVisibility(View.GONE);
            btnContactHozoWorker.setVisibility(View.GONE);

            isDelete = false;
            isReportTask = true;
            isShowCancel = false;
            isFollow = false;

            layoutInputComment.setVisibility(View.VISIBLE);
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

                        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
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
                        } else {
                            try {
                                if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && DateTimeUtils.minutesBetween(DateTimeUtils.toCalendar(taskResponse.getOfferAssignedAt()), Calendar.getInstance()) > 30) {
                                    DialogUtils.showOkAndCancelDialog(
                                            DetailTaskActivity.this, getString(R.string.title_cancel_task), getString(R.string.cancel_task_content_assign, Utils.formatNumber(taskResponse.getBidDepositAmount())), getString(R.string.cancel_task_ok),
                                            getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                                @Override
                                                public void onSubmit() {
                                                    doCacelTask();
                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                } else {
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
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


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
                        Intent intent = new Intent(DetailTaskActivity.this, PostTaskActivity.class);
                        intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                        intent.putExtra(Constants.TASK_EDIT_EXTRA, Constants.TASK_COPY);
                        startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                    case R.id.edit_task:
                        Intent intentEdit = new Intent(DetailTaskActivity.this, PostTaskActivity.class);
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

        if (moreDetailVisibility != -1) {
            if (moreDetailVisibility == View.VISIBLE) {
                moreDetailLayout.setVisibility(View.VISIBLE);
                tvSeeMoreDetail.setText(getString(R.string.advance_more_hide));
                tvSeeMoreDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_blue, 0);
            } else {
                moreDetailLayout.setVisibility(View.GONE);
                tvSeeMoreDetail.setText(getString(R.string.see_more_detail));
                tvSeeMoreDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_blue, 0);
            }
        }


        if (moreDFooterVisibility != -1) {
            if (moreDFooterVisibility == View.VISIBLE) {
                moreFooterLayout.setVisibility(View.VISIBLE);
                tvSeeMoreFooter.setText(getString(R.string.advance_more_hide));
                tvSeeMoreFooter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_blue, 0);
            } else {
                moreFooterLayout.setVisibility(View.GONE);
                tvSeeMoreFooter.setText(getString(R.string.see_more_detail_footer));
                tvSeeMoreFooter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_blue, 0);

            }
        }

    }

    private void showExpand(boolean isShow) {
        if (isShow) {
            //see more detail
            tvSeeMoreDetail.setVisibility(View.GONE);
            moreDetailLayout.setVisibility(View.VISIBLE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.GONE);
            moreFooterLayout.setVisibility(View.VISIBLE);
        } else {
            //see more detail
            tvSeeMoreDetail.setVisibility(View.VISIBLE);
            moreDetailLayout.setVisibility(View.GONE);

            // see more footer
            tvSeeMoreFooter.setVisibility(View.VISIBLE);
            moreFooterLayout.setVisibility(View.GONE);
        }
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

    private void updateStatusTask(boolean isShowStatus, boolean isShowProgress, String status, Drawable drawable) {
        if (isShowStatus) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(status);
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                tvStatus.setBackgroundDrawable(drawable);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvStatus.setBackground(drawable);
            }
        } else tvStatus.setVisibility(View.GONE);

        if (isShowProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(taskResponse.getWorkerCount());
            progressBar.setProgress(taskResponse.getAssigneeCount());
        } else
            progressBar.setVisibility(View.GONE);

    }

    private void doSeeMoreDetail() {
        if (moreDetailLayout.getVisibility() == View.VISIBLE) {
            moreDetailLayout.setVisibility(View.GONE);
            tvSeeMoreDetail.setText(getString(R.string.see_more_detail));
            tvSeeMoreDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_blue, 0);
        } else {
            moreDetailLayout.setVisibility(View.VISIBLE);
            tvSeeMoreDetail.setText(getString(R.string.advance_more_hide));
            tvSeeMoreDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_blue, 0);

        }
    }

    private void doMoreFooter() {
        if (moreFooterLayout.getVisibility() == View.VISIBLE) {
            moreFooterLayout.setVisibility(View.GONE);
            tvSeeMoreFooter.setText(getString(R.string.see_more_detail_footer));
            tvSeeMoreFooter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_blue, 0);
        } else {
            moreFooterLayout.setVisibility(View.VISIBLE);
            tvSeeMoreFooter.setText(getString(R.string.advance_more_hide));
            tvSeeMoreFooter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_blue, 0);
        }
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
                Intent intentRate = new Intent(DetailTaskActivity.this, RatingActivity.class);
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
                intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
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

    private void doSend() {
        if (edtComment.getText().toString().trim().equals("")) {
            Utils.showLongToast(this, getString(R.string.empty_content_comment_error), true, false);
            return;
        } else if (!Utils.validateInput(this, edtComment.getText().toString().trim())) {
            Utils.showLongToast(this, getString(R.string.post_a_task_input_error), true, false);
            return;
        }

        if (imgPath == null) {
            doComment();
        } else {
            doAttachImage();
        }
    }

    private void doComment() {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("body", edtComment.getText().toString().trim());
            if (imgPath != null)
                jsonRequest.put("image_id", tempId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "commentTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().commentTask(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                LogUtils.d(TAG, "commentTask , code : " + response.code());
                LogUtils.d(TAG, "commentTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);

                    getData();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.status().equals(Constants.DUPLICATE_COMMENT)) {
                        DialogUtils.showOkDialog(DetailTaskActivity.this, getString(R.string.error), getString(R.string.comment_duplicate_error), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else {
                        DialogUtils.showOkDialog(DetailTaskActivity.this, getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else {
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doComment();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doComment();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void doAttachImage() {
        ProgressDialogUtils.showProgressDialog(this);
        File fileUp = Utils.compressFile(fileAttach);
        LogUtils.d(TAG, "doAttachImage , file Name : " + fileUp.getName());
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileUp);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileUp.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    tempId = imageResponse.getIdTemp();
                    doComment();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(DetailTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(DetailTaskActivity.this);
                } else {
                    ProgressDialogUtils.dismissProgressDialog();
                    DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAttachImage();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(DetailTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doAttachImage();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(DetailTaskActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();
    }

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
                permissionGranted();
            } else {
                permissionDenied();
            }
        }
    }

    private void permissionDenied() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PICK_IMAGE
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(DetailTaskActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(DetailTaskActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.COMMENT_REQUEST_CODE && resultCode == Constants.COMMENT_RESPONSE_CODE) {
            Comment comment = (Comment) data.getSerializableExtra(Constants.COMMENT_EXTRA);
            for (int i = 0; i < mComments.size(); i++)
                if (comment.getId() == mComments.get(i).getId()) {
                    mComments.set(i, comment);
                    commentsAdapter.notifyDataSetChanged();
                }
        }
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
        }
    }

    private void doSeeMoreBidders() {
        Intent intent = new Intent(this, BiddersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.BIDDER_TYPE_EXTRA, bidderType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_BINDDER, TransitionScreen.DOWN_TO_UP);
    }

    private void doSeeMoreAssigns() {
        Intent intent = new Intent(this, AssignersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.ASSIGNER_TYPE_EXTRA, assigerType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_ASSIGNER, TransitionScreen.DOWN_TO_UP);
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
            case R.id.img_avatar_cm:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, taskResponse.getPoster().getId());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_bid:
                Intent intentBid = new Intent(this, ConfirmBidActivity.class);
                intentBid.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                startActivity(intentBid, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_contact:
            case R.id.btn_contact_worker:
                Intent intentContact = new Intent(DetailTaskActivity.this, ChatActivity.class);
                intentContact.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intentContact.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
                intentContact.putExtra(Constants.TITLE_INFO_EXTRA, taskResponse.getTitle());
                intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                startActivityForResult(intentContact, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
                break;

            case R.id.btn_rate:
                Intent intentRate = new Intent(DetailTaskActivity.this, RatingActivity.class);
                intentRate.putExtra(Constants.TASK_RESPONSE_RATING, taskResponse);
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
                break;

            case R.id.tv_see_more_detail_footer:
                doMoreFooter();
                break;

            case R.id.btn_comment:
                doSend();
                break;

            case R.id.tv_attach:
                checkPermission();
                break;

            case R.id.img_delete:
                imgLayout.setVisibility(View.GONE);
                imgPath = null;
                break;

            case R.id.tv_see_more_comment:
                Intent intentCommentAll = new Intent(DetailTaskActivity.this, CommentAllActivity.class);
                intentCommentAll.putExtra(Constants.TASK_ID_EXTRA, taskId);
                intentCommentAll.putExtra(Constants.POSTER_ID_EXTRA, taskResponse.getPoster().getId());
                intentCommentAll.putExtra(Constants.COMMENT_VISIBILITY, layoutInputComment.getVisibility());
                startActivity(intentCommentAll, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.tv_see_more_bidders:
                doSeeMoreBidders();
                break;

            case R.id.tv_see_more_assigns:
                doSeeMoreAssigns();
                break;

            case R.id.btn_contact_hozo:
                startActivity(SupportActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}
