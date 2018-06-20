package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

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
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 11/21/17.
 */

public class VerifyCMNDActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = VerifyCMNDActivity.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView imgAddBefore, imgAddAfter, imgAddPortrait;
    private ImageView imgShowBefore, imgShowAfter, getImgShowPortrait;
    private String imgPathBefore = "";
    private String imgPathAfter = "";
    private String imgPathPortrait = "";
    private ArrayList<File> files = new ArrayList<>();
    private LinearLayout layoutBefore, layoutAfter, layoutPortrait;
    private TextViewHozo btnEditbf, btnDeletebf;
    private TextViewHozo btnEditAf, btnDeleteAf;
    private TextViewHozo btnDeletePt, btnEditPt;
    private int countImage;
    private int imageAttachCount;
    private int[] imagesArr;


    @Override
    protected int getLayout() {
        return R.layout.activity_verify_cmnd;
    }

    @Override
    protected void initView() {
        imgAddBefore = (ImageView) findViewById(R.id.img_add_before);
        imgAddBefore.setOnClickListener(this);
        imgAddAfter = (ImageView) findViewById(R.id.img_add_after);
        imgAddAfter.setOnClickListener(this);
        imgAddPortrait = (ImageView) findViewById(R.id.img_add_portrait);
        imgAddPortrait.setOnClickListener(this);

        imgShowBefore = (ImageView) findViewById(R.id.img_show_before);
        imgShowAfter = (ImageView) findViewById(R.id.img_show_after);
        getImgShowPortrait = (ImageView) findViewById(R.id.img_show_portrait);

        layoutBefore = (LinearLayout) findViewById(R.id.layout_before);
        layoutAfter = (LinearLayout) findViewById(R.id.layout_after);
        layoutPortrait = (LinearLayout) findViewById(R.id.layout_portrait);

        btnEditbf = (TextViewHozo) findViewById(R.id.btn_edit_before);
        btnEditAf = (TextViewHozo) findViewById(R.id.btn_edit_after);
        btnEditPt = (TextViewHozo) findViewById(R.id.btn_edit_portrait);

        btnDeletebf = (TextViewHozo) findViewById(R.id.btn_delete_before);
        btnDeleteAf = (TextViewHozo) findViewById(R.id.btn_delete_after);
        btnDeletePt = (TextViewHozo) findViewById(R.id.btn_delete_portrait);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_save).setOnClickListener(this);
        imgShowBefore.setOnClickListener(this);
        imgShowAfter.setOnClickListener(this);
        getImgShowPortrait.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        btnEditbf.setOnClickListener(this);
        btnDeletebf.setOnClickListener(this);
        btnEditAf.setOnClickListener(this);
        btnDeleteAf.setOnClickListener(this);
        btnEditPt.setOnClickListener(this);
        btnDeletePt.setOnClickListener(this);
        if (imgPathBefore.isEmpty()) {
            layoutBefore.setVisibility(View.GONE);
            imgAddBefore.setEnabled(true);
        }
        if (imgPathAfter.isEmpty()) {
            layoutAfter.setVisibility(View.GONE);
            imgAddAfter.setEnabled(true);
        }
        if (imgPathPortrait.isEmpty()) {
            layoutPortrait.setVisibility(View.GONE);
            imgAddPortrait.setEnabled(true);
        }
    }


    @Override
    protected void resumeData() {

    }

    private void checkPermissionBackground(int isBefore) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE_BACKGROUND);
        } else {
            permissionGrantedBackground(isBefore);
        }
    }

    private void permissionGrantedBackground(final int isBefore) {
        PickImageDialog pickImageDialog = new PickImageDialog(VerifyCMNDActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUriBackground(isBefore));
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA_BACKGROUND);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(VerifyCMNDActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    private Uri setImageUriBackground(int isBefore) {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        if (isBefore == 0)
            this.imgPathBefore = file.getAbsolutePath();
        else if (isBefore == 1)
            this.imgPathAfter = file.getAbsolutePath();
        else this.imgPathPortrait = file.getAbsolutePath();
        return imgUri;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_BACKGROUND) {
            permissionGrantedBackground(countImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            if (countImage == 0) {
                imgPathBefore = imagesSelected.get(0).getPath();
                files.add(0, new File(imgPathBefore));
            } else if (countImage == 1) {
                imgPathAfter = imagesSelected.get(0).getPath();
                files.add(1, new File(imgPathAfter));
            } else {
                imgPathPortrait = imagesSelected.get(0).getPath();
                files.add(1, new File(imgPathPortrait));
            }
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA_BACKGROUND && resultCode == Activity.RESULT_OK) {
            if (countImage == 0)
                files.add(0, new File(imgPathBefore));
            else if (countImage == 1)
                files.add(1, new File(imgPathAfter));
            else
                files.add(2, new File(imgPathPortrait));
        }
        if (!imgPathBefore.isEmpty()) {
            layoutBefore.setVisibility(View.VISIBLE);
            imgShowBefore.setVisibility(View.VISIBLE);
            Utils.displayImage(VerifyCMNDActivity.this, imgShowBefore, imgPathBefore);
            imgAddBefore.setEnabled(false);
        }
        if (!imgPathAfter.isEmpty()) {
            layoutAfter.setVisibility(View.VISIBLE);
            imgShowAfter.setVisibility(View.VISIBLE);
            Utils.displayImage(VerifyCMNDActivity.this, imgShowAfter, imgPathAfter);
            imgAddAfter.setEnabled(false);
        }

        if (!imgPathPortrait.isEmpty()) {
            layoutPortrait.setVisibility(View.VISIBLE);
            getImgShowPortrait.setVisibility(View.VISIBLE);
            Utils.displayImage(VerifyCMNDActivity.this, getImgShowPortrait, imgPathPortrait);
            imgAddPortrait.setEnabled(false);
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void doAttachFiles() {
        ProgressDialogUtils.showProgressDialog(this);
        imageAttachCount = files.size();
        imagesArr = new int[files.size()];
        for (int i = 0; i < files.size(); i++) {
            attachFile(files.get(i), i);
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
                        updateProfile();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(VerifyCMNDActivity.this);
                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
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
            jsonRequest.put(Constants.PARAMETER_USE_IDENTITY_FRONT_ID, imagesArr[0]);
            jsonRequest.put(Constants.PARAMETER_USE_IDENTITY_BACK_ID, imagesArr[1]);
            jsonRequest.put(Constants.PARAMETER_USE_PORTRAIN_IMAGE_ID, imagesArr[2]);
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
                switch (response.code()) {
                    case Constants.HTTP_CODE_OK:
                        UserEntity userEntity = response.body();
                        assert userEntity != null;
                        userEntity.setAccessToken(UserManager.getMyUser().getAccessToken());
                        userEntity.setTokenExp(UserManager.getMyUser().getTokenExp());
                        userEntity.setRefreshToken(UserManager.getMyUser().getRefreshToken());
                        UserManager.insertUser(response.body(), true);
                        //set return
                        finish();
                        break;
                    case Constants.HTTP_CODE_UNAUTHORIZED:
                        NetworkUtils.refreshToken(VerifyCMNDActivity.this, new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                updateProfile();
                            }
                        });
                        break;
                    case Constants.HTTP_CODE_BLOCK_USER:
                        Utils.blockUser(VerifyCMNDActivity.this);
                        break;
                    default:
                        showRetryDialog(VerifyCMNDActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                updateProfile();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        break;
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure error : " + t.getMessage());
                showRetryDialog(VerifyCMNDActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
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


    private void doSave() {
        if (imgPathBefore.isEmpty()) {
            Utils.showLongToast(this, getString(R.string.no_cmnd_before), true, false);
            return;
        }
        if (imgPathAfter.isEmpty()) {
            Utils.showLongToast(this, getString(R.string.no_cmnd_after), true, false);
            return;
        }
        if (imgPathPortrait.isEmpty()) {
            Utils.showLongToast(this, getString(R.string.no_portrait_after), true, false);
            return;
        }
        doAttachFiles();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_add_before:
                countImage = 0;
                checkPermissionBackground(countImage);
                break;
            case R.id.img_add_after:
                countImage = 1;
                checkPermissionBackground(countImage);
                break;

            case R.id.img_add_portrait:
                countImage = 2;
                checkPermissionBackground(countImage);
                break;
            case R.id.btn_edit_before:
                countImage = 0;
                checkPermissionBackground(countImage);
                break;
            case R.id.btn_edit_after:
                countImage = 1;
                checkPermissionBackground(countImage);
                break;
            case R.id.btn_edit_portrait:
                countImage = 2;
                checkPermissionBackground(countImage);
                break;
            case R.id.btn_delete_before:
                imgPathBefore = "";
                imgShowBefore.setVisibility(View.GONE);
                imgAddBefore.setEnabled(true);
                layoutBefore.setVisibility(View.GONE);
                break;


            case R.id.btn_delete_after:
                imgPathAfter = "";
                imgShowAfter.setVisibility(View.GONE);
                imgAddAfter.setEnabled(true);
                layoutAfter.setVisibility(View.GONE);
                break;

            case R.id.btn_delete_portrait:
                imgPathPortrait = "";
                getImgShowPortrait.setVisibility(View.GONE);
                imgAddPortrait.setEnabled(true);
                layoutPortrait.setVisibility(View.GONE);
                break;
            case R.id.img_save:
                doSave();
                break;

            case R.id.img_show_before:
                if (!imgPathBefore.isEmpty()) {
                    Intent intentView = new Intent(VerifyCMNDActivity.this, PreviewImageActivity.class);
                    intentView.putExtra(Constants.EXTRA_IMAGE_PATH, imgPathBefore);
                    startActivity(intentView, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;
            case R.id.img_show_after:
                if (!imgPathAfter.isEmpty()) {
                    Intent intentaf = new Intent(VerifyCMNDActivity.this, PreviewImageActivity.class);
                    intentaf.putExtra(Constants.EXTRA_IMAGE_PATH, imgPathAfter);
                    startActivity(intentaf, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;
            case R.id.img_show_portrait:
                if (!imgPathPortrait.isEmpty()) {
                    Intent intentpt = new Intent(VerifyCMNDActivity.this, PreviewImageActivity.class);
                    intentpt.putExtra(Constants.EXTRA_IMAGE_PATH, imgPathPortrait);
                    startActivity(intentpt, TransitionScreen.RIGHT_TO_LEFT);
                }
                break;
        }
    }
}
