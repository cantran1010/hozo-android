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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AddVerifyActivity;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.EditProfileActivity;
import vn.tonish.hozo.activity.HomeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.PosterReviewFragment;
import vn.tonish.hozo.fragment.workerReviewFragment;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ProfileView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REVIEW_TYPE_POSTER;
import static vn.tonish.hozo.common.Constants.REVIEW_TYPE_TASKER;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;
import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by Can Tran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ImageView imgback, imgEdit;
    private TextView btnWorker, btnPoster, btnAddVerify;
    private FrameLayout btnLogOut;
    private ProfileView profileView;
    private float ratingPoster, ratingTasker;
    private List<Review> posterReviews, taskerReviews;
    private Bundle bundleTasker, bundlePoster;
    private int tabIndex = 0;

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
        btnAddVerify = (TextViewHozo) findViewById(R.id.tv_add_verify);
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
        btnAddVerify.setOnClickListener(this);
        //     set cache data
        setUserInfoFromCache();
        //     get data from server
        updateUserInfoFromServer();
        //       onClick(btnPoster);
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
                doEdit();
                break;
            case R.id.btn_logout:
                logOut();
                break;
            case R.id.btn_poster:
                if (tabIndex == 1) break;
                tabIndex = 1;
                selectab(1);
                break;
            case R.id.btn_worker:
                if (tabIndex == 2) break;
                tabIndex = 2;
                selectab(2);
                break;
            case R.id.tv_add_verify:
                startActivityForResult(new Intent(ProfileActivity.this, AddVerifyActivity.class),Constants.REQUEST_CODE_ADD_VERIFY,TransitionScreen.RIGHT_TO_LEFT);
                break;
        }
    }

    private void doEdit() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        UserEntity userEntity = UserManager.getUserLogin(this);
        intent.putExtra(Constants.USER, DataParse.convertUserEntityToUser(userEntity));
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PROFILE,TransitionScreen.RIGHT_TO_LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_UPDATE_PROFILE && resultCode == Constants.RESULT_CODE_UPDATE_PROFILE) {
            setUserInfoFromCache();
        }
    }

    private void logOut() {
        ProgressDialogUtils.showProgressDialog(this);
        DialogUtils.showOkAndCancelDialog(this, getString(R.string.msg_logOut), getString(R.string.msg_contten_logOut), "Có", "huỷ", new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                ApiClient.getApiService().logOut(UserManager.getUserToken(ProfileActivity.this)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ProgressDialogUtils.dismissProgressDialog();
                        if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                            UserManager.deleteAll(ProfileActivity.this);
                            ReviewManager.deleteAll();
                            startActivityAndClearAllTask(new Intent(ProfileActivity.this, HomeActivity.class), TransitionScreen.FADE_IN);
                        } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                            NetworkUtils.RefreshToken(ProfileActivity.this, new NetworkUtils.RefreshListener() {
                                @Override
                                public void onRefreshFinish() {
                                    logOut();
                                }
                            });
                        } else {
                            DialogUtils.showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                                @Override
                                public void onSubmit() {
                                    logOut();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        ProgressDialogUtils.dismissProgressDialog();
                        showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                logOut();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                    }
                });

            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void updateUserInfoFromServer() {

        ApiClient.getApiService().getMyAccountInfor(UserManager.getUserToken(this)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    LogUtils.d(TAG, "response body: " + response.body().toString());
                    DataParse.updateUser(response.body(), ProfileActivity.this);
                    DataParse.insertReviewtoDb(response.body());
                    LogUtils.d(TAG, "update reviews : " + ReviewManager.getAllReview().toString());

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
            }
        });


    }

    private void setUserInfoFromCache() {

        UserEntity userEntity = new UserEntity();
        userEntity = UserManager.getUserLogin(this);
        List<ReviewEntity> posterReviewEntity = new ArrayList<>();
        List<ReviewEntity> taskerReviewEntity = new ArrayList<>();
        posterReviewEntity = ReviewManager.getReviewByType(REVIEW_TYPE_POSTER);
        taskerReviewEntity = ReviewManager.getReviewByType(REVIEW_TYPE_TASKER);
        for (int i = 0; i < posterReviewEntity.size(); i++) {
            Review review = new Review();
            review.setId(posterReviewEntity.get(i).getId());
            review.setAuthorAvatar(posterReviewEntity.get(i).getAuthorAvatar());
            review.setAuthorId(posterReviewEntity.get(i).getAuthorId());
            review.setAuthorName(posterReviewEntity.get(i).getAuthorName());
            review.setBody(posterReviewEntity.get(i).getBody());
            review.setRating(posterReviewEntity.get(i).getRating());
            review.setCreatedAt(posterReviewEntity.get(i).getCreatedAt());
            review.setTaskName(posterReviewEntity.get(i).getTaskName());
            review.setType(posterReviewEntity.get(i).getType());
            posterReviews.add(review);
            LogUtils.d(TAG, "update posterReviews: " + posterReviews.toString());
        }
        for (int i = 0; i < taskerReviewEntity.size(); i++) {
            Review review = new Review();
            review.setId(taskerReviewEntity.get(i).getId());
            review.setAuthorAvatar(taskerReviewEntity.get(i).getAuthorAvatar());
            review.setAuthorId(taskerReviewEntity.get(i).getAuthorId());
            review.setAuthorName(taskerReviewEntity.get(i).getAuthorName());
            review.setBody(taskerReviewEntity.get(i).getBody());
            review.setRating(taskerReviewEntity.get(i).getRating());
            review.setCreatedAt(taskerReviewEntity.get(i).getCreatedAt());
            review.setTaskName(taskerReviewEntity.get(i).getTaskName());
            review.setType(taskerReviewEntity.get(i).getType());
            taskerReviews.add(review);
            LogUtils.d(TAG, "update taskerReviews: " + taskerReviews.toString());
        }
        ratingPoster = userEntity.getPosterAverageRating();
        ratingTasker = userEntity.getTaskerAverageRating();
        profileView.updateData(true, userEntity.getAvatar(), userEntity.getFullName(), userEntity.getDateOfBirth(), "Nam", userEntity.getAddress(), userEntity.getPhoneNumber(), userEntity.getVerified());

        bundleTasker.putFloat(Constants.USER_TASKER_RATING, ratingTasker);
        bundleTasker.putInt(Constants.USER_ID, userEntity.getId());
        bundleTasker.putParcelableArrayList(Constants.USER_TASKER_REVIEWS, (ArrayList<? extends Parcelable>) taskerReviews);

        bundlePoster.putFloat(Constants.USER_POSTER_RATING, ratingPoster);
        bundlePoster.putInt(Constants.USER_ID, userEntity.getId());
        bundlePoster.putParcelableArrayList(Constants.USER_POSTER_REVIEWS, (ArrayList<? extends Parcelable>) posterReviews);
    }

    private void selectab(int i) {
        if (i == 1) {
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_selected));
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.black));
            setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_press));
            openFragmentBundle(R.id.layout_container, PosterReviewFragment.class, bundlePoster, false, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_selected));
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.black));
            setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_default));
            openFragmentBundle(R.id.layout_container, workerReviewFragment.class, bundleTasker, false, TransitionScreen.RIGHT_TO_LEFT);

        }
    }


}

