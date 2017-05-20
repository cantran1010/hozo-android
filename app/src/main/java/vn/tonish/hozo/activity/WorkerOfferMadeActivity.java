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
import android.view.View;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.TaskStatus;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.CancelOfferResponse;
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

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;


/**
 * Created by LongBui on 4/25/2017.
 */

public class WorkerOfferMadeActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {


    private static final String TAG = PosterOpenTaskActivity.class.getSimpleName();
    private CommentViewFull commentViewFull;
    private WorkDetailView workDetailView;
    private ArrayList<Comment> comments = new ArrayList<>();
    private TaskResponse taskResponse;

    private ImageView imgAttach, imgAttached, imgDelete;
    private EdittextHozo edtComment;
    private RelativeLayout imgLayout;
    private String imgPath;
    private ScrollView scv;
    private ImageView imgBack;

    private ImageView imgComment;

    private int taskId = 123;
    private GoogleMap googleMap;
    private int tempId = 0;
    private File fileAttach;
    private TextViewHozo tvSeeMore;
    private TextViewHozo tvCancel;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA};
    private TaskStatus taskStatus;

    @Override
    protected int getLayout() {
        return R.layout.worker_offer_made_activity;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);
        commentViewFull = (CommentViewFull) findViewById(R.id.comment_view_full);

        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);

        imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);

        imgAttached = (ImageView) findViewById(R.id.img_attached);
        imgAttached.setOnClickListener(this);

        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(this);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);

        imgComment = (ImageView) findViewById(R.id.img_send);
        imgComment.setOnClickListener(this);

        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        tvSeeMore = (TextViewHozo) findViewById(R.id.tv_see_more_comment);
        tvSeeMore.setOnClickListener(this);

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

        taskStatus = (TaskStatus) getIntent().getSerializableExtra(Constants.TASK_STATUS_EXTRA);
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);

        //test data
        taskStatus = TaskStatus.WorkerDoneTask;

        switch (taskStatus) {

            case WorkerOfferMade:
                workDetailView.updateBtnOffer(false);
                workDetailView.updateStatus(getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
                workDetailView.updateBtnCallRate(false, false, "");
                workDetailView.updateTaskProgressViewVisibility(false);
                break;

            case WorkerAcceptedTask:
                workDetailView.updateBtnOffer(false);
                workDetailView.updateStatus(getString(R.string.received), ContextCompat.getDrawable(this, R.drawable.bg_border_received));
                workDetailView.updateBtnCallRate(true, true, getString(R.string.call));
                workDetailView.updateTaskProgressViewVisibility(false);
                break;

            case WorkerDoneTask:
                workDetailView.updateBtnOffer(false);
                workDetailView.updateStatus(getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
                workDetailView.updateBtnCallRate(true, false, getString(R.string.rate));
                workDetailView.updateTaskProgressViewVisibility(false);
                break;

        }

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
        TaskEntity taskEntity = TaskManager.getTaskById(this, 123);
        if (taskEntity != null) {
            taskResponse = DataParse.converTaskEntityToTaskReponse(taskEntity);
            updateUi();
        }
    }

    private void getData() {
        ProgressDialogUtils.showProgressDialog(this);

        Map<String, String> params = new HashMap<>();
        params.put("id", taskId + "");

        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), params).enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body().get(0);
                    updateUi();
                    storeTaskToDatabase();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(WorkerOfferMadeActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
                DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private void updateUi() {
        workDetailView.updateWork(taskResponse);

        if (googleMap != null) {
            LatLng latLng = new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_MAP_ZOOM_LEVEL));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
            googleMap.addMarker(marker);
        }

        //update comments
        comments = (ArrayList<Comment>) taskResponse.getComments();
        commentViewFull.updateData(comments);
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
                PickImageDialog pickImageDialog = new PickImageDialog(WorkerOfferMadeActivity.this);
                pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                    @Override
                    public void onCamera() {
                        checkPermission();
                    }

                    @Override
                    public void onGallery() {
                        Intent intent = new Intent(WorkerOfferMadeActivity.this, AlbumActivity.class);
                        intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
                    }
                });
                pickImageDialog.showView();
                break;

            case R.id.img_delete:
                imgLayout.setVisibility(View.GONE);
                imgPath = null;
                break;

            case R.id.img_attached:
                Intent intent = new Intent(WorkerOfferMadeActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.img_send:
                doSend();
                break;

            case R.id.tv_see_more_comment:
                doSeeMoreComment();
                break;

            case R.id.tv_cancel:
                DialogUtils.showOkAndCancelDialog(
                        this, getString(R.string.title_cancel_offer), getString(R.string.content_cancel_offer), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                doCancelOfferTask();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                break;

        }
    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != Constants.PERMISSION_REQUEST_CODE
                || grantResults.length == 0
                || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }


    private void doCancelOfferTask() {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAM_UPDATE_TASK, Constants.PARAM_UPDATE_TASK_CANCEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doCancelOfferTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().cancelOffer(UserManager.getUserToken(), taskId, body).enqueue(new Callback<CancelOfferResponse>() {
            @Override
            public void onResponse(Call<CancelOfferResponse> call, Response<CancelOfferResponse> response) {
                LogUtils.d(TAG, "doCancelOfferTask , code : " + response.code());
                LogUtils.d(TAG, "doCancelOfferTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(WorkerOfferMadeActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCancelOfferTask();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doCancelOfferTask();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<CancelOfferResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doCancelOfferTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void doSeeMoreComment() {

    }

    private void doSend() {
        if (edtComment.getText().toString().trim().equals("")) {
            Utils.showLongToast(this, getString(R.string.empty_content_comment_error));
            return;
        }
        if (imgPath == null) {
            doComment();
        } else {
            doAttachImage();
        }
    }

    private void doAttachImage() {
        ProgressDialogUtils.showProgressDialog(this);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileAttach);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileAttach.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    tempId = imageResponse.getIdTemp();
                    doComment();
                    FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(WorkerOfferMadeActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAttachImage();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doAttachImage();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
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
                    comments.add(response.body());
                    commentViewFull.updateData(comments);

                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(WorkerOfferMadeActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(WorkerOfferMadeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    public Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(WorkerOfferMadeActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(WorkerOfferMadeActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        }

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Comment comment = (Comment) intent.getSerializableExtra(Constants.COMMENT_EXTRA);
            LogUtils.d(TAG, "broadcastReceiver , comment : " + comment.toString());
            edtComment.setText("@" + comment.getFullName() + " \n");

            edtComment.setSelection(edtComment.getText().length());
        }
    };
}
