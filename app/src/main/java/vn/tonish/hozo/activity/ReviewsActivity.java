package vn.tonish.hozo.activity;

import android.content.Intent;
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
import vn.tonish.hozo.adapter.ReviewsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.R.id.lvList;

/**
 * Created by CanTran on 14/05/2017.
 */

public class ReviewsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = ReviewsActivity.class.getSimpleName();
    private final static int LIMIT = 20;
    private RecyclerView rcvReviews;
    private final List<ReviewEntity> mReviewEntities = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private String strSince;
    private boolean isLoadingMoreFromServer = true;
    private ImageView imgBack;
    private int user_id;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Call<List<ReviewEntity>> call;

    @Override
    protected int getLayout() {
        return R.layout.activity_reviews;
    }

    @Override
    protected void initView() {
        rcvReviews = (RecyclerView) findViewById(lvList);
        imgBack = (ImageView) findViewById(R.id.img_back);
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);
        Intent i = getIntent();
        user_id = i.getExtras().getInt(Constants.USER_ID);
        setUpRecyclerView();
//        getReviews(null, user_id);
    }

    @Override
    protected void resumeData() {

    }

    private void setUpRecyclerView() {
        reviewsAdapter = new ReviewsAdapter(this, mReviewEntities);
        LinearLayoutManager lvManager = new LinearLayoutManager(this);
        rcvReviews.setLayoutManager(lvManager);
        rcvReviews.setAdapter(reviewsAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isLoadingMoreFromServer) getReviews(strSince, user_id);
            }
        };

        rcvReviews.addOnScrollListener(endlessRecyclerViewScrollListener);

    }

    private void getReviews(final String since, final int userId) {
        LogUtils.d(TAG, "getReviews start");
        Map<String, String> params = new HashMap<>();
        if (since != null)
            params.put("since", "");
        params.put("limit", LIMIT + "");
        String typeReview = "worker";
        params.put("review_type", typeReview);
        call = ApiClient.getApiService().getUserReviews(UserManager.getUserToken(), userId, params);
        call.enqueue(new Callback<List<ReviewEntity>>() {
            @Override
            public void onResponse(Call<List<ReviewEntity>> call, Response<List<ReviewEntity>> response) {
                LogUtils.d(TAG, "getUserReviews code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    LogUtils.d(TAG, "getUserReviews body : " + response.body());
                    List<ReviewEntity> reviewEntities = response.body();
                    if (since == null) {
                        mReviewEntities.clear();
                        endlessRecyclerViewScrollListener.resetState();
                    }
                    if (reviewEntities.size() > 0)
                        strSince = reviewEntities.get(reviewEntities.size() - 1).getCreatedAt();
                    mReviewEntities.addAll(reviewEntities);
                    reviewsAdapter.notifyDataSetChanged();

                    if (reviewEntities.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        reviewsAdapter.stopLoadMore();
                    }
                    ReviewManager.insertReviews(reviewEntities);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ReviewsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getReviews(since, user_id);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(ReviewsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getReviews(since, user_id);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                onStopRefresh();


            }


            @Override
            public void onFailure(Call<List<ReviewEntity>> call, Throwable t) {
                LogUtils.e(TAG, "getUserReviews , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(ReviewsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        reviewsAdapter.onLoadMore();
                        getReviews(since, userId);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                reviewsAdapter.stopLoadMore();
                onStopRefresh();
                reviewsAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();

                break;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        strSince = null;
        reviewsAdapter.onLoadMore();
        getReviews(null, user_id);
    }

}
