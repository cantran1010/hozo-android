package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by tonish1 on 5/9/17.
 */

public class ReviewsView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvReviews, tvTimeAgo;
    private RatingBar ratingBar;

    public ReviewsView(Context context) {
        super(context);
        initView();
    }

    public ReviewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ReviewsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReviewsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.reviews_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvReviews = (TextViewHozo) findViewById(R.id.tv_reviews);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rating);
    }

    public void updateData(Review review) {

        Utils.displayImageAvatar(getContext(), imgAvatar, review.getAuthorAvatar());
        tvName.setText(review.getAuthorName());
        ratingBar.setRating((float) review.getRating());
        tvReviews.setText(review.getBody());
        tvTimeAgo.setText(review.getCreatedAt());
    }

}

