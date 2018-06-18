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

import java.io.File;
import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 11/21/17.
 */

public class VerifyCMNDActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = VerifyCMNDActivity.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView imgAddBefore, imgAddAfter;
    private ImageView imgShowBefore, imgShowAfter;
    private String imgPathBefore = "";
    private String imgPathAfter = "";
    private File fileAfter, fileBefore;
    private LinearLayout layoutBefore, layoutAfter;
    private TextViewHozo btnEditbf, btnDeletebf;
    private TextViewHozo btnEditAf, btnDeleteAf;
    private boolean isBf;


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
        imgShowBefore = (ImageView) findViewById(R.id.img_show_before);
        imgShowAfter = (ImageView) findViewById(R.id.img_show_after);
        layoutBefore = (LinearLayout) findViewById(R.id.layout_before);
        layoutAfter = (LinearLayout) findViewById(R.id.layout_after);
        btnEditbf = (TextViewHozo) findViewById(R.id.btn_edit_before);
        btnDeletebf = (TextViewHozo) findViewById(R.id.btn_delete_before);
        btnEditAf = (TextViewHozo) findViewById(R.id.btn_edit_after);
        btnDeleteAf = (TextViewHozo) findViewById(R.id.btn_delete_after);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_save).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        btnEditbf.setOnClickListener(this);
        btnDeletebf.setOnClickListener(this);
        btnEditAf.setOnClickListener(this);
        btnDeleteAf.setOnClickListener(this);
        if (imgPathBefore.isEmpty()) {
            layoutBefore.setVisibility(View.GONE);
            imgAddBefore.setEnabled(true);
        }
        if (imgPathAfter.isEmpty()) {
            layoutAfter.setVisibility(View.GONE);
            imgAddAfter.setEnabled(true);
        }
    }


    @Override
    protected void resumeData() {

    }

    private void checkPermissionBackground(boolean isBefore) {
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

    private void permissionGrantedBackground(final boolean isBefore) {
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

    private Uri setImageUriBackground(boolean isBefore) {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        if (isBefore)
            this.imgPathBefore = file.getAbsolutePath();
        else
            this.imgPathAfter = file.getAbsolutePath();
        return imgUri;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_BACKGROUND) {
            permissionGrantedBackground(isBf);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            if (isBf) {
                imgPathBefore = imagesSelected.get(0).getPath();
                fileBefore = new File(imgPathBefore);
            } else {
                imgPathAfter = imagesSelected.get(0).getPath();
                fileAfter = new File(imgPathAfter);
            }
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA_BACKGROUND && resultCode == Activity.RESULT_OK) {
            if (isBf)
                fileBefore = new File(imgPathBefore);
            else
                fileAfter = new File(imgPathAfter);
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
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }

    private void doSave() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add_before:
                isBf = true;
                checkPermissionBackground(isBf);
                break;
            case R.id.img_add_after:
                isBf = false;
                checkPermissionBackground(isBf);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_edit_before:
                isBf = true;
                checkPermissionBackground(isBf);
                break;
            case R.id.btn_delete_before:
                imgPathBefore = "";
                imgShowBefore.setVisibility(View.GONE);
                imgAddBefore.setEnabled(true);
                layoutBefore.setVisibility(View.GONE);
                break;

            case R.id.btn_edit_after:
                isBf = false;
                checkPermissionBackground(isBf);
                break;
            case R.id.btn_delete_after:
                imgPathAfter = "";
                imgShowAfter.setVisibility(View.GONE);
                imgAddAfter.setEnabled(true);
                layoutAfter.setVisibility(View.GONE);
                break;
            case R.id.img_save:
                doSave();
                break;

        }
    }
}
