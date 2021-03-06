package vn.tonish.hozo.activity.comment;

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
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.adapter.CommentsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.PxUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

public class CommentsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    private final static int LIMIT = 20;
    private CommentsAdapter commentsAdapter;
    private RecyclerView lvList;
    private EdittextHozo edtComment;
    private RelativeLayout imgLayout;
    private LinearLayout layoutFooter;
    private ImageView imgAttached;
    private List<Comment> mComments;
    private int taskId = 0;
    private int tempId = 0;
    private File fileAttach;
    private String imgPath;
    private String strSince;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean isLoadingMoreFromServer = true;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private int commentType = 0;
    private boolean isSend = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);
        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);
        layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);
        imgAttached = (ImageView) findViewById(R.id.img_attached);
        ImageView imgDelete = (ImageView) findViewById(R.id.img_delete);
        ImageView imgComment = (ImageView) findViewById(R.id.img_send);
        ImageView imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);
        imgAttached.setOnClickListener(this);
        imgComment.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        taskId = getIntent().getExtras().getInt(Constants.TASK_ID_EXTRA);
        commentType = getIntent().getIntExtra(Constants.COMMENT_STATUS_EXTRA, 0);
        int vilibisity = getIntent().getIntExtra(Constants.COMMENT_VISIBILITY, 0);
        LogUtils.d(TAG, "intent :" + taskId + ": " + commentType);
        setUpRecyclerView();
        if (View.VISIBLE == vilibisity) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }
    }

    private void setUpRecyclerView() {
        mComments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, mComments);
        LinearLayoutManager lvManager = new LinearLayoutManager(this);
        lvManager.setReverseLayout(true);
        lvManager.setStackFromEnd(true);
        lvList.setLayoutManager(lvManager);
        commentsAdapter.setCommentType(commentType);
        lvList.setAdapter(commentsAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isLoadingMoreFromServer) getComments(strSince);
            }
        };

        lvList.addOnScrollListener(endlessRecyclerViewScrollListener);

    }


    private void getComments(final String since) {
        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();
        if (since != null)
            params.put("since", since);
        params.put("limit", LIMIT + "");
        Call<List<Comment>> call = ApiClient.getApiService().getComments(UserManager.getUserToken(), taskId, params);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                LogUtils.d(TAG, "getComments code : " + response.code());
                LogUtils.d(TAG, "getComments body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (since == null) {
                        mComments.clear();
                        endlessRecyclerViewScrollListener.resetState();
                    }
                    List<Comment> comments = response.body();
                    if ((comments != null ? comments.size() : 0) > 0)
                        strSince = comments.get((comments != null ? comments.size() : 0) - 1).getCreatedAt();
                    for (Comment comment : comments) {
                        comment.setTaskId(taskId);
                    }
                    mComments.addAll(comments);
                    commentsAdapter.notifyDataSetChanged();
                    if (comments.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        commentsAdapter.stopLoadMore();
                    }
                    LogUtils.d(TAG, "getComments size : " + mComments.size());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(since);
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CommentsActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(CommentsActivity.this, error.message(), false, true);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                LogUtils.e(TAG, "getComments , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        commentsAdapter.onLoadMore();
                        getComments(since);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                commentsAdapter.stopLoadMore();
                commentsAdapter.notifyDataSetChanged();

            }
        });
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

    private void doSend() {
        isSend = true;
        if (edtComment.getText().toString().trim().equals("")) {
            Utils.showLongToast(this, getString(R.string.empty_content_comment_error), true, false);
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

        ApiClient.getApiService().commentTask(UserManager.getUserToken(), taskId, body).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                LogUtils.d(TAG, "commentTask , code : " + response.code());
                LogUtils.d(TAG, "commentTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {

                    response.body().setTaskId(taskId);

                    mComments.add(0, response.body());
                    commentsAdapter.notifyDataSetChanged();
                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CommentsActivity.this);
                } else {
                    DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CommentsActivity.this);
                } else {
                    ProgressDialogUtils.dismissProgressDialog();
                    DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImageRounded(this, imgAttached, imgPath, (int) PxUtils.pxFromDp(this, 10), 0);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImageRounded(this, imgAttached, imgPath, (int) PxUtils.pxFromDp(this, 10), 0);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        }

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
                permissionDenied();
            }
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(CommentsActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(CommentsActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
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

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra(Constants.COMMENT_EXTRA)) {
                Comment comment = (Comment) intent.getSerializableExtra(Constants.COMMENT_EXTRA);
                LogUtils.d(TAG, "broadcastReceiver , comment : " + comment.toString());
                edtComment.setText(getString(R.string.task_detail_reply_member, comment.getFullName()));
                edtComment.setSelection(edtComment.getText().length());
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent in = new Intent();
        in.putExtra(Constants.EXTRA_SEND_COMMENT, isSend);
        setResult(Constants.RESULT_CODE_SEND_COMMENT, in);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent in = new Intent();
                in.putExtra(Constants.EXTRA_SEND_COMMENT, isSend);
                setResult(Constants.RESULT_CODE_SEND_COMMENT, in);
                finish();
                break;
            case R.id.img_send:
                doSend();
                break;
            case R.id.img_delete:
                imgLayout.setVisibility(View.GONE);
                imgPath = null;
                break;
            case R.id.img_attach:
                checkPermission();
                break;
            case R.id.img_attached:
                Intent intent = new Intent(CommentsActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;
        }

    }

}