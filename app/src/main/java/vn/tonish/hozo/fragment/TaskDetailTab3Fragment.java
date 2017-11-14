package vn.tonish.hozo.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import vn.tonish.hozo.activity.CommentsAnswerActivity;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.task_detail.TaskDetailNewActivity;
import vn.tonish.hozo.adapter.CommentTaskAdapter;
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
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;

/**
 * Created by LongBui on 8/22/17.
 */

public class TaskDetailTab3Fragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = TaskDetailTab3Fragment.class.getSimpleName();
    public final static int LIMIT = 20;
    private RecyclerView rcvComment;
    private ArrayList<Comment> mComments = new ArrayList<>();
    private TaskResponse taskResponse;
    private CommentTaskAdapter commentsAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private String strSince;
    private boolean isLoadingMoreFromServer = true;
    private boolean isSend = false;
    private EdittextHozo edtComment;
    private String imgPath;
    private int tempId = 0;
    private RelativeLayout imgLayout;
    private ImageView imgAttached;
    private File fileAttach;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private LinearLayout layoutFooter;
    private TextViewHozo tvNoComment;
    private Call<List<Comment>> call;

    @Override
    protected int getLayout() {
        return R.layout.task_detail_tab3_layout;
    }

    @Override
    protected void initView() {

        createSwipeToRefresh();

        rcvComment = (RecyclerView) findViewById(R.id.rcv_comment);
        edtComment = (EdittextHozo) findViewById(R.id.edt_comment);
        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);
        imgAttached = (ImageView) findViewById(R.id.img_attached);

        ImageView imgDelete = (ImageView) findViewById(R.id.img_delete);
        ImageView imgComment = (ImageView) findViewById(R.id.img_send);
        ImageView imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);
        imgAttached.setOnClickListener(this);
        imgComment.setOnClickListener(this);
        imgDelete.setOnClickListener(this);

        layoutFooter = (LinearLayout) findViewById(R.id.layout_footer);

        tvNoComment = (TextViewHozo) findViewById(R.id.tv_no_comment);
    }

    @Override
    protected void initData() {
        taskResponse = ((TaskDetailNewActivity) getActivity()).getTaskResponse();
        updateInputComment();
        setUpRecyclerView();
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if (getActivity() instanceof TaskDetailNewActivity) {

                if (((TaskDetailNewActivity) getActivity()).getCommentCount() != 0) {
                    ((TaskDetailNewActivity) getActivity()).updateCommentCount(0);

                    if (call != null) call.cancel();
                    isLoadingMoreFromServer = true;
                    strSince = null;
                    commentsAdapter.onLoadMore();
                    getComments(null);
                }

            }
        }
    }

    private void updateInputComment() {
        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            layoutFooter.setVisibility(View.GONE);
        }
        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            layoutFooter.setVisibility(View.GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            layoutFooter.setVisibility(View.GONE);
        } else {
            layoutFooter.setVisibility(View.VISIBLE);
        }

    }

    private void setUpRecyclerView() {
        mComments = new ArrayList<>();
        commentsAdapter = new CommentTaskAdapter(getActivity(), mComments);
        final LinearLayoutManager lvManager = new LinearLayoutManager(getActivity());
        lvManager.setReverseLayout(true);
        lvManager.setStackFromEnd(true);
        rcvComment.setLayoutManager(lvManager);
        commentsAdapter.setCommentType(layoutFooter.getVisibility());
        rcvComment.setAdapter(commentsAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isLoadingMoreFromServer) getComments(strSince);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);

                LogUtils.d(TAG, "manager.findFirstCompletelyVisibleItemPosition()" + lvManager.findFirstCompletelyVisibleItemPosition());
                LogUtils.d(TAG, "manager.findLastCompletelyVisibleItemPosition()" + lvManager.findLastCompletelyVisibleItemPosition());

                if (lvManager.findLastCompletelyVisibleItemPosition() == mComments.size() - 1)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        };

        rcvComment.addOnScrollListener(endlessRecyclerViewScrollListener);

        commentsAdapter.setAnswerListener(new CommentTaskAdapter.AnswerListener() {
            @Override
            public void onAnswer(int position) {
                Intent intentAnswer = new Intent(getContext(), CommentsAnswerActivity.class);
                String commentType = getContext().getString(R.string.comment_setting_visible);
//                intentAnswer.putExtra(Constants.TASK_ID_EXTRA, mComments.get(position).getTaskId());
//                intentAnswer.putExtra(Constants.COMMENT_STATUS_EXTRA, commentType);
//                intentAnswer.putExtra(Constants.COMMENT_VISIBILITY, View.VISIBLE);
                intentAnswer.putExtra(Constants.COMMENT_EXTRA, mComments.get(position));
                intentAnswer.putExtra(Constants.COMMENT_INPUT_EXTRA, layoutFooter.getVisibility());
                startActivityForResult(intentAnswer, Constants.COMMENT_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });

    }

    private void getComments(final String since) {
        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();
        if (since != null)
            params.put("since", since);
        params.put("limit", LIMIT + "");
        LogUtils.d(TAG, "getComments params : " + params.toString());

        call = ApiClient.getApiService().getComments(UserManager.getUserToken(), taskResponse.getId(), params);
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
                    mComments.addAll(comments);
                    commentsAdapter.notifyDataSetChanged();
                    if (comments.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        commentsAdapter.stopLoadMore();
                    }
                    LogUtils.d(TAG, "getComments size : " + mComments.size());

                    if (since == null) {
                        rcvComment.smoothScrollToPosition(0);

                        Intent intentComment = new Intent();
                        intentComment.setAction(Constants.BROAD_CAST_MY);
                        intentComment.putExtra(Constants.COMMENT_EXTRA, 0);

                        if (getActivity() != null)
                            getActivity().sendBroadcast(intentComment);

                    }

                    if (mComments.size() == 0)
                        tvNoComment.setVisibility(View.VISIBLE);
                    else
                        tvNoComment.setVisibility(View.GONE);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(since);
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), false, true);
                }

                onStopRefresh();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                LogUtils.e(TAG, "getComments , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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
                onStopRefresh();
            }
        });
    }

    private void doSend() {
        isSend = true;
        if (edtComment.getText().toString().trim().equals("")) {
            Utils.showLongToast(getActivity(), getString(R.string.empty_content_comment_error), true, false);
            return;
        } else if (!Utils.validateInput(getActivity(), edtComment.getText().toString().trim())) {
            Utils.showLongToast(getActivity(), getString(R.string.post_a_task_input_error), true, false);
            return;
        }

        if (imgPath == null) {
            doComment();
        } else {
            doAttachImage();
        }
    }

    private void doAttachImage() {
        ProgressDialogUtils.showProgressDialog(getActivity());

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
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    ProgressDialogUtils.dismissProgressDialog();
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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
        ProgressDialogUtils.showProgressDialog(getActivity());
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
//                    mComments.add(0, response.body());
//                    commentsAdapter.notifyDataSetChanged();
                    imgPath = null;
                    edtComment.setText(getString(R.string.empty));
                    imgLayout.setVisibility(View.GONE);

                    strSince = null;
                    isLoadingMoreFromServer = true;
                    getComments(null);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doComment();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.status().equals(Constants.DUPLICATE_COMMENT)) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.error), getString(R.string.comment_duplicate_error), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
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
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult , requestCode : " + requestCode + " , resultCode : " + resultCode);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(getActivity(), imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
            fileAttach = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(getActivity(), imgAttached, imgPath);
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

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, Constants.PERMISSION_REQUEST_CODE);
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
                permissionGranted();
            } else {
                permissionDenied();
            }
        }
    }

    private void permissionDenied() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        swipeRefreshLayout.setEnabled(true);
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        strSince = null;
        commentsAdapter.onLoadMore();
        getComments(null);
//
//        Intent intentComment = new Intent();
//        intentComment.setAction(Constants.BROAD_CAST_MY);
//        intentComment.putExtra(Constants.COMMENT_EXTRA, 0);
//        getActivity().sendBroadcast(intentComment);
    }

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(getActivity());
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(getActivity(), AlbumActivity.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                Intent intent = new Intent(getActivity(), PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;
        }
    }

}
