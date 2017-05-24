package vn.tonish.hozo.activity.other;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import vn.tonish.hozo.activity.ReviewsActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.ReviewManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.Token;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REVIEW_TYPE_POSTER;
import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyDateFromIso;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;
import static vn.tonish.hozo.utils.Utils.converGender;
import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by Can Tran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ImageView imgback, imgEdit;
    private TextView btnWorker, btnPoster, btnAddVerify;
    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDateOfBirth, tvAddress, tvMobile, tvGender, tvRateCount, btnMoreReview, btnLogOut;
    private RatingBar ratingBar;
    private LinearLayout layoutInfor;
    private float ratingPoster, ratingTasker;
    private ReviewsListView reviewsListView;
    private int tabIndex = 0;
    private boolean isMyUser;
    private int rateCountPoster, retaCountWorker;
    private int userId;
    private UserEntity mUserEntity;
    List<ReviewEntity> reviewEntities = new ArrayList<>();
    List<ReviewEntity> posterReviewEntity = new ArrayList<>();
    List<ReviewEntity> taskerReviewEntity = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        layoutInfor = (LinearLayout) findViewById(R.id.layout_infor);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        tvDateOfBirth = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvMobile = (TextViewHozo) findViewById(R.id.tv_phone);
        tvGender = (TextViewHozo) findViewById(R.id.tv_gender);
        btnAddVerify = (TextViewHozo) findViewById(R.id.tv_add_verify);
        btnLogOut = (TextViewHozo) findViewById(R.id.btn_logout);
        btnWorker = (TextViewHozo) findViewById(R.id.btn_worker);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        btnAddVerify = (TextViewHozo) findViewById(R.id.tv_add_verify);
        tvRateCount = (TextViewHozo) findViewById(R.id.tv_rate);
        reviewsListView = (ReviewsListView) findViewById(R.id.rcv_reviews);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
        ratingPoster = 0f;
        ratingTasker = 0f;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userId = intent.getExtras().getInt(Constants.USER_ID);
        isMyUser = intent.getExtras().getBoolean(Constants.IS_MY_USER);
        LogUtils.d(TAG, "userId" + userId + "isMyuser" + isMyUser);
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnPoster.setOnClickListener(this);
        btnWorker.setOnClickListener(this);
        btnAddVerify.setOnClickListener(this);
        btnMoreReview.setOnClickListener(this);
        if (isMyUser) {
            mUserEntity = UserManager.getMyUser();
        } else {
            mUserEntity = UserManager.getUserById(userId);
        }
        updateUi(mUserEntity);
        updateUserFromServer();

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
                startActivityForResult(new Intent(ProfileActivity.this, AddVerifyActivity.class), Constants.REQUEST_CODE_ADD_VERIFY, TransitionScreen.RIGHT_TO_LEFT);
                break;
            case R.id.tv_more_reviews:
                Intent intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra(Constants.USER_ID, userId);
                startActivity(intent, TransitionScreen.DOWN_TO_UP);
                break;
        }
    }

    private void doEdit() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        UserEntity userEntity = UserManager.getMyUser();
        intent.putExtra(Constants.USER, DataParse.convertUserEntityToUser(userEntity));
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PROFILE, TransitionScreen.RIGHT_TO_LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_UPDATE_PROFILE && resultCode == Constants.RESULT_CODE_UPDATE_PROFILE) {
            updateUi(UserManager.getMyUser());
        }
    }

    private void logOut() {
        ProgressDialogUtils.showProgressDialog(this);
        DialogUtils.showOkAndCancelDialog(this, getString(R.string.msg_logOut), getString(R.string.msg_contten_logOut), "Có", "huỷ", new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                ApiClient.getApiService().logOut(UserManager.getUserToken()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ProgressDialogUtils.dismissProgressDialog();
                        if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                            UserManager.deleteAll();
                            ReviewManager.deleteAll();
                            startActivityAndClearAllTask(new Intent(ProfileActivity.this, HomeActivity.class), TransitionScreen.FADE_IN);
                        } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                            NetworkUtils.refreshToken(ProfileActivity.this, new NetworkUtils.RefreshListener() {
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

    private void updateUserFromServer() {
        if (mUserEntity == null) {
            ProgressDialogUtils.showProgressDialog(this);
        }
        LogUtils.d(TAG, "onResponse updateUserFromServer start");
        ApiClient.getApiService().getUser(UserManager.getUserToken(), userId).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse updateUserFromServer status code : " + response.code());
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "onResponse updateUserFromServer body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        UserEntity userEntity = response.body();
                        Token token = new Token();
                        if (isMyUser) {
                            userEntity.setAccessToken(UserManager.getMyUser().getAccessToken());
                            userEntity.setTokenExp(UserManager.getMyUser().getTokenExp());
                            userEntity.setRefreshToken(UserManager.getMyUser().getRefreshToken());
                        }
                        UserManager.insertUser(response.body(), isMyUser);
                        updateUi(response.body());

                    } else {
                        DialogUtils.showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                updateUserFromServer();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateUserFromServer();
                        }
                    });
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Toast.makeText(ProfileActivity.this, error.message(), Toast.LENGTH_SHORT).show();

                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                DialogUtils.showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateUserFromServer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });


    }

    private void updateUi(UserEntity userEntity) {
        if (userEntity != null) {
            posterReviewEntity.clear();
            taskerReviewEntity.clear();
            if (userEntity.getReviews() != null)
                for (ReviewEntity reviewEntity : userEntity.getReviews()) {
                    if (reviewEntity.getType().equals(REVIEW_TYPE_POSTER)) {
                        posterReviewEntity.add(reviewEntity);
                    } else {
                        taskerReviewEntity.add(reviewEntity);
                    }

                }
            Utils.displayImageAvatar(this, imgAvatar, userEntity.getAvatar());
            ratingPoster = userEntity.getPosterAverageRating();
            rateCountPoster = userEntity.getPosterReviewCount();
            retaCountWorker = userEntity.getTaskerReviewCount();
            ratingTasker = userEntity.getTaskerAverageRating();
            tvName.setText(userEntity.getFullName());
            tvDateOfBirth.setText(getOnlyDateFromIso(userEntity.getDateOfBirth()));
            tvGender.setText(converGender(this, userEntity.getGender()));
            if (isMyUser) {
                layoutInfor.setVisibility(View.VISIBLE);
                tvAddress.setText(userEntity.getAddress());
                tvMobile.setText(userEntity.getPhone());
                btnAddVerify.setVisibility(View.VISIBLE);
                btnAddVerify.setEnabled(true);
                btnLogOut.setVisibility(View.VISIBLE);
            } else {
                layoutInfor.setVisibility(View.GONE);
                btnAddVerify.setVisibility(View.GONE);
                btnAddVerify.setEnabled(false);
                btnLogOut.setVisibility(View.GONE);
            }
            if (reviewEntities.size() > 5) {
                btnMoreReview.setVisibility(View.VISIBLE);
            } else {
                btnMoreReview.setVisibility(View.GONE);
            }
            setDataSelected(true);
        } else {

        }

    }


    private void setDataSelected(boolean isPoster) {
        reviewEntities.clear();
        if (isPoster) {
            tvRateCount.setText(getString(R.string.profile_rate) + "(" + rateCountPoster + ")");
            ratingBar.setRating(ratingPoster);
            reviewEntities.addAll(posterReviewEntity);
        } else {
            tvRateCount.setText(getString(R.string.profile_rate) + "(" + retaCountWorker + ")");
            ratingBar.setRating(ratingTasker);
            reviewEntities.addAll(taskerReviewEntity);
        }
        reviewsListView.updateData((ArrayList<ReviewEntity>) reviewEntities);
        LogUtils.d(TAG, "reviews " + isPoster + reviewEntities.toString());


    }

    private void selectab(int i) {
        if (i == 1) {
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_selected));
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.black));
            setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_press));
            setDataSelected(true);


        } else {
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnWorker, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_no_active_selected));
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.black));
            setViewBackground(btnPoster, ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_profile_left_reviews_default));
            setDataSelected(false);
        }
    }

}

