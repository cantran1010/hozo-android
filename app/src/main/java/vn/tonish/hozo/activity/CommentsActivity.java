package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
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
    public final static int LIMIT = 10;
    protected LinearLayoutManager linearLayoutManager;
    private CommentsAdapter commentsAdapter;
    protected RecyclerView lvList;
    private List<Comment> mComments = new ArrayList<>();
    private int taskId = 0;
    private String since;
    private Date sinceDate;
    private boolean isLoadingMoreFromServer = true;
    private boolean isLoadingMoreFromDb = true;
    private boolean isLoadingFromServer = false;
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
        getCacheDataPage();
        getComments(false);

    }

    private void getCacheDataPage() {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<Comment> comments = CommentsManager.getCommentsSince(sinceDate, taskId);
        if (comments.size() > 0)
            sinceDate = comments.get(comments.size() - 1).getCreatedDateAt();
        if (comments.size() < LIMIT) isLoadingMoreFromDb = false;
        mComments.addAll(comments);
        refreshList();

    }

    private void getComments(final boolean isSince) {
        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();
        if (isSince && since != null)
            params.put("since", since);
        params.put("limit", LIMIT + "");
        ApiClient.getApiService().getComments(UserManager.getUserToken(), taskId, params).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                LogUtils.d(TAG, "getComments code : " + response.code());
                LogUtils.d(TAG, "getComments body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<Comment> comments = response.body();
                    if (comments.size() > 0)
                        since = comments.get(comments.size() - 1).getCreatedAt();
                    for (Comment comment : comments) {
                        comment.setTaskId(taskId);
                        if (!checkContainsComments(mComments, comment))
                            mComments.add(comment);
                    }
                    if (comments.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        commentsAdapter.stopLoadMore();
                    }
                    LogUtils.d(TAG, "getComments size : " + mComments.size());

                    refreshList();
                    CommentsManager.insertComments(comments);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(isSince);
                        }
                    });

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
//                    Toast.makeText(CommentsActivity.this, error.message(), Toast.LENGTH_SHORT).show();
                    Utils.showLongToast(CommentsActivity.this, error.message(), false, true);
                }
                isLoadingFromServer = false;
                onStopRefresh();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                LogUtils.e(TAG, "getComments , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(CommentsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getComments(isSince);
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
            commentsAdapter = new CommentsAdapter(CommentsActivity.this, mComments);
            linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
            lvList.setLayoutManager(linearLayoutManager);
            commentsAdapter.setCommentType(commentType);
            lvList.setAdapter(commentsAdapter);

            lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage();
                    if (isLoadingMoreFromServer) getComments(true);

                }
            });

        } else {
            lvList.post(new Runnable() {
                public void run() {
                    commentsAdapter.notifyDataSetChanged();
                }
            });

        }

        LogUtils.d(TAG, "refreshList , comments size : " + mComments.size());

    }


    private boolean checkContainsComments(List<Comment> comments, Comment comment) {
        for (int i = 0; i < comments.size(); i++)
            if (mComments.get(i).getId() == comment.getId()) return true;
        return false;
    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        since = null;
        getComments(false);
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