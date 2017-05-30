package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

public class CommentsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    public final static int LIMIT = 10;
    protected LinearLayoutManager linearLayoutManager;
    private CommentsAdapter commentsAdapter;
    protected RecyclerView lvList;
    private ImageView imgBack;
    private List<Comment> mComments = new ArrayList<>();
    private String since;
    private Date sinceDate;
    private int taskId = 0;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        getCacheDataFirstPage();
        getComments(false, taskId);

    }

    private void getCacheDataFirstPage() {
        LogUtils.d(TAG, "getCacheDataFirstPage start");
        List<Comment> comments = CommentsManager.getFirstPage(taskId);
        if (comments.size() > 0)
            sinceDate = comments.get(comments.size() - 1).getCreatedDateAt();
        mComments.addAll(comments);
        refreshList();
        if (comments.size() < LIMIT) isLoadingMoreFromDb = false;
    }

    private void getCacheDataPage() {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<Comment> comments = CommentsManager.getCommentsSince(sinceDate, taskId);
        if (comments.size() > 0)
            sinceDate = comments.get(comments.size() - 1).getCreatedDateAt();
        mComments.addAll(comments);
        refreshList();
        if (comments.size() < LIMIT) isLoadingMoreFromDb = false;

    }

    public void getComments(final boolean isSince, final int taskId) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        commentsAdapter.stopLoadMore();

        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();

        if (isSince && since != null)
            params.put("since", since);
        params.put("limit", LIMIT + "");

        ApiClient.getApiService().getCommens(UserManager.getUserToken(), taskId, params).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                LogUtils.d(TAG, "getCommens code : " + response.code());

                LogUtils.d(TAG, "getCommens body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (response.body() != null || response.body().size() > 0) {
                        List<Comment> comments = response.body();
                        if (comments.size() > 0)
                            since = comments.get(comments.size() - 1).getCreatedAt();
                        for (Comment comment : comments) {
                            if (!checkContainsComments(mComments, comment))
                                mComments.add(comment);
                        }
                        commentsAdapter.notifyDataSetChanged();
                        CommentsManager.insertComments(comments);
                        if (comments.size() < LIMIT) {
                            isLoadingMoreFromServer = false;
                            commentsAdapter.stopLoadMore();
                        }
                        refreshList();
                    }

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CommentsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getComments(isSince, taskId);
                        }
                    });

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Toast.makeText(CommentsActivity.this, error.message(), Toast.LENGTH_SHORT).show();
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
                        getComments(isSince, taskId);
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
                    if (isLoadingMoreFromServer) getComments(true, taskId);

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


    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getComments(false, taskId);
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