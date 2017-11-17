package vn.tonish.hozo.activity.profile;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.image.CropImageActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Image;
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
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.img_avatar;
import static vn.tonish.hozo.R.string.post_task_map_get_location_error_next;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE_AVATA;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.DateTimeUtils.getDateBirthDayFromIso;
import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyIsoFromDate;


/**
 * Created by LongBui on 4/21/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private CircleImageView imgAvatar;
    private String imgPath, imgPathBackground;
    //    private RadioButton rbMale, rbFemale;
    private EdittextHozo edtName, edtAddress, edtDes, edtExperience;
    private TextViewHozo tvBirthday;
    private final Calendar calendar = Calendar.getInstance();
    //    private RadioGroup rgRadius;
    private File file, fileBackground;
    private int avataId, backgroundId;
    private boolean isUpdateAvata = false;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView imgMale, imgFemale;
    private String gender;
    private double lat, lon;
    private String address = "";
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private RadioButton rbMale, rbFemale, rbAny;
    private CheckBox cbHideGender, cbHideBirth;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private final ArrayList<Image> images = new ArrayList<>();
    private RelativeLayout skillsLayout, languagesLayout;
    private UserEntity userEntity;
    private int imageAttachCount = 0;
    private RadioButton rbPoster, rbWorker, rbBoth;
    private ImageView imgEditBackground, imgBackground;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initView() {
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

//        ButtonHozo btnSave = findViewById(R.id.btn_save);
//        btnSave.setOnClickListener(this);

        ImageView imgCancel = (ImageView) findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);

        ImageView imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);

        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);

        RelativeLayout layoutBirthday = (RelativeLayout) findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);


        edtDes = (EdittextHozo) findViewById(R.id.edt_description);
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.edt_address);

        rbMale = (RadioButton) findViewById(R.id.rd_male);
        rbFemale = (RadioButton) findViewById(R.id.rd_female);
        rbAny = (RadioButton) findViewById(R.id.rd_any);

        cbHideGender = (CheckBox) findViewById(R.id.cb_hide_gender);
        cbHideBirth = (CheckBox) findViewById(R.id.cb_hide_birth);

        grImage = (MyGridView) findViewById(R.id.gr_image);

        skillsLayout = (RelativeLayout) findViewById(R.id.skill_layout);
        languagesLayout = (RelativeLayout) findViewById(R.id.languages_layout);

        skillsLayout.setOnClickListener(this);
        languagesLayout.setOnClickListener(this);

        edtExperience = (EdittextHozo) findViewById(R.id.edt_experience);

        rbPoster = (RadioButton) findViewById(R.id.rd_poster);
        rbWorker = (RadioButton) findViewById(R.id.rd_worker);
        rbBoth = (RadioButton) findViewById(R.id.rd_both);

        imgEditBackground = (ImageView) findViewById(R.id.img_edit_background);
        imgEditBackground.setOnClickListener(this);

        imgBackground = (ImageView) findViewById(R.id.img_background);
        imgBackground.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Constants.MAX_IMAGE_ATTACH = 9;
        userEntity = UserManager.getMyUser();

        LogUtils.d(TAG, "EditProfileActivity userEntity : " + userEntity.toString());

        if (userEntity.getAvatar() != null)
            Utils.displayImageAvatar(this, imgAvatar, userEntity.getAvatar());

        edtName.setText(userEntity.getFullName());

        Utils.displayImage(this, imgBackground, userEntity.getBackground());

        if (userEntity.getGender() != null) {
            gender = userEntity.getGender();
            updateGender(gender);
        }

        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) gender = "male";
            }
        });

        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) gender = "female";
            }
        });

        rbAny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) gender = "any";
            }
        });

        cbHideGender.setChecked(userEntity.isPrivacyGender());

        if (userEntity.getDateOfBirth().equals("0001-01-01")) {
            tvBirthday.setText("");
        } else {
            tvBirthday.setText(getDateBirthDayFromIso(userEntity.getDateOfBirth()));
        }

        cbHideBirth.setChecked(userEntity.isPrivacyBirth());

        address = userEntity.getAddress();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.edt_address);

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

        edtDes.setText(userEntity.getDescription());

        for (int i = 0; i < userEntity.getImages().size(); i++) {
            Image image1 = new Image();
            image1.setId(userEntity.getImages().get(i).getId());
            image1.setPath(userEntity.getImages().get(i).getUrl());
            images.add(image1);
        }

        //images
        final Image image = new Image();
        image.setAdd(true);
        images.add(image);

        imageAdapter = new ImageAdapter(this, images);
        grImage.setAdapter(imageAdapter);

        grImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (images.get(position).isAdd) {
                    if (images.size() >= 10) {
                        Utils.showLongToast(EditProfileActivity.this, getString(R.string.max_image_attach_err, 9), true, false);
                    } else {
                        checkPermissionImageAttach();
                    }
                } else {
                    Intent intent = new Intent(EditProfileActivity.this, PreviewImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, images.get(position).getPath());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            }
        });

        edtExperience.setText(userEntity.getExperiences());

        if (userEntity.getRole().equals("poster")) {
            rbPoster.setChecked(true);
        } else if (userEntity.getRole().equals("tasker")) {
            rbWorker.setChecked(true);
        } else {
            rbBoth.setChecked(true);
        }

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
            rbMale.setChecked(true);
        } else if (gender.equals(getString(R.string.gender_female))) {
            rbFemale.setChecked(true);
        } else
            rbAny.setChecked(true);
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
            updateBackground();
        }

    }

    private void updateAvata() {

        ProgressDialogUtils.showProgressDialog(this);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "updateAvata onResponse : " + response.body());
                LogUtils.d(TAG, "updateAvata code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    avataId = imageResponse != null ? imageResponse.getIdTemp() : 0;
                    updateBackground();
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
                LogUtils.e(TAG, "updateAvata onFailure : " + t.getMessage());
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

    private void updateBackground() {

        if (fileBackground == null) {
            updateImageAttach();
            return;
        }

        ProgressDialogUtils.showProgressDialog(this);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileBackground);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileBackground.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "updateBackground onResponse : " + response.body());
                LogUtils.d(TAG, "updateBackground code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    backgroundId = imageResponse != null ? imageResponse.getIdTemp() : 0;
                    updateImageAttach();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateBackground();
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
                            updateBackground();
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
                LogUtils.e(TAG, "updateBackground onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(EditProfileActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateBackground();
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

    private void updateImageAttach() {
        LogUtils.d(TAG, " updateImageAttach start images : " + images.toString());

        if (images.size() > 1) {
            for (int i = 0; i < images.size() - 1; i++)
                if (images.get(i).getId() == 0) imageAttachCount++;

            if (imageAttachCount == 0) {
                updateProfile();
                return;
            }

            for (int i = 0; i < images.size() - 1; i++) {
                if (images.get(i).getId() == 0) {
                    LogUtils.d(TAG, " attachAllFile image " + i + " : " + images.get(i).getPath());
                    File file = new File(images.get(i).getPath());
                    attachFile(file, i);
                }
            }
        } else {
            updateProfile();
        }

    }

    private void attachFile(final File file, final int position) {
        File fileUp = Utils.compressFile(file);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileUp);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileUp.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    imageAttachCount--;

                    if (imageResponse != null)
                        images.get(position).setId(imageResponse.getIdTemp());

                    if (imageAttachCount == 0)
                        updateProfile();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(EditProfileActivity.this);
                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                imageAttachCount--;
                if (imageAttachCount == 0)
                    updateProfile();
            }
        });
    }

    private void updateProfile() {
        ProgressDialogUtils.showProgressDialog(this);
        JSONObject jsonRequest = new JSONObject();
        try {
            if (isUpdateAvata)
                jsonRequest.put(Constants.PARAMETER_AVATA_ID, avataId);

            if (fileBackground != null)
                jsonRequest.put("background_id", backgroundId);

            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            if (gender != null)
                jsonRequest.put(Constants.PARAMETER_GENDER, gender);
            jsonRequest.put("privacy_hide_gender", cbHideGender.isChecked());
            if (!tvBirthday.getText().toString().equals(""))
                jsonRequest.put(Constants.PARAMETER_DATE_OF_BIRTH, getOnlyIsoFromDate(tvBirthday.getText().toString()));
            jsonRequest.put("privacy_hide_date_of_birth", cbHideBirth.isChecked());
            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);
            if (address != null && lat != 0 && lon != 0) {
                jsonRequest.put(Constants.PARAMETER_ADDRESS, address);
            }

            jsonRequest.put(Constants.PARAMETER_DESCRIPTION, edtDes.getText().toString().trim());

            if (images.size() > 1) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < images.size() - 1; i++)
                    jsonArray.put(images.get(i).getId());
                jsonRequest.put("images", jsonArray);
            }

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < userEntity.getSkills().size(); i++) {
                jsonArray.put(userEntity.getSkills().get(i).getValue());
            }
            jsonRequest.put("skills", jsonArray);

            JSONArray jsonArrayLanguage = new JSONArray();
            for (int i = 0; i < userEntity.getLanguages().size(); i++) {
                jsonArrayLanguage.put(userEntity.getLanguages().get(i).getValue());
            }
            jsonRequest.put("languages", jsonArrayLanguage);

            jsonRequest.put("experiences", edtExperience.getText().toString());

            if (rbPoster.isChecked()) {
                jsonRequest.put("role", "poster");
            } else if (rbWorker.isChecked()) {
                jsonRequest.put("role", "tasker");
            } else {
                jsonRequest.put("role", "any");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "updateUser jsonRequest : " + jsonRequest.toString());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {

                LogUtils.d(TAG, "updateUser onResponse : " + response.body());
                LogUtils.d(TAG, "updateUser code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    userEntity = response.body();
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

        //avata
        if (requestCode == REQUEST_CODE_PICK_IMAGE_AVATA
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);
            file = new File(imgPath);
            isUpdateAvata = true;
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA_AVATA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(EditProfileActivity.this, CropImageActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, getImagePath());
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
        }

        // background
        else if (requestCode == REQUEST_CODE_PICK_IMAGE_BACKGROUND
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            String imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(EditProfileActivity.this, imgBackground, imgPath);
            LogUtils.d(TAG, "imgPath background : " + imgPath);
            fileBackground = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA_BACKGROUND && resultCode == Activity.RESULT_OK) {
            String selectedImagePath = getImagePathBackground();
            Utils.displayImage(EditProfileActivity.this, imgBackground, selectedImagePath);
            fileBackground = new File(selectedImagePath);
        }

        // image attach
        else if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            images.addAll(0, imagesSelected);
            imageAdapter.notifyDataSetChanged();
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            final String selectedImagePath = getImagePath();
            LogUtils.d(TAG, "onActivityResult selectedImagePath : " + selectedImagePath);
            Image image = new Image();
            image.setAdd(false);
            image.setPath(selectedImagePath);
            images.add(0, image);
            imageAdapter.notifyDataSetChanged();
        }


        // crop image
        else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data != null ? data.getStringExtra(Constants.EXTRA_IMAGE_PATH) : null;
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);
            file = new File(imgPath != null ? imgPath : null);
            isUpdateAvata = true;
        }

        // skill
        else if (requestCode == Constants.REQUEST_CODE_SKILL && resultCode == Constants.RESULT_CODE_TAG) {
            userEntity = UserManager.getMyUser();
            LogUtils.d(TAG, "onActivityResult userEntity REQUEST_CODE_SKILL : " + userEntity.toString());
        }

        // language
        else if (requestCode == Constants.REQUEST_CODE_LANGUAGE && resultCode == Constants.RESULT_CODE_TAG) {
            userEntity = UserManager.getMyUser();
            LogUtils.d(TAG, "onActivityResult userEntity REQUEST_CODE_LANGUAGE : " + userEntity.toString());
        }

    }

    private void doPickImageAvata() {
        checkPermissionAvata();
    }

    private void doPickImageBackground() {
        checkPermissionBackground();
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

    private void checkPermissionBackground() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE_BACKGROUND);
        } else {
            permissionGrantedBackground();
        }
    }

    private void checkPermissionImageAttach() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGrantedImageAttach();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
//                requestCode != Constants.PERMISSION_REQUEST_CODE
//                ||
                grantResults.length == 0
                        || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_AVATA) {
            permissionGrantedAvata();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_BACKGROUND) {
            permissionGrantedBackground();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            permissionGrantedImageAttach();
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void permissionGrantedAvata() {

        PickImageDialog pickImageDialog = new PickImageDialog(EditProfileActivity.this);
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
                Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE_AVATA, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    private void permissionGrantedBackground() {
        PickImageDialog pickImageDialog = new PickImageDialog(EditProfileActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUriBackground());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA_BACKGROUND);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    private void permissionGrantedImageAttach() {
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
                intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, images.size() - 1);
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

    private Uri setImageUriBackground() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPathBackground = file.getAbsolutePath();
        return imgUri;
    }

    private String getImagePath() {
        return imgPath;
    }

    private String getImagePathBackground() {
        return imgPathBackground;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        Toast.makeText(this,
                getString(R.string.gg_api_error),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                doPickImageAvata();
                break;

            case R.id.img_cancel:
                doSave();
                break;

            case R.id.layout_birthday:
                openDatePicker();
                break;

//            case R.id.layout_male:
//                gender = getString(R.string.gender_male);
//                updateGender(getString(R.string.gender_male));
//                break;
//
//            case R.id.layout_female:
//                gender = getString(R.string.gender_female);
//                updateGender(getString(R.string.gender_female));
//                break;

            case img_avatar:
                if (!UserManager.getMyUser().getAvatar().trim().equals("")) {
                    Intent intentView = new Intent(EditProfileActivity.this, PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, UserManager.getMyUser().getAvatar());
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;

            case R.id.skill_layout:
                Intent intent = new Intent(this, TagActivity.class);
                intent.putExtra(Constants.REQUEST_CODE_EXTRA, Constants.REQUEST_CODE_SKILL);
                startActivityForResult(intent, Constants.REQUEST_CODE_SKILL, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.languages_layout:
                Intent intentL = new Intent(this, TagActivity.class);
                intentL.putExtra(Constants.REQUEST_CODE_EXTRA, Constants.REQUEST_CODE_LANGUAGE);
                startActivityForResult(intentL, Constants.REQUEST_CODE_SKILL, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.img_edit_background:
                doPickImageBackground();
                break;

            case R.id.img_background:

                if (TextUtils.isEmpty(userEntity.getBackground())) return;

                Intent intentView = new Intent(EditProfileActivity.this, PreviewImageActivity.class);
                intentView.putExtra(Constants.EXTRA_IMAGE_PATH, userEntity.getBackground());
                startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}
