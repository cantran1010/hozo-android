package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by CanTran on 14/05/2017.
 */

public class ReviewsView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvReviews, tvTimeAgo;
    private RatingBar ratingBar;
    private ReviewEntity reviewEntity;

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
        layoutInflater.inflate(R.layout.view_reviews, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvReviews = (TextViewHozo) findViewById(R.id.tv_reviews);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        imgAvatar.setOnClickListener(this);
    }

    public void updateData(ReviewEntity review) {
        this.reviewEntity = review;
        Utils.displayImageAvatar(getContext(), imgAvatar, review.getAuthorAvatar());
        tvName.setText(review.getAuthorName());
        ratingBar.setRating((float) review.getRating());
        tvReviews.setText(review.getBody());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(review.getCreatedAt(), getContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, reviewEntity.getAuthorId());
                intent.putExtra(Constants.IS_MY_USER, reviewEntity.getAuthorId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;
        }
    }
}

