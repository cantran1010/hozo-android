package vn.tonish.hozo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

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
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;


/**
 * Created by LongBui on 4/21/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    protected ButtonHozo btnSave;
    private CircleImageView imgAvatar;
    private String imgPath;
    private TextViewHozo tvCancel;
    private ImageView imgCamera;
    private EdittextHozo edtName, edtAddress;
    private TextViewHozo tvBirthday;
    private Calendar calendar = Calendar.getInstance();
    private File file;
    private User user;
    private int avataId;
    private boolean isUpdateAvata = false;

    @Override
    protected int getLayout() {
        return R.layout.edit_profile_activity;
    }

    @Override
    protected void initView() {
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);

        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvBirthday.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        user = (User) getIntent().getSerializableExtra(Constants.USER);

        edtName.setText(user.getFullName());
        edtAddress.setText(user.getAddress());
        tvBirthday.setText(user.getDateOfBirth());
        Utils.displayImageAvatar(this, imgAvatar, user.getAvatar());
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

            case R.id.img_camera:
                doPickImage();
                break;

            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_birthday:
                openDatePicker();
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

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(this), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    avataId = imageResponse.getIdTemp();
                    updateProfile();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
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
            }
        });
    }

    private void updateProfile() {

        ProgressDialogUtils.showProgressDialog(this);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            jsonRequest.put(Constants.PARAMETER_EMAIL, user.getEmail());
            jsonRequest.put(Constants.PARAMETER_ADDRESS, edtAddress.getText().toString());
            jsonRequest.put(Constants.PARAMETER_DATE_OF_BIRTH, tvBirthday.getText().toString());

            if (isUpdateAvata)
                jsonRequest.put(Constants.PARAMETER_AVATA_ID, avataId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "updateUser jsonRequest : " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(this), body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                LogUtils.d(TAG, "updateUser onResponse : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {

                    //update in database
                    DataParse.updateUser(response.body(), EditProfileActivity.this);

                    //set return
                    Intent intent = new Intent();
                    setResult(Constants.RESULT_CODE_UPDATE_PROFILE, intent);
                    finish();

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateAvata();
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
            public void onFailure(Call<User> call, Throwable t) {
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

        Date date = DateTimeUtils.convertToDate(tvBirthday.getText().toString());
        calendar.setTime(date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE);
        } else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
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
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });
        pickImageDialog.showView();
    }

    public Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }
}
