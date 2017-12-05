package vn.tonish.hozo.activity.profile;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.activity.ReviewsActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.image.PreviewImageListActivity;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.adapter.ProfileTagAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageProfileResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;
import static vn.tonish.hozo.utils.Utils.setViewBackground;


/**
 * Created by CanTran on 4/11/17.
 */


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ImageView imgback, imgEdit, imgCheckedFb, imgCheckedEmail, imgBackground;
    private TextView btnWorker, btnPoster, tvTitle;
    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDateOfBirth, tvAddress, tvMobile, tvRateLbl, btnMoreReview, btnLogOut, tvAbout, tvCountActivity, tvCountFollow, tvMobileLbl, tvExperience;
    private TextViewHozo tvReviewsCount, tvTaskCount, tvCompletionRate;
    private RatingBar ratingBar;
    private float ratingPoster, ratingTasker;
    private ReviewsListView reviewsListView;
    private FrameLayout layoutLogout;
    private NestedScrollView scrollView;
    private int tabIndex = 1;
    private boolean isMyUser;
    private int rateCountPoster, retaCountWorker, taskPostPoster, taskPostWorker;
    private float percentDonePoster, percentDoneWorker;
    private int userId;
    private UserEntity mUserEntity;
    private final List<ReviewEntity> reviewEntities = new ArrayList<>();
    private final List<ReviewEntity> posterReviewEntity = new ArrayList<>();
    private final List<ReviewEntity> taskerReviewEntity = new ArrayList<>();
    private ImageView imgFbVerify, imgEmailVerify;

    private RecyclerView rcvSkills, rcvLanguages;
    private ButtonHozo btnVerify;
    private MyGridView myGridView;
    private ButtonHozo btnFollow;
    private boolean isFollow = false;
    private boolean isTabPoster = true;
    private LinearLayout layoutFollow;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        imgback = (ImageView) findViewById(R.id.img_back);
        imgEdit = (ImageView) findViewById(R.id.img_edit);
        imgAvatar = (CircleImageView) findViewById(img_avatar);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        tvDateOfBirth = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvMobile = (TextViewHozo) findViewById(R.id.tv_phone);
        btnLogOut = (TextViewHozo) findViewById(R.id.btn_logout);
        btnWorker = (TextViewHozo) findViewById(R.id.btn_worker);
        btnPoster = (TextViewHozo) findViewById(R.id.btn_poster);
        tvRateLbl = (TextViewHozo) findViewById(R.id.tv_rate);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvAbout = (TextViewHozo) findViewById(R.id.tv_about);
        tvReviewsCount = (TextViewHozo) findViewById(R.id.tv_reviews_count);
        tvTaskCount = (TextViewHozo) findViewById(R.id.tv_task_count);
        tvCompletionRate = (TextViewHozo) findViewById(R.id.tv_completion_rate);
        layoutLogout = (FrameLayout) findViewById(R.id.layout_logout);
        reviewsListView = (ReviewsListView) findViewById(R.id.rcv_reviews);
        btnMoreReview = (TextViewHozo) findViewById(R.id.tv_more_reviews);
        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        imgFbVerify = (ImageView) findViewById(R.id.fb_verify);
        imgEmailVerify = (ImageView) findViewById(R.id.email_verify);
        ratingPoster = 0f;
        ratingTasker = 0f;
        imgBackground = (ImageView) findViewById(R.id.img_background);
        imgBackground.setOnClickListener(this);

        tvMobileLbl = (TextViewHozo) findViewById(R.id.tv_phone_lbl);

        btnVerify = (ButtonHozo) findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);

        rcvSkills = (RecyclerView) findViewById(R.id.rcv_skill);
        rcvLanguages = (RecyclerView) findViewById(R.id.rcv_languages);

        tvCountActivity = (TextViewHozo) findViewById(R.id.tv_count_activity);
        tvCountFollow = (TextViewHozo) findViewById(R.id.tv_count_follow);

        imgCheckedFb = (ImageView) findViewById(R.id.img_checked_fb);
        imgCheckedEmail = (ImageView) findViewById(R.id.img_checked_email);
        myGridView = (MyGridView) findViewById(R.id.gr_image);

        tvExperience = (TextViewHozo) findViewById(R.id.tv_experience);

        btnFollow = (ButtonHozo) findViewById(R.id.btn_follow);
        btnFollow.setOnClickListener(this);

        layoutFollow = (LinearLayout) findViewById(R.id.layout_follow);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        userId = intent.getExtras().getInt(Constants.USER_ID);
        isMyUser = (userId == UserManager.getMyUser().getId());

        LogUtils.d(TAG, "userId" + userId + "isMyuser" + isMyUser);
        imgback.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnPoster.setOnClickListener(this);
        btnWorker.setOnClickListener(this);
        btnMoreReview.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
//        tvAddVerify.setOnClickListener(this);
        if (isMyUser) {
            mUserEntity = UserManager.getMyUser();
        } else {
            mUserEntity = UserManager.getUserById(userId);
        }
        updateUi();
        getUserFromServer();
        scrollView.fullScroll(ScrollView.FOCUS_UP);

    }

    @Override
    protected void resumeData() {
        getUserFromServer();
    }

    private void doEdit() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_PROFILE, TransitionScreen.UP_TO_DOWN);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == Constants.REQUEST_CODE_UPDATE_PROFILE && resultCode == Constants.RESULT_CODE_UPDATE_PROFILE) {
////            updateUi(UserManager.getMyUser());
////        } else if (requestCode == Constants.REQUEST_CODE_VERIFY && resultCode == Constants.RESULT_CODE_VERIFY) {
////            updateUi(UserManager.getMyUser());
////        }
//        mUserEntity = UserManager.getUserById(userId);
//        updateUi();
//    }

    private void getUserFromServer() {
        if (mUserEntity == null) {
            ProgressDialogUtils.showProgressDialog(this);
        }
        LogUtils.d(TAG, "onResponse getUserFromServer start");
        ApiClient.getApiService().getUser(UserManager.getUserToken(), userId).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse getUserFromServer status code : " + response.code());
                LogUtils.d(TAG, "onResponse getUserFromServer body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    UserEntity userEntity = response.body();
                    if (isMyUser) {
                        if (!(UserManager.getMyUser().getAccessToken() == null))
                            userEntity.setAccessToken(UserManager.getMyUser().getAccessToken());
                        if (!(UserManager.getMyUser().getTokenExp() == null))
                            userEntity.setTokenExp(UserManager.getMyUser().getTokenExp());
                        if (!(UserManager.getMyUser().getRefreshToken() == null))
                            userEntity.setRefreshToken(UserManager.getMyUser().getRefreshToken());
                    }
                    UserManager.insertUser(response.body(), isMyUser);

                    mUserEntity = userEntity;
                    isFollow = mUserEntity.isFollowed();

                    updateUi();

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(ProfileActivity.this);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getUserFromServer();
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
                        getUserFromServer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }

    private void updateUi() {
        if (mUserEntity != null) {

            LogUtils.d(TAG, "updateUi , mUserEntity : " + mUserEntity.toString());

            Utils.displayImageAvatar(getApplicationContext(), imgAvatar, mUserEntity.getAvatar());

            tvName.setText(mUserEntity.getFullName());

            if (!TextUtils.isEmpty(mUserEntity.getBackground()))
                Utils.displayImage(this, imgBackground, mUserEntity.getBackground());

            String genderAge = "";

            if (isMyUser)
                genderAge = Utils.converGenderVn(this, mUserEntity.getGender()) + " - " + DateTimeUtils.getAgeFromIso(mUserEntity.getDateOfBirth()) + " " + getString(R.string.profile_age);
            else {
                if (mUserEntity.isPrivacyGender() && mUserEntity.isPrivacyBirth())
                    tvDateOfBirth.setVisibility(View.GONE);
                else if (mUserEntity.isPrivacyGender() && !mUserEntity.isPrivacyBirth())
                    genderAge = DateTimeUtils.getAgeFromIso(mUserEntity.getDateOfBirth()) + " " + getString(R.string.profile_age);
                else if (!mUserEntity.isPrivacyGender() && mUserEntity.isPrivacyBirth())
                    genderAge = Utils.converGenderVn(this, mUserEntity.getGender());
                else
                    genderAge = Utils.converGenderVn(this, mUserEntity.getGender()) + " - " + DateTimeUtils.getAgeFromIso(mUserEntity.getDateOfBirth()) + " " + getString(R.string.profile_age);
            }

            tvDateOfBirth.setText(genderAge);

            tvCountActivity.setText(getString(R.string.count_activity, mUserEntity.getActivitiesCount()));
            tvCountFollow.setText(getString(R.string.count_follow, mUserEntity.getFollowersCount()));

            updateFollowUi();

            if (mUserEntity.getFacebookId() != null && !mUserEntity.getFacebookId().trim().equals("")) {
                imgFbVerify.setImageResource(R.drawable.facebook_on);
                imgCheckedFb.setVisibility(View.VISIBLE);
            } else {
                imgFbVerify.setImageResource(R.drawable.facebook_off);
                imgCheckedFb.setVisibility(View.GONE);
            }

            if (mUserEntity.isEmailActive()) {
                imgEmailVerify.setImageResource(R.drawable.email_on);
                imgCheckedEmail.setVisibility(View.VISIBLE);
            } else {
                imgEmailVerify.setImageResource(R.drawable.email_off);
                imgCheckedEmail.setVisibility(View.GONE);
            }

            tvAddress.setText(mUserEntity.getAddress());

            if (isMyUser) {
                tvMobile.setText(mUserEntity.getPhone());

                layoutLogout.setVisibility(View.VISIBLE);
                imgEdit.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.my_account));
                btnVerify.setVisibility(View.VISIBLE);
            } else {
                tvMobile.setVisibility(View.GONE);
                tvMobileLbl.setVisibility(View.GONE);

                imgEdit.setVisibility(View.GONE);
                layoutLogout.setVisibility(View.GONE);
                tvTitle.setText(getString(R.string.user_account));
                btnVerify.setVisibility(View.GONE);
            }

            tvAbout.setText(mUserEntity.getDescription());

            updateImagesAttach();
            updateSkills();
            updateLanguages();

            tvExperience.setText(mUserEntity.getExperiences());

            if (reviewEntities.size() > 5) {
                btnMoreReview.setVisibility(View.VISIBLE);
            } else {
                btnMoreReview.setVisibility(View.GONE);
            }

            setDataSelected(isTabPoster);

            posterReviewEntity.clear();
            taskerReviewEntity.clear();
            if (mUserEntity.getReviews() != null)
                for (ReviewEntity reviewEntity : mUserEntity.getReviews()) {
                    if (reviewEntity.getType().equals(Constants.REVIEW_TYPE_POSTER)) {
                        posterReviewEntity.add(reviewEntity);
                    } else {
                        taskerReviewEntity.add(reviewEntity);
                    }

                }

            ratingPoster = mUserEntity.getPosterAverageRating();
            rateCountPoster = mUserEntity.getPosterReviewCount();
            retaCountWorker = mUserEntity.getTaskerReviewCount();
            taskPostPoster = mUserEntity.getPosterDoneCount();
            taskPostWorker = mUserEntity.getTaskerDoneCount();
            percentDonePoster = mUserEntity.getPosterDoneRate();
            percentDoneWorker = mUserEntity.getTaskerDoneRate();
            ratingTasker = mUserEntity.getTaskerAverageRating();
        }
    }

    private void updateFollowUi() {
        if (isMyUser) {
            btnFollow.setVisibility(View.GONE);
            layoutFollow.setVisibility(View.GONE);
        } else {
            if (isFollow) btnFollow.setText(getString(R.string.un_follow));
            else btnFollow.setText(getString(R.string.follow));

        }
    }

    private void updateImagesAttach() {
        if (mUserEntity.getImages().size() > 0) {
            final ArrayList<String> attachments = new ArrayList<>();

            for (ImageProfileResponse imageProfile : mUserEntity.getImages()) {
                attachments.add(imageProfile.getUrl());
            }

            ImageDetailTaskAdapter imageDetailTaskAdapter = new ImageDetailTaskAdapter(this, attachments);
            myGridView.setAdapter(imageDetailTaskAdapter);

            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ProfileActivity.this, PreviewImageListActivity.class);
                    intent.putStringArrayListExtra(Constants.IMAGE_ATTACHS_EXTRA, attachments);
                    intent.putExtra(Constants.IMAGE_POSITITON_EXTRA, position);
                    startActivity(intent);
                }
            });
            imageDetailTaskAdapter.notifyDataSetChanged();
            myGridView.setVisibility(View.VISIBLE);
        } else {
            myGridView.setVisibility(View.GONE);
        }
    }

    private void updateSkills() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setAutoMeasureEnabled(true);
        rcvSkills.setNestedScrollingEnabled(false);

        rcvSkills.setLayoutManager(layoutManager);
        ProfileTagAdapter skillsAdapter = new ProfileTagAdapter(mUserEntity.getSkills());
        rcvSkills.setAdapter(skillsAdapter);
    }

    private void updateLanguages() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setAutoMeasureEnabled(true);
        rcvLanguages.setNestedScrollingEnabled(false);

        rcvLanguages.setLayoutManager(layoutManager);
        ProfileTagAdapter languagesAdapter = new ProfileTagAdapter(mUserEntity.getLanguages());
        rcvLanguages.setAdapter(languagesAdapter);
    }


    private void setDataSelected(boolean isPoster) {
        isTabPoster = isPoster;
        reviewEntities.clear();
        if (isPoster) {
            tvReviewsCount.setText(getString(R.string.reviews_count, rateCountPoster));
            tvTaskCount.setText(getString(R.string.post_count, taskPostPoster));
            String percentDone = (int) (percentDonePoster * 100) + "% " + getString(R.string.completion_rate);
            tvCompletionRate.setText(percentDone);
            ratingBar.setRating(ratingPoster);
            reviewEntities.addAll(posterReviewEntity);

            if (rateCountPoster == 0) tvRateLbl.setVisibility(View.GONE);
            else
                tvRateLbl.setVisibility(View.VISIBLE);

        } else {
            tvReviewsCount.setText(getString(R.string.reviews_count, retaCountWorker));
            tvTaskCount.setText(getString(R.string.task_count, taskPostWorker));
            String percentDone = (int) (percentDoneWorker * 100) + "% " + getString(R.string.completion_rate);
            tvCompletionRate.setText(percentDone);
            ratingBar.setRating(ratingTasker);
            reviewEntities.addAll(taskerReviewEntity);

            if (retaCountWorker == 0) tvRateLbl.setVisibility(View.GONE);
            else
                tvRateLbl.setVisibility(View.VISIBLE);

        }
        reviewsListView.updateData((ArrayList<ReviewEntity>) reviewEntities);
        LogUtils.d(TAG, "reviews " + isPoster + reviewEntities.toString());


    }

    private void selectab(int i) {
        if (i == 1) {
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnPoster, ContextCompat.getDrawable(this, R.drawable.my_task_worker_active));
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.tv_gray_new));
            setViewBackground(btnWorker, ContextCompat.getDrawable(this, R.drawable.my_task_poster_default));
            setDataSelected(true);
        } else {
            btnPoster.setTextColor(ContextCompat.getColor(this, R.color.tv_gray_new));
            setViewBackground(btnPoster, ContextCompat.getDrawable(this, R.drawable.my_task_worker_default));
            btnWorker.setTextColor(ContextCompat.getColor(this, R.color.white));
            setViewBackground(btnWorker, ContextCompat.getDrawable(this, R.drawable.my_task_poster_active));
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
                            AccountKit.logOut();
                            if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
                                LoginManager.getInstance().logOut();
                            }

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.deleteAll();
                            realm.commitTransaction();
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
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

    private void doFollow() {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("follow", !isFollow);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doFollow data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().follow(UserManager.getUserToken(), userId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                LogUtils.d(TAG, "doFollow onResponse : " + response.body());
                LogUtils.d(TAG, "doFollow code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    isFollow = !isFollow;
                    updateFollowUi();

                    if (mUserEntity.isFollowed()) {
                        if (isFollow)
                            tvCountFollow.setText(getString(R.string.count_follow, mUserEntity.getFollowersCount()));
                        else
                            tvCountFollow.setText(getString(R.string.count_follow, mUserEntity.getFollowersCount() - 1));
                    } else {
                        if (isFollow)
                            tvCountFollow.setText(getString(R.string.count_follow, mUserEntity.getFollowersCount() + 1));
                        else
                            tvCountFollow.setText(getString(R.string.count_follow, mUserEntity.getFollowersCount()));
                    }

                } else {
                    DialogUtils.showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doFollow();
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
                DialogUtils.showRetryDialog(ProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doFollow();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
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
                intent.putExtra(Constants.TAB_EXTRA, isTabPoster);
                startActivity(intent, TransitionScreen.DOWN_TO_UP);
                break;
            case img_avatar:
                if (!mUserEntity.getAvatar().trim().equals("")) {
                    Intent intentView = new Intent(ProfileActivity.this, PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, mUserEntity.getAvatar());
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;
            case R.id.btn_verify:
                Intent intentVerify = new Intent(this, VerifyUserActivity.class);
                startActivityForResult(intentVerify, Constants.REQUEST_CODE_VERIFY, TransitionScreen.RIGHT_TO_LEFT);
                break;
            case R.id.btn_follow:
                doFollow();
                break;
            case R.id.img_background:

                if (TextUtils.isEmpty(mUserEntity.getBackground())) return;

                Intent intentView = new Intent(ProfileActivity.this, PreviewImageActivity.class);
                intentView.putExtra(Constants.EXTRA_IMAGE_PATH, mUserEntity.getBackground());
                startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                break;
        }
    }


}

