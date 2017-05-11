package vn.tonish.hozo.fragment;

import android.os.Bundle;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by tonish1 on 5/9/17.
 */

public class workerReviewFragment extends BaseFragment {
    private RatingBar posterRating;
    private TextViewHozo tvRate;
    private ReviewsListView reviewsListView;
    private List<Review> reviews = new ArrayList<>();
    private float userRate = 0f;

    @Override
    protected int getLayout() {
        return R.layout.review_fragment;
    }

    @Override
    protected void initView() {
        posterRating = (RatingBar) findViewById(R.id.poster_rating);
        tvRate = (TextViewHozo) findViewById(R.id.tv_rate);
        reviewsListView = (ReviewsListView) findViewById(R.id.rcv_reviews);

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userRate = bundle.getFloat(Constants.USER_TASKER_RATING);
            reviews = bundle.getParcelableArrayList(Constants.USER_TASKER_REVIEWS);
            posterRating.setRating(userRate);
            reviewsListView.updateData((ArrayList<Review>) reviews);
            String rate = getContext().getString(R.string.profile_rate) + " (" + reviews.size() + ") ";
            tvRate.setText(rate);
        }
        LogUtils.d("bundel",bundle.toString());
    }

    @Override
    protected void resumeData() {

    }
}
