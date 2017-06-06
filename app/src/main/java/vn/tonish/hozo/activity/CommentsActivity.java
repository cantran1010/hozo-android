package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CommentsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.CommentsManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

public class CommentsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    public final static int LIMIT = 20;
    private CommentsAdapter commentsAdapter;
    protected RecyclerView lvList;
    private List<Comment> mComments;
    private int taskId = 0;
    private String strSince;
    private boolean isLoadingMoreFromServer = true;
    private LinearLayoutManager lvManager;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Call<List<Comment>> call;
    String commentType = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        taskId = getIntent().getExtras().getInt(Constants.TASK_ID_EXTRA);
        commentType = getIntent().getStringExtra(Constants.COMMENT_STATUS_EXTRA);
        LogUtils.d(TAG, "intent :" + taskId + ": " + commentType);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mComments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, mComments);
        lvManager = new LinearLayoutManager(this);
        lvList.setLayoutManager(lvManager);
        commentsAdapter.setCommentType(commentType);
        lvList.setAdapter(commentsAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
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
        call = ApiClient.getApiService().getComments(UserManager.getUserToken(), taskId, params);
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
                    if (comments.size() > 0)
                        strSince = comments.get(comments.size() - 1).getCreatedAt();
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
                    CommentsManager.insertComments(comments);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(since);
                        }
                    });

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(CommentsActivity.this, error.message(), false, true);
                }
                onStopRefresh();
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
                onStopRefresh();
                commentsAdapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        commentsAdapter.onLoadMore();
        strSince = null;
        getComments(null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }

    }

}