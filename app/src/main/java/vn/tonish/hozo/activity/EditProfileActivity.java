package vn.tonish.hozo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.customview.CircleImageView;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICKIMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICKIMAGE;

/**
 * Created by ADMIN on 4/17/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    protected Button btnSave;
    private CircleImageView imgAvata;
    private String imgPath;
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.edit_profile_activity;
    }

    @Override
    protected void initView() {
        imgAvata = (CircleImageView) findViewById(R.id.img_avata);
        imgAvata.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:

                break;

            case R.id.img_avata:
                doPickImage();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtils.d(TAG, "onActivityResult requestCode : " + requestCode + " , resultCode : " + resultCode);

        if (requestCode == REQUEST_CODE_PICKIMAGE
                && resultCode == RESPONSE_CODE_PICKIMAGE
                && data != null) {

            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Utils.displayImage(EditProfileActivity.this, imgAvata, imagesSelected.get(0).getPath());

        } else if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            String selectedImagePath = getImagePath();
            Utils.displayImage(EditProfileActivity.this, imgAvata, selectedImagePath);
        }

    }

    private void doPickImage() {
        final CharSequence[] items = {getString(R.string.pick_image_take_picture), getString(R.string.pick_image_album),};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getString(R.string.pick_image_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                        break;

                    case 1:
                        Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                        intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                        startActivityForResult(intent, REQUEST_CODE_PICKIMAGE);
                        break;
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }
}
