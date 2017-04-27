package vn.tonish.hozo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ImageSelectAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_CROP_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

/**
 * Created by LongBD on 4/21/2017.
 */

public class ImageSelectActivity extends BaseActivity implements View.OnClickListener {

    private String album;
    private GridView grImage;
    private ImageSelectAdapter imageSelectAdapter;
    private ArrayList<Image> images;
    private final String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    protected RelativeLayout layoutBack;
    protected TextView tvDone, tvAlbumName;
    private boolean isOnlyImage = false;
    private boolean isCropProfile = false;

    @Override
    protected int getLayout() {
        return R.layout.image_select_activity;
    }

    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        tvDone = (TextView) findViewById(R.id.tv_done);
        tvDone.setOnClickListener(this);

        tvAlbumName = (TextView) findViewById(R.id.tv_album_name);

        grImage = (GridView) findViewById(R.id.gr_image);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        boolean isExtra = intent != null && intent.hasExtra(Constants.EXTRA_ONLY_IMAGE);
        if (isExtra)
            isOnlyImage = intent.getBooleanExtra(Constants.EXTRA_ONLY_IMAGE, false);

        if (intent.hasExtra(Constants.EXTRA_IS_CROP_PROFILE))
            isCropProfile = intent.getBooleanExtra(Constants.EXTRA_IS_CROP_PROFILE, false);

        album = intent.getStringExtra(Constants.INTENT_EXTRA_ALBUM);
        tvAlbumName.setText(album);

        getImage();
    }

    @Override
    protected void resumeData() {

    }

    private void getImage() {
        AsyncTask<Void, Void, Void> imageTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                File file;

                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{album}, MediaStore.Images.Media.DATE_ADDED);
                if (cursor == null) {
                    return null;
                }

            /*
            In case this runnable is executed to onChange calling loadImages,
            using countSelected variable can result in a race condition. To avoid that,
            tempCountSelected keeps track of number of selected images. On handling
            FETCH_COMPLETED message, countSelected is assigned value of tempCountSelected.
             */
                ArrayList<Image> temp = new ArrayList<>(cursor.getCount());
                if (cursor.moveToLast()) {
                    do {
                        if (Thread.interrupted()) {
                            return null;
                        }

                        long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                        String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                        String path = cursor.getString(cursor.getColumnIndex(projection[2]));
//                        boolean isSelected = selectedImages.contains(id);
//                        if (isSelected) {
//                            tempCountSelected++;
//                        }

                        file = new File(path);
                        if (file.exists()) {
                            temp.add(new Image(id, name, path, false, false));
                        }

                    } while (cursor.moveToPrevious());
                }
                cursor.close();

                if (images == null) {
                    images = new ArrayList<>();
                }
                images.clear();
                images.addAll(temp);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                imageSelectAdapter = new ImageSelectAdapter(ImageSelectActivity.this, images, isOnlyImage);
                grImage.setAdapter(imageSelectAdapter);
            }
        };
        imageTask.execute();
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES, getSelectedImage());
        setResult(RESPONSE_CODE_PICK_IMAGE, intent);
        finish();
    }

    private ArrayList<Image> getSelectedImage() {
        ArrayList<Image> selectedImages = new ArrayList<>();
        for (int i = 0, l = images.size(); i < l; i++) {
            if (images.get(i).isSelected) {
                selectedImages.add(images.get(i));
            }
        }
        return selectedImages;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE
                && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE
                && data != null) {
            setResult(RESPONSE_CODE_PICK_IMAGE, data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_back:
                finish();
                break;

            case R.id.tv_done:
                if (getSelectedImage().size() == 0) {
                    Utils.showLongToast(ImageSelectActivity.this, getString(R.string.err_pick_image));
                } else {

                    if (isCropProfile) {
                        Image imageCrop = getSelectedImage().get(0);
                        Intent intent = new Intent(ImageSelectActivity.this, CropImageActivity.class);
                        intent.putExtra(Constants.EXTRA_IMAGE_PATH, imageCrop.getPath());
                        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
                    } else {
                        sendIntent();
                    }

                }
                break;

        }
    }
}
