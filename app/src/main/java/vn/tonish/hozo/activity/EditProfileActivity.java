package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DateTimeUtils.getDateBirthDayFromIso;
import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyIsoFromDate;


/**
 * Created by LongBui on 4/21/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private CircleImageView imgAvatar;
    private String imgPath;
    //    private RadioButton rbMale, rbFemale;
    private EdittextHozo edtName, edtAddress;
    private TextViewHozo tvBirthday;
    private final Calendar calendar = Calendar.getInstance();
    //    private RadioGroup rgRadius;
    private File file;
    private int avataId;
    private boolean isUpdateAvata = false;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA};
    private RelativeLayout layoutBirthday;
    private TextViewHozo tvMale, tvFemale;
    private ImageView imgMale, imgFemale;
    private RelativeLayout layoutMale, layoutFemale;
    private String gender;

    @Override
    protected int getLayout() {
        return R.layout.edit_profile_activity;
    }

    @Override
    protected void initView() {
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        ButtonHozo btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        ImageView imgCancel = (ImageView) findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);

        ImageView imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);

        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);

        layoutBirthday = (RelativeLayout) findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);

        tvMale = (TextViewHozo) findViewById(R.id.tv_male);
        tvFemale = (TextViewHozo) findViewById(R.id.tv_female);

        imgMale = (ImageView) findViewById(R.id.img_male);
        imgFemale = (ImageView) findViewById(R.id.img_female);

        layoutMale = (RelativeLayout) findViewById(R.id.layout_male);
        layoutMale.setOnClickListener(this);

        layoutFemale = (RelativeLayout) findViewById(R.id.layout_female);
        layoutFemale.setOnClickListener(this);

//        rgRadius = (RadioGroup) findViewById(R.id.rg_gender);
//        rbMale = (RadioButton) findViewById(R.id.rb_male);
//        rbFemale = (RadioButton) findViewById(R.id.rb_female);
    }

    @Override
    protected void initData() {
        UserEntity userEntity = UserManager.getMyUser();
        edtName.setText(userEntity.getFullName());
        if (userEntity.getAddress() != null)
            edtAddress.setText(userEntity.getAddress());

        if (userEntity.getDateOfBirth().equals(getString(R.string.timezero))) {
            tvBirthday.setText("");
        } else {
            tvBirthday.setText(getDateBirthDayFromIso(userEntity.getDateOfBirth()));
        }

        if (userEntity.getAvatar() != null)
            Utils.displayImageAvatar(this, imgAvatar, userEntity.getAvatar());

        if (userEntity.getGender() != null) {
            gender = userEntity.getGender();
            updateGender(gender);
        }

    }

    @Override
    protected void resumeData() {

    }

    private void updateGender(String gender) {
        if (gender.equals(getString(R.string.gender_male))) {
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.tv_black));
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.tv_gray));
            imgMale.setImageResource(R.drawable.gender_male_on);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        } else if (gender.equals(getString(R.string.gender_female))) {
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.tv_gray));
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.tv_black));
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_on);
        } else {
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.tv_gray));
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.tv_gray));
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:
                doSave();
                break;

            case R.id.img_camera:
                doPickImage();
                break;

            case R.id.img_cancel:
                finish();
                break;

            case R.id.layout_birthday:
                openDatePicker();
                break;

            case R.id.layout_male:
                gender = getString(R.string.gender_male);
                updateGender(getString(R.string.gender_male));
                break;

            case R.id.layout_female:
                gender = getString(R.string.gender_female);
                updateGender(getString(R.string.gender_female));
                break;

        }
    }

    private void doSave() {
        if (isUpdateAvata) {
            updateAvata();
        } else {
            updateProfile();
        }
    }

    private void updateAvata() {

        ProgressDialogUtils.showProgressDialog(this);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    avataId = imageResponse.getIdTemp();
                    updateProfile();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateAvata();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(EditProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            updateAvata();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(EditProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateAvata();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void updateProfile() {
        ProgressDialogUtils.showProgressDialog(this);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            jsonRequest.put(Constants.PARAMETER_ADDRESS, edtAddress.getText().toString());

            if (!tvBirthday.getText().toString().equals(""))
                jsonRequest.put(Constants.PARAMETER_DATE_OF_BIRTH, getOnlyIsoFromDate(tvBirthday.getText().toString()));

            if (gender != null)
                jsonRequest.put(Constants.PARAMETER_GENDER, gender);

            if (isUpdateAvata)
                jsonRequest.put(Constants.PARAMETER_AVATA_ID, avataId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "updateUser jsonRequest : " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                LogUtils.d(TAG, "updateUser onResponse : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    UserEntity userEntity = response.body();
                    userEntity.setAccessToken(UserManager.getMyUser().getAccessToken());
                    userEntity.setTokenExp(UserManager.getMyUser().getTokenExp());
                    userEntity.setRefreshToken(UserManager.getMyUser().getRefreshToken());
                    UserManager.insertUser(response.body(), true);

                    //set return
                    Intent intent = new Intent();
                    setResult(Constants.RESULT_CODE_UPDATE_PROFILE, intent);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateProfile();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(EditProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            updateProfile();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure error : " + t.getMessage());
                DialogUtils.showRetryDialog(EditProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateProfile();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });

    }

    private void openDatePicker() {

        if (!tvBirthday.getText().toString().isEmpty()) {
            Date date = DateTimeUtils.convertToDate(tvBirthday.getText().toString());
            calendar.setTime(date);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 10000);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);
            file = new File(imgPath);
            isUpdateAvata = true;
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(EditProfileActivity.this, CropImageActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, getImagePath());
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
        } else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data != null ? data.getStringExtra(Constants.EXTRA_IMAGE_PATH) : null;
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);
            file = new File(imgPath);
            isUpdateAvata = true;
        }

    }

    private void doPickImage() {

        PickImageDialog pickImageDialog = new PickImageDialog(EditProfileActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                checkPermission();
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != Constants.PERMISSION_REQUEST_CODE
                || grantResults.length == 0
                || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void permissionGranted() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
    }

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private String getImagePath() {
        return imgPath;
    }
}
