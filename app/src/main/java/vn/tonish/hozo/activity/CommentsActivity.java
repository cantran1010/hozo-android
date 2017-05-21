package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
import vn.tonish.hozo.adapter.CommentsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.CommentsManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

public class CommentsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    public final static int LIMIT = 10;
    protected LinearLayoutManager linearLayoutManager;
    private CommentsAdapter commentsAdapter;
    private RelativeLayout imgLayout;
    protected RecyclerView lvList;
    private EdittextHozo edtComment;
    private ImageView imgAttach, imgAttached, imgDelete, imgComment;
    private List<Comment> mComments = new ArrayList<>();
    private File fileAttach;
    private String imgPath;
    private String since;
    private Date sinceDate;
    private int tempId = 0;
    private int taskId = 123;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA};

    @Override
    protected int getLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initView() {
        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);
        lvList = (RecyclerView) findViewById(R.id.lvList);
        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);
        imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgAttached = (ImageView) findViewById(R.id.img_attached);
        imgComment = (ImageView) findViewById(R.id.img_send);
        imgComment.setOnClickListener(this);
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        imgAttach.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        imgAttached.setOnClickListener(this);
        imgComment.setOnClickListener(this);
        getCacheDataFirstPage();
        getComments(false, 0);

    }

    private void getCacheDataFirstPage() {
        LogUtils.d(TAG, "getCacheDataFirstPage start");
        List<Comment> comments = CommentsManager.getFirstPage();
        if (comments.size() > 0)
            sinceDate = comments.get(comments.size() - 1).getCraeatedDateAt();
        mComments.addAll(comments);

        if (comments.size() < LIMIT) isLoadingMoreFromDb = false;
        refreshList();
    }

    private void getCacheDataPage() {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<Comment> comments = CommentsManager.getCommentsSince(sinceDate);

        if (comments.size() > 0)
            sinceDate = comments.get(comments.size() - 1).getCraeatedDateAt();

        if (comments.size() < LIMIT) isLoadingMoreFromDb = false;

        mComments.addAll(comments);
        refreshList();
    }

    public void getComments(final boolean isSince, int taskId) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        commentsAdapter.stopLoadMore();

        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();

        if (isSince && since != null)
            params.put("since", "2017-04-24T07:13:41+07:00");
        params.put("limit", LIMIT + "");

        ApiClient.getApiService().getCommens(UserManager.getUserToken(), taskId, params).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                LogUtils.d(TAG, "getCommens code : " + response.code());
                LogUtils.d(TAG, "getCommens body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<Comment> comments = response.body();
                    for (int i = 0; i < comments.size(); i++) {
                        if (!checkContainsComments(mComments, comments.get(i)))
                            mComments.add(comments.get(i));
                    }
                    since = comments.get(comments.size() - 1).getCreatedAt();

                    if (comments.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        commentsAdapter.stopLoadMore();
                    }
                    refreshList();
                    CommentsManager.insertComments(comments);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(isSince, 123);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getComments(isSince, 123);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                isLoadingFromServer = false;
                onStopRefresh();


            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                LogUtils.e(TAG, "getCommens , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getComments(isSince, 123);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                commentsAdapter.stopLoadMore();
                isLoadingFromServer = false;
                onStopRefresh();


            }
        });
    }

    private void refreshList() {
        if (commentsAdapter == null) {
            commentsAdapter = new CommentsAdapter(CommentsActivity.this, (ArrayList<Comment>) mComments);
            linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
            lvList.setLayoutManager(linearLayoutManager);
            lvList.setAdapter(commentsAdapter);

            lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage();
                    if (isLoadingMoreFromServer) getComments(true, 0);

                }
            });

        } else {
            commentsAdapter.notifyDataSetChanged();
        }

        LogUtils.d(TAG, "refreshList , comments size : " + mComments.size());

    }


    private boolean checkContainsComments(List<Comment> comments, Comment comment) {
        for (int i = 0; i < comments.size(); i++)
            if (mComments.get(i).getId() == comment.getId()) return true;
        return false;
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
                    mComments.add(response.body());
                    if (mComments.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        commentsAdapter.stopLoadMore();
                    }
                    refreshList();
                    CommentsManager.insertComments(mComments);
                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
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
                    NetworkUtils.RefreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else {
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
            }
        });
    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getComments(false, 0);
    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    private void permissionGranted() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
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
            Utils.displayImage(CommentsActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(CommentsActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_attach:
                PickImageDialog pickImageDialog = new PickImageDialog(CommentsActivity.this);
                pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                    @Override
                    public void onCamera() {
                        checkPermission();
                    }

                    @Override
                    public void onGallery() {
                        Intent intent = new Intent(CommentsActivity.this, AlbumActivity.class);
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
                Intent intent = new Intent(CommentsActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.img_send:
                doSend();
                break;


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
