package vn.tonish.hozo.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AlbumActivity;
import vn.tonish.hozo.activity.CropImageActivity;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.PreviewImageActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyIsoFromDate;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 5/10/17.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = RegisterFragment.class.getName();
    private EdittextHozo edtName, edtAddress, edtDes;
    private TextViewHozo btnSave;
    private TextViewHozo tvMale, tvFemale;
    private ImageView imgMale, imgFemale;
    private TextViewHozo tvBirthday;
    private String gender;
    private final Calendar calendar = Calendar.getInstance();
    private CircleImageView imgAvatar;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgPath;
    private File file;
    private int avataId;
    private boolean isUpdateAvata = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {
        tvMale = (TextViewHozo) findViewById(R.id.tv_male);
        tvFemale = (TextViewHozo) findViewById(R.id.tv_female);
        imgMale = (ImageView) findViewById(R.id.img_male);
        imgFemale = (ImageView) findViewById(R.id.img_female);
        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);
        edtDes = (EdittextHozo) findViewById(R.id.edt_description);

        ImageView imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        RelativeLayout layoutMale = (RelativeLayout) findViewById(R.id.layout_male);
        layoutMale.setOnClickListener(this);
        RelativeLayout layoutBirthday = (RelativeLayout) findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);
        RelativeLayout layoutFemale = (RelativeLayout) findViewById(R.id.layout_female);
        layoutFemale.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        btnSave = (TextViewHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        imgAvatar = (CircleImageView) findViewById(img_avatar);
        imgAvatar.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        btnSave.setOnClickListener(this);
    }


    @Override
    protected void resumeData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                doSave();
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

            case R.id.img_camera:
                doPickImage();
                break;

            case img_avatar:
                if (file != null) {
                    Intent intentView = new Intent(getActivity(), PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, file.getPath());
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;

        }

    }

    private void doSave() {

        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) {
            edtName.requestFocus();
            edtName.setError(getActivity().getString(R.string.erro_empty_name));
            return;
        } else if (edtDes.getText().toString().trim().length() > 500) {
            edtDes.requestFocus();
            edtDes.setError(getString(R.string.error_des));
            return;
        } else if (edtAddress.getText().toString().trim().equals("")) {
            edtAddress.requestFocus();
            edtAddress.setError(getActivity().getString(R.string.error_address));
            return;
        }

        if (isUpdateAvata) {
            updateAvata();
        } else {
            updateInfor();
        }
    }

    private void updateAvata() {

        ProgressDialogUtils.showProgressDialog(getActivity());

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    avataId = imageResponse != null ? imageResponse.getIdTemp() : 0;
                    updateInfor();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateAvata();
                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    ProgressDialogUtils.dismissProgressDialog();
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            updateAvata();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();

                }
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private void doPickImage() {
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.d(TAG, "onRequestPermissionsResult start");
        if (requestCode != Constants.PERMISSION_REQUEST_CODE
                || grantResults.length == 0
                || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {

        PickImageDialog pickImageDialog = new PickImageDialog(getActivity());
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(getActivity(), AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(getActivity(), imgAvatar, imgPath);
            file = new File(imgPath);
            isUpdateAvata = true;
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(getActivity(), CropImageActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, getImagePath());
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
        } else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data != null ? data.getStringExtra(Constants.EXTRA_IMAGE_PATH) : null;
            Utils.displayImage(getActivity(), imgAvatar, null);
            file = new File(imgPath != null ? imgPath : null);
            isUpdateAvata = true;
        }

    }

    private void openDatePicker() {

        if (!tvBirthday.getText().toString().isEmpty()) {
            Date date = DateTimeUtils.convertToDate(tvBirthday.getText().toString());
            calendar.setTime(date);
        }

        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthday.setText(getString(R.string.edit_profile_birthday, dayOfMonth, monthOfYear + 1, year));
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 10000);
        datePickerDialog.show();
    }

    private void updateGender(String gender) {
        if (gender.equals(getString(R.string.gender_male))) {
            tvMale.setAlpha(1);
            tvFemale.setAlpha(0.7f);
            imgMale.setImageResource(R.drawable.gender_male_on);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        } else if (gender.equals(getString(R.string.gender_female))) {
            tvMale.setAlpha(0.7f);
            tvFemale.setAlpha(1);
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_white);
        } else {
            tvMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_gray));
            tvFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_gray));
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        }
    }

    private void updateInfor() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            jsonRequest.put(Constants.PARAMETER_ADDRESS, edtAddress.getText().toString());
            jsonRequest.put(Constants.PARAMETER_DESCRIPTION, edtDes.getText().toString().trim());

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
        LogUtils.d(TAG, "aaaa onResponse updateInfor UserManager.getUserToken() : " + UserManager.getUserToken());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse updateInfor code : " + response.code());

                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "onResponse updateInfor body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        if (response.body() != null) {
                            UserEntity myUser = UserManager.getMyUser();
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            myUser.setFullName(edtName.getText().toString());
                            realm.commitTransaction();
                        }
                        startActivityAndClearAllTask(new Intent(getContext(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateInfor();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateInfor();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

}
