package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CommentAdapter;
import vn.tonish.hozo.database.manager.CommentsManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;

public class CommentsActivity extends BaseActivity {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    public final static int LIMIT = 20;


    protected LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;
    protected RecyclerView lvList;
    private List<Comment> mComments = new ArrayList<>();
    private String since;
    private Date sinceDate;
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
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        getCacheDataFirstPage();
        getComments(false,0);

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

    public void getComments(final boolean isSince,int taskId) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        commentAdapter.stopLoadMore();

        LogUtils.d(TAG, "getComments start");
        Map<String, String> params = new HashMap<>();

        if (isSince && since != null)
            params.put("since", since);

        params.put("limit", LIMIT + "");

        ApiClient.getApiService().getCommens(UserManager.getUserToken(), taskId, params).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }

    private void refreshList() {
        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(CommentsActivity.this, (ArrayList<Comment>) mComments);
            linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
            lvList.setLayoutManager(linearLayoutManager);
            lvList.setAdapter(commentAdapter);

            lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage();
                    if (isLoadingMoreFromServer) getComments(true,0);

                }
            });

        } else {
            commentAdapter.notifyDataSetChanged();
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
        getComments(false,0);
    }



}
