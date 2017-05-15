package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ReviewsActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by tonish1 on 5/9/17.
 */

public class PosterReviewFragment extends BaseFragment implements View.OnClickListener {
    private RatingBar posterRating;
    private TextViewHozo tvRate, btnMoreReview;
    private ReviewsListView reviewsListView;
    private List<Review> reviews = new ArrayList<>();
    private float userRate = 0f;
    protected LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.review_fragment;
    }

    @Override
    protected void initView() {
        posterRating = (RatingBar) findViewById(R.id.poster_rating);
        tvRate = (TextViewHozo) findViewById(R.id.tv_rate);
        reviewsListView = (ReviewsListView) findViewById(R.id.rcv_reviews);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
    }

    @Override
    protected void initData() {
        btnMoreReview.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userRate = bundle.getFloat(Constants.USER_POSTER_RATING);
            reviews = bundle.getParcelableArrayList(Constants.USER_POSTER_REVIEWS);
            posterRating.setRating(userRate);
            reviewsListView.updateData((ArrayList<Review>) reviews);
            String rate = getContext().getString(R.string.profile_rate) + " (" + reviews.size() + ") ";
            tvRate.setText(rate);
        }

    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_reviews:
                startActivity(new Intent(getContext(), ReviewsActivity.class));
                break;
        }

    }
}
