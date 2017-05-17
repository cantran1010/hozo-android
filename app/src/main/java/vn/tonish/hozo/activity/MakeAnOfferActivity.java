package vn.tonish.hozo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import vn.tonish.hozo.adapter.AssignerAdapter;
import vn.tonish.hozo.adapter.BidderAdapter;
import vn.tonish.hozo.common.Constants;
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
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
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
 * Created by LongBui on 4/21/2017.
 */

public class MakeAnOfferActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = MakeAnOfferActivity.class.getSimpleName();
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

    private RecyclerView rcvBidder;
    private ArrayList<Bidder> bidders = new ArrayList<>();
    private BidderAdapter bidderAdapter;

    private RecyclerView rcvAssign;
    private ArrayList<Assigner> assigners = new ArrayList<>();
    private AssignerAdapter assignerAdapter;

    private ImageView imgComment;

    private int taskId = 0;
    private GoogleMap googleMap;
    private int tempId = 0;
    private File fileAttach;
    private TextViewHozo tvSeeMore;

    @Override
    protected int getLayout() {
        return R.layout.make_an_offer_activity;
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

        rcvBidder = (RecyclerView) findViewById(R.id.rcv_bidders);
        rcvAssign = (RecyclerView) findViewById(R.id.rcv_assign);

        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);

        imgComment = (ImageView) findViewById(R.id.img_send);
        imgComment.setOnClickListener(this);

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
        workDetailView.updateBtnCallRate(false, false, getString(R.string.empty));
        workDetailView.updateStatus(getString(R.string.recruitment), ContextCompat.getDrawable(this, R.drawable.bg_border_recruitment));
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
                    storeTaskToDatabase();
                    updateUi();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(MakeAnOfferActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

        // update bidder list
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        refreshBidderList();

        //update assigners list
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        refreshAssignerList();

        //update comments
        comments = (ArrayList<Comment>) taskResponse.getComments();
        commentViewFull.updateData(comments);

    }

    private void refreshAssignerList() {
        assignerAdapter = new AssignerAdapter(assigners);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        rcvAssign.setAdapter(assignerAdapter);
    }

    private void refreshBidderList() {
        bidderAdapter = new BidderAdapter(bidders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBidder.setLayoutManager(linearLayoutManager);
        rcvBidder.setAdapter(bidderAdapter);
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

                PickImageDialog pickImageDialog = new PickImageDialog(MakeAnOfferActivity.this);
                pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                    @Override
                    public void onCamera() {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                    }

                    @Override
                    public void onGallery() {
                        Intent intent = new Intent(MakeAnOfferActivity.this, AlbumActivity.class);
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
                Intent intent = new Intent(MakeAnOfferActivity.this, PreviewImageActivity.class);
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

        }
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
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(MakeAnOfferActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                    NetworkUtils.RefreshToken(MakeAnOfferActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(MakeAnOfferActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
            Utils.displayImage(MakeAnOfferActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(MakeAnOfferActivity.this, imgAttached, imgPath);
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
