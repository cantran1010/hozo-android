package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by Can Tran on 14/05/2017.
 */

public class workerReviewFragment extends BaseFragment implements View.OnClickListener {
    private final static String typeReview = Constants.REVIEW_TYPE_TASKER;
    private RatingBar posterRating;
    private TextViewHozo tvRate, btnMoreReview;
    private ReviewsListView reviewsListView;
    private List<Review> reviews = new ArrayList<>();
    private float userRate = 0;
    private int userID = 0;

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
            reviews = bundle.getParcelableArrayList(Constants.USER_TASKER_REVIEWS);
            posterRating.setRating(userRate);
            userID = bundle.getInt(Constants.USER_ID);
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
                Intent intent = new Intent(getContext(), ReviewsActivity.class);
                intent.putExtra(typeReview, typeReview);
                intent.putExtra(Constants.USER_ID, userID);
                startActivity(intent);
                break;
        }

    }
}
