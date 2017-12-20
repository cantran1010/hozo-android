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
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.PostTaskActivity;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.PostTaskEntity;
import vn.tonish.hozo.database.manager.PostTaskManager;
import vn.tonish.hozo.dialog.AlertDialogCancelTask;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.HozoAutoCompleteTextView;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.MAX_LENGTH_DES;
import static vn.tonish.hozo.common.Constants.MAX_LENGTH_TITLE;
import static vn.tonish.hozo.common.Constants.MIN_LENGTH_DES;
import static vn.tonish.hozo.common.Constants.MIN_LENGTH_TITLE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by CanTran on 12/6/17.
 */

public class CreateTaskFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = CreateTaskFragment.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextViewHozo tvTaskNameMsg;
    private TextViewHozo tvDesMsg;
    private EdittextHozo edtWorkName, edtDescription;
    private CheckBoxHozo checkBoxHozo;
    private TaskResponse taskResponse = new TaskResponse();
    private LinearLayout addressLayout;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private String imgPath;
    private int countImageCopy;

    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private HozoAutoCompleteTextView autocompleteView;
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
        TextViewHozo tvImg = (TextViewHozo) findViewById(R.id.tv_img);
        grImage = (MyGridView) findViewById(R.id.gr_image);
        tvImg.setOnClickListener(this);
        autocompleteView = (HozoAutoCompleteTextView) findViewById(R.id.edt_address);
        tvTaskNameMsg = (TextViewHozo) findViewById(R.id.tv_title_msg);
        tvDesMsg = (TextViewHozo) findViewById(R.id.tv_des_msg);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Constants.MAX_IMAGE_ATTACH = 6;

        taskResponse = ((PostTaskActivity) getActivity()).getTaskResponse();
        imageAdapter = new ImageAdapter(getContext(), ((PostTaskActivity) getActivity()).images);
        grImage.setAdapter(imageAdapter);
        toDoAddress();
        getDefaultAddress();
        edtWorkName.addTextChangedListener(new MyTextWatcher(edtWorkName));
        edtDescription.addTextChangedListener(new MyTextWatcher(edtDescription));
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
                if (((PostTaskActivity) getActivity()).images.get(position).isAdd) {
                    if (((PostTaskActivity) getActivity()).images.size() >= 6) {
                        Utils.showLongToast(getContext(), getString(R.string.post_a_task_max_attach_err), true, false);
                    }
                } else {
                    Intent intent = new Intent(getContext(), PreviewImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, ((PostTaskActivity) getActivity()).images.get(position).getPath());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            }
        });

        if (((PostTaskActivity) getActivity()).isExtraTask) {
            edtWorkName.setText(taskResponse.getTitle());
            tvTaskNameMsg.setText(getString(R.string.post_a_task_msg_length, edtWorkName.getText().toString().length(), MAX_LENGTH_TITLE));
            edtDescription.setText(taskResponse.getDescription());
            tvDesMsg.setText(getString(R.string.post_a_task_msg_length, edtDescription.getText().toString().length(), MAX_LENGTH_DES));
            checkBoxHozo.setChecked(taskResponse.isOnline());
            lat = taskResponse.getLatitude();
            lon = taskResponse.getLongitude();
            address = taskResponse.getAddress();
            autocompleteView.setText(address);
            if (taskResponse.getAttachments() != null && taskResponse.getAttachments().size() > 0) {
                ((PostTaskActivity) getActivity()).setCopy(true);
                checkPermission();
            }
        }
        edtWorkName.setHint(((PostTaskActivity) getActivity()).getCategory().getSuggestTitle());
        edtDescription.setHint(((PostTaskActivity) getActivity()).getCategory().getDescription());

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {
        if (((PostTaskActivity) getActivity()).isCopy()) {
            ((PostTaskActivity) getActivity()).setCopy(false);
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
                                resource = resource.copy(resource.getConfig(), true); // safe copy
                                Glide.clear(this);
                                @SuppressWarnings("AccessStaticViaInstance") File fileSave = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                                Utils.compressBitmapToFile(resource, fileSave.getPath());
                                LogUtils.d(TAG, "onResourceReady complete , path : " + fileSave.getPath());
                                Image imageCopy = new Image();
                                imageCopy.setAdd(false);
                                imageCopy.setPath(fileSave.getPath());
                                ((PostTaskActivity) getActivity()).images.add(0, imageCopy);
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
                    intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, ((PostTaskActivity) getActivity()).images.size());
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

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    private void toDoAddress() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        autocompleteView.setThreshold(1);
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .build();
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
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {
                final Place place = places.get(0);
                LogUtils.e(TAG, "Place address : " + place.getAddress());
                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                address = autocompleteView.getText().toString().trim();
                autocompleteView.setError(null);
                places.release();
                hideKeyBoard(getActivity());
            } catch (Exception e) {
                Utils.showLongToast(getContext(), getString(R.string.post_task_map_get_location_error_next), true, false);
            }
        }
    };

    private void getDefaultAddress() {
        PostTaskEntity postTaskEntity = PostTaskManager.getPostTaskEntity();
        if (postTaskEntity != null) {
            lat = postTaskEntity.getLat();
            lon = postTaskEntity.getLon();
            address = postTaskEntity.getAddress();
            LogUtils.d(TAG, "getDefaultAddress" + postTaskEntity.toString());
            autocompleteView.setText(address);
        }
    }

    private void doNext() {
        if (edtWorkName.getText().toString().length() < 10) {
            edtWorkName.requestFocus();
            edtWorkName.setError(getString(R.string.post_a_task_name_error));
        } else if (!checkBoxHozo.isChecked() && !validAdress()) {
            autocompleteView.requestFocus();
            autocompleteView.setError(getString(R.string.post_task_address_error_google));
            address = "";
            lat = 0;
            lon = 0;
            autocompleteView.requestFocus();
        } else {
            inserTask(((PostTaskActivity) getActivity()).getTaskResponse());
            openFragment(R.id.layout_container, PostTaskFragment.class, new Bundle(), true, TransitionScreen.RIGHT_TO_LEFT);
        }


    }

    private boolean validAdress() {
        String strAuto = autocompleteView.getText().toString().trim();
        if (strAuto.isEmpty())
            return false;
        else if (address.isEmpty())
            return false;
        else
            return !(lat == 0 && lon == 0) && strAuto.equalsIgnoreCase(address);
    }

    private void doClose() {
        if (edtWorkName.getText().toString().trim().equals("") && edtDescription.getText().toString().trim().equals("")
                && edtDescription.getText().toString().trim().equals("") && !checkBoxHozo.isChecked()) {
            getActivity().finish();

        } else {

            final AlertDialogCancelTask alertDialogCancelTask = new AlertDialogCancelTask(getContext());
            alertDialogCancelTask.setAlertConfirmDialogListener(new AlertDialogCancelTask.AlertConfirmDialogListener() {
                @Override
                public void onOk() {
                    alertDialogCancelTask.hideView();
                    getActivity().finish();
                }

                @Override
                public void onCancel() {
                    alertDialogCancelTask.hideView();
                }
            });
            alertDialogCancelTask.showView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            ((PostTaskActivity) getActivity()).images.addAll(0, imagesSelected);
            imageAdapter.notifyDataSetChanged();
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            final String selectedImagePath = getImagePath();
            LogUtils.d(TAG, "onActivityResult selectedImagePath : " + selectedImagePath);
            Image image = new Image();
            image.setAdd(false);
            image.setPath(selectedImagePath);
            ((PostTaskActivity) getActivity()).images.add(0, image);
            imageAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                doClose();
                break;
            case R.id.tv_img:

                if (((PostTaskActivity) getActivity()).images.size() < 6) {
                    checkPermission();
                } else {
                    Utils.showLongToast(getContext(), getString(R.string.post_a_task_max_attach_err), true, false);
                }
                break;
            case R.id.btn_next:
                doNext();
                break;
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Utils.showLongToast(getContext(), getString(R.string.gg_api_error), true, false);
    }


    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_task_name:
                    if (editable.toString().length() > MAX_LENGTH_TITLE) {
                        edtWorkName.setText(editable.subSequence(0, MAX_LENGTH_TITLE));
                        edtWorkName.setSelection(MAX_LENGTH_TITLE);
                    } else if (editable.toString().length() < MIN_LENGTH_TITLE) {
                        tvTaskNameMsg.setTextColor(ContextCompat.getColor(getContext(), R.color.color_count_word));
                        tvTaskNameMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MIN_LENGTH_TITLE));
                    } else {
                        tvTaskNameMsg.setTextColor(ContextCompat.getColor(getContext(), R.color.color_create_task_lable));
                        tvTaskNameMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_TITLE));
                    }
                    break;
                case R.id.edt_description:
                    if (editable.toString().length() > MAX_LENGTH_DES) {
                        edtDescription.setText(editable.subSequence(0, MAX_LENGTH_DES));
                        edtDescription.setSelection(MAX_LENGTH_DES);
                    } else if (editable.toString().length() < MIN_LENGTH_DES) {
                        tvDesMsg.setTextColor(ContextCompat.getColor(getContext(), R.color.color_count_word));
                        tvDesMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MIN_LENGTH_DES));
                    } else {
                        tvDesMsg.setTextColor(ContextCompat.getColor(getContext(), R.color.color_create_task_lable));
                        tvDesMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_DES));
                    }
                    break;
            }

        }
    }

    private void inserTask(TaskResponse response) {
        response.setCategoryId(((PostTaskActivity) getActivity()).getCategory().getId());
        response.setTitle(edtWorkName.getText().toString().trim());
        response.setDescription(edtDescription.getText().toString().trim());
        response.setOnline(checkBoxHozo.isChecked());
        if (!checkBoxHozo.isChecked()) {
            response.setLatitude(lat);
            response.setLongitude(lon);
            response.setAddress(address);
        }

    }
}