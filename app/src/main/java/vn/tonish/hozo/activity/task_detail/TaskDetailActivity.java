package vn.tonish.hozo.activity.task_detail;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
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
import vn.tonish.hozo.activity.CommentsActivity;
import vn.tonish.hozo.activity.PostATaskActivity;
import vn.tonish.hozo.activity.RateActivity;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.adapter.AssignerCallAdapter;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.TaskManager;
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
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.ResizeAnimation;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CommentViewFull;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;
import vn.tonish.hozo.view.WorkAroundMapFragment;
import vn.tonish.hozo.view.WorkDetailView;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_RATE;

/**
 * Created by LongBui on 5/30/17.
 */

public class TaskDetailActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = TaskDetailActivity.class.getSimpleName();
    private CommentViewFull commentViewFull;
    private WorkDetailView workDetailView;
    private ArrayList<Comment> comments = new ArrayList<>();
    private TaskResponse taskResponse;

    private ImageView imgAttached;
    private EdittextHozo edtComment;
    private RelativeLayout imgLayout;
    private String imgPath;
    private ScrollView scv;

    private RecyclerView rcvBidder;
    private ArrayList<Bidder> bidders = new ArrayList<>();

    private RecyclerView rcvAssign;
    private ArrayList<Assigner> assigners = new ArrayList<>();

    private int taskId = 0;
    private GoogleMap googleMap;
    private int tempId = 0;
    private File fileAttach;
    private TextViewHozo tvSeeMore, tvSeeMoreBidders, tvSeeMoreAssigners;
    private TextViewHozo tvCommentCount, tvBidderCount, tvAssignCount;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout layoutFooter;
    //    private TextViewHozo tvCancel;
    private String commentType;
    private LinearLayout layoutBidderCount, layoutAssignCount;
    private ImageView imgMenu;
    private boolean isShowCancel;
    private boolean isReportTask;
    private boolean mMapViewExpanded = false;
    private WorkAroundMapFragment mapFragment;
    private boolean isDelete = true;
    private Call<TaskResponse> call;
    private String mBidderType = "";
    private String mAssigerType = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);
        commentViewFull = (CommentViewFull) findViewById(R.id.comment_view_full);

        createSwipeToRefresh();

        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);

        ImageView imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);

        imgAttached = (ImageView) findViewById(R.id.img_attached);
        imgAttached.setOnClickListener(this);

        ImageView imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(this);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        rcvBidder = (RecyclerView) findViewById(R.id.rcv_bidders);
        rcvAssign = (RecyclerView) findViewById(R.id.rcv_assign);

        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);

        ImageView imgComment = (ImageView) findViewById(R.id.img_send);
        imgComment.setOnClickListener(this);

        tvSeeMore = (TextViewHozo) findViewById(R.id.tv_see_more_comment);
        tvSeeMore.setOnClickListener(this);
        tvSeeMoreBidders = (TextViewHozo) findViewById(R.id.tv_see_more_bidders);
        tvSeeMoreBidders.setOnClickListener(this);
        tvSeeMoreAssigners = (TextViewHozo) findViewById(R.id.tv_see_more_assigns);
        tvSeeMoreAssigners.setOnClickListener(this);

        tvBidderCount = (TextViewHozo) findViewById(R.id.tv_bidder_count);
        tvAssignCount = (TextViewHozo) findViewById(R.id.tv_assign_count);
        tvCommentCount = (TextViewHozo) findViewById(R.id.tv_comment_count);

//        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
//        tvCancel.setOnClickListener(this);

        layoutBidderCount = (LinearLayout) findViewById(R.id.layout_bidder_count);
        layoutAssignCount = (LinearLayout) findViewById(R.id.layout_assign_count);

        layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);

        scv = (ScrollView) findViewById(R.id.scv);

        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);

        mapFragment = (WorkAroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.setListener(new WorkAroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scv.requestDisallowInterceptTouchEvent(true);

            }
        });
    }

    private void animateMapView(WorkAroundMapFragment mMapView) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mMapView.getView().getLayoutParams();

        ResizeAnimation a = new ResizeAnimation(mMapView.getView());
        a.setDuration(500);

        if (!getMapViewStatus()) {
            mMapViewExpanded = true;
            a.setParams(lp.height, dpToPx(getResources(), 300));
        } else {
            mMapViewExpanded = false;
            a.setParams(lp.height, dpToPx(getResources(), 150));
        }
        mMapView.getView().startAnimation(a);
    }

    private boolean getMapViewStatus() {
        return mMapViewExpanded;
    }

    public int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    @Override
    protected void initData() {
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        useCacheData();
        getData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        taskId = intent.getIntExtra(Constants.TASK_ID_EXTRA, 0);
        useCacheData();
        getData();
    }

    @Override
    protected void resumeData() {
        registerReceiver(broadcastReceiver, new IntentFilter("MyBroadcast"));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        getData();
    }

    private void useCacheData() {
        TaskEntity taskEntity = TaskManager.getTaskById(taskId);
        if (taskEntity != null) {
            taskResponse = DataParse.converTaskEntityToTaskReponse(taskEntity);
            updateUi();
        }
    }

    private void getData() {
//        ProgressDialogUtils.showProgressDialog(this);
        LogUtils.d(TAG, "getDetailTask , taskId : " + taskId);
        LogUtils.d(TAG, "getDetailTask , UserManager.getUserToken() : " + UserManager.getUserToken());

        call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskId);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    updateRole();
                    updateUi();
                    storeTaskToDatabase();

                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody" + error.toString());
                    if (error.status().equals(Constants.TASK_DETAIL_INPUT_REQUIRE) || error.status().equals(Constants.TASK_DETAIL_NO_EXIT)) {
                        DialogUtils.showOkDialog(TaskDetailActivity.this, getString(R.string.task_detail_no_exit), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.TASK_DETAIL_BLOCK)) {
//                        taskResponse = new TaskResponse();
//                        taskResponse.setStatus(Constants.TASK_TYPE_BLOCK);
//                        updateUi();
                        finish();

                        Intent intent = new Intent(TaskDetailActivity.this, BlockTaskActivity.class);
                        intent.putExtra(Constants.TITLE_INFO_EXTRA, getString(R.string.task_detail_block));
                        intent.putExtra(Constants.CONTENT_INFO_EXTRA, getString(R.string.task_detail_block_reasons) + " " + error.message());
                        startActivity(intent, TransitionScreen.FADE_IN);

//                        DialogUtils.showOkDialog(TaskDetailActivity.this, getString(R.string.task_detail_block), getString(R.string.task_detail_block_reasons) + " " + error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                            @Override
//                            public void onSubmit() {
//                                finish();
//                            }
//                        });
                    } else {
                        DialogUtils.showOkDialog(TaskDetailActivity.this, getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getData();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
//                ProgressDialogUtils.dismissProgressDialog();
                onStopRefresh();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getData();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
//                ProgressDialogUtils.dismissProgressDialog();
                onStopRefresh();
            }
        });

    }

    private void storeTaskToDatabase() {
        TaskEntity taskEntity = DataParse.converTaskReponseToTaskEntity(taskResponse);
        TaskManager.insertTask(taskEntity);
    }

    private void updateRole() {

        //fix crash on fabric -> I don't know why crash :(
        if (UserManager.getMyUser() == null) return;

        //poster
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            taskResponse.setRole(Constants.ROLE_POSTER);
        }
        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            taskResponse.setRole(Constants.ROLE_TASKER);
        }
        // find task
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            taskResponse.setRole(Constants.ROLE_FIND_TASK);
        }
    }

    private void updateUi() {
        LogUtils.d(TAG, "task detail view , update ui : " + taskResponse.toString());
        String bidderType = "";
        String assigerType = "";
        commentType = getString(R.string.comment_setting_visible);

        isShowCancel = true;
        isDelete = true;
        isReportTask = true;
//        tvCancel.setVisibility(View.VISIBLE);
        layoutFooter.setVisibility(View.VISIBLE);
        rcvBidder.setVisibility(View.VISIBLE);
        rcvAssign.setVisibility(View.VISIBLE);

        workDetailView.updateWork(taskResponse);

//        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_BLOCK)) {
//            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            layoutFooter.setVisibility(View.GONE);
//            rcvBidder.setVisibility(View.GONE);
//            rcvAssign.setVisibility(View.GONE);
//            imgMenu.setVisibility(View.GONE);
//            workDetailView.updateBtnRate(false);
//        }
//        //poster
//        else

        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.update_task), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
            bidderType = getString(R.string.assign);
            assigerType = getString(R.string.call);
            isDelete = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.VISIBLE);
            rcvBidder.setVisibility(View.VISIBLE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
            assigerType = getString(R.string.call);
            isDelete = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isDelete = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);

            assigerType = getString(R.string.rate);
            commentType = getString(R.string.comment_setting_invisible);
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        }

        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_worker_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = true;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = true;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
            isReportTask = true;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
            isDelete = false;
            isReportTask = true;
            workDetailView.updateBtnOffer(Constants.OFFER_PENDING);
            workDetailView.updateStatus(false, getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            isDelete = false;
            isReportTask = true;
            workDetailView.updateBtnOffer(Constants.OFFER_CALL);
            workDetailView.updateStatus(true, getString(R.string.received), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            isDelete = false;
            isReportTask = false;
            workDetailView.updateBtnOffer(Constants.OFFER_RATTING);
            workDetailView.updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        }

        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            isDelete = false;
            isReportTask = true;
            workDetailView.updateBtnOffer(Constants.OFFER_ACTIVE);
            workDetailView.updateStatus(false, "", ContextCompat.getDrawable(this, R.drawable.bg_border_done));
//            tvCancel.setVisibility(View.GONE);
            isShowCancel = false;
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() != UserManager.getMyUser().getId()) {
            isDelete = false;
            isReportTask = true;
            workDetailView.updateStatus(true, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
            workDetailView.updateBtnOffer(Constants.OFFER_GONE);
            isShowCancel = false;
        }

        workDetailView.setWorkDetailViewRateListener(new WorkDetailView.WorkDetailViewRateListener() {
            @Override
            public void onRate() {
                Intent intent = new Intent(TaskDetailActivity.this, RateActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
                intent.putExtra(Constants.AVATAR_EXTRA, taskResponse.getPoster().getAvatar());
                intent.putExtra(Constants.NAME_EXTRA, taskResponse.getPoster().getFullName());
                startActivityForResult(intent, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            }
        });

        workDetailView.setWorkDetailViewListener(new WorkDetailView.WorkDetailViewListener() {
            @Override
            public void onWorkDetailViewListener(TaskResponse taskResponseOffer) {
                taskResponse = taskResponseOffer;
                updateUi();
            }
        });

        if (googleMap != null) {
            LatLng latLng = new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_MAP_ZOOM_LEVEL));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
            googleMap.addMarker(marker);
        }

        // update bidder list
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        refreshBidderList(bidderType);

        //update assigners list
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        refreshAssignerList(assigerType);

        //update comments
        comments = (ArrayList<Comment>) taskResponse.getComments();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < comments.size(); i++) comments.get(i).setTaskId(taskId);
        realm.commitTransaction();
//        commentViewFull.setCommentType(commentType);
        commentViewFull.updateData(comments);
        tvBidderCount.setText(getString(R.string.count_in_detail, taskResponse.getBidderCount()));
        tvAssignCount.setText(getString(R.string.count_in_detail, taskResponse.getAssigneeCount()));
        tvCommentCount.setText(getString(R.string.count_in_detail, taskResponse.getCommentsCount()));
        mBidderType = bidderType;
        mAssigerType = assigerType;
        updateSeeMoreComment();
    }

    private void refreshBidderList(String bidderType) {
        ArrayList<Bidder> mBidders = new ArrayList<>();
        if (bidders.size() > 5) {
            tvSeeMoreBidders.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5; i++) {
                mBidders.add(bidders.get(i));
            }

        } else {
            tvSeeMoreBidders.setVisibility(View.GONE);
            mBidders.addAll(bidders);
        }
        PosterOpenAdapter posterOpenAdapter = new PosterOpenAdapter(mBidders, bidderType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBidder.setLayoutManager(linearLayoutManager);
        posterOpenAdapter.setTaskId(taskId);
        rcvBidder.setAdapter(posterOpenAdapter);
    }

    private void refreshAssignerList(String assignType) {
        ArrayList<Assigner> mAssigners = new ArrayList<>();
        if (assigners.size() > 5) {
            tvSeeMoreAssigners.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5; i++) {
                mAssigners.add(assigners.get(i));
            }

        } else {
            tvSeeMoreAssigners.setVisibility(View.GONE);
            mAssigners.addAll(assigners);
        }
        AssignerCallAdapter assignerAdapter = new AssignerCallAdapter(mAssigners, assignType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskId);
        rcvAssign.setAdapter(assignerAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (taskResponse != null) {
            LatLng latLng = new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_MAP_ZOOM_LEVEL));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
            googleMap.addMarker(marker);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    animateMapView(mapFragment);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_attach:
                checkPermission();
                break;

            case R.id.img_delete:
                imgLayout.setVisibility(View.GONE);
                imgPath = null;
                break;

            case R.id.img_attached:
                Intent intent = new Intent(TaskDetailActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.img_send:
                doSend();
                break;

            case R.id.tv_see_more_comment:
                doSeeMoreComment();
                break;
            case R.id.tv_see_more_bidders:
                doSeeMoreBidders();
                break;

            case R.id.tv_see_more_assigns:
                doSeeMoreAssigns();
                break;

//            case R.id.tv_cancel:
//                DialogUtils.showOkAndCancelDialog(
//                        this, getString(R.string.title_cancel_task), getString(R.string.content_cancel_task), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
//                            @Override
//                            public void onSubmit() {
//                                doCacelTask();
//                            }
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        });
//
//                break;

            case R.id.img_menu:
                showMenu(isShowCancel, isDelete, isReportTask);
                break;

        }
    }

    private void doSeeMoreAssigns() {
        Intent intent = new Intent(this, AssignersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.ASSIGNER_TYPE_EXTRA, mAssigerType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_ASSIGNER, TransitionScreen.DOWN_TO_UP);
    }

    private void doSeeMoreBidders() {
        Intent intent = new Intent(this, BiddersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.BIDDER_TYPE_EXTRA, mBidderType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_BINDDER, TransitionScreen.DOWN_TO_UP);
    }

    private void showMenu(boolean isShowCancel, boolean isDelete, boolean isReportTask) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(TaskDetailActivity.this, imgMenu);
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

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.share:
                        Utils.shareTask(TaskDetailActivity.this, taskId);
                        break;

                    case R.id.cancel_task:
                        DialogUtils.showOkAndCancelDialog(
                                TaskDetailActivity.this, getString(R.string.title_cancel_task), getString(R.string.cancel_task_content), getString(R.string.cancel_task_ok),
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
                                TaskDetailActivity.this, getString(R.string.title_delete_task), getString(R.string.content_detete_task), getString(R.string.cancel_task_ok),
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
                        Intent intent = new Intent(TaskDetailActivity.this, PostATaskActivity.class);
                        intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                        startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                    case R.id.report_task:
                        doReportTask();
                        break;

                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private void doReportTask() {
        DialogUtils.showOkAndCancelDialog(TaskDetailActivity.this, getString(R.string.report_task_title), getString(R.string.report_task_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
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
        ProgressDialogUtils.showProgressDialog(TaskDetailActivity.this);
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
                    Utils.showLongToast(TaskDetailActivity.this, getString(R.string.report_task_success), false, false);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doReportTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doReportTask();
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
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doReportTask();
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
                    Utils.showLongToast(TaskDetailActivity.this, getString(R.string.delete_task_success_msg), false, false);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                    setResult(Constants.RESULT_CODE_TASK_DELETE, intent);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDeleteTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(TaskDetailActivity.this, error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private void doCacelTask() {
        ProgressDialogUtils.showProgressDialog(this);

        ApiClient.getApiService().cancelTask(UserManager.getUserToken(), taskId).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "doCacelTask , code : " + response.code());
                LogUtils.d(TAG, "doCacelTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(TaskDetailActivity.this, getString(R.string.cancel_task_success_msg), false, false);
                    taskResponse = response.body();
                    updateRole();
                    storeTaskToDatabase();
                    onBackPressed();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCacelTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {

                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(TaskDetailActivity.this, error.message(), false, true);

//                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//                            doCacelTask();
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
                // write your logic here
                permissionGranted();
            } else {
//                snackbar = Snackbar.make(findViewById(android.R.id.content),
//                        getString(R.string.attach_image_permission_message),
//                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.attach_image_permission_confirm),
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ActivityCompat.requestPermissions(PostATaskActivity.this, permissions, Constants.PERMISSION_REQUEST_CODE);
//                            }
//                        });
//                snackbar.show();
                permissionDenied();
            }
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(TaskDetailActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(TaskDetailActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();
    }

    private void doSeeMoreComment() {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
        intent.putExtra(Constants.COMMENT_STATUS_EXTRA, commentType);
        intent.putExtra(Constants.COMMENT_VISIBILITY, layoutFooter.getVisibility());
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_COMMENT, TransitionScreen.DOWN_TO_UP);
    }

    private void doSend() {
        if (edtComment.getText().toString().trim().equals("")) {
            Utils.showLongToast(this, getString(R.string.empty_content_comment_error), true, false);
            return;
        }
        if (imgPath == null) {
            doComment();
        } else {
            doAttachImage();
        }
        updateSeeMoreComment();
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
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    ProgressDialogUtils.dismissProgressDialog();
                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

        ApiClient.getApiService().commentTask(UserManager.getUserToken(), taskId, body).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                LogUtils.d(TAG, "commentTask , code : " + response.code());
                LogUtils.d(TAG, "commentTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {

                    response.body().setTaskId(taskId);
                    comments.add(0, response.body());
                    if (comments.size() > 5) comments.remove(comments.size() - 1);
                    commentViewFull.updateData(comments);
                    taskResponse.setCommentsCount(taskResponse.getCommentsCount() + 1);
                    tvCommentCount.setText(getString(R.string.count_in_detail, taskResponse.getCommentsCount()));

                    updateSeeMoreComment();

                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private void updateSeeMoreComment() {
        if (taskResponse.getCommentsCount() > 5)
            tvSeeMore.setVisibility(View.VISIBLE);
        else tvSeeMore.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(TaskDetailActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(TaskDetailActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_RATE && resultCode == RESPONSE_CODE_RATE) {
            getData();
        } else if (requestCode == Constants.REQUEST_CODE_SEND_COMMENT && resultCode == Constants.RESULT_CODE_SEND_COMMENT) {
            if (data.getExtras().getBoolean(Constants.EXTRA_SEND_COMMENT)) {
                LogUtils.d(TAG, "is Send " + data.getExtras().getBoolean(Constants.EXTRA_SEND_COMMENT));
                getData();
            }
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
        } else if (requestCode == Constants.REQUEST_CODE_SEND_BINDDER && resultCode == Constants.RESULT_CODE_BIDDER) {
            if (data.hasExtra(Constants.EXTRA_BIDDER_TASKRESPONSE)) {
                taskResponse = (TaskResponse) data.getExtras().get(Constants.EXTRA_BIDDER_TASKRESPONSE);
                updateUi();
            }
        } else if (requestCode == Constants.REQUEST_CODE_SEND_ASSIGNER && resultCode == Constants.RESULT_CODE_ASSIGNER) {
            if (data.hasExtra(Constants.EXTRA_ASSIGNER_TASKRESPONSE)) {
                taskResponse = (TaskResponse) data.getExtras().get(Constants.EXTRA_ASSIGNER_TASKRESPONSE);
                updateUi();
            }
        }

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if (intent.hasExtra(Constants.COMMENT_EXTRA)) {
                Comment comment = (Comment) intent.getSerializableExtra(Constants.COMMENT_EXTRA);
                LogUtils.d(TAG, "broadcastReceiver , comment : " + comment.toString());
                edtComment.setText(getString(R.string.task_detail_reply_member, comment.getFullName()));
                edtComment.setSelection(edtComment.getText().length());
            } else if (intent.hasExtra(Constants.EXTRA_TASK)) {
                taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
                updateRole();
                LogUtils.d(TAG, "broadcastReceiver , taskResponse : " + taskResponse.toString());
                updateUi();
                storeTaskToDatabase();
            } else if (intent.hasExtra(Constants.ASSIGNER_RATE_EXTRA)) {
                Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_RATE_EXTRA);
                Intent intentRate = new Intent(TaskDetailActivity.this, RateActivity.class);
                intentRate.putExtra(Constants.TASK_ID_EXTRA, taskId);
                intentRate.putExtra(Constants.USER_ID_EXTRA, assigner.getId());
                intentRate.putExtra(Constants.AVATAR_EXTRA, assigner.getAvatar());
                intentRate.putExtra(Constants.NAME_EXTRA, assigner.getFullName());
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            } else if (intent.hasExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA)) {
                DialogUtils.showOkAndCancelDialog(TaskDetailActivity.this, getString(R.string.cancel_bid_title), getString(R.string.cancel_bid_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA);
                        doCancelBid(assigner);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
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

        ApiClient.getApiService().cancelBid(UserManager.getUserToken(), taskId, body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {

                LogUtils.d(TAG, "doCancelBid onResponse : " + response.body());
                LogUtils.d(TAG, "doCancelBid code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    updateRole();
                    updateUi();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(TaskDetailActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCancelBid(assigner);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(TaskDetailActivity.this);
                } else {
                    DialogUtils.showRetryDialog(TaskDetailActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_TASK, taskResponse);
        setResult(Constants.RESULT_CODE_TASK_EDIT, intent);
        finish();
    }
}