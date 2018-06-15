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

/**
 * Created by CanTran on 11/21/17.
 */

public class VerifyCMNDActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = VerifyCMNDActivity.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView imgAddBefore;
    private ImageView imgShowBefore;
    private String imgPath, imgPathBackground;
    private File file, fileBackground;


    @Override
    protected int getLayout() {
        return R.layout.activity_verify_cmnd;
    }

    @Override
    protected void initView() {
        imgAddBefore = (ImageView) findViewById(R.id.img_add_before);
        imgAddBefore.setOnClickListener(this);
        imgShowBefore = (ImageView) findViewById(R.id.img_show_before);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void resumeData() {
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

    private void permissionGrantedBackground() {
        PickImageDialog pickImageDialog = new PickImageDialog(VerifyCMNDActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUriBackground());
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

    private Uri setImageUriBackground() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPathBackground = file.getAbsolutePath();
        return imgUri;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else if (requestCode == Constants.PERMISSION_REQUEST_CODE_BACKGROUND) {
            permissionGrantedBackground();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PICK_IMAGE_BACKGROUND
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            String imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(VerifyCMNDActivity.this, imgShowBefore, imgPath);
            LogUtils.d(TAG, "imgPath background : " + imgPath);
            fileBackground = new File(imgPath);
//            Realm.getDefaultInstance().beginTransaction();
//            userEntity.setBackground(imgPath);
//            Realm.getDefaultInstance().commitTransaction();
        }
        else
            if (requestCode == Constants.REQUEST_CODE_CAMERA_BACKGROUND && resultCode == Activity.RESULT_OK) {
            String selectedImagePath = getImagePathBackground();
                Utils.displayImage(VerifyCMNDActivity.this, imgShowBefore, selectedImagePath);
            fileBackground = new File(selectedImagePath);
//            Realm.getDefaultInstance().beginTransaction();
//            userEntity.setBackground(imgPath);
//            Realm.getDefaultInstance().commitTransaction();
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied camera");
    }
    private String getImagePathBackground() {
        return imgPathBackground;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add_before:
                checkPermissionBackground();
                break;

        }
    }
}
