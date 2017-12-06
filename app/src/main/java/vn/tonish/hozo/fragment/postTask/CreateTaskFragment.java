package vn.tonish.hozo.fragment.postTask;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by CanTran on 12/6/17.
 */

public class CreateTaskFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = CreateTaskFragment.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextViewHozo tvImg;
    private EdittextHozo edtWorkName, edtDescription;
    private CheckBoxHozo checkBoxHozo;
    private TaskResponse taskResponse = new TaskResponse();
    private LinearLayout addressLayout;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private final ArrayList<Image> images = new ArrayList<>();
    private String imgPath;
    private int countImageCopy;
    private boolean isCopy = false;
    private int imageAttachCount;
    private int[] imagesArr;

    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private ButtonHozo btnNext;
    private double lat, lon;
    private String address = "";


    protected int getLayout() {
        return R.layout.fragment_create_task;
    }

    @Override
    protected void initView() {
        ButtonHozo btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        edtWorkName = (EdittextHozo) findViewById(R.id.edt_task_name);
        edtDescription = (EdittextHozo) findViewById(R.id.edt_description);
        checkBoxHozo = (CheckBoxHozo) findViewById(R.id.cb_online_task);
        addressLayout = (LinearLayout) findViewById(R.id.layout_address);
        btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        tvImg = (TextViewHozo) findViewById(R.id.tv_img);
        grImage = (MyGridView) findViewById(R.id.gr_image);
        tvImg.setOnClickListener(this);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {
        if (isCopy) {
            isCopy = false;
            countImageCopy = taskResponse.getAttachments().size();
            ProgressDialogUtils.showProgressDialog(getContext());
            for (int i = 0; i < taskResponse.getAttachments().size(); i++) {
                Glide.with(this)
                        .load(taskResponse.getAttachments().get(i))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                LogUtils.d(TAG, "onResourceReady complete , resource , width : " + resource.getWidth() + " , height : " + resource.getHeight());

                                // sometime glide recycled bitmap
                                resource = resource.copy(resource.getConfig(), true); // safe copy
                                Glide.clear(this); // added to release original bitmap

                                @SuppressWarnings("AccessStaticViaInstance") File fileSave = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                                Utils.compressBitmapToFile(resource, fileSave.getPath());
                                LogUtils.d(TAG, "onResourceReady complete , path : " + fileSave.getPath());

                                Image imageCopy = new Image();
                                imageCopy.setAdd(false);
                                imageCopy.setPath(fileSave.getPath());
                                images.add(0, imageCopy);
                                imageAdapter.notifyDataSetChanged();

                                countImageCopy--;
                                if (countImageCopy == 0)
                                    ProgressDialogUtils.dismissProgressDialog();
                            }
                        });
            }
        } else {
            PickImageDialog pickImageDialog = new PickImageDialog(getContext());
            pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                @Override
                public void onCamera() {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                }

                @Override
                public void onGallery() {
                    Intent intent = new Intent(getContext(), AlbumActivity.class);
                    intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, images.size() - 1);
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
                }
            });
            pickImageDialog.showView();
        }

    }

    private Uri setImageUri() {
        @SuppressWarnings("AccessStaticViaInstance") File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    @Override
    protected void initData() {
        imageAdapter = new ImageAdapter(getContext(), images);
        grImage.setAdapter(imageAdapter);
        toDoAddress();
        edtWorkName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkBoxHozo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) addressLayout.setVisibility(View.GONE);
                else addressLayout.setVisibility(View.VISIBLE);
            }
        });
        grImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (images.get(position).isAdd) {
                    if (images.size() >= 7) {
                        Utils.showLongToast(getContext(), getString(R.string.post_a_task_max_attach_err), true, false);
                    }
                } else {
                    Intent intent = new Intent(getContext(), PreviewImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, images.get(position).getPath());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
                permissionGranted();
            }
        }
    }

    private String getImagePath() {
        return imgPath;
    }

    @Override
    protected void resumeData() {

    }

    private void toDoAddress() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
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
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), googleApiClient, null,
                autocompleteFilter);
        autocompleteView.setAdapter(placeAutocompleteAdapter);

    }

    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item != null ? item.getPlaceId() : null;
            final CharSequence primaryText = item.getPrimaryText(null);

            LogUtils.i(TAG, "Autocomplete item selected: " + primaryText);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            LogUtils.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

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
                hideKeyBoard(getActivity());

            } catch (Exception e) {
                Utils.showLongToast(getContext(), getString(R.string.post_task_map_get_location_error_next), true, false);
            }
        }
    };


    private void doNext() {
        if (images.size() > 1) doAttachFiles();
        else toDoContinue();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
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
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_img:
                checkPermission();
                break;
            case R.id.btn_next:
                doNext();
                break;
        }

    }

    private void doAttachFiles() {
        ProgressDialogUtils.showProgressDialog(getContext());
        imageAttachCount = images.size() - 1;
        imagesArr = new int[images.size() - 1];

        for (int i = 0; i < images.size() - 1; i++) {
            LogUtils.d(TAG, " attachAllFile image " + i + " : " + images.get(i).getPath());
            File file = new File(images.get(i).getPath());
            attachFile(file, i);
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
                        imagesArr[position] = imageResponse.getIdTemp();

                    if (imageAttachCount == 0)
                        toDoContinue();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getContext());
                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                imageAttachCount--;
                if (imageAttachCount == 0)
                    toDoContinue();
                ;
            }
        });
    }

    private void toDoContinue() {
        if (edtWorkName.getText().toString().length() < 10) {
            edtWorkName.requestFocus();
            edtWorkName.setError(getString(R.string.post_a_task_name_error));
            return;
        } else if (edtDescription.getText().toString().length() < 25) {
            edtDescription.requestFocus();
            edtDescription.setError(getString(R.string.post_a_task_description_error));
            return;
        }
        showFragment(R.id.layout_container, PostTaskFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}