package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.common.Constants.REVIEW_TYPE_POSTER;
import static vn.tonish.hozo.utils.DateTimeUtils.getDateBirthDayFromIso;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;
import static vn.tonish.hozo.utils.Utils.converGenderVn;
import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by CanTran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ImageView imgback, imgEdit;
    private TextView btnWorker, btnPoster, tvTitle;
    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDateOfBirth, tvAddress, tvMobile, tvGender, tvRateCount, btnMoreReview, btnLogOut, tvAbout;
    private TextViewHozo tvReviewsCount, tvTaskCount, tvCompletionRate, tvAddVerify;
    private RatingBar ratingBar;
    private LinearLayout layoutInfor;
    private float ratingPoster, ratingTasker;
    private ReviewsListView reviewsListView;
    private FrameLayout layoutLogout;
    private ScrollView scrollView;
    private int tabIndex = 0;
    private boolean isMyUser;
    private int rateCountPoster, retaCountWorker, taskPostPoster, taskPostWorker;
    private float percentDonePoster, percentDoneWorker;
    private int userId;
    private UserEntity mUserEntity;
    private final List<ReviewEntity> reviewEntities = new ArrayList<>();
    private final List<ReviewEntity> posterReviewEntity = new ArrayList<>();
    private final List<ReviewEntity> taskerReviewEntity = new ArrayList<>();
    private ImageView imgFbVerify, imgEmailVerify;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        imgAvatar = (CircleImageView) findViewById(img_avatar);
        layoutInfor = (LinearLayout) findViewById(R.id.layout_infor);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        tvDateOfBirth = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvMobile = (TextViewHozo) findViewById(R.id.tv_phone);
        tvGender = (TextViewHozo) findViewById(R.id.tv_gender);
        btnLogOut = (TextViewHozo) findViewById(R.id.btn_logout);
        btnWorker = (TextViewHozo) findViewById(R.id.btn_worker);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        tvRateCount = (TextViewHozo) findViewById(R.id.tv_rate);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvAbout = (TextViewHozo) findViewById(R.id.tv_about);
        tvReviewsCount = (TextViewHozo) findViewById(R.id.tv_reviews_count);
        tvTaskCount = (TextViewHozo) findViewById(R.id.tv_task_count);
        tvCompletionRate = (TextViewHozo) findViewById(R.id.tv_completion_rate);
        layoutLogout = (FrameLayout) findViewById(R.id.layout_logout);
        reviewsListView = (ReviewsListView) findViewById(R.id.rcv_reviews);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
        tvAddVerify = (TextViewHozo) findViewById(R.id.btn_add_verify);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        imgFbVerify = findViewById(R.id.fb_verify);
        imgEmailVerify = findViewById(R.id.email_verify);
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
        btnMoreReview.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        tvAddVerify.setOnClickListener(this);
        if (isMyUser) {
            mUserEntity = UserManager.getMyUser();
        } else {
            mUserEntity = UserManager.getUserById(userId);
        }
        updateUi(mUserEntity);
        updateUserFromServer();
        scrollView.fullScroll(ScrollView.FOCUS_UP);

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
            case R.id.tv_more_reviews:
                Intent intent = new Intent(this, ReviewsActivity.class);
                intent.putExtra(Constants.USER_ID, userId);
                startActivity(intent, TransitionScreen.DOWN_TO_UP);
                break;
            case img_avatar:
                if (!mUserEntity.getAvatar().trim().equals("")) {
                    Intent intentView = new Intent(ProfileActivity.this, PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, mUserEntity.getAvatar());
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;
            case R.id.btn_add_verify:
                Intent intentVerify = new Intent(this, GiveInforActivity.class);
                startActivityForResult(intentVerify, Constants.REQUEST_CODE_VERIFY, TransitionScreen.RIGHT_TO_LEFT);
                break;
        }
    }

    private void doEdit() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PROFILE, TransitionScreen.UP_TO_DOWN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_UPDATE_PROFILE && resultCode == Constants.RESULT_CODE_UPDATE_PROFILE) {
            updateUi(UserManager.getMyUser());
        } else if (requestCode == Constants.REQUEST_CODE_VERIFY && resultCode == Constants.RESULT_CODE_VERIFY) {
            updateUi(UserManager.getMyUser());
        }
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
                LogUtils.d(TAG, "onResponse updateUserFromServer body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    UserEntity userEntity = response.body();
                    if (isMyUser) {
                        assert userEntity != null;
                        userEntity.setAccessToken(UserManager.getMyUser().getAccessToken());
                        userEntity.setTokenExp(UserManager.getMyUser().getTokenExp());
                        userEntity.setRefreshToken(UserManager.getMyUser().getRefreshToken());
                    }
                    UserManager.insertUser(response.body(), isMyUser);
                    updateUi(response.body());

                    mUserEntity = userEntity;

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(ProfileActivity.this);
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
//                    Toast.makeText(ProfileActivity.this, error.message(), Toast.LENGTH_SHORT).show();
                    Utils.showLongToast(ProfileActivity.this, error.message(), true, false);

                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());

                if (isFinishing()) return;
                showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
            Utils.displayImageAvatar(getApplicationContext(), imgAvatar, userEntity.getAvatar());
            ratingPoster = userEntity.getPosterAverageRating();
            rateCountPoster = userEntity.getPosterReviewCount();
            retaCountWorker = userEntity.getTaskerReviewCount();
            taskPostPoster = userEntity.getPosterDoneCount();
            taskPostWorker = userEntity.getTaskerDoneCount();
            percentDonePoster = userEntity.getPosterDoneRate();
            percentDoneWorker = userEntity.getTaskerDoneRate();

            ratingTasker = userEntity.getTaskerAverageRating();
            tvName.setText(userEntity.getFullName());
            if (userEntity.getDateOfBirth().equals("0001-01-01")) {
                tvDateOfBirth.setVisibility(View.GONE);
            } else {
                tvDateOfBirth.setVisibility(View.VISIBLE);
                tvDateOfBirth.setText(getDateBirthDayFromIso(userEntity.getDateOfBirth()));
            }
            if (userEntity.getGender().equals(getString(R.string.gender_any)))
                tvGender.setVisibility(View.GONE);
            else {
                tvGender.setVisibility(View.VISIBLE);
                tvGender.setText(converGenderVn(this, userEntity.getGender()));
            }
            if (isMyUser) {
                layoutInfor.setVisibility(View.VISIBLE);
                tvAddress.setText(userEntity.getAddress());
                tvMobile.setText(userEntity.getPhone());
                layoutLogout.setVisibility(View.VISIBLE);
                imgEdit.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.my_account));
                tvAddVerify.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.GONE);
                layoutInfor.setVisibility(View.GONE);
                layoutLogout.setVisibility(View.GONE);
                tvTitle.setText(getString(R.string.user_account));
                tvAddVerify.setVisibility(View.GONE);
            }
            if (reviewEntities.size() > 5) {
                btnMoreReview.setVisibility(View.VISIBLE);
            } else {
                btnMoreReview.setVisibility(View.GONE);
            }

            if (userEntity.getFacebookId() != null && !userEntity.getFacebookId().trim().equals(""))
                imgFbVerify.setVisibility(View.VISIBLE);
            else imgFbVerify.setVisibility(View.GONE);

            if (userEntity.getEmail() != null && !userEntity.getEmail().trim().equals(""))
                imgEmailVerify.setVisibility(View.VISIBLE);
            else imgEmailVerify.setVisibility(View.GONE);

            setDataSelected(true);
            tvAbout.setText(userEntity.getDescription());
        }


    }


    private void setDataSelected(boolean isPoster) {
        reviewEntities.clear();
        if (isPoster) {
            tvReviewsCount.setText(getString(R.string.reviews_count, rateCountPoster));
            tvTaskCount.setText(getString(R.string.post_count, taskPostPoster));
            String percentDone = (int) percentDonePoster * 100 + "% " + getString(R.string.completion_rate);
            tvCompletionRate.setText(percentDone);
            tvRateCount.setText(R.string.profile_rate);
            ratingBar.setRating(ratingPoster);
            reviewEntities.addAll(posterReviewEntity);
        } else {
            tvReviewsCount.setText(getString(R.string.reviews_count, retaCountWorker));
            tvTaskCount.setText(getString(R.string.task_count, taskPostWorker));
            String percentDone = (int) percentDoneWorker * 100 + "% " + getString(R.string.completion_rate);
            tvCompletionRate.setText(percentDone);
            tvRateCount.setText(R.string.profile_rate);
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

    private void logOut() {
        DialogUtils.showOkAndCancelDialog(ProfileActivity.this, getString(R.string.msg_logOut), getString(R.string.msg_contten_logOut), getString(R.string.oke), getString(R.string.report_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                ProgressDialogUtils.showProgressDialog(ProfileActivity.this);
                ApiClient.getApiService().logOut(UserManager.getUserToken()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (response.isSuccessful()) {
                        if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.deleteAll();
                            realm.commitTransaction();
                            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                            intent.putExtra(Constants.LOGOUT_EXTRA, true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                            NetworkUtils.refreshToken(ProfileActivity.this, new NetworkUtils.RefreshListener() {
                                @Override
                                public void onRefreshFinish() {
                                    logOut();
                                }
                            });
                        } else {
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
                        ProgressDialogUtils.dismissProgressDialog();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(imgAvatar);

    }


}

