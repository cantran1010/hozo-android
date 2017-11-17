package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
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
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE_AVATA;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

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
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private TextViewHozo tvPolicy;
    private TextInputLayout inputLayoutName, inputLayoutAddress, inputLayoutCoupon;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutAddress = (TextInputLayout) findViewById(R.id.input_layout_address);
        inputLayoutCoupon = (TextInputLayout) findViewById(R.id.input_layout_coupon);
        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        edtCoupon = (EdittextHozo) findViewById(R.id.edt_coupon);
        tvPolicy = (TextViewHozo) findViewById(R.id.tv_policy);
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        ImageView imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        imgAvatar = (CircleImageView) findViewById(img_avatar);

        ButtonHozo btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        edtName.addTextChangedListener(new MyTextWatcher(edtName));
        autocompleteView.addTextChangedListener(new MyTextWatcher(autocompleteView));
        edtCoupon.addTextChangedListener(new MyTextWatcher(edtCoupon));

    }


    @Override
    protected void initData() {
        setUnderLinePolicy(tvPolicy);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        autocompleteView.setThreshold(1);

        // Register a listener that receives callbacks when a suggestion has been selected
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("VN")
                .build();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, null,
                autocompleteFilter);
        autocompleteView.setAdapter(placeAutocompleteAdapter);


    }


    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            LogUtils.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            LogUtils.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                LogUtils.e(TAG, "Place address : " + place.getAddress());
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                address = autocompleteView.getText().toString().trim();
                autocompleteView.setError(null);
                places.release();
                Utils.hideKeyBoard(RegisterActivity.this);

            } catch (Exception e) {
                inputLayoutAddress.setError(getString(R.string.post_task_map_get_location_error_next));
            }
        }
    };

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
                new ForegroundColorSpan(Color.parseColor("#000000")), // Span to add
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
                new ForegroundColorSpan(Color.parseColor("#000000")),
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
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
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
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);

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
                        Utils.settingDefault(RegisterActivity.this);
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
                } else {
                    APIError error = ErrorUtils.parseError(response);
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
                case R.id.autocomplete_places:
                    validateAdress();
                    break;
                case R.id.edt_coupon:
                    validatePhone();
                    break;
            }
        }
    }

    private boolean validateAdress() {
        if (lat == 0 && lon == 0) {
            inputLayoutAddress.setError(getString(R.string.post_task_address_error_google));
            requestFocus(autocompleteView);
            return false;
        } else if (TextUtils.isEmpty(address)) {
            inputLayoutAddress.setError(getString(R.string.post_task_address_error));
            requestFocus(autocompleteView);
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (edtName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(edtName);
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
            requestFocus(edtCoupon);
            return false;
        } else if (isNumberValid("84", mb)) {
            inputLayoutCoupon.setError(getString(R.string.code_coupon_error));
            requestFocus(edtCoupon);
        } else {
            inputLayoutCoupon.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
