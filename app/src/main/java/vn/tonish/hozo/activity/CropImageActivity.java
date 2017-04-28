package vn.tonish.hozo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBD.
 */

public class CropImageActivity extends BaseActivity implements View.OnClickListener, CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    private static final String TAG = CropImageActivity.class.getName();
    private CropImageView cropImageView;
    private Button btnCrop;
    public static Bitmap bitmapCrop;
    private RelativeLayout layoutBack;
    private CropImageViewOptions mCropImageViewOptions = new CropImageViewOptions();
    private ImageView imgRotate;

    @Override
    protected int getLayout() {
        return R.layout.activity_crop_image;
    }

    @Override
    protected void initView() {
        cropImageView = (CropImageView) findViewById(R.id.crop_image_view);
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);

        btnCrop = (Button) findViewById(R.id.btn_crop);

        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);

        imgRotate = (ImageView) findViewById(R.id.img_rotate);

    }

    @Override
    protected void initData() {
        btnCrop.setOnClickListener(this);
        layoutBack.setOnClickListener(this);
        imgRotate.setOnClickListener(this);

        String uriStr = getIntent().getStringExtra(Constants.EXTRA_IMAGE_PATH);
        Uri uri = Uri.fromFile(new File(uriStr));

        cropImageView.setImageUriAsync(uri);

        mCropImageViewOptions.fixAspectRatio = true;
        mCropImageViewOptions.aspectRatio = new Pair<>(1, 1);

        setCropImageViewOptions(mCropImageViewOptions);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_crop:
                cropImageView.getCroppedImageAsync();
                break;

            case R.id.layout_back:
                finish();
                break;

            case R.id.img_rotate:
                cropImageView.rotateImage(90);
                break;

        }
    }

    public void setCropImageViewOptions(CropImageViewOptions options) {
        cropImageView.setAspectRatio(options.aspectRatio.first, options.aspectRatio.second);
//        cropImageView.setScaleType(options.scaleType);
//        cropImageView.setCropShape(options.cropShape);
//        cropImageView.setGuidelines(options.guidelines);
//        cropImageView.setFixedAspectRatio(options.fixAspectRatio);
//        cropImageView.setMultiTouchEnabled(options.multitouch);
//        cropImageView.setShowCropOverlay(options.showCropOverlay);
//        cropImageView.setShowProgressBar(options.showProgressBar);
//        cropImageView.setAutoZoomEnabled(options.autoZoomEnabled);
//        cropImageView.setMaxZoom(options.maxZoomLevel);
//        cropImageView.setCropRect(new Rect(100, 300, 1100, 1300));
    }

    /**
     * Set the initial rectangle to use.
     */
    public void setInitialCropRect() {
        cropImageView.setCropRect(new Rect(100, 300, 1100, 1300));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            if (result.getUri() != null) {
                LogUtils.d(TAG, "handleCropResult 111111111111");
//                intent.putExtra("URI", result.getUri());
            } else {
                LogUtils.d(TAG, "handleCropResult 222222222222");
//                CropResultActivity.mImage = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
//                        ? CropImage.toOvalBitmap(result.getBitmap())
//                        : result.getBitmap();

                bitmapCrop = cropImageView.getCropShape() == CropImageView.CropShape.OVAL
                        ? CropImage.toOvalBitmap(result.getBitmap())
                        : result.getBitmap();

//                int width = bitmapCrop.getWidth();
//                int height = bitmapCrop.getHeight();
//
//                int expected = 1000;
//
//                LogUtils.d(TAG, "handleCropResult , width: " + width + " , height : " + height);
//
//                if (width > height && width > expected) {
//                    float scale = width / expected;
//                    bitmapCrop = Bitmap.createScaledBitmap(bitmapCrop, expected, (int) (height / scale), false);
//                } else if (height > width && height > expected) {
//                    float scale = height / expected;
//                    bitmapCrop = Bitmap.createScaledBitmap(bitmapCrop, (int) (width / scale), expected, false);
//                }

                File fileSave = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".png");
                Utils.compressBitmapToFile(bitmapCrop, fileSave.getPath());

                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, fileSave.getPath());
                setResult(Constants.RESPONSE_CODE_CROP_IMAGE, intent);
                finish();
            }
        } else {
            Log.e(TAG, "Failed to crop image", result.getError());
            Toast.makeText(this, "Image crop failed: " + result.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
//        Utils.showLongToast(this, "Crop success ok ....");
    }

    public final class CropImageViewOptions {

        public CropImageView.ScaleType scaleType = CropImageView.ScaleType.CENTER_INSIDE;

        public CropImageView.CropShape cropShape = CropImageView.CropShape.RECTANGLE;

        public CropImageView.Guidelines guidelines = CropImageView.Guidelines.ON_TOUCH;

        public Pair<Integer, Integer> aspectRatio = new Pair<>(1, 1);

        public boolean autoZoomEnabled;

        public int maxZoomLevel;

        public boolean fixAspectRatio;

        public boolean multitouch;

        public boolean showCropOverlay;

        public boolean showProgressBar;
    }

}