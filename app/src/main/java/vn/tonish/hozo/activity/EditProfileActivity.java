package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

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
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
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

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.R.string.post_task_map_get_location_error_next;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DateTimeUtils.getDateBirthDayFromIso;
import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyIsoFromDate;


/**
 * Created by LongBui on 4/21/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private CircleImageView imgAvatar;
    private String imgPath;
    //    private RadioButton rbMale, rbFemale;
    private EdittextHozo edtName, edtAddress, edtDes;
    private TextViewHozo tvBirthday;
    private final Calendar calendar = Calendar.getInstance();
    //    private RadioGroup rgRadius;
    private File file;
    private int avataId;
    private boolean isUpdateAvata = false;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextViewHozo tvMale, tvFemale;
    private ImageView imgMale, imgFemale;
    private String gender;
    private double lat, lon;
    private String address = "";
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;


    @Override
    protected int getLayout() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initView() {
        imgAvatar = findViewById(img_avatar);
        imgAvatar.setOnClickListener(this);

        ButtonHozo btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        ImageView imgCancel = findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);

        ImageView imgCamera = findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        edtName = findViewById(R.id.edt_name);

        tvBirthday = findViewById(R.id.tv_birthday);

        RelativeLayout layoutBirthday = findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);

        tvMale = findViewById(R.id.tv_male);
        tvFemale = findViewById(R.id.tv_female);

        imgMale = findViewById(R.id.img_male);
        imgFemale = findViewById(R.id.img_female);

        edtDes = findViewById(R.id.edt_description);

        RelativeLayout layoutMale = findViewById(R.id.layout_male);
        layoutMale.setOnClickListener(this);

        RelativeLayout layoutFemale = findViewById(R.id.layout_female);
        layoutFemale.setOnClickListener(this);
        autocompleteView = findViewById(R.id.autocomplete_places);

    }

    @Override
    protected void initData() {
        UserEntity userEntity = UserManager.getMyUser();
        edtName.setText(userEntity.getFullName());
//        if (userEntity.getAddress() != null)
//            edtAddress.setText(userEntity.getAddress());

        if (userEntity.getDateOfBirth().equals("0001-01-01")) {
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
        address = userEntity.getAddress();
        edtDes.setText(userEntity.getDescription());
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
        lat = userEntity.getLatitude();
        lon = userEntity.getLongitude();

        autocompleteView.setText(userEntity.getAddress());

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
                address = autocompleteView.getText().toString();
                autocompleteView.setError(null);
                places.release();
                Utils.hideKeyBoard(EditProfileActivity.this);

            } catch (Exception e) {
                Utils.showLongToast(EditProfileActivity.this, getString(post_task_map_get_location_error_next), true, false);
            }
        }
    };

    @Override
    protected void resumeData() {

    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    private void updateGender(String gender) {
        if (gender.equals(getString(R.string.gender_male))) {
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.tv_black));
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.tv_gray));
            imgMale.setImageResource(R.drawable.gender_male_black);
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

            case img_avatar:
                if (!UserManager.getMyUser().getAvatar().trim().equals("")) {
                    Intent intentView = new Intent(EditProfileActivity.this, PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, UserManager.getMyUser().getAvatar());
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;

        }
    }

    private void doSave() {

        if (edtName.getText().toString().trim().equals("")) {
            edtName.requestFocus();
            edtName.setError(getString(R.string.erro_empty_name));
            return;
        } else if (edtDes.getText().toString().trim().length() > 500) {
            edtDes.requestFocus();
            edtDes.setError(getString(R.string.error_des));
            return;
        } else if (lat == 0 && lon == 0) {
            Log.d(TAG, "lat +long" + lat + " : " + lon);
            autocompleteView.requestFocus();
            autocompleteView.setError(getString(R.string.post_task_address_error_google));
            return;
        } else if (TextUtils.isEmpty(address)) {
            autocompleteView.requestFocus();
            autocompleteView.setError(getString(R.string.post_task_address_error));
            return;
        }

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
                    avataId = imageResponse != null ? imageResponse.getIdTemp() : 0;
                    updateProfile();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateAvata();
                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(EditProfileActivity.this);
                    ProgressDialogUtils.dismissProgressDialog();
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
                    ProgressDialogUtils.dismissProgressDialog();
                }
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
//                ProgressDialogUtils.dismissProgressDialog();
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
//            jsonRequest.put(Constants.PARAMETER_ADDRESS, edtAddress.getText().toString());
            jsonRequest.put(Constants.PARAMETER_DESCRIPTION, edtDes.getText().toString().trim());
            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);
            if (address != null && lat != 0 && lon != 0) {
                jsonRequest.put(Constants.PARAMETER_ADDRESS, address);
            }

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
                    assert userEntity != null;
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
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(EditProfileActivity.this);
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

        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
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
            Utils.displayImage(EditProfileActivity.this, imgAvatar, null);
            file = new File(imgPath != null ? imgPath : null);
            isUpdateAvata = true;
        }

    }

    private void doPickImage() {
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
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
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        Toast.makeText(this,
                getString(R.string.gg_api_error),
                Toast.LENGTH_SHORT).show();

    }
}
