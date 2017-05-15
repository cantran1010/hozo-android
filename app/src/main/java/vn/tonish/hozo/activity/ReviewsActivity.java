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
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;

import static vn.tonish.hozo.R.id.lvList;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class ReviewsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = ReviewsActivity.class.getSimpleName();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rcvReviews;
    private List<Review> reviews;
    private ReviewsAdapter reviewsAdapter;
    private int pageSize = 20;
    private String lastTime = "2017-04-24T07:13:41Z";
    private String typeReview = "";
    private int user_id = 0;
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_reviews;
    }

    @Override
    protected void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        rcvReviews = (RecyclerView) findViewById(lvList);
        imgBack = (ImageView) findViewById(R.id.img_back);
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(this, (ArrayList<Review>) reviews);
        rcvReviews.setLayoutManager(linearLayoutManager);
        rcvReviews.setAdapter(reviewsAdapter);

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);
        Intent i = getIntent();
        typeReview = i.getExtras().getString(Constants.REVIEW_TYPE_POSTER);
        user_id = i.getExtras().getInt(Constants.USER_ID);
        createSwipeToRefresh();
        getReviews(user_id, pageSize, typeReview, "2017-04-24T07:13:41Z", true);
        rcvReviews.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getReviews(user_id, pageSize, typeReview, lastTime, true);
                reviewsAdapter.notifyDataSetChanged();
                reviewsAdapter.stopLoadMore();
            }
        });


    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getReviews(UserManager.getUserLogin(this).getId(), pageSize, typeReview, "2017-04-24T07:13:41Z", true);
        onStopRefresh();

    }

    public void getReviews(int userId, int limit, String type, String since, final boolean isRefresh) {
        if (reviews != null && reviews.size() > 0) {
            lastTime = reviews.get(reviews.size() - 1).getCreatedAt();
        }
        if (!isRefresh) {
            ProgressDialogUtils.showProgressDialog(this);
        }

        Map<String, String> option = new HashMap<>();
        option.put("limit", String.valueOf(limit));
        option.put("review_type", type);
        option.put("since", since);
        ApiClient.getApiService().getUserReviews(UserManager.getUserToken(ReviewsActivity.this), userId, option).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (isRefresh) {
                        reviews.clear();
                    }
                    reviews.addAll(response.body());
                    reviewsAdapter.notifyDataSetChanged();
                    LogUtils.d(TAG, "getReviewsonResponse size : " + reviews.size());
                    lastTime = reviews.get(reviews.size() - 1).getCreatedAt();
                }
                if (!isRefresh) {
                    ProgressDialogUtils.dismissProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                if (!isRefresh) {
                    ProgressDialogUtils.dismissProgressDialog();
                }
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
}
