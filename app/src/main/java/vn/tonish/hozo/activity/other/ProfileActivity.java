package vn.tonish.hozo.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.EditProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.PosterReviewFragment;
import vn.tonish.hozo.fragment.workerReviewFragment;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.ProfileView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by Can Tran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgback, imgEdit;
    private TextView btnWorker, btnPoster, btnMoreReview;
    private FrameLayout btnLogOut;
    private ProfileView profileView;
    private float ratingPoster, ratingTasker;
    private List<Review> posterReviews, taskerReviews;
    private Bundle bundleTasker, bundlePoster;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        profileView = (ProfileView) findViewById(R.id.view_profile);
        btnLogOut = (FrameLayout) findViewById(R.id.btn_logout);
        btnWorker = (TextViewHozo) findViewById(R.id.btn_worker);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
        posterReviews = new ArrayList<>();
        taskerReviews = new ArrayList<>();
        ratingPoster = 0f;
        ratingTasker = 0f;
        bundlePoster = new Bundle();
        bundleTasker = new Bundle();
    }

    @Override
    protected void initData() {
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnPoster.setOnClickListener(this);
        btnWorker.setOnClickListener(this);
        //set cache data
        setUserInfoFromCache();
        //get data from server
        updateUserInfoFromServer();
        onClick(btnPoster);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_edit:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.btn_logout:
                logOut();
                break;
            case R.id.btn_poster:
                btnPoster.setTextColor(ContextCompat.getColor(this, R.color.white));
                setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_selected));
                btnWorker.setTextColor(ContextCompat.getColor(this, R.color.black));
                setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_press));
                openFragmentBundle(R.id.layout_container, PosterReviewFragment.class, bundlePoster, false, true);
                break;
            case R.id.btn_worker:
                btnWorker.setTextColor(ContextCompat.getColor(this, R.color.white));
                setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_selected));
                btnPoster.setTextColor(ContextCompat.getColor(this, R.color.black));
                setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_press));
                openFragmentBundle(R.id.layout_container, workerReviewFragment.class, bundleTasker, false, false);
                break;
        }
    }

    private void logOut() {
        UserEntity userEntity = UserManager.getUserLogin(this);
        LogUtils.d("", "user logout " + userEntity.toString());

    }

    private void
    updateUserInfoFromServer() {


    }

    private void setUserInfoFromCache() {
        UserEntity userEntity = new UserEntity();
        userEntity = UserManager.getUserLogin(this);
        ReviewEntity reviewEntity = new ReviewEntity();
        for (int i = 0; i < ReviewManager.getReviewByType(2).size(); i++) {
            Review review = new Review();
            review.setAuthorAvatar(ReviewManager.getReviewByType(2).get(i).getAuthorAvatar());
            review.setAuthorId(ReviewManager.getReviewByType(2).get(i).getAuthorId());
            review.setAuthorName(ReviewManager.getReviewByType(2).get(i).getAuthorName());
            review.setBody(ReviewManager.getReviewByType(2).get(i).getBody());
            review.setRating(ReviewManager.getReviewByType(2).get(i).getRating());
            review.setCreatedAt(ReviewManager.getReviewByType(2).get(i).getCreatedAt());
            review.setTaskName(ReviewManager.getReviewByType(2).get(i).getTaskName());
            review.setType(ReviewManager.getReviewByType(2).get(i).getType());
            posterReviews.add(review);
        }
        for (int i = 0; i < ReviewManager.getReviewByType(1).size(); i++) {
            Review review = new Review();
            review.setAuthorAvatar(ReviewManager.getReviewByType(1).get(i).getAuthorAvatar());
            review.setAuthorId(ReviewManager.getReviewByType(1).get(i).getAuthorId());
            review.setAuthorName(ReviewManager.getReviewByType(1).get(i).getAuthorName());
            review.setBody(ReviewManager.getReviewByType(1).get(i).getBody());
            review.setRating(ReviewManager.getReviewByType(1).get(i).getRating());
            review.setCreatedAt(ReviewManager.getReviewByType(1).get(i).getCreatedAt());
            review.setTaskName(ReviewManager.getReviewByType(1).get(i).getTaskName());
            review.setType(ReviewManager.getReviewByType(1).get(i).getType());
            taskerReviews.add(review);
        }
        ratingPoster = userEntity.getPosterAverageRating();
        ratingTasker = userEntity.getTaskerAverageRating();
        profileView.updateData(true, userEntity.getAvatar(), userEntity.getFullName(), userEntity.getDateOfBirth(), "Nam", userEntity.getAddress(), userEntity.getPhoneNumber(), userEntity.getVerified());

        bundleTasker.putFloat(Constants.USER_TASKER_RATING, ratingTasker);
        bundleTasker.putParcelableArrayList(Constants.USER_TASKER_REVIEWS, (ArrayList<? extends Parcelable>) taskerReviews);
        bundlePoster.putFloat(Constants.USER_POSTER_RATING, ratingPoster);
        bundlePoster.putParcelableArrayList(Constants.USER_POSTER_REVIEWS, (ArrayList<? extends Parcelable>) posterReviews);
    }


}

