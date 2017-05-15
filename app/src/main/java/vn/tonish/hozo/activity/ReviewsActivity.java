package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
import vn.tonish.hozo.utils.ProgressDialogUtils;

import static vn.tonish.hozo.R.id.lvList;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class ReviewsActivity extends BaseActivity {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rcvReviews;
    private List<Review> reviews;
    private ReviewsAdapter reviewsAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int pageSize = 20;
    private int pageNumber = 0;
    String lastTime = "";
    private final static int limit = 10;

    @Override
    protected int getLayout() {
        return R.layout.activity_reviews;
    }

    @Override
    protected void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        rcvReviews = (RecyclerView) findViewById(lvList);
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter((ArrayList<Review>) reviews);
        rcvReviews.setAdapter(reviewsAdapter);

    }

    @Override
    protected void initData() {
        getReviews(UserManager.getUserLogin(this).getId(), pageSize, "poster", "", false);
        rcvReviews.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getReviews(UserManager.getUserLogin(ReviewsActivity.this).getId(), pageSize, "poster", lastTime, false);
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    public void getReviews(int userId, int limit, String type, String since, final boolean isRefresh) {
        if (reviews != null && reviews.size() > 0) {
            lastTime = reviews.get(reviews.size() - 1).getCreatedAt();
            pageSize++;
        }
        ProgressDialogUtils.showProgressDialog(this);
        ApiClient.getApiService().getUserReviews(UserManager.getUserToken(ReviewsActivity.this), userId, limit, type, lastTime).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                ProgressDialogUtils.dismissProgressDialog();
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (response.body() != null) {
                        if (isRefresh) {
                            reviews.clear();
                        }
                        reviews.addAll(response.body());
                        reviewsAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void refreshData() {
        createSwipeToRefresh();
        getReviews(UserManager.getUserLogin(ReviewsActivity.this).getId(), pageSize * pageNumber, "poster", lastTime, false);

    }
}
