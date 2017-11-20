package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.CropImageActivity;
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
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE_AVATA;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by CanTran on 10/23/17.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private final static String TAG = RegisterActivity.class.getName();
    private EdittextHozo edtName, edtCoupon;
    private CircleImageView imgAvatar;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgPath;
    private File file;
    private int avataId;
    private boolean isUpdateAvata = false;
    private double lat, lon;
    private String address = "";
    private EdittextHozo edtAddress;
    private TextViewHozo tvPolicy;
    private android.support.design.widget.TextInputLayout inputLayoutName, inputLayoutAddress, inputLayoutCoupon;
    private CheckBoxHozo checkBoxCoupon;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        inputLayoutName = (android.support.design.widget.TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutAddress = (android.support.design.widget.TextInputLayout) findViewById(R.id.input_layout_address);
        inputLayoutCoupon = (android.support.design.widget.TextInputLayout) findViewById(R.id.input_layout_coupon);
        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        edtCoupon = (EdittextHozo) findViewById(R.id.edt_coupon);
        tvPolicy = (TextViewHozo) findViewById(R.id.tv_policy);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);
        ImageView imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);
        imgAvatar = (CircleImageView) findViewById(img_avatar);
        ButtonHozo btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        checkBoxCoupon = (CheckBoxHozo) findViewById(R.id.ckBox_coupon);
        if (checkBoxCoupon.isChecked()) inputLayoutCoupon.setVisibility(View.VISIBLE);
        else inputLayoutCoupon.setVisibility(View.GONE);
        btnSave.setOnClickListener(this);
        edtAddress.setOnClickListener(this);
        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        edtAddress.addTextChangedListener(new MyTextWatcher(edtAddress));
        edtCoupon.addTextChangedListener(new MyTextWatcher(edtCoupon));

    }


    @Override
    protected void initData() {
        hideKeyBoard(this);
        setUnderLinePolicy(tvPolicy);
        checkBoxCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) inputLayoutCoupon.setVisibility(View.VISIBLE);
                else inputLayoutCoupon.setVisibility(View.GONE);

            }
        });
    }


    public void findPlace() {

        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("VN")
                .build();

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(autocompleteFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            LogUtils.d(TAG, "GooglePlayServicesRepairableException" + e.toString());
            inputLayoutAddress.setError(getString(R.string.post_task_map_get_location_error_next));
        } catch (GooglePlayServicesNotAvailableException e) {
            LogUtils.d(TAG, "GooglePlayServicesNotAvailableException" + e.toString());
            inputLayoutAddress.setError(getString(R.string.post_task_map_get_location_error_next));
        }
    }


    private void setUnderLinePolicy(TextViewHozo textViewHozo) {
        String text = getString(R.string.tv_login_policy);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

        ClickableSpan conditionClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openGeneralInfoActivity(getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
            }
        };
        ClickableSpan nadClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openGeneralInfoActivity(getString(R.string.other_nad), "http://hozo.vn/chinh-sach-bao-mat/?ref=app");
            }
        };

        ssBuilder.setSpan(
                conditionClickableSpan, // Span to add
                text.indexOf(getString(R.string.login_policy_condition)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.login_policy_condition)) + String.valueOf(getString(R.string.login_policy_condition)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#73888e")), // Span to add
                text.indexOf(getString(R.string.login_policy_condition)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.login_policy_condition)) + String.valueOf(getString(R.string.login_policy_condition)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                nadClickableSpan,
                text.indexOf(getString(R.string.login_policy_nad)),
                text.indexOf(getString(R.string.login_policy_nad)) + String.valueOf(getString(R.string.login_policy_nad)).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#73888e")),
                text.indexOf(getString(R.string.login_policy_nad)),
                text.indexOf(getString(R.string.login_policy_nad)) + String.valueOf(getString(R.string.login_policy_nad)).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        textViewHozo.setText(ssBuilder);
        textViewHozo.setMovementMethod(LinkMovementMethod.getInstance());
        textViewHozo.setHighlightColor(Color.TRANSPARENT);
    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(this, GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void resumeData() {

    }

    private void doPickImage() {
        checkPermissionAvata();
    }

    private void checkPermissionAvata() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE_AVATA);
        } else {
            permissionGrantedAvata();
        }
    }


    private void permissionGrantedAvata() {

        PickImageDialog pickImageDialog = new PickImageDialog(RegisterActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                LogUtils.d(TAG, "permissionGrantedAvata onCamera start");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA_AVATA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(RegisterActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE_AVATA, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //avata
        if (requestCode == REQUEST_CODE_PICK_IMAGE_AVATA
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(RegisterActivity.this, imgAvatar, imgPath);
            file = new File(imgPath);
            isUpdateAvata = true;
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA_AVATA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(RegisterActivity.this, CropImageActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, getImagePath());
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
        } else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data != null ? data.getStringExtra(Constants.EXTRA_IMAGE_PATH) : null;
            Utils.displayImage(RegisterActivity.this, imgAvatar, imgPath);
            file = new File(imgPath != null ? imgPath : null);
            isUpdateAvata = true;
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LogUtils.d(TAG, "Place: " + place.getName());
                // Get the Place object from the buffer.
                LogUtils.e(TAG, "Place address : " + place.getAddress());
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                edtAddress.setText(place.getAddress());
                address = place.getAddress().toString();
                hideKeyBoard(RegisterActivity.this);
                inputLayoutAddress.setErrorEnabled(false);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                LogUtils.d(TAG, status.getStatusMessage());
                inputLayoutAddress.setError(getString(R.string.post_task_map_get_location_error_next));


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
                grantResults.length == 0
                        || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_AVATA) {
            permissionGrantedAvata();
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private String getImagePath() {
        return imgPath;
    }

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                doPickImage();
                break;
            case R.id.btn_save:
                doSave();
                break;
            case R.id.edt_address:
                findPlace();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AccountKit.logOut();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.putExtra(Constants.LOGOUT_EXTRA, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, TransitionScreen.FADE_IN);

    }

    private void doSave() {

        if (!validateName()) {
            return;
        }
        if (!validateAdress()) {
            return;
        }
        if (checkBoxCoupon.isChecked()) {
            if (!validatePhone()) {
                return;
            }
        }
        if (isUpdateAvata) {
            updateAvata();
        } else {
            updateInfor();
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
                    avataId = imageResponse != null ? imageResponse.getIdTemp() : 0;
                    updateInfor();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(RegisterActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateAvata();
                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    ProgressDialogUtils.dismissProgressDialog();
                    Utils.blockUser(RegisterActivity.this);
                } else {
                    showRetryDialog(RegisterActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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
                showRetryDialog(RegisterActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private void updateInfor() {
        ProgressDialogUtils.showProgressDialog(RegisterActivity.this);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString().trim());
            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);
            if (checkBoxCoupon.isChecked() && validatePhone())
                jsonRequest.put("referrer_phone", edtCoupon.getText().toString().trim());

            if (address != null && lat != 0 && lon != 0) {
                jsonRequest.put(Constants.PARAMETER_ADDRESS, address);
            }

            if (isUpdateAvata)
                jsonRequest.put(Constants.PARAMETER_AVATA_ID, avataId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, " onResponse updateInfor UserManager.getUserToken() : " + UserManager.getUserToken());
        LogUtils.d(TAG, " jsonRequest updateInfor: " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse updateInfor code : " + response.code());
                APIError error = ErrorUtils.parseError(response);
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "onResponse updateInfor body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        if (response.body() != null) {
                            UserEntity myUser = UserManager.getMyUser();
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            myUser.setFullName(edtName.getText().toString());
                            myUser.setLongitude(lat);
                            myUser.setLongitude(lon);
                            realm.commitTransaction();

                        }
                        Utils.showLongToast(RegisterActivity.this, getString(R.string.register_done), false, false);
                        startActivityAndClearAllTask(new Intent(RegisterActivity.this, MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    LogUtils.d(TAG, "onResponse HTTP_CODE_UNAUTHORIZED : ");
                    NetworkUtils.refreshToken(RegisterActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateInfor();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(RegisterActivity.this);
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    if (error.status().equals(Constants.NO_REFERRER)) {
                        Utils.showLongToast(RegisterActivity.this, getString(R.string.no_referrer), true, false);
                        edtCoupon.requestFocus();
                        inputLayoutCoupon.setError(getString(R.string.no_referrer));
                    } else if (error.status().equals(Constants.REFERRER_PHONE_YOURSELF)) {
                        Utils.showLongToast(RegisterActivity.this, getString(R.string.referrer_phone_yourself), true, false);
                        edtCoupon.requestFocus();
                        inputLayoutCoupon.setError(getString(R.string.referrer_phone_yourself));
                    } else if (error.status().equals(Constants.INVALID_REFERRER_PHONE)) {
                        Utils.showLongToast(RegisterActivity.this, getString(R.string.invalid_referrer_phone), true, false);
                        edtCoupon.requestFocus();
                        inputLayoutCoupon.setError(getString(R.string.invalid_referrer_phone));
                    } else if (error.status().equals(Constants.EMPTY_PARAMETERS)) {
                        Utils.showLongToast(RegisterActivity.this, getString(R.string.empty_parameters_register), true, false);
                        edtCoupon.requestFocus();
                        inputLayoutCoupon.setError(getString(R.string.empty_parameters_register));
                    }
                } else {
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(RegisterActivity.this, error.message(), true, false);
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                showRetryDialog(RegisterActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        Toast.makeText(RegisterActivity.this,
                getString(R.string.gg_api_error),
                Toast.LENGTH_SHORT).show();

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_name:
                    validateName();
                    break;
                case R.id.edt_address:
                    validateAdress();
                    break;
                case R.id.edt_coupon:
                    String mb = edtCoupon.getText().toString().trim();
                    if (isNumberValid("84", mb)) {
                        hideSoftKeyboard(RegisterActivity.this, edtCoupon);
                        inputLayoutCoupon.setErrorEnabled(false);
                    }
                    break;
            }

        }
    }

    private boolean validateAdress() {
        if (lat == 0 && lon == 0) {
            inputLayoutAddress.setError(getString(R.string.post_task_address_error_google));
            edtAddress.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            inputLayoutAddress.setError(getString(R.string.empty_adress));
            edtAddress.requestFocus();
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (edtName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            edtName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        String mb = edtCoupon.getText().toString().trim();
        if (mb.isEmpty()) {
            inputLayoutCoupon.setError(getString(R.string.code_coupon_empty));
            edtCoupon.requestFocus();
            return false;
        } else if (!isNumberValid("84", mb)) {
            inputLayoutCoupon.setError(getString(R.string.code_coupon_error));
            edtCoupon.requestFocus();
            return false;
        } else {
            hideSoftKeyboard(this, edtCoupon);
            inputLayoutCoupon.setErrorEnabled(false);
        }

        return true;
    }


    private boolean isNumberValid(String countryCode, String phNumber) {
        if (TextUtils.isEmpty(countryCode)) {// Country code could not be empty
            return false;
        }
        if (phNumber.length() < 6) {        // Phone number should be at least 6 digits
            return false;
        }
        boolean resultPattern = Patterns.PHONE.matcher(phNumber).matches();
        if (!resultPattern) {
            return false;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (Exception e) {
            return false;
        }

        return phoneNumberUtil.isValidNumber(phoneNumber);
    }

}
