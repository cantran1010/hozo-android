package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import vn.tonish.hozo.adapter.AssignerCallAdapter;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
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
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CommentViewFull;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;
import vn.tonish.hozo.view.WorkAroundMapFragment;
import vn.tonish.hozo.view.WorkDetailView;

import static vn.tonish.hozo.R.string.call;
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
    private TextViewHozo tvSeeMore;
    private TextViewHozo tvCommentCount, tvBidderCount, tvAssignCount;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout layoutFooter;
    private TextViewHozo tvCancel;
    private String commentType;
    private LinearLayout layoutBidderCount, layoutAssignCount;

    @Override
    protected int getLayout() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);
        commentViewFull = (CommentViewFull) findViewById(R.id.comment_view_full);

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

        tvBidderCount = (TextViewHozo) findViewById(R.id.tv_bidder_count);
        tvAssignCount = (TextViewHozo) findViewById(R.id.tv_assign_count);
        tvCommentCount = (TextViewHozo) findViewById(R.id.tv_comment_count);

        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        layoutBidderCount = (LinearLayout) findViewById(R.id.layout_bidder_count);
        layoutAssignCount = (LinearLayout) findViewById(R.id.layout_assign_count);

        layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);

        scv = (ScrollView) findViewById(R.id.scv);

        WorkAroundMapFragment mapFragment = (WorkAroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.setListener(new WorkAroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scv.requestDisallowInterceptTouchEvent(true);
            }
        });

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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void useCacheData() {
        TaskEntity taskEntity = TaskManager.getTaskById(taskId);
        if (taskEntity != null) {
            taskResponse = DataParse.converTaskEntityToTaskReponse(taskEntity);
            updateUi();
        }
    }

    private void getData() {
        ProgressDialogUtils.showProgressDialog(this);
        LogUtils.d(TAG, "getDetailTask , taskId : " + taskId);
        LogUtils.d(TAG, "getDetailTask , UserManager.getUserToken() : " + UserManager.getUserToken());

        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskId).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    updateRole();
                    updateUi();
                    storeTaskToDatabase();

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
                ProgressDialogUtils.dismissProgressDialog();
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
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

    private void storeTaskToDatabase() {
        TaskEntity taskEntity = DataParse.converTaskReponseToTaskEntity(taskResponse);
        TaskManager.insertTask(taskEntity);
    }

    private void updateRole() {
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

        tvCancel.setVisibility(View.VISIBLE);
        layoutFooter.setVisibility(View.VISIBLE);
        rcvBidder.setVisibility(View.VISIBLE);
        rcvAssign.setVisibility(View.VISIBLE);

        workDetailView.updateWork(taskResponse);

        //poster
        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.update_task), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            bidderType = getString(R.string.assign);
            assigerType = getString(R.string.call);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.delivered), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            assigerType = getString(R.string.call);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);

            assigerType = getString(R.string.rate);
            commentType = getString(R.string.comment_setting_invisible);
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        }

        //bidder

        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_overdue), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_poster_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
            workDetailView.updateBtnOffer(false);
            workDetailView.updateStatus(false, getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
            workDetailView.updateBtnCallRate(false, false, "");
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            workDetailView.updateBtnOffer(false);
            workDetailView.updateStatus(true, getString(R.string.received), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
            workDetailView.updateBtnCallRate(true, true, getString(call));
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            workDetailView.updateBtnOffer(false);
            workDetailView.updateStatus(true, getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
            workDetailView.updateBtnCallRate(true, false, getString(R.string.rate));
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_worker_missed), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            workDetailView.updateStatus(true, getString(R.string.my_task_status_worker_canceled), ContextCompat.getDrawable(this, R.drawable.bg_border_missed));
            workDetailView.updateBtnOffer(false);
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);

            layoutBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
            layoutAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);

            layoutFooter.setVisibility(View.GONE);
            commentType = getString(R.string.comment_setting_invisible);
        }

        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            workDetailView.updateBtnOffer(true);
            workDetailView.updateStatus(false, "", ContextCompat.getDrawable(this, R.drawable.bg_border_done));
            workDetailView.updateBtnCallRate(false, false, "");
            tvCancel.setVisibility(View.GONE);
        }

        workDetailView.setWorkDetailViewRateListener(new WorkDetailView.WorkDetailViewRateListener() {
            @Override
            public void onRate() {
                Intent intent = new Intent(TaskDetailActivity.this, RateActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
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
        commentViewFull.setCommentType(commentType);
        commentViewFull.updateData(comments);
        tvBidderCount.setText(getString(R.string.count_in_detail, taskResponse.getBidderCount()));
        tvAssignCount.setText(getString(R.string.count_in_detail, taskResponse.getAssigneeCount()));
        tvCommentCount.setText(getString(R.string.count_in_detail, taskResponse.getCommentsCount()));

        updateSeeMoreComment();
    }

    private void refreshBidderList(String bidderType) {
        PosterOpenAdapter posterOpenAdapter = new PosterOpenAdapter(bidders, bidderType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBidder.setLayoutManager(linearLayoutManager);
        posterOpenAdapter.setTaskId(taskId);
        rcvBidder.setAdapter(posterOpenAdapter);
    }

    private void refreshAssignerList(String assignType) {
        AssignerCallAdapter assignerAdapter = new AssignerCallAdapter(assigners, assignType);
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

            case R.id.tv_cancel:
                DialogUtils.showOkAndCancelDialog(
                        this, getString(R.string.title_cancel_task), getString(R.string.content_cancel_task), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                doCacelTask();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                break;

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
            jsonRequest.put("body", edtComment.getText().toString());
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
        }

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            }

        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_TASK, taskResponse);
        setResult(Constants.RESULT_CODE_TASK_EDIT, intent);
        finish();
    }
}