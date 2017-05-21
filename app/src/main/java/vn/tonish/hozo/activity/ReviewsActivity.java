package vn.tonish.hozo.activity;

import android.content.Intent;
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
import vn.tonish.hozo.adapter.ReviewsAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.R.id.lvList;
import static vn.tonish.hozo.fragment.BrowseTaskFragment.limit;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class ReviewsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = ReviewsActivity.class.getSimpleName();
    public final static int LIMIT = 10;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rcvReviews;
    private List<ReviewEntity> mReviewEntities = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private String since;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;
    private Date sinceDate;
    private ImageView imgBack;
    private String typeReview = "worker";
    private int user_id = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_reviews;
    }

    @Override
    protected void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        rcvReviews = (RecyclerView) findViewById(lvList);
        imgBack = (ImageView) findViewById(R.id.img_back);
        createSwipeToRefresh();

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);
        Intent i = getIntent();
//        typeReview = i.getExtras().getString(Constants.REVIEW_TYPE_POSTER);
//        user_id = i.getExtras().getInt(Constants.USER_ID);
        getCacheDataFirstPage();
        getReviews(false, user_id);


    }

    @Override
    protected void resumeData() {

    }

    private void getCacheDataFirstPage() {
        LogUtils.d(TAG, "getCacheDataFirstPage start");
        List<ReviewEntity> reviewEntities = ReviewManager.getFirstPage();
        if (reviewEntities.size() > 0)
            sinceDate = reviewEntities.get(reviewEntities.size() - 1).getCraeatedDateAt();
        mReviewEntities = reviewEntities;

        if (reviewEntities.size() < LIMIT) isLoadingMoreFromDb = false;
        refreshList();
    }


    private void getCacheDataPage() {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<ReviewEntity> reviewEntities = ReviewManager.getReviewsSince(sinceDate);

        if (reviewEntities.size() > 0)
            sinceDate = reviewEntities.get(reviewEntities.size() - 1).getCraeatedDateAt();

        if (reviewEntities.size() < limit) isLoadingMoreFromDb = false;

        mReviewEntities.addAll(reviewEntities);
        refreshList();
    }

    public void getReviews(final boolean isSince, final int userId) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        reviewsAdapter.stopLoadMore();

        LogUtils.d(TAG, "getReviews start");
        Map<String, String> params = new HashMap<>();

        if (isSince && since != null)
            params.put("since", "");
        params.put("limit", LIMIT + "");
        params.put("review_type", typeReview);

        ApiClient.getApiService().getUserReviews(UserManager.getUserToken(), userId, params).enqueue(new Callback<List<ReviewEntity>>() {
            @Override
            public void onResponse(Call<List<ReviewEntity>> call, Response<List<ReviewEntity>> response) {
                LogUtils.d(TAG, "getUserReviews code : " + response.code());
                LogUtils.d(TAG, "getUserReviews body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<ReviewEntity> reviewEntities = response.body();
                    for (int i = 0; i < reviewEntities.size(); i++) {
                        if (!checkContainsReviews(mReviewEntities, reviewEntities.get(i)))
                            mReviewEntities.add(reviewEntities.get(i));
                    }
                    since = reviewEntities.get(reviewEntities.size() - 1).getCreatedAt();

                    if (reviewEntities.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        reviewsAdapter.stopLoadMore();
                    }
                    refreshList();
                    ReviewManager.insertReviews(reviewEntities);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(ReviewsActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getReviews(isSince, 123);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(ReviewsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getReviews(isSince, 123);
                            ;
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
            public void onFailure(Call<List<ReviewEntity>> call, Throwable t) {
                LogUtils.e(TAG, "getUserReviews , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(ReviewsActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getReviews(isSince, userId);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                reviewsAdapter.stopLoadMore();
                isLoadingFromServer = false;
                onStopRefresh();


            }
        });
    }

    private void refreshList() {
        if (reviewsAdapter == null) {
            reviewsAdapter = new ReviewsAdapter(ReviewsActivity.this, mReviewEntities);
            linearLayoutManager = new LinearLayoutManager(ReviewsActivity.this);
            rcvReviews.setLayoutManager(linearLayoutManager);
            rcvReviews.setAdapter(reviewsAdapter);

            rcvReviews.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage();
                    if (isLoadingMoreFromServer) getReviews(false, user_id);

                }
            });

        } else {
            reviewsAdapter.notifyDataSetChanged();
        }

        LogUtils.d(TAG, "refreshList , getUserReviews size : " + mReviewEntities.size());

    }

    private boolean checkContainsTaskResponse(List<TaskResponse> taskResponses, TaskResponse response) {
        for (int i = 0; i < taskResponses.size(); i++)
            if (taskResponses.get(i).getId() == response.getId()) return true;
        return false;
    }


    private boolean checkContainsReviews(List<ReviewEntity> reviewEntities, ReviewEntity reviewEntity) {
        for (int i = 0; i < reviewEntities.size(); i++)
            if (reviewEntities.get(i).getId() == reviewEntity.getId()) return true;
        return false;
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
        getReviews(false, user_id);
    }

}
